package ad.inventory.api.security.two_factor;

import ad.inventory.api.users.UserLocal;

public class CustomGeneratorCode2FA implements GeneratorCode2FA {

	@Override
	public boolean sendCodeVerification(UserLocal user, String code) {
		return true;
	}

}
