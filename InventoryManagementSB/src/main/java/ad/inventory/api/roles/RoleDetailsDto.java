package ad.inventory.api.roles;

import java.util.ArrayList;
import java.util.List;

import ad.inventory.api.security.authorities.AuthorityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDetailsDto {

	private Long id;
	private String name;
	private String description;
	private Boolean hidden;
	private List<AuthorityDto> authorities;
	
	public RoleDetailsDto() {
		this.authorities = new ArrayList<>();
	}
}
