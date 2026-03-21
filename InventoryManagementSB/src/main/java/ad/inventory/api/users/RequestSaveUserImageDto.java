package ad.inventory.api.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSaveUserImageDto {

	private String imageProfile;
	
	public RequestSaveUserImageDto() {
		
	}
	
	public RequestSaveUserImageDto(String imageProfile) {
		this.setImageProfile(imageProfile);
	}
}
