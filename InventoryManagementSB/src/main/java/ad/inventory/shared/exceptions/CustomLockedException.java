package ad.inventory.shared.exceptions;

import org.springframework.security.authentication.LockedException;

public class CustomLockedException extends LockedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3568454659122168215L;
	
	private long remainingSecondsAsBlocked;
	
	public CustomLockedException(String msg, long remainingSecondsAsBlocked) {
		super(msg);
		this.remainingSecondsAsBlocked = remainingSecondsAsBlocked;
	}

	public long getRemainingSecondsAsBlocked() {
		return remainingSecondsAsBlocked;
	}

	public void setRemainingSecondsAsBlocked(long remainingSecondsAsBlocked) {
		this.remainingSecondsAsBlocked = remainingSecondsAsBlocked;
	}
}
