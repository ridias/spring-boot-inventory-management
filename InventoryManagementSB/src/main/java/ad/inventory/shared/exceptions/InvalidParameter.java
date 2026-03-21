package ad.inventory.shared.exceptions;

public class InvalidParameter extends CustomError{
	
	public InvalidParameter() {
		super("Invalid Parameter", 400, "Invalid Parameter!");
	}
	
	public InvalidParameter(String message) {
		super("Invalid parameter", 400, message);
	}
	
	public InvalidParameter(int code, String message) {
		super("Invalid parameter", code, message);
	}
}
