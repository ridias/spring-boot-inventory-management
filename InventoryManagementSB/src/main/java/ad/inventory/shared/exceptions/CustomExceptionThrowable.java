package ad.inventory.shared.exceptions;

import lombok.Getter;

@Getter
public class CustomExceptionThrowable extends Exception {

	private static final long serialVersionUID = -5095678612379164055L;
	private final String message;
	private final int code;
	private final String description;
	
	public CustomExceptionThrowable(String message, int code, String description) {
		this.message = message;
		this.code = code;
		this.description = description;
	}
}
