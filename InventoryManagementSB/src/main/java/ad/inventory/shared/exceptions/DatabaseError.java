package ad.inventory.shared.exceptions;

public class DatabaseError extends CustomError {
	
	private static final String DATABASE_EXCEPTION = "Database exception";

	public DatabaseError() {
		super(DATABASE_EXCEPTION, 500, "Database Exception!");
	}
	
	public DatabaseError(String message) {
		super(DATABASE_EXCEPTION, 500, message);
	}
	
	public DatabaseError(int code, String message) {
		super(DATABASE_EXCEPTION, code, message);
	}
}
