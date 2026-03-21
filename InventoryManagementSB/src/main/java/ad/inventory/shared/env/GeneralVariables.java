package ad.inventory.shared.env;

public class GeneralVariables {

	public GeneralVariables() {
		
	}
	
	public static final String COLUMN_TIMESTAMP_WITH_TIME_ZONE = "TIMESTAMP WITH TIME ZONE";
	
	public static final String KEY_USER_ID = "userId";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_ROLE_ID = "roleId";
	public static final String KEY_ROLE_NAME = "roleName";
	
	public static final int DAYS_PASSWORD_RENOVATION_DEFAULT = 31;
	public static final int MINS_USER_BLOCKED_DEFAULT = 10;
	public static final int MAX_ATTEMPS_LOGIN_DEFAULT = 3;
	public static final int PASSWORD_REUSE_LIMIT_DEFAULT = 10;
}
