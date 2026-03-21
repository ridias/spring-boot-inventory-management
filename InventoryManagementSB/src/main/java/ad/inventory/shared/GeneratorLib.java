package ad.inventory.shared;

import java.util.UUID;

public class GeneratorLib {

	private GeneratorLib() {
		
	}

	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public static String generateCodeValidation() {
		return RandomLib.getRandomNumberBetween(100000, 1000000) + "";
	}
}
