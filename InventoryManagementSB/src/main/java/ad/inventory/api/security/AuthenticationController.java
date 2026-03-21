package ad.inventory.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ad.inventory.api.audit.AuditService;
import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.dtos.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1")
public class AuthenticationController {

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private AuditService auditService;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private CookieHelper cookieHelper;
	
	@GetMapping(value = "/auth/isLoggedIn")
	public ResponseEntity<String> isLoggedIn(
			@CookieValue(name = "cityeyeMasterToken", required = false) String token){
		
		if(token == null)
			return new ResponseEntity<>("0", HttpStatus.OK);
		if(!jwtUtils.validateToken(token))
			return new ResponseEntity<>("0", HttpStatus.OK);
		
		return new ResponseEntity<>("1", HttpStatus.OK);
	}
	
	@PostMapping(value = "/auth/recovery/password/request")
	public ResponseEntity<ResponseDto<Boolean>> recoveryPassword(
			@RequestBody RequestRecoveryPasswordDto request){
		
		var response = this.authenticationService.sendRecoveryPassword(request);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping(value = "/auth/recovery/password/update")
	public ResponseEntity<ResponseDto<Boolean>> recoveryPassword(
			@RequestBody RequestRecoveryUpdatePasswordDto request){
		
		var response = this.authenticationService.updatePasswordFromRecovery(request);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/auth/logout")
	public ResponseEntity<String> logout(
			@CookieValue(name = "cityeyeMasterToken", required = false) String token,
			HttpServletRequest req,
			HttpServletResponse res){
		
		if(token != null) {
			Long idUserLoggedIn = jwtUtils.getUserIdFromJwtToken(token);
			String ip = req.getRemoteAddr();
			String browser = req.getHeader("User-Agent");
			
			var requestSaveAudit = new RequestSaveAudit(39L, idUserLoggedIn, ip, "", browser, "{}");
			
			this.auditService.saveAsReadAuditRequest(requestSaveAudit, "idUser=" + idUserLoggedIn);
		}
		
		res.addHeader("Set-Cookie", 
				this.cookieHelper.createCookieAsNone(
						this.cookieHelper.createCookie("", true, true, 0)));
		
		return new ResponseEntity<>("1", HttpStatus.OK);
	}
}
