package ad.inventory.api.security.authorities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ad.inventory.api.security.JwtUtils;
import ad.inventory.api.security.permissions.Permission;
import ad.inventory.api.security.permissions.PermissionService;
import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.dtos.ResponseDto;

@RestController
@RequestMapping("api/v1/authorities")
public class AuthorityController {

	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private JwtUtils jwtUtils;
	
	@GetMapping("")
	public ResponseEntity<?> doesHaveAtLeastOnePermission(
			@RequestParam("idPermissions") List<Long> idPermissions,
			@RequestParam("action") String action,
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		String nameGroup = jwtUtils.getRoleNameFromJwtToken(token);
		var request = new RequestCheckMultiplePermissionsDto(
				idPermissions, action, nameGroup);
		
		var response = this.authorityService.doesHaveAtLeastOnePermission(request);
		if(!response.isSuccess())
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/read")
	public ResponseEntity<Boolean> doesHavePermission(
			@RequestParam("permissionId") Long permissionId,
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		String nameGroup = jwtUtils.getRoleNameFromJwtToken(token);
		var response = this.authorityService.doesHavePermissionToRead(nameGroup, permissionId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/permissions/all")
	public ResponseEntity<ResponseDto<Permission>> getAllPermissions(){
		
		var response = this.permissionService.getAll();
		if(!response.isSuccess())
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/permissions/group/{id}")
	public ResponseEntity<ResponseDto<AuthorityDto>> getPermissionsByIdGroup(
			@PathVariable Long id){
		
		var response = this.authorityService.getAllById(id);
		if(!response.isSuccess())
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/permission")
	public ResponseEntity<ResponseDto<AuthorityDto>> getAllAuthoritiesByPermissionName(
			@RequestParam Long idPermission,
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		Long idGroup = jwtUtils.getGroupIdFromJwtToken(token);
		var response = this.authorityService.getAllAuthoritiesByPermissionIdAndIdGroup(idPermission, idGroup);
		if(!response.isSuccess())
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/menu")
	public ResponseEntity<?> getAuthoritiesMenu(
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		Long idGroup = jwtUtils.getGroupIdFromJwtToken(token);
		
		var response = this.authorityService.getAllById(idGroup);
		if(!response.isSuccess())
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
