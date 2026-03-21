package ad.inventory.api.security.authorities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCheckMultiplePermissionsDto {

	private List<Long> idPermissions;
	private String action;
	private String nameGroup;
	
	public RequestCheckMultiplePermissionsDto(List<Long> idPermissions, String action, String nameGroup) {
		super();
		this.idPermissions = idPermissions;
		this.action = action;
		this.nameGroup = nameGroup;
	}
	
	public RequestCheckMultiplePermissionsDto() {
		this.idPermissions = new ArrayList<>();
	}
}
