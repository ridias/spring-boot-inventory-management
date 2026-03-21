package ad.inventory.api.security.authorities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthoritySDto {

	private Long idPermission;
	private Boolean activeCreate;
	private Boolean activeRead;
	private Boolean activeUpdate;
	private Boolean activeDelete;
}
