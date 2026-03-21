package ad.inventory.api.security.authorities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCheckPermissionDto {

	private String groupName;
	private Long permissionId;
	private String action;
	
	public RequestCheckPermissionDto() {
		
	}
	
	public RequestCheckPermissionDto(String groupName, Long permissionId, String action) {
		this.action = action;
		this.setGroupName(groupName);
		this.permissionId = permissionId;
	}
}
