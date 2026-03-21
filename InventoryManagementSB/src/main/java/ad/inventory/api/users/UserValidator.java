package ad.inventory.api.users;

import java.util.regex.Pattern;

import ad.inventory.shared.env.GeneralErrors;

public class UserValidator {

	private UserLocal user;
	
	public UserValidator(UserLocal user) {
		this.user = user;
	}
	
	public boolean isRequiredFieldsNull() {
		if(this.user == null) return true;
		return this.user.getUsername() == null ||
				this.user.getEmail() == null ||
				this.user.getActive() == null;
	}
	
	public String getMessageAfterValidation() {
		if(!this.isPasswordValid())
			return GeneralErrors.ERR_400_USER_PASSWORD_VALID_MSG; 
		
		return this.getMessageAfterValidationNoPassword();
	}
	
	public int getCodeAfterValidation() {
		if(!this.isPasswordValid())
			return GeneralErrors.ERR_400_USER_PASSWORD_VALID_CODE; 
		
		return this.getCodeAfterValidationNoPassword();
	}
	
	public String getMessageAfterValidationNoPassword() {
		if(this.user == null)
			return GeneralErrors.ERR_400_USER_UNDEFINED_MSG;
		if(!this.isUsernameValid())
			return GeneralErrors.ERR_400_USER_USERNAME_VALID_MSG;
		if(!this.isEmailValid())
			return GeneralErrors.ERR_400_USER_EMAIL_VALID_MSG;
		if(!this.isFirstNameValid())
			return GeneralErrors.ERR_400_USER_FIRST_NAME_VALID_MSG;
		if(!this.isLastNameValid())
			return GeneralErrors.ERR_400_USER_LAST_NAME_VALID_MSG;
		return "OK";
	}
	
	public int getCodeAfterValidationNoPassword() {
		if(this.user == null)
			return GeneralErrors.ERR_400_USER_UNDEFINED_CODE;
		if(!this.isUsernameValid())
			return GeneralErrors.ERR_400_USER_USERNAME_VALID_CODE;
		if(!this.isEmailValid())
			return GeneralErrors.ERR_400_USER_EMAIL_VALID_CODE;
		if(!this.isFirstNameValid())
			return GeneralErrors.ERR_400_USER_FIRST_NAME_VALID_CODE;
		if(!this.isLastNameValid())
			return GeneralErrors.ERR_400_USER_LAST_NAME_VALID_CODE;
		return 0;
	}
	
	protected boolean isUsernameValid() {
		if(this.user == null) return false;
		var value = this.user.getUsername();
		if(value == null) return false;
		return Pattern.matches("^\\w{3,30}$", value);
	}
	
	protected boolean isEmailValid() {
		if(this.user == null) return false;
		var value = this.user.getEmail();
		if(value == null) return false;
		return Pattern.matches("[\\-a-zA-Z0-9._%\\+]+@[\\-a-zA-Z0-9\\.]+\\.[a-zA-Z]{2,}", value);
	}
	
	protected boolean isFirstNameValid() {
		if(this.user == null) return false;
		var value = this.user.getFirstName();
		if(value == null) return false;
		return Pattern.matches("^[A-Za-zÀ-ÿ ]{3,20}$", value);
	}
	
	protected boolean isLastNameValid() {
		if(this.user == null) return false;
		var value = this.user.getLastName();
		if(value == null) return true;
		return Pattern.matches("^[A-Za-zÀ-ÿ ]{0,50}$", value);
	}
	
	protected boolean isPasswordValid() {
		if(this.user == null) return false;
		var value = this.user.getPassword();
		if(value == null) return false;
		return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d\\s])[^\\s]{12,18}$", value);
	}
	
	public boolean isPasswordValid(String password) {
		if(password == null) return false;
		return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d\\s])[^\\s]{12,18}$", password);
	}
}
