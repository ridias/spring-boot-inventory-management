package ad.inventory.api.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRecoveryUpdatePasswordDto {

	private String password;
	private String token;
	
}
