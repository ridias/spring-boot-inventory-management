package ad.inventory.api.security.authorities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.api.roles.RoleRepository;
import ad.inventory.api.security.permissions.PermissionRepository;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.InvalidParameter;
import ad.inventory.shared.exceptions.InvalidRequest;
import ad.inventory.shared.exceptions.NotFound;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private RoleRepository userGroupRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	public boolean doesHavePermissionToRead(String role, String permissionName) {
		if(role == null || permissionName == null)
			return false;
		
		var result = this.authorityRepository.getPermissionReadByRoleAndPermissionName(role, permissionName);
		if(result.isEmpty())
			return false;
		
		return result.get().getActive();
	}
	
	public boolean doesHavePermissionToRead(String role, Long permissionId) {
		if(role == null || permissionId == null)
			return false;
		
		var result = this.authorityRepository.getPermissionReadByRoleAndPermissionId(role, permissionId);
		if(result.isEmpty())
			return false;
		
		return result.get().getActive();
	}
	
	public boolean doesHavePermissionToUpdate(String role, String permissionName) {
		if(role == null || permissionName == null)
			return false;
		
		var result = this.authorityRepository.getPermissionUpdateByRoleAndPermissionName(role, permissionName);
		if(result.isEmpty())
			return false;
		
		return result.get().getActive();
	}
	
	public boolean doesHavePermissionToCreate(String role, String permissionName) {
		if(role == null || permissionName == null)
			return false;
		
		var result = this.authorityRepository.getPermissionCreateByRoleAndPermissionName(role, permissionName);
		if(result.isEmpty())
			return false;
		
		return result.get().getActive();
	}
	
	public boolean doesHavePermissionToDelete(String role, String permissionName) {
		if(role == null || permissionName == null)
			return false;
		
		var result = this.authorityRepository.getPermissionDeleteByRoleAndPermissionName(role, permissionName);
		if(result.isEmpty())
			return false;
		
		return result.get().getActive();
	}

	public ResponseDto<Boolean> doesHaveAtLeastOnePermission(RequestCheckMultiplePermissionsDto request) {
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		if(request.getIdPermissions() == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		for(int i = 0; i < request.getIdPermissions().size(); i++) {
			var requestCheck = new RequestCheckPermissionDto();
			requestCheck.setAction(request.getAction());
			requestCheck.setGroupName(request.getNameGroup());
			requestCheck.setPermissionId(request.getIdPermissions().get(i));
			
			if(doesHavePermission(requestCheck)) {
				return GeneratorResponse.ok(List.of(true));
			}
		}
		
		return GeneratorResponse.ok(List.of(false));
	}
	
	public ResponseDto<Authority> getAllByNameGroup(String nameGroup){
		if(nameGroup == null)
			return GeneratorResponse.fail(new InvalidParameter(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var groupOpt = this.userGroupRepository.findByName(nameGroup);
		if(groupOpt.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_CODE, GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_MSG));
		
		var group = groupOpt.get();
		var list = this.authorityRepository.getAllByIdGroup(group.getId());
		return GeneratorResponse.ok(list);
	}
	
	public ResponseDto<AuthorityDto> getAllById(Long idGroup){
		if(idGroup == null)
			return GeneratorResponse.fail(new InvalidParameter(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var groupOpt = this.userGroupRepository.findById(idGroup);
		if(groupOpt.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_CODE, GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_MSG));
		
		var group = groupOpt.get();
		var list = this.authorityRepository.getAllByIdGroup(group.getId());
		var listDto = new ArrayList<AuthorityDto>();
		for(int i = 0; i < list.size(); i++) {
			listDto.add(AuthorityMapper.transform(list.get(i)));
		}
		
		return GeneratorResponse.ok(listDto);
	}
	
	public boolean doesHavePermission(RequestCheckPermissionDto request){
		if(request == null)
			return false;
		
		var groupOpt = this.userGroupRepository.findByName(request.getGroupName());
		if(groupOpt.isEmpty())
			return false;
		
		var permissionOpt = this.permissionRepository.findById(request.getPermissionId());
		if(permissionOpt.isEmpty())
			return false;
		
		var idGroup = groupOpt.get().getId();
		var page = permissionOpt.get();
		var idPermission = page.getId();
		var action = request.getAction();
		var permissionGroup = this.authorityRepository.getByIdGroupAndIdPermission(idGroup, idPermission);
		if(permissionGroup.isEmpty())
			return false;
		
		Boolean doesHavePermission = false;
		if( ("read".equals(action) && permissionGroup.get(0).getActiveRead() && page.getIsRead()) || 
			("create".equals(action) && permissionGroup.get(0).getActiveCreate() && page.getIsCreate()) || 
			("update".equals(action) && permissionGroup.get(0).getActiveUpdate() && page.getIsUpdate()) || 
			("delete".equals(action) && permissionGroup.get(0).getActiveDelete() && page.getIsDelete())) {
			
			doesHavePermission = true;
		}
		
		return doesHavePermission;
	}
	
	public ResponseDto<AuthorityDto> getAllAuthoritiesByPermissionIdAndIdGroup(Long idPermission, Long idGroup){
		if(idPermission == null || idGroup == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var permissionOpt = this.permissionRepository.findById(idPermission);
		if(permissionOpt.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_PAGE_NOT_FOUND_CODE, GeneralErrors.ERR_404_PAGE_NOT_FOUND_MSG));
		
		var authorities = this.authorityRepository.getByIdGroupAndIdPermission(idGroup, idPermission);
		var authoritiesDto = new ArrayList<AuthorityDto>();
		
		for(int i = 0; i < authorities.size(); i++) {
			authoritiesDto.add(AuthorityMapper.transform(authorities.get(i)));
		}
		
		return GeneratorResponse.ok(authoritiesDto);
	}
	
}
