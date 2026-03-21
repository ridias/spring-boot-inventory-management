package ad.inventory.shared.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class BadCredentialsRetriesException extends BadCredentialsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int remainingRetries;
	
	public BadCredentialsRetriesException(String msg, int remainingRetries) {
		super(msg);
		this.setRemainingRetries(remainingRetries);
	}

	public int getRemainingRetries() {
		return remainingRetries;
	}

	public void setRemainingRetries(int remainingRetries) {
		this.remainingRetries = remainingRetries;
	}
}
