package ad.inventory.api.security.two_factor;

import org.springframework.context.annotation.Configuration;


@Configuration
public class GeneratorCode2FAFactory {
	
	public GeneratorCode2FA create(Integer type2FA, String key) {
		if(key == null || type2FA == null) return null;
		
		GeneratorCode2FA generatorCode = new CustomGeneratorCode2FA();
		
		return generatorCode;
	}
}
