package ad.inventory.api.users;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLocalDto {

	private Long id;
	private String username;
	private String email;
	private String imageProfile;
	private Long roleId;
	private LocalDateTime lastSession;
	
	public UserLocalDto() {
		
	}
	
	public UserLocalDto(Long id, 
			String username, 
			String email, 
			String imageProfile,
			Long roleId,
			LocalDateTime lastSession) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.imageProfile = imageProfile;
		this.roleId = roleId;
		this.lastSession = lastSession;
	}
	
}
