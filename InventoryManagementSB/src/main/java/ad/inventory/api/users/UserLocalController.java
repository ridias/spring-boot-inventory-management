package ad.inventory.api.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ad.inventory.api.audit.AuditGeneratorService;
import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.api.security.JwtUtils;
import ad.inventory.api.security.authorization_routes.RequirePermission;
import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.env.GeneralPages;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/users")
public class UserLocalController {

	@Autowired
	private UserLocalService userService;
    @Autowired
    private AuditGeneratorService auditGenerator;
	@Autowired
	private JwtUtils jwtUtils;
	
	private static final String USER_AGENT = "User-Agent";
	
	
	@GetMapping("/all")
    @RequirePermission(permissionId = GeneralPages.ID_PERMISSION_USERS, action = "read")
	public ResponseEntity<ResponseDto<UserLocalDto>> getAll(
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		ResponseDto<UserLocalDto> response = this.userService.getAll();
		
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/by/pagination")
    @RequirePermission(permissionId = GeneralPages.ID_PERMISSION_USERS, action = "read")
	public ResponseEntity<ResponseDto<UserLocalRowDto>> getByPagination(
			@RequestParam(required = false) int limit,
			@RequestParam(required = false) int page,
			@RequestParam(required = false) Long idGroup,
			@RequestParam(required = false) String active,
			@RequestParam(required = false) String orderById,
			@RequestParam(required = false) String orderByUsername,
			@RequestParam(required = false) String orderByEmail,
			@RequestParam(required = false) String orderByFirstName,
			@RequestParam(required = false) String orderByLastName,
			@RequestParam(required = false) String orderByGroup,
			@RequestParam(required = false) String orderByActive,
			@RequestParam(required = false) String orderByLastSession,
			@RequestParam(required = false) String search,
			@CookieValue(name = "inventoryMngToken", required = true) String token,
			HttpServletRequest req){
		
		var requestSaveAudit = auditGenerator.createRequestSaveAudit(token, req, 26L);
		var request = new RequestPaginationUserDto(limit, page, search, idGroup, active,
				orderById, orderByUsername, orderByEmail, orderByFirstName, orderByLastName, 
				orderByGroup, orderByActive, orderByLastSession);
		
		var response = this.userService.getAllByPagination(request, requestSaveAudit);
				
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/total")
    @RequirePermission(permissionId = GeneralPages.ID_PERMISSION_USERS, action = "read")
	public ResponseEntity<ResponseDto<Long>> getTotal(
			@RequestParam(required = false) Long idGroup,
			@RequestParam(required = false) String active,
			@RequestParam(required = false) String search,
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		var request = new RequestPaginationUserDto(0, 0, search, idGroup, active,
				"", "", "", "", "", "", "", "");
		
		var response = this.userService.getAllTotal(request);
		
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/details/{id}")
    @RequirePermission(permissionId = GeneralPages.ID_PERMISSION_USERS, action = "read")
	public ResponseEntity<ResponseDto<UserLocalDetailsDto>> getDetailsById(
			@PathVariable Long id,
			@CookieValue(name = "inventoryMngToken", required = true) String token,
			HttpServletRequest req){

		var requestSaveAudit = auditGenerator.createRequestSaveAudit(token, req, 27L);
		
		var response = this.userService.getDetailsById(id, requestSaveAudit);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/profile/details")
	public ResponseEntity<ResponseDto<UserLocalDto>> getProfileDetails(
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		Long idUserLoggedIn = jwtUtils.getUserIdFromJwtToken(token);

		var response = this.userService.getDetailsProfileById(idUserLoggedIn);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/token/details")
	public ResponseEntity<ResponseDto<UserLocalDto>> getTokenDetails(
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		Long roleId = jwtUtils.getRoleIdFromJwtToken(token);
		var dto = new UserLocalDto();
		dto.setRoleId(roleId);
		
		var response = GeneratorResponse.ok(List.of(dto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/is/renovation")
	public ResponseEntity<ResponseDto<RenovationTimeDto>> isTimeRenovationPassword(
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		Long idUserLoggedIn = jwtUtils.getUserIdFromJwtToken(token);
		
		var response = this.userService.isRenovationPassword(idUserLoggedIn);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("")
    @RequirePermission(permissionId = GeneralPages.ID_PERMISSION_USERS, action = "create")
	public ResponseEntity<ResponseDto<Boolean>> save(
			@RequestBody RequestSaveUserDto request,
			@CookieValue(name = "inventoryMngToken", required = true) String token,
			HttpServletRequest req){
	
		var requestSaveAudit = auditGenerator.createRequestSaveAudit(token, req, 28L);
		
		var response = this.userService.save(request, requestSaveAudit);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("")
    @RequirePermission(permissionId = GeneralPages.ID_PERMISSION_USERS, action = "update")
	public ResponseEntity<ResponseDto<Boolean>> update(
			@RequestBody RequestSaveUserDto request,
			@CookieValue(name = "inventoryMngToken", required = true) String token,
			HttpServletRequest req){
		
		Long idUserLoggedIn = jwtUtils.getUserIdFromJwtToken(token);
		String ip = req.getRemoteAddr();
		String browser = req.getHeader(USER_AGENT);
		
		var requestSaveAudit = new RequestSaveAudit(29L, idUserLoggedIn, ip, "", browser, "{}");
		
		var response = this.userService.update(request, requestSaveAudit);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return ResponseEntity.ok()
				.body(response);
	}
	
	@PutMapping("/renovation")
	public ResponseEntity<ResponseDto<Boolean>> updatePasswordAsRenovation(
			@RequestBody RequestPasswordAsRenovationDto request,
			@CookieValue(name = "inventoryMngToken", required = true) String token,
			HttpServletRequest req){
		
		Long idUserLoggedIn = jwtUtils.getUserIdFromJwtToken(token);
		String ip = req.getRemoteAddr();
		String browser = req.getHeader(USER_AGENT);
		
		var requestSaveAudit = new RequestSaveAudit(29L, idUserLoggedIn, ip, "", browser, "{}");
		var newRequest = new RequestPasswordAsRenovationDto(idUserLoggedIn, request.getPassword());
		var response = this.userService.updatePasswordAsRenovation(newRequest, requestSaveAudit);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/image")
	public ResponseEntity<ResponseDto<Boolean>> update(
			@RequestBody RequestSaveUserImageDto request,
			@CookieValue(name = "inventoryMngToken", required = true) String token){
		
		Long idUserLoggedIn = jwtUtils.getUserIdFromJwtToken(token);
		
		var response = this.userService.updateImage(request, idUserLoggedIn);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
    @RequirePermission(permissionId = GeneralPages.ID_PERMISSION_USERS, action = "delete")
	public ResponseEntity<ResponseDto<Boolean>> deleteById(
			@PathVariable Long id,
			@CookieValue(name = "inventoryMngToken", required = true) String token,
			HttpServletRequest req){
		
		Long idUserLoggedIn = jwtUtils.getUserIdFromJwtToken(token);
		String ip = req.getRemoteAddr();
		String browser = req.getHeader(USER_AGENT);
		
		var requestSaveAudit = new RequestSaveAudit(30L, idUserLoggedIn, ip, "", browser, "{}");
		
		var response = this.userService.deleteById(id, idUserLoggedIn, requestSaveAudit);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response,
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
