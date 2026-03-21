package ad.inventory.api.users;

import lombok.Getter;

@Getter
public class RequestPasswordAsRenovationDto {

	private final Long id;
	private final String password;
	
	public RequestPasswordAsRenovationDto(
			Long id,
			String password) {
		
		this.id = id;
		this.password = password;
	}
}
