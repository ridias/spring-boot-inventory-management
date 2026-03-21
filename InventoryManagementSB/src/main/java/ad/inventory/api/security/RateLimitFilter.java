package ad.inventory.api.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RateLimitFilter extends OncePerRequestFilter {

	private final HttpServletRequestHelper httpRequestHelper;
	
	private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
	
	private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
	
	public RateLimitFilter(HttpServletRequestHelper httpRequestHelper) {
		this.httpRequestHelper = httpRequestHelper;
	}
	
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.builder()
        		.capacity(3000)
        		.refillGreedy(1000, Duration.ofSeconds(20))
        		.build();
        
        return Bucket.builder().addLimit(limit).build();
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String path = request.getRequestURI();
		if(isIgnoredPath(path)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());
        String username = this.httpRequestHelper.getUsernameFromTokenCookie(request);
        
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
        	log.warn("Too many requests for user {}, try again later.", username);
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Too many requests, try again later.\"}");
        }
	}
	
	protected boolean isIgnoredPath(String path) {
		return path.contains("/logout") || 
				path.contains("/api/v1/2fa") || 
				path.contains("/api/v1/csrf-token");
	}
}
