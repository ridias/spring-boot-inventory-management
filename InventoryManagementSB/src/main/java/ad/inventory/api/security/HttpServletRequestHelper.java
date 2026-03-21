package ad.inventory.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class HttpServletRequestHelper {

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private CookieHelper cookieHelper;
	
	public String getUsernameFromTokenCookie(HttpServletRequest request) {
		String username = "anonymous";
		String token = this.cookieHelper.getToken(request);
		if(token != null) {
			username = jwtUtils.getEmailFromJwtToken(token);
		}
		
		return username;
	}
}
