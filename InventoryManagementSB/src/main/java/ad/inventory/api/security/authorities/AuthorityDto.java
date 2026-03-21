package ad.inventory.api.security.authorities;

import ad.inventory.api.security.permissions.Permission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityDto {

	private Permission permission;
	private Boolean activeCreate;
	private Boolean activeRead;
	private Boolean activeUpdate;
	private Boolean activeDelete;
}
