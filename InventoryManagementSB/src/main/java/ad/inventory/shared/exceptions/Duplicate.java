package ad.inventory.shared.exceptions;

public class Duplicate extends CustomError {
	
	private static final String DUPLICATE_PARAM = "Duplicate";
	
	public Duplicate() {
		super(DUPLICATE_PARAM, 400, "Duplicated!");
	}
	
	public Duplicate(String message) {
		super(DUPLICATE_PARAM, 400, message);
	}
	
	public Duplicate(int code, String message) {
		super(DUPLICATE_PARAM, code, message);
	}
}
