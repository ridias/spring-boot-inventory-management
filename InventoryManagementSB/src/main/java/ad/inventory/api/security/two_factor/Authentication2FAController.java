package ad.inventory.api.security.two_factor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.api.security.CookieHelper;
import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.dtos.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/2fa")
public class Authentication2FAController {

	@Autowired
	private Authentication2FAService auth2faService;
	@Autowired
	private CookieHelper cookieCreator;
	
	@GetMapping(value = "/regenerate")
	public ResponseEntity<ResponseDto<Boolean>> regenerateCode(
			@RequestParam(required = true) Long idUser){
		
		var response = this.auth2faService.regenerateCode(idUser);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/validation")
	public ResponseEntity<ResponseDto<ResponseCodeValidationDto>> validateCode(
			@RequestBody RequestCodeValidationDto request,
			HttpServletRequest req,
			HttpServletResponse res){
		
		var idUser = request.getIdUser();
		var code = request.getCode();
		String ip = req.getRemoteAddr();
		String browser = req.getHeader("User-Agent");
		var requestSaveAudit = new RequestSaveAudit(38L, idUser, ip, "", browser, "{}");
		
		var response = this.auth2faService.validateCode(idUser, code, requestSaveAudit);
		if(!response.isSuccess()) {
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
		}
		
		if(Boolean.TRUE.equals(response.getItems().get(0).getValid())) {
			String token = response.getItems().get(0).getToken();
			res.addHeader("Set-Cookie", 
					this.cookieCreator.createCookieAsNone(
							this.cookieCreator.createCookie(token, true, true, 8 * 60 * 60)));
			
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
