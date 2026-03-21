package ad.inventory.api.security;

import lombok.Getter;

@Getter
public class RequestLoginDto {

	private final String email;
	private final String password;
	
	public RequestLoginDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
