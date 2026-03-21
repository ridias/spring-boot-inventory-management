package ad.inventory.api.roles;

import java.util.List;

import ad.inventory.api.security.authorities.AuthorityDto;

public class RoleMapper {

	private RoleMapper() {
		
	}

	public static RoleDto transformToRoleDto(RoleByTotalUsers e) {
		if(e == null) return null;
		return new RoleDto(e.getId(), 
				e.getName(), 
				e.getDescription(), 
				e.getTotal());
	}
	
	public static Role transformToRole(RequestSaveRoleDto request, Role group) {
		if(request == null || group == null) return group;

		group.setDescription(request.getDescription());
		group.setId(request.getId());
		group.setName(request.getName());
		group.setHidden(false);
		return group;
	}
	
	public static RoleDetailsDto transformToUserDetails(Role group, List<AuthorityDto> items) {
		var dto = new RoleDetailsDto();
		dto.setId(group.getId());
		dto.setName(group.getName());
		dto.setDescription(group.getDescription());
		dto.setHidden(group.getHidden());
		dto.setAuthorities(items);
		return dto;
	}
}
