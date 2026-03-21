package ad.inventory.api.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private JwtUtils jwtUtils;
	private CookieHelper cookieHelper;
	
	public CustomAuthorizationFilter(JwtUtils jwtUtils, CookieHelper cookieHelper) {
		this.jwtUtils = jwtUtils;
		this.cookieHelper = cookieHelper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		
		String path = request.getServletPath();
		
		if(this.isIgnoredPath(path)) {
			filterChain.doFilter(request, response);
		}else {
			String username = null;
			String token = this.cookieHelper.getToken(request);
			if(token != null) {
				username = jwtUtils.getEmailFromJwtToken(token);
			}
			
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if(!jwtUtils.validateToken(token)) {
					response.setStatus(401);
				}else if(jwtUtils.validateToken(token)) {
					var role = this.jwtUtils.getRoleNameFromJwtToken(token);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority(role));
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				}
			}
			
			if(token != null && jwtUtils.isTokenExpiring(token)) {
				String newAccessToken = jwtUtils.generateRefreshToken(token);
				response.setHeader("Set-Cookie", this.cookieHelper.createCookieAsNone(
						this.cookieHelper.createCookie(newAccessToken, true, true, 8 * 60 * 60)));
				
				filterChain.doFilter(request, response);
			}
		}
	}
	
	private boolean isIgnoredPath(String path) {
		return path.contains("/logout") || 
				path.contains("/api/v1/2fa") || 
				path.contains("/api/v1/csrf-token") || 
				path.contains("/login") || 
				path.contains("/api/v1/auth") ||
				path.contains("/api/v1/glpi");
	}
}
