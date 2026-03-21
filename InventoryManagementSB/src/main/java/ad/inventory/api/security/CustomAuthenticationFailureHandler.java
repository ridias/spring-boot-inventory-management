package ad.inventory.api.security;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ad.inventory.shared.exceptions.BadCredentialsRetriesException;
import ad.inventory.shared.exceptions.CustomLockedException;
import ad.inventory.shared.exceptions.Unauthorized;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final String KEY_TIMESTAMP = "timestamp";
	private static final String KEY_TYPE = "type";
	private static final String KEY_EXCEPTION = "exception";
	private static final String VALUE_TYPE_BLOCKED = "BLOCKED";
	private static final String VALUE_TYPE_BAD_CREDENTIALS_R = "BAD_CREDENTIALS_R";
	private static final String VALUE_TYPE_UNAUTHORIZED = "UNAUTHORIZED";
	private static final String VALUE_TYPE_BAD_CREDENTIALS = "BAD_CREDENTIALS";
	private static final String KEY_REMAINING_RETRIES = "remainingRetries";
	private static final String KEY_REMAINING_SECONDS = "remainingSeconds";
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		Map<String, Object> data = new HashMap<>();
		data.put(KEY_TIMESTAMP, Calendar.getInstance().getTime());
		
		if(exception instanceof CustomLockedException exp) {
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			data.put(KEY_REMAINING_SECONDS, exp.getRemainingSecondsAsBlocked());
			data.put(KEY_TYPE, VALUE_TYPE_BLOCKED);
		}else if(exception instanceof BadCredentialsRetriesException exp){
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		    data.put(KEY_EXCEPTION, exp.getMessage());
		    data.put(KEY_REMAINING_RETRIES, exp.getRemainingRetries());
		    data.put(KEY_TYPE, VALUE_TYPE_BAD_CREDENTIALS_R);
		}else if(exception instanceof Unauthorized exp) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			data.put(KEY_EXCEPTION, exp.getMessage());
			data.put(KEY_TYPE, VALUE_TYPE_UNAUTHORIZED);
		}else {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		    data.put(KEY_EXCEPTION, exception.getMessage());
		    data.put(KEY_TYPE, VALUE_TYPE_BAD_CREDENTIALS);
		}
		
		response.getOutputStream()
        	.println(objectMapper.writeValueAsString(data));
	}
}
