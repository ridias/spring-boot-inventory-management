package ad.inventory.shared.exceptions;

public class NotFound extends CustomError {
	
	private static final String NOT_FOUND = "Not Found";

	public NotFound() {
		super(NOT_FOUND, 404, "Not found!");
	}
	
	public NotFound(String message) {
		super(NOT_FOUND, 404, message);
	}
	
	public NotFound(int code, String message) {
		super(NOT_FOUND, code, message);
	}
}
