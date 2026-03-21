package ad.inventory.api.roles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ad.inventory.shared.env.GeneralErrors;

public class RoleValidator {

	private Role role;
	
	public RoleValidator(Role role) {
		this.role = role;
	}
	
	public boolean isRequiredFieldsNull() {
		if(this.role == null) return true;
		return this.role.getName() == null;
	}
	
	public int getCodeAfterValidation() {
		if(this.role == null)
			return GeneralErrors.ERR_400_GROUP_USERS_UNDEFINED_CODE;
		if(!this.isNameValid())
			return GeneralErrors.ERR_400_GROUP_USERS_NAME_CODE;
		if(!this.isDescriptionValid())
			return GeneralErrors.ERR_400_GROUP_USERS_DESCRIPTION_CODE;
		
		return 0;
	}
	
	public String getMessageAfterValidation() {
		if(this.role == null)
			return GeneralErrors.ERR_400_GROUP_USERS_UNDEFINED_MSG;
		if(!this.isNameValid())
			return GeneralErrors.ERR_400_GROUP_USERS_NAME_MSG;
		if(!this.isDescriptionValid())
			return GeneralErrors.ERR_400_GROUP_USERS_DESCRIPTION_MSG;
		
		return "OK";
	}
	
	private boolean isNameValid() {
		if(this.role == null) return false;
		var name = this.role.getName();
		if(name == null) return false;
		Pattern p = Pattern.compile("^[A-Z_]{2,50}$");
		Matcher matcher = p.matcher(name);
		return matcher.matches();
	}
	
	private boolean isDescriptionValid() {
		if(this.role == null) return false;
		var description = this.role.getDescription();
		if(description == null) return true;
		Pattern p = Pattern.compile("^[A-Za-zÀ-ÿ _\\-0-9\\.\\,]{0,50}$");
		Matcher matcher = p.matcher(description);
		return matcher.matches();
	}
	
}
