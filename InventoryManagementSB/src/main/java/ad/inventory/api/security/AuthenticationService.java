package ad.inventory.api.security;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ad.inventory.api.users.UserLocalRepository;
import ad.inventory.api.users.UserPasswordUsedService;
import ad.inventory.emails.EmailService;
import ad.inventory.emails.MessageRequestRecoveryPasswordBundle;
import ad.inventory.emails.MessageRequestRecoveryPasswordBase;
import ad.inventory.emails.EmailMessageGenerator;
import ad.inventory.shared.GeneratorLib;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.ThreadHelper;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.InvalidParameter;
import ad.inventory.shared.exceptions.InvalidRequest;
import ad.inventory.shared.exceptions.NotFound;
import ad.inventory.shared.exceptions.ServerError;


@Service
public class AuthenticationService {

	@Autowired
	private UserLocalRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserPasswordUsedService userPasswordUsedService;
	@Autowired
	private EmailService emailService;
	
	private static int limitSendEmailInMs = 60000;
	private static int limitValidTokenInMs = 1800000;
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
	
	public ResponseDto<Boolean> sendRecoveryPassword(RequestRecoveryPasswordDto request){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		String email = request.getEmail();
		var userInDb = this.userRepository.findByEmailAndActiveAndIsDeleted(email, true, false);
		if(userInDb.isEmpty())
			return GeneratorResponse.ok(List.of(true));
		
		var user = userInDb.get();
		if(user.getLastEmailSent() != null) {
			var lastEmailSend = user.getLastEmailSent();
			var now = LocalDateTime.now();
			lastEmailSend = lastEmailSend.plusSeconds(limitSendEmailInMs / 1000);
			
			if(lastEmailSend.isAfter(now)) {
				return GeneratorResponse.fail(new ServerError(
						GeneralErrors.ERR_500_RECOVERY_PASS_SENT_RECENTLY_CODE, GeneralErrors.ERR_500_RECOVERY_PASS_SENT_RECENTLY_MSG));
			}
		}
		
		String token = GeneratorLib.generateUUID();
		try {
			String dnsClient = "http://localhost:4200";
			String urlPath = dnsClient + "/auth-password/reset/password/" + token;
			EmailMessageGenerator messageGenerator = new MessageRequestRecoveryPasswordBase(MessageRequestRecoveryPasswordBundle.CAT);
			
			Map<String, String> properties = new HashMap<>();
			properties.put("url", urlPath);
			String message = messageGenerator.getMessage(properties);
			ThreadHelper.sleep(2000);
			if(message != null) {
				this.emailService.sendEmailAsync(user.getEmail(), "Canvi de contrasenya", message);
			}else {
				log.error("Email message generated for recovery password is null!");
			}
		}catch(Exception ex) {
			log.error("It wasn't possible to send the email to " + email + ", more details: ", ex);
			return GeneratorResponse.fail(new ServerError(
					GeneralErrors.ERR_500_RECOVERY_PASS_EMAIL_NOT_SENT_CODE, GeneralErrors.ERR_500_RECOVERY_PASS_EMAIL_NOT_SENT_MSG));
		}
		
		this.userRepository.updateLastEmailSentToNowAndTokenById(user.getId(), token);
		return GeneratorResponse.ok(List.of(true));
	}
	
	public ResponseDto<Boolean> updatePasswordFromRecovery(RequestRecoveryUpdatePasswordDto request){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		String token = request.getToken();
		String password = request.getPassword();
		var users = this.userRepository.findAllByRecoveryTokenAndActiveAndIsDeleted(token, true, false);
		if(users.isEmpty())
			return GeneratorResponse.fail(new NotFound(
					GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		if(users.size() > 1)
			return GeneratorResponse.fail(new ServerError(
					GeneralErrors.ERR_500_RECOVERY_PASS_NOT_UPDATED_CODE, GeneralErrors.ERR_500_RECOVERY_PASS_NOT_UPDATED_MSG));
		
		var user = users.get(0);
		if(user.getLastEmailSent() != null) {
			var lastEmailSend = user.getLastEmailSent();
			var now = LocalDateTime.now();
			lastEmailSend = lastEmailSend.plusSeconds(limitValidTokenInMs / 1000);
			if(now.isAfter(lastEmailSend)) {
				return GeneratorResponse.fail(new ServerError(
						GeneralErrors.ERR_500_RECOVERY_PASS_TOKEN_EXPIRED_CODE, GeneralErrors.ERR_500_RECOVERY_PASS_TOKEN_EXPIRED_MSG));
			}
		}
		
		if(!this.isPasswordValid(password)) {
			return GeneratorResponse.fail(new InvalidParameter(
				GeneralErrors.ERR_400_USER_PASSWORD_VALID_CODE, GeneralErrors.ERR_400_USER_PASSWORD_VALID_MSG));
		}
		
		if(this.userPasswordUsedService.isPasswordUsed(password, user.getId()))
			return GeneratorResponse.fail(new InvalidParameter(
					GeneralErrors.ERR_400_USER_PASSWORD_ALREADY_USED_CODE, GeneralErrors.ERR_400_USER_PASSWORD_ALREADY_USED_MSG));
		
		user.setRecoveryToken(null);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setLastPasswordRenovation(LocalDateTime.now());
		this.userRepository.save(user);
		return GeneratorResponse.ok(List.of(true));
	}
	
	protected boolean isPasswordValid(String password) {
		if(password == null) return false;
		return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d\\s])[^\\s]{12,18}$", password);
	}
}
