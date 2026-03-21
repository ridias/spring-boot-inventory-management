package ad.inventory.shared;

import java.security.SecureRandom;

public class RandomLib {

	private static final SecureRandom random = new SecureRandom();
	
	private RandomLib() {
		
	}

	public static int getRandomNumberBetween(int low, int upper) {
		return random.nextInt(upper-low) + low;
	}
}
