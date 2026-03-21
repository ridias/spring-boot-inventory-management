package ad.inventory.api.users;

import ad.inventory.shared.dtos.SimpleDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLocalDetailsDto {

	private Long id;
	private String email;
	private String username;
	private SimpleDto role;
	private String firstName;
	private String lastName;
	private Boolean active;
	private Integer type2FA;
	
}
