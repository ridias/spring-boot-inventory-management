package ad.inventory.api.security.two_factor;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import ad.inventory.api.audit.AuditService;
import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.api.security.JwtUtils;
import ad.inventory.api.users.UserLocalRepository;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.RandomLib;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.InvalidRequest;
import ad.inventory.shared.exceptions.NotFound;
import ad.inventory.shared.exceptions.ServerError;

@Service
public class Authentication2FAService {
	
	private static final Logger log = LoggerFactory.getLogger(Authentication2FAService.class);
	
	@Autowired
	private UserLocalRepository userRepository;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private GeneratorCode2FAFactory generatorCodeFACreator;
	@Autowired
	private AuditService auditService;
	
	@Value("${inventory.app.dev}")
	protected String isDevMode;
	
	private static int limitSendCodeSmsInMs = 120000;
	
	public ResponseDto<Boolean> generateCode(Long idUser){
		if(idUser == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userRepository.findById(idUser);
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(
					GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var user = userInDb.get();
		
		if(user.getLastTimeGenerated2fa() != null) {
			var lastTime2fa = user.getLastTimeGenerated2fa();
			var now = LocalDateTime.now();
			lastTime2fa = lastTime2fa.plusSeconds(limitSendCodeSmsInMs / 1000 / 2);
			if(lastTime2fa.isAfter(now)) {
				return GeneratorResponse.fail(new ServerError(
						GeneralErrors.ERR_500_SMS_REQUEST_SENT_RECENTLY_CODE, GeneralErrors.ERR_500_SMS_REQUEST_SENT_RECENTLY_MSG));
			}
		}
		
		String code = RandomLib.getRandomNumberBetween(100000, 999999) + "";
		this.userRepository.updateRetries2faById(3, idUser);
		
		Integer type2FA = user.getType2FA();
		if(type2FA == null) type2FA = 1;
		
		if(isDevMode.equals("false")){
			boolean codeSent = false;
			GeneratorCode2FA generatorCode = generatorCodeFACreator.create(type2FA, code);
			if(generatorCode == null) {
				log.error("CodeGenerator 2FA Service not defined, type2fa = " + type2FA);
				return GeneratorResponse.fail(new ServerError(
						GeneralErrors.ERR_500_SMS_OPERATOR_NAME_UNKNOWN_CODE, GeneralErrors.ERR_500_SMS_OPERATOR_NAME_UNKNOWN_MSG));
			}
			
			codeSent = generatorCode.sendCodeVerification(user, code);
			if(!codeSent) {
				return GeneratorResponse.fail(new ServerError(
						GeneralErrors.ERR_500_SMS_CODE_NOT_SENT_CODE, GeneralErrors.ERR_500_SMS_CODE_NOT_SENT_MSG));
			}
		}
		
		LocalDateTime now = LocalDateTime.now();
		this.userRepository.updateCode2faAndLastTimeGenerated2faById(code, now, idUser);
		return GeneratorResponse.ok(List.of(true));
	}
	
	public ResponseDto<Boolean> regenerateCode(Long idUser){
		return this.generateCode(idUser);
	}
	
	public ResponseDto<ResponseCodeValidationDto> validateCode(Long idUser, String codeToValidate, RequestSaveAudit requestAudit){
		if(idUser == null || codeToValidate == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userRepository.findById(idUser);
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(
					GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var user = userInDb.get();
		String code = user.getCode2fa();
		Integer retries = user.getRetries2fa();
		String status = "SUCCESS_2FA";
		
		if(retries == null) {
			retries = 3;
		}else if(retries == 0) {
			status = "FAILED_2FA";
			this.saveAudit(status, retries, requestAudit);
			return GeneratorResponse.fail(new ServerError(
					GeneralErrors.ERR_500_2FA_FAILED_LIMIT_CODE, GeneralErrors.ERR_500_2FA_FAILED_LIMIT_MSG));
		}
		
		if(user.getLastTimeGenerated2fa() != null) {
			var lastTime2fa = user.getLastTimeGenerated2fa();
			var now = LocalDateTime.now();
			lastTime2fa = lastTime2fa.plusSeconds(limitSendCodeSmsInMs / 1000);
			if(lastTime2fa.isBefore(now)) {
				status = "CODE_EXPIRED";
				this.saveAudit(status, retries, requestAudit);
				return GeneratorResponse.fail(new ServerError(
						GeneralErrors.ERR_500_2FA_CODE_EXPIRED_CODE, GeneralErrors.ERR_500_2FA_CODE_EXPIRED_MSG));
			}
		}
		
		var response = new ResponseCodeValidationDto();
		if(!codeToValidate.equals(code)) {
			retries -= 1;
			status = "FAILED_2FA";
			this.userRepository.updateRetries2faById(retries, idUser);
			response.setValid(false);
		}else {
			this.userRepository.updateCode2faAndLastTimeGenerated2faById(null, null, idUser);
			this.userRepository.updateRetries2faById(3, idUser);
			response.setValid(true);
			
			var roles = user.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.toList();

			String token = this.jwtUtils.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
			response.setToken(token);
			response.setRole(roles.get(0));
		}
		
		response.setRetries(retries);
		
		this.saveAudit(status, retries, requestAudit);
		return GeneratorResponse.ok(List.of(response));
	}
	
	protected void saveAudit(String status, int retries, RequestSaveAudit requestAudit) {
		var query = "status=" + status + "&retries=" + retries;
		this.auditService.saveAsReadAuditRequest(requestAudit, query);
	}
	
}
