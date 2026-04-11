package ad.inventory.shared.env;

public class GeneralErrors {

	private GeneralErrors() {
		
	}
	
	// errors code 400
    public static final int ERR_400_BAD_REQUEST_CODE = 40000000;
    public static final int ERR_400_NAME_DUPLICATED_CODE = 40000001;
    public static final int ERR_400_UNDEFINED_FIELDS_CODE = 40000002;
    
    public static final int ERR_400_GROUP_USERS_UNDEFINED_CODE = 40000010;
    public static final int ERR_400_GROUP_USERS_NAME_CODE = 40000011;
    public static final int ERR_400_GROUP_USERS_DESCRIPTION_CODE = 40000012;
    
    public static final int ERR_400_USER_DUPLICATE_USERNAME_CODE = 40000016;
    public static final int ERR_400_USER_DUPLICATE_EMAIL_CODE = 40000017;
    public static final int ERR_400_USER_LINKED_TO_CLIENT_UNKNOWN_CODE = 40000018;
    public static final int ERR_400_USER_PASSWORD_VALID_CODE = 40000019;
    public static final int ERR_400_USER_PASSWORD_ALREADY_USED_CODE = 40000020;
    public static final int ERR_400_USER_UNDEFINED_CODE = 40000021;
    public static final int ERR_400_USER_USERNAME_VALID_CODE = 40000022;
    public static final int ERR_400_USER_EMAIL_VALID_CODE = 40000023;
    public static final int ERR_400_USER_FIRST_NAME_VALID_CODE = 40000024;
    public static final int ERR_400_USER_LAST_NAME_VALID_CODE = 40000025;
    
    // errors code 401
    public static final int ERR_401_NOT_AUTHENTICATED_CODE = 40100000;
    
    // errors code 403 
    public static final int ERR_403_SERVICE_ACCESS_RESTRICTED_CODE = 40300001;
    
    // error codes 404
    public static final int ERR_404_NOT_FOUND_CODE = 40400000;
    public static final int ERR_404_LOG_NOT_FOUND_CODE = 40400004;
    public static final int ERR_404_GROUP_USERS_NOT_FOUND_CODE = 40400008;
    public static final int ERR_404_PERMISSION_NOT_FOUND_CODE = 40400010;
    public static final int ERR_404_USER_NOT_FOUND_CODE = 40400011;
    public static final int ERR_404_WIREHOUSE_NOT_FOUND_CODE = 40400012;
    
    
    public static final int ERR_404_PAGE_NOT_FOUND_CODE = 40400060;
    
    
    // error codes 500
    public static final int ERR_500_CHANGES_NOT_APPLIED_CODE = 50000001;
    public static final int ERR_500_SMS_REQUEST_SENT_RECENTLY_CODE = 50000006;
    public static final int ERR_500_SMS_OPERATOR_NAME_UNKNOWN_CODE = 50000007;
    public static final int ERR_500_SMS_CODE_NOT_SENT_CODE = 50000008;
    public static final int ERR_500_2FA_FAILED_LIMIT_CODE = 50000009;
    public static final int ERR_500_2FA_CODE_EXPIRED_CODE = 50000010;
    public static final int ERR_500_RECOVERY_PASS_SENT_RECENTLY_CODE = 50000011;
    public static final int ERR_500_RECOVERY_PASS_NOT_UPDATED_CODE = 50000012;
    public static final int ERR_500_RECOVERY_PASS_TOKEN_EXPIRED_CODE = 50000013;
    public static final int ERR_500_RECOVERY_PASS_EMAIL_NOT_SENT_CODE = 50000014;
    public static final int ERR_500_USER_GROUP_NOT_DELETED_CODE = 50000059;
    
    
    // error msgs 400
    public static final String ERR_400_BAD_REQUEST_MSG = "Bad request"; 
    public static final String ERR_400_NAME_DUPLICATED_MSG = "Name already exists";
    public static final String ERR_400_UNDEFINED_FIELDS_MSG = "Some fields are not defined";
    public static final String ERR_400_GROUP_USERS_UNDEFINED_MSG = "The user group to be validated is not defined!";
    public static final String ERR_400_GROUP_USERS_NAME_MSG = "The group name can only contain uppercase letters and underscores. It must be between 2 and 50 characters.";
    public static final String ERR_400_GROUP_USERS_DESCRIPTION_MSG = "The description can only contain letters, numbers, and spaces. It must be between 0 and 50 characters.";
    public static final String ERR_400_USER_DUPLICATE_USERNAME_MSG = "Username already exists";
    public static final String ERR_400_USER_DUPLICATE_EMAIL_MSG = "Email already exists";
    public static final String ERR_400_USER_LINKED_TO_CLIENT_UNKNOWN_MSG = "Client linked to user unknown";
    public static final String ERR_400_USER_PASSWORD_VALID_MSG = "The password must have 1 uppercase letter, 1 lowercase letter, 1 number and be between 12 and 18 characters.";
    public static final String ERR_400_USER_PASSWORD_ALREADY_USED_MSG = "Password already used recently";
    public static final String ERR_400_USER_UNDEFINED_MSG = "The user to be validated is not defined.";
    public static final String ERR_400_USER_USERNAME_VALID_MSG = "The username can only contain letters, numbers, and underscores and be between 3 and 20 characters.";
    public static final String ERR_400_USER_EMAIL_VALID_MSG = "The email is not valid.";
    public static final String ERR_400_USER_FIRST_NAME_VALID_MSG = "The name can only contain letters and be between 3 and 20 characters long.";
    public static final String ERR_400_USER_LAST_NAME_VALID_MSG = "The last name can only contain letters, spaces, and be between 0 and 50 characters.";
    
    // error msgs 403
    
    public static final String ERR_403_SERVICE_ACCESS_RESTRICTED_MSG = "You do not have permission to access this information.";
    
    // error msgs 404
    public static final String ERR_404_NOT_FOUND_MSG = "Not found"; 
    public static final String ERR_404_GROUP_USERS_NOT_FOUND_MSG = "Group users not found";
    public static final String ERR_404_PAGE_NOT_FOUND_MSG = "Page or permission not found";
    public static final String ERR_404_USER_NOT_FOUND_MSG = "User not found";
    public static final String ERR_404_WIREHOUSE_NOT_FOUND_MSG = "Wirehouse not found";
    
    // errors msg 500
    public static final String ERR_500_USER_GROUP_NOT_DELETED_MSG = "User group cannot be deleted if there are users inside";
    public static final String ERR_500_SMS_REQUEST_SENT_RECENTLY_MSG = "Request recently sent, try again in 1 minute";
    public static final String ERR_500_SMS_OPERATOR_NAME_UNKNOWN_MSG = "Unable to send verification code";
    public static final String ERR_500_SMS_CODE_NOT_SENT_MSG = "Unable to send verification code, please try again later";
    public static final String ERR_500_2FA_FAILED_LIMIT_MSG = "The number of attempts has reached its limit.";
    public static final String ERR_500_2FA_CODE_EXPIRED_MSG = "Verification code has expired, please resend.";
    public static final String ERR_500_RECOVERY_PASS_SENT_RECENTLY_MSG = "Request recently sent, please try again in 1 minute";
    public static final String ERR_500_RECOVERY_PASS_NOT_UPDATED_MSG = "Could not change password, please try again later.";
    public static final String ERR_500_RECOVERY_PASS_TOKEN_EXPIRED_MSG = "Unable to change password, token expired";
    public static final String ERR_500_RECOVERY_PASS_EMAIL_NOT_SENT_MSG = "It was not possible to send the email, please try again later";
}
