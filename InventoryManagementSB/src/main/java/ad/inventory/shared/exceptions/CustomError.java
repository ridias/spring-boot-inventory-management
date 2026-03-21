package ad.inventory.shared.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomError {

	private String message;
	private int code;
	private String description;
	
	public CustomError(String message, int code, String description) {
		this.message = message;
		this.code = code;
		this.description = description;
	}

}
