package ad.inventory.shared.exceptions;

public class InvalidRequest extends CustomError {

	private static final String INVALID_REQUEST = "Invalid Request";
	
	public InvalidRequest() {
		super(INVALID_REQUEST, 400, "Invalid Request!");
	}
	
	public InvalidRequest(String message) {
		super(INVALID_REQUEST, 400, message);
	}
	
	public InvalidRequest(int code, String message) {
		super(INVALID_REQUEST, code, message);
	}
}
