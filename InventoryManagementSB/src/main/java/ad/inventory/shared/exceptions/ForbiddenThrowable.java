package ad.inventory.shared.exceptions;

public class ForbiddenThrowable extends CustomExceptionThrowable {

	private static final long serialVersionUID = -8886124061569605336L;
	private static final String FORBIDDEN = "Forbidden";
	
	public ForbiddenThrowable() {
		super(FORBIDDEN, 403, "Forbidden!");
	}
	
	public ForbiddenThrowable(String message) {
		super(FORBIDDEN, 403, message);
	}
	
	public ForbiddenThrowable(int code, String message) {
		super(FORBIDDEN, code, message);
	}
}
