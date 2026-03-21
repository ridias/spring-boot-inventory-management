package ad.inventory.api.security.two_factor;

import lombok.Getter;

@Getter
public class RequestCodeValidationDto {

	private final Long idUser;
	private final String code;
	
	public RequestCodeValidationDto(String code, Long idUser) {
		this.code = code;
		this.idUser = idUser;
	}
}
