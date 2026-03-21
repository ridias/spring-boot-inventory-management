package ad.inventory.api.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import ad.inventory.api.security.sessions.LoginSecurityService;
import ad.inventory.api.system.CSystemService;
import ad.inventory.api.users.UserLocal;
import ad.inventory.shared.exceptions.BadCredentialsRetriesException;
import ad.inventory.shared.exceptions.CustomLockedException;
import ad.inventory.shared.exceptions.Unauthorized;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceSecurity userDetailsService;
    private final LoginSecurityService loginSecurityService;
    private final PasswordEncoder passwordEncoder;
    private final CSystemService systemService;

    public CustomAuthenticationProvider(
    		UserDetailsServiceSecurity userDetailsService, 
    		LoginSecurityService loginSecurityService, 
    		PasswordEncoder passwordEncoder, 
    		CSystemService systemService) {
        this.userDetailsService = userDetailsService;
        this.loginSecurityService = loginSecurityService;
        this.passwordEncoder = passwordEncoder;
        this.systemService = systemService;
    }
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        String ipAddress = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
		
        UserLocal user = (UserLocal) userDetailsService.loadUserByUsername(email);
        
        if(Boolean.FALSE.equals(user.getActive())) {
        	throw new Unauthorized("Not Authorized");
        }
        
        var maxMinutesBlockConfig  = this.systemService.getMinsUserBlocked();
    	int minutesBlock = Integer.parseInt(maxMinutesBlockConfig.getName());
        
        if (loginSecurityService.isCurrentlyBlocked(user, minutesBlock)) {
        	long remainingSeconds = this.loginSecurityService.getRemainingSecondsBlocked(user);
        	throw new CustomLockedException("Account or IP temporarily blocked due to multiple failed attempts", remainingSeconds);
        }
        
        try {
        	if (passwordEncoder.matches(password, user.getPassword())) {
                loginSecurityService.recordLoginAttempt(user.getEmail(), ipAddress, true);
                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            }else {
                loginSecurityService.recordLoginAttempt(email, ipAddress, false);
                int remainingRetries = this.loginSecurityService.getRemainingAttemps(user);
                if(remainingRetries == 0) {
                	long remainingSeconds = this.loginSecurityService.getRemainingSecondsBlocked(user);
                	throw new CustomLockedException("Account or IP temporarily blocked due to multiple failed attempts", remainingSeconds);
                }
                throw new BadCredentialsRetriesException("Invalid Credentials", remainingRetries); 
            }
        }catch(UsernameNotFoundException e) {
        	loginSecurityService.recordLoginAttempt(email, ipAddress, true);
        	throw e;
        }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
