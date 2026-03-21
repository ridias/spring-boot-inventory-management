package ad.inventory.shared.exceptions;

public class ServerError extends CustomError {

	private static final String SERVER_ERROR = "Server error";
	
	public ServerError() {
		super(SERVER_ERROR, 500, "Server error!");
	}
	
	public ServerError(String message) {
		super(SERVER_ERROR, 500, message);
	}
	
	public ServerError(int code, String message) {
		super(SERVER_ERROR, code, message);
	}
}
