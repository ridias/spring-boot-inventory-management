package ad.inventory.shared.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class Unauthorized extends BadCredentialsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Unauthorized(String msg) {
		super(msg);
	}
}
