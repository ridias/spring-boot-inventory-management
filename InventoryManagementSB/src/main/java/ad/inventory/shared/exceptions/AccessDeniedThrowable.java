package ad.inventory.shared.exceptions;

public class AccessDeniedThrowable extends CustomExceptionThrowable {

	private static final long serialVersionUID = -2740583034133810672L;
	private static final String ACCESS_DENIED = "Access Denied";
	
	public AccessDeniedThrowable() {
		super(ACCESS_DENIED, 401, "Not authenticated!");
	}
	
	public AccessDeniedThrowable(String message) {
		super(ACCESS_DENIED, 401, message);
	}
	
	public AccessDeniedThrowable(int code, String message) {
		super(ACCESS_DENIED, code, message);
	}
}
