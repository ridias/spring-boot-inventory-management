package ad.inventory.api.users;

import lombok.Getter;

@Getter
public class RequestSaveUserConfigDto {

	private final Long id;
	private final String email;
	private final String username;
	private final String firstName;
	private final String lastName;
	private final String password;
	
	public RequestSaveUserConfigDto(Long id, 
			String email, 
			String username, 
			String firstName, 
			String lastName, 
			String password) {
		
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
}
