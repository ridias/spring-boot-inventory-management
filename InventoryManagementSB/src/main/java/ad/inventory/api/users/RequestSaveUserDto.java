package ad.inventory.api.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSaveUserDto {

	private Long id;
	private String email;
	private String username;
	private Long idRole;
	private String firstName;
	private String lastName;
	private String password;
	private Boolean active;
	private Integer type2FA;
	
}
