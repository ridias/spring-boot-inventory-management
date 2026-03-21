package ad.inventory.api.security.authorization_routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.exceptions.AccessDenied;
import ad.inventory.shared.exceptions.AccessDeniedThrowable;
import ad.inventory.shared.exceptions.Forbidden;
import ad.inventory.shared.exceptions.ForbiddenThrowable;

@RestControllerAdvice
public class AuthorizationExceptionHandler {

	@ExceptionHandler(AccessDeniedThrowable.class)
    public ResponseEntity<ResponseDto<Object>> handleAccessDeniedException(AccessDeniedThrowable ex) {
		var error = new AccessDenied(ex.getCode(), ex.getDescription());
        var response = GeneratorResponse.fail(error);
        return new ResponseEntity<>(response, 
            ControllerHelper.getHttpStatusByCode(ex.getCode()));
    }
	
	@ExceptionHandler(ForbiddenThrowable.class)
	public ResponseEntity<ResponseDto<Object>> handleForbiddenException(ForbiddenThrowable ex){
		var error = new Forbidden(ex.getCode(), ex.getDescription());
		var response = GeneratorResponse.fail(error);
		return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(ex.getCode()));
	}
}
