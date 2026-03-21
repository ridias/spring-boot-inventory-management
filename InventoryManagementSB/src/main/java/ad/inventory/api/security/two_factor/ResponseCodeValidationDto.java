package ad.inventory.api.security.two_factor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCodeValidationDto {

	private Boolean valid;
	private Integer retries;
	private String token;
	private String role;
	
}
