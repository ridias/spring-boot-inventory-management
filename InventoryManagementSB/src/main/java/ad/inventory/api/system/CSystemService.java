package ad.inventory.api.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.shared.dtos.SimpleStrDto;
import ad.inventory.shared.env.GeneralVariables;

@Service
public class CSystemService {

	@Autowired
	private CSystemRepository systemRepository;
	
	public SimpleStrDto getDaysPasswordRenovation() {
		return this.getRestrictionLogin("days_password_renovation", 
				GeneralVariables.DAYS_PASSWORD_RENOVATION_DEFAULT);
	}
	
	public SimpleStrDto getMinsUserBlocked() {
		return this.getRestrictionLogin("mins_user_blocked", 
				GeneralVariables.MINS_USER_BLOCKED_DEFAULT);
	}
	
	public SimpleStrDto getMaxAttempsLogin() {
		return this.getRestrictionLogin("max_attempts_login", 
				GeneralVariables.MAX_ATTEMPS_LOGIN_DEFAULT);
	}
	
	public SimpleStrDto getLimitPasswordsReuse() {
		return this.getRestrictionLogin("password_reuse_limit", 
				GeneralVariables.PASSWORD_REUSE_LIMIT_DEFAULT);
	}
	
	private SimpleStrDto getRestrictionLogin(String key, int defaultValue) {
		var systemRowInDb = this.systemRepository.getByKey(key);
		if(systemRowInDb.isEmpty()) {
			return new SimpleStrDto(key, defaultValue + "");
		}
		
		int value = 0;
		try {
			value = Integer.parseInt(systemRowInDb.get().getValue());
		}catch(Exception ex) {
			value = defaultValue;
		}
		
		return new SimpleStrDto(key, value + "");
	}
}
