package ad.inventory.api.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.google.gson.Gson;

import ad.inventory.api.audit.AuditService;
import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.api.security.two_factor.Authentication2FAService;
import ad.inventory.api.users.UserLocal;
import ad.inventory.api.users.UserLocalRepository;
import ad.inventory.shared.exceptions.CustomLockedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private CustomAuthenticationProvider authenticationProvider;
	private JwtUtils jwtUtils;
	private AuthenticationFailureHandler failureHandler;
	private UserLocalRepository userLocalRepository;
	private AuditService auditService;
	private Authentication2FAService auth2faService;
	private CookieHelper cookieCreator;

	private String isDevMode;
	
	private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

	public CustomAuthenticationFilter(
			CustomAuthenticationProvider authenticationProvider,
			JwtUtils jwtUtils,
			AuthenticationFailureHandler failureHandler,
			Authentication2FAService auth2faService,
			UserLocalRepository userLocalRepository,
			AuditService auditService,
			CookieHelper cookieCreator,
			String isDevMode) {
		this.authenticationProvider = authenticationProvider;
		this.jwtUtils = jwtUtils;
		this.failureHandler = failureHandler;
		this.userLocalRepository = userLocalRepository;
		this.auditService = auditService;
		this.isDevMode = isDevMode;
		this.auth2faService = auth2faService;
		this.cookieCreator = cookieCreator;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String body = "";
		
		try {
			body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		}catch(IOException ex) {
			log.error("Problema al intentar recuperar les dades del http request body, més detalls: ", ex);
		}
		
		var username = "";
		var password = "";
		if(Pattern.matches("^(?=.*username=)(?=.*&(pass)(word)=).*$", body)) {
			try{
				String[] bodySplitted = body.split("&");
				username = bodySplitted[0].split("=")[1];
				password = bodySplitted[1].split("=")[1];
			}catch(Exception ex) {
				log.error("No ha sigut possible autenticar l'usuari, més detalls: ", ex);
			}
		}else if(body != null && !body.isBlank()) {
			try {
				Gson gson = new Gson();
				var requestLogin = gson.fromJson(body, RequestLoginDto.class);
				username = requestLogin.getEmail();
				password = requestLogin.getPassword();
			}catch(Exception ex) {
				log.error("No ha sigut possible autenticar l'usuari, més detalls: ", ex);
			}
		}
		
		log.info("Procedint a verificar credencials per usuari " + username);
		request.setAttribute("failed_username", username);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		authenticationToken.setDetails(new WebAuthenticationDetails(request));
		return this.authenticationProvider.authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
			FilterChain chain, Authentication authentication) throws IOException, ServletException {
		
		UserLocal user = (UserLocal) authentication.getPrincipal();
		var username = user.getUsername();
		
		String ip = request.getRemoteAddr();
		String browser = request.getHeader("User-Agent");
		
		var requestSaveAudit = new RequestSaveAudit(38L, user.getId(), ip, "", browser, "{}");
		this.auditService.saveAsReadAuditRequest(requestSaveAudit, "");
		requestSaveAudit.setDescription("");
		requestSaveAudit.setRequest("{}");
		this.userLocalRepository.updateLastSessionById(user.getId());
		
		if(isDevMode.equals("true")) {
			var roles = user.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.toList();
			
			String token = this.jwtUtils.generateToken(user.getId(), username, user.getEmail(), user.getRole());

			response.addHeader("Set-Cookie", 
					this.cookieCreator.createCookieAsNone(
							this.cookieCreator.createCookie(token, true, true, 8 * 60 * 60)));
			
			String role = roles.size() > 0 ? roles.get(0) : "";
			
			PrintWriter out = response.getWriter();
			out.print("{\"result\": \"OK\", " + 
					"\"role\": \"" + role + "\"}");
			
			
		}else {
			boolean isCodeGenerated = false;
			var responseCodeGenerated = this.auth2faService.generateCode(user.getId());
			if(responseCodeGenerated.isSuccess()) {
				isCodeGenerated = true;
			}
			
			var type2fa = user.getType2FA();
			if(type2fa == null) type2fa = 1;
			
			PrintWriter out = response.getWriter();
			out.print("{\"result\": \"OK\", " + 
					"\"code_generated\": \"" + isCodeGenerated + 
					"\",\"type_2fa\": \"" + type2fa +
					"\",\"id_user\": \"" + user.getId() + "\"}");
			out.flush();
		}
	}
	
	@Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String email = (String) request.getAttribute("failed_username");
        log.info("Error authentication (" + email + "): " + failed.getMessage());
        var userInDb = this.userLocalRepository.findByEmailAndActiveAndIsDeleted(email, true, false);
        String description = failed.getMessage();
        long idUser = -1;
        
        if(userInDb.isEmpty()) {
        	description = "Unknown email = " + email;
        }else {
        	idUser = userInDb.get().getId();
        }
        
		String ip = request.getRemoteAddr();
		String browser = request.getHeader("User-Agent");
		var requestSaveAudit = new RequestSaveAudit(38L, idUser, ip, "", browser, "{}");
        
        String query = "status=FAILED&error=";
        if(failed instanceof CustomLockedException) {
        	query += "LOCKED_USER_OR_IP";
        }else {
        	query += "ERROR_CREDENTIALS";
        }
        
        this.auditService.saveAsReadAuditRequest(requestSaveAudit, query);
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
