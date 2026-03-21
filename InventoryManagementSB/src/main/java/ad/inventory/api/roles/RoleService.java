package ad.inventory.api.roles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ad.inventory.api.audit.AuditService;
import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.api.security.authorities.Authority;
import ad.inventory.api.security.authorities.AuthorityId;
import ad.inventory.api.security.authorities.AuthorityRepository;
import ad.inventory.api.security.authorities.AuthoritySDto;
import ad.inventory.api.security.authorities.AuthorityService;
import ad.inventory.api.security.permissions.Permission;
import ad.inventory.api.security.permissions.PermissionRepository;
import ad.inventory.shared.DirectionSortLib;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.dtos.SimpleDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.Duplicate;
import ad.inventory.shared.exceptions.InvalidParameter;
import ad.inventory.shared.exceptions.InvalidRequest;
import ad.inventory.shared.exceptions.NotFound;
import ad.inventory.shared.exceptions.ServerError;

import org.springframework.data.domain.Sort;

@Service
public class RoleService {

	@Autowired 
	private RoleRepository roleRepository;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private AuditService auditService;
	
	public List<SimpleDto> getUnlinkedRoleClientIds(List<Long> ids) {
		List<SimpleDto> result = new ArrayList<>();
		if(ids == null || ids.isEmpty())
			return result;
		
		List<Long> idsFiltered = ids.stream().filter(e -> e.equals(1L) || e.equals(2L) || e.equals(3L)).toList();
		var groups = this.roleRepository.findAllById(idsFiltered);
		for(int i = 0; i < groups.size(); i++) {
			result.add(new SimpleDto(groups.get(i).getId(), groups.get(i).getName(), ""));
		}
		
		return result;
	}
	
	
	public ResponseDto<SimpleDto> getAll(){
		var result = new ArrayList<SimpleDto>();
		var groups = this.roleRepository.getAllByHidden(false);
				
		for(int i = 0; i < groups.size(); i++) {
			var group = groups.get(i);
			result.add(new SimpleDto(group.getId(), group.getName(), ""));
		}
		
		return GeneratorResponse.ok(result);
	}

	public ResponseDto<RoleDto> getByPagination(
			RequestPaginationRoleDto request,
			RequestSaveAudit requestAudit){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		List<RoleDto> result = new ArrayList<>();
		String searchVal = request.getSearch();
		Page<RoleByTotalUsers> resultSet = null;
		PageRequest pageRequest = this.getPageRequest(request);
		
		resultSet = searchVal.isEmpty() 
				? this.roleRepository.findUserCount(pageRequest)
				: this.roleRepository.findUserCount(searchVal, pageRequest);
		
		if(!resultSet.getContent().isEmpty()) {
			List<RoleByTotalUsers> userGroups = resultSet.getContent();
			for(int i = 0; i < userGroups.size(); i++) {
				result.add(RoleMapper.transformToRoleDto(userGroups.get(i)));
			}
		}
		
		this.auditService.saveAsReadAuditRequest(requestAudit, request.toString());
		return GeneratorResponse.ok(result);
	}
	
	public ResponseDto<Long> getTotalPages(){
		var totalPages = 0L;
		totalPages = this.roleRepository.getTotal();
		
		return GeneratorResponse.ok(List.of(totalPages));
	}
	
	public ResponseDto<Long> getTotalPages(String search){
		var totalPages = 0L;
		totalPages = this.roleRepository.getTotal(search);
		
		return GeneratorResponse.ok(List.of(totalPages));
	}
	
	public ResponseDto<RoleDetailsDto> getDetailsById(Long id, RequestSaveAudit requestAudit){
		if(id == null)
			return GeneratorResponse.fail(new InvalidParameter(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userGroupInDb = this.roleRepository.findById(id);
		if(userGroupInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_CODE, GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_MSG));
		
		var response = this.authorityService.getAllById(id);
		if(!response.isSuccess())
			return GeneratorResponse.fail(null);
		
		var items = response.getItems();
		var dto = RoleMapper.transformToUserDetails(userGroupInDb.get(), items);
		
		this.auditService.saveAsReadAuditRequest(requestAudit, "id=" + id);
		return GeneratorResponse.ok(List.of(dto));
	}
	
	public ResponseDto<Boolean> save(RequestSaveRoleDto request, RequestSaveAudit requestAudit){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));

		var name = request.getName();
		var groupsInDb = this.roleRepository.getAllByName(name);
		if(!groupsInDb.isEmpty())
			return GeneratorResponse.fail(new Duplicate(GeneralErrors.ERR_400_NAME_DUPLICATED_CODE, GeneralErrors.ERR_400_NAME_DUPLICATED_MSG));
		
		var userGroupToSave = RoleMapper.transformToRole(request, new Role());
		
		var userGroupValidator = new RoleValidator(userGroupToSave);
		if(userGroupValidator.isRequiredFieldsNull())
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_UNDEFINED_FIELDS_CODE, GeneralErrors.ERR_400_NAME_DUPLICATED_MSG));
		
		var codeError = userGroupValidator.getCodeAfterValidation();
		var message = userGroupValidator.getMessageAfterValidation();
		if(!message.equals("OK"))
			return GeneratorResponse.fail(new InvalidRequest(codeError, message));
		
		userGroupToSave.setId(null);
		var userGroupSaved = this.roleRepository.save(userGroupToSave);
		var allPermissions = permissionRepository.findAll();
		
		var authoritiesToLink = this.generateAuthorities(request.getPermissions(), allPermissions, userGroupSaved);
		authoritiesToLink.forEach(auth -> this.authorityRepository.save(auth));
		
		this.auditService.saveAsCreateAuditRequest(requestAudit, userGroupSaved.toString());
		
		return GeneratorResponse.ok(List.of(true));
	}

	public ResponseDto<Boolean> update(RequestSaveRoleDto request, RequestSaveAudit requestAudit){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var groupInDb = this.roleRepository.findById(request.getId());
		if(groupInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_CODE, GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_MSG));
	
		var previousParameters = groupInDb.get().toString();
		var name = request.getName();
		var groupsInDb = this.roleRepository.getAllByName(name);
		
		for(int i = 0; i < groupsInDb.size(); i++) {
			var group = groupsInDb.get(i);
			if(group.getName().equals(request.getName()) && !group.getId().equals(request.getId())) {
				return GeneratorResponse.fail(new Duplicate(GeneralErrors.ERR_400_NAME_DUPLICATED_CODE, GeneralErrors.ERR_400_NAME_DUPLICATED_MSG));
			}
		}
		
		var userGroupToUpdate = RoleMapper.transformToRole(request, groupInDb.get());
		userGroupToUpdate.setHidden(groupInDb.get().getHidden());
		
		var userGroupValidator = new RoleValidator(userGroupToUpdate);
		if(userGroupValidator.isRequiredFieldsNull())
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_UNDEFINED_FIELDS_CODE, GeneralErrors.ERR_400_UNDEFINED_FIELDS_MSG));
		
		var codeError = userGroupValidator.getCodeAfterValidation();
		var message = userGroupValidator.getMessageAfterValidation();
		if(!message.equals("OK"))
			return GeneratorResponse.fail(new InvalidRequest(codeError, message));
		
		var userGroupUpdated = this.roleRepository.save(userGroupToUpdate);
		var allPermissions = permissionRepository.findAll();
		
		var authoritiesToLink = this.generateAuthorities(request.getPermissions(), allPermissions, userGroupUpdated);
		authoritiesToLink.forEach(auth -> this.authorityRepository.save(auth));
		
		this.auditService.saveAsUpdateAuditRequest(requestAudit, previousParameters, userGroupToUpdate.toString());
		
		return GeneratorResponse.ok(List.of(true));
	}
	
	public ResponseDto<Boolean> deleteById(Long id, RequestSaveAudit requestAudit){
		if(id == null)
			return GeneratorResponse.fail(new InvalidParameter(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var totalUsers = this.roleRepository.getTotalUsersByIdGroup(id);
		if(totalUsers > 0)
			return GeneratorResponse.fail(new ServerError(GeneralErrors.ERR_500_USER_GROUP_NOT_DELETED_CODE, GeneralErrors.ERR_500_USER_GROUP_NOT_DELETED_MSG));
		
		var userGroupInDb = this.roleRepository.findById(id);
		if(!userGroupInDb.isEmpty() && userGroupInDb.get().getHidden())
			return GeneratorResponse.ok(List.of(true));

		this.authorityRepository.deleteByIdGroup(id);
		this.roleRepository.deleteById(id);
		
		this.auditService.saveAsDeleteAuditRequest(requestAudit, "id=" + id);
		return GeneratorResponse.ok(List.of(true));
	}
	
	private List<Authority> generateAuthorities(List<AuthoritySDto> authorities, 
			List<Permission> permissions,
			Role role){
		
		var result = new ArrayList<Authority>();
		for(int i = 0; i < permissions.size(); i++) {
			var permission = permissions.get(i);
			for(int j = 0; j < authorities.size(); j++) {
				var authority = authorities.get(j);
				if(authority.getIdPermission().equals(permission.getId())) {
					var authorityToLinkId = new AuthorityId();
					authorityToLinkId.setIdGroup(role.getId());
					authorityToLinkId.setIdPermission(permission.getId());
					
					var authorityToLink = new Authority();
					authorityToLink.setActiveCreate(Boolean.TRUE.equals(permission.getIsCreate()) && Boolean.TRUE.equals(authority.getActiveCreate()));
					authorityToLink.setActiveRead(Boolean.TRUE.equals(permission.getIsRead()) && Boolean.TRUE.equals(authority.getActiveRead()));
					authorityToLink.setActiveUpdate(Boolean.TRUE.equals(permission.getIsUpdate()) && Boolean.TRUE.equals(authority.getActiveUpdate()));
					authorityToLink.setActiveDelete(Boolean.TRUE.equals(permission.getIsDelete()) && Boolean.TRUE.equals(authority.getActiveDelete()));
					authorityToLink.setRole(role);
					authorityToLink.setPermission(permission);
					authorityToLink.setId(authorityToLinkId);
					result.add(authorityToLink);
					break;
				}
			}
		}
		
		return result;
	}
	
	protected PageRequest getPageRequest(RequestPaginationRoleDto request) {
		PageRequest pageRequest = null;
		
		int limit = request.getLimit();
		int page = request.getPage();
		if(limit <= 0) limit = 10;
		if(page < 0) page = 0;
		
		if(request.getOrderById() != null && !request.getOrderById().isEmpty()) {
			Direction sort = DirectionSortLib.getDirectionSort(request.getOrderById());
			pageRequest = PageRequest.of(page,  limit, sort, "id");
		}else if(request.getOrderByName() != null && !request.getOrderByName().isEmpty()) {
			Direction sort = DirectionSortLib.getDirectionSort(request.getOrderByName());
			pageRequest = PageRequest.of(page,  limit, sort, "name");
		}else if(request.getOrderByDescription() != null && !request.getOrderByDescription().isEmpty()) {
			Direction sort = DirectionSortLib.getDirectionSort(request.getOrderByDescription());
			pageRequest = PageRequest.of(page, limit, sort, "description");
		}else if(request.getOrderByTotalUsers() != null && !request.getOrderByTotalUsers().isEmpty()) {
			Direction sort = DirectionSortLib.getDirectionSort(request.getOrderByTotalUsers());
			pageRequest = PageRequest.of(page, limit, sort, "total");
		}else {
			pageRequest = PageRequest.of(page, limit, Sort.Direction.ASC, "id");
		}
		
		return pageRequest;
	}
}
