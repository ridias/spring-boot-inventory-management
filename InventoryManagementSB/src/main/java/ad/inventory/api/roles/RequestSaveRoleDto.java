package ad.inventory.api.roles;

import java.util.ArrayList;
import java.util.List;

import ad.inventory.api.security.authorities.AuthoritySDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSaveRoleDto {

	private Long id;
	private String name;
	private String description;
	private List<AuthoritySDto> permissions;
	
	public RequestSaveRoleDto() {
		this.permissions = new ArrayList<>();
	}
}
