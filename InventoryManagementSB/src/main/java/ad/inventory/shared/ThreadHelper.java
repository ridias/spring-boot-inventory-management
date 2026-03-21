package ad.inventory.shared;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadHelper {
	
	private ThreadHelper() {
		
	}
	
	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		}catch(InterruptedException ex) {
			log.error("It wasn't possible to wait {} ms", milliseconds);
			Thread.currentThread().interrupt();
		}
	}
}
