package ad.inventory.api.security.two_factor;

import ad.inventory.api.users.UserLocal;

public interface GeneratorCode2FA {

	boolean sendCodeVerification(UserLocal user, String code);
}
