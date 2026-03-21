package ad.inventory.shared.exceptions;

public class Forbidden extends CustomError {

	private static final String FORBIDDEN_PARAM = "Forbidden";
	
	public Forbidden() {
		super(FORBIDDEN_PARAM, 403, "Forbidden!");
	}
	
	public Forbidden(String message) {
		super(FORBIDDEN_PARAM, 403, message);
	}
	
	public Forbidden(int code, String message) {
		super(FORBIDDEN_PARAM, code, message);
	}
}
