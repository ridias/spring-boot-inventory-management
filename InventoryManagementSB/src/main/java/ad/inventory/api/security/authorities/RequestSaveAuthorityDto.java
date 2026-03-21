package ad.inventory.api.security.authorities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSaveAuthorityDto {

	private Long idRole;
	private List<AuthorityDto> authorities;
	
	public RequestSaveAuthorityDto() {
		this.authorities = new ArrayList<>();
	}
}
