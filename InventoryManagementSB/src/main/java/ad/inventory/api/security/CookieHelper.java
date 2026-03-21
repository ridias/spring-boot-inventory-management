package ad.inventory.api.security;

import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieHelper {

	@Value("${inventory.app.domain}")
	protected String cookieDomain;
	
	public Cookie createCookie(String token, boolean httpOnly, boolean secure, int maxAge) {
		Cookie tokenCookie = new Cookie("inventoryMngToken", token);
		tokenCookie.setHttpOnly(httpOnly);
		tokenCookie.setSecure(secure);
		tokenCookie.setDomain(cookieDomain);
		tokenCookie.setPath("/");
		tokenCookie.setMaxAge(maxAge);
		return tokenCookie;
	}
	
	public String createCookieAsNone(Cookie tokenCookie) {
		return String.format("%s=%s; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=%d",
				tokenCookie.getName(), 
				tokenCookie.getValue(), 
				tokenCookie.getMaxAge());
	}
	
	public String getToken(HttpServletRequest request) {
		String token = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("inventoryMngToken".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		
		return token;
	}
}
