package ad.inventory.shared;

import org.springframework.http.HttpStatus;

public class ControllerHelper {

	private ControllerHelper() {
		
	}

	public static HttpStatus getHttpStatusByCode(int code) {
		
		if(code == 404) {
			return HttpStatus.NOT_FOUND;
		}else if(code == 401) {
			return HttpStatus.UNAUTHORIZED;
		}else if(code == 403) {
			return HttpStatus.FORBIDDEN;
		}else if(code == 400) {
			return HttpStatus.BAD_REQUEST;
		}
		
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
