package ad.inventory.api.audit;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ad.inventory.api.security.authorization_routes.RequirePermission;
import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.env.GeneralPages;

public class AuditController {

	@Autowired
	private AuditService auditService;
	
	@GetMapping("/by/pagination")
	@RequirePermission(permissionId = GeneralPages.ID_PERMISSION_AUDIT, action = "read")
	public ResponseEntity<ResponseDto<AuditRowDto>> getByPagination(
			@RequestParam int limit,
			@RequestParam int page,
			@RequestParam(required = false) Long idUser,
			@RequestParam(required = false) Long idAction,
			@RequestParam(required = false) LocalDateTime start,
			@RequestParam(required = false) LocalDateTime end,
			@RequestParam(required = false) String orderByData,
			@RequestParam(required = false) String orderByAction,
			@RequestParam(required = false) String orderByUser,
			@RequestParam(required = false) String orderByIp,
			@RequestParam(required = false) String orderByBrowser,
			@RequestParam(required = false) String search){
		
		var request = new RequestPaginationAuditDto(limit, page, search, idUser,
				idAction, start, end, orderByData, orderByAction, orderByUser, orderByIp, orderByBrowser);

		var response = this.auditService.getByPagination(request);
		
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/total")
	@RequirePermission(permissionId = GeneralPages.ID_PERMISSION_AUDIT, action = "read")
	public ResponseEntity<ResponseDto<Long>> getTotal(
			@RequestParam(required = false) Long idUser,
			@RequestParam(required = false) Long idAction,
			@RequestParam(required = false) LocalDateTime start,
			@RequestParam(required = false) LocalDateTime end,
			@RequestParam(required = false) String search){
		
		var request = new RequestPaginationAuditDto(0, 0, search, idUser,
				idAction, start, end, "", "", "", "", "");
		
		var response = this.auditService.getTotal(request);
		
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
					ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/details/{id}")
	@RequirePermission(permissionId = GeneralPages.ID_PERMISSION_AUDIT, action = "read")
	public ResponseEntity<ResponseDto<AuditDetailsDto>> getDetailsById(
			@PathVariable Long id){
		
		var response = this.auditService.getDetailsById(id);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
					ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
