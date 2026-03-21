package ad.inventory.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ad.inventory.api.audit.AuditService;
import ad.inventory.api.security.sessions.LoginSecurityService;
import ad.inventory.api.security.two_factor.Authentication2FAService;
import ad.inventory.api.system.CSystemService;
import ad.inventory.api.users.UserLocalRepository;

@Configuration
public class ApplicationSecurityConfig {

	AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserLocalRepository userLocalRepository;
	@Autowired
	private AuditService auditService;
	@Autowired
	private Authentication2FAService auth2faService;
	@Autowired
	private HttpServletRequestHelper httpRequestHelper;
	@Autowired
	private CookieHelper cookieCreator;
	
	@Value("${inventory.app.dev}")
	private String isDevMode;
	@Value("${inventory.app.domain}")
	private String cookieDomain;
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, CustomAuthenticationProvider customAuthenticationProvider) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .authenticationProvider(customAuthenticationProvider)
	            .build();
	}
	
	@Bean
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    }
	
	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
	
	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider(
			PasswordEncoder passwordEncoder, 
			UserDetailsServiceSecurity userDetailsService, 
			LoginSecurityService loginSecurityService, 
			CSystemService systemService) {
	    return new CustomAuthenticationProvider(userDetailsService, loginSecurityService, passwordEncoder, systemService);
	}
	
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("XSRF-TOKEN");
        repository.setHeaderName("X-XSRF-TOKEN");
        repository.setParameterName("_csrf");
        repository.setCookieCustomizer(cookie -> {
        	cookie.sameSite("None");
        	cookie.secure(true);
        	cookie.httpOnly(false);
        	cookie.domain(cookieDomain);
        });
        
        return repository;
    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, 
    		AuthenticationManager authenticationManager,
    		CustomAuthenticationProvider customAuthenticationProvider) throws Exception{
		var customAuthorizationFilter = new CustomAuthorizationFilter(jwtUtils, cookieCreator);
		var customAuthenticationFilter = new CustomAuthenticationFilter(
				 customAuthenticationProvider, 
				 jwtUtils,
				 authenticationFailureHandler(), 
				 auth2faService,
				 userLocalRepository,
				 auditService,
				 cookieCreator,
				 isDevMode);
		customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
	     
	    RateLimitFilter rateLimitFilter = new RateLimitFilter(httpRequestHelper);
	     
	    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
	    requestHandler.setCsrfRequestAttributeName("_csrf");
	     
		http
			.cors(cors -> {})
			.csrf(csrf -> csrf
					.csrfTokenRepository(csrfTokenRepository())
					.csrfTokenRequestHandler(requestHandler)
					.ignoringRequestMatchers(
				            new AntPathRequestMatcher("/api/v1/csrf-token")))
			.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilter(customAuthenticationFilter)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/v1/csrf-token").permitAll()
				.requestMatchers("/api/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(login -> {
				login.loginProcessingUrl("/api/v1/login");
				login.failureHandler(authenticationFailureHandler());
			});
			
		return http.build();
    }
}
