package ad.inventory.shared.exceptions;

public class AccessDenied extends CustomError {

	private static final String ACCESS_DENIED = "Access Denied";
	
	public AccessDenied() {
		super(ACCESS_DENIED, 401, "Not authenticated!");
	}
	
	public AccessDenied(String message) {
		super(ACCESS_DENIED, 401, message);
	}
	
	public AccessDenied(int code, String message) {
		super(ACCESS_DENIED, code, message);
	}
}
