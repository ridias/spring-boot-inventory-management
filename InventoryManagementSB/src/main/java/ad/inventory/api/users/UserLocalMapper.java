package ad.inventory.api.users;

import ad.inventory.shared.dtos.SimpleDto;

public class UserLocalMapper {

private UserLocalMapper() {
		
	}

	public static UserLocalDto transformToUserLocalDto(UserLocal e) {
		if(e == null) return null;
		return new UserLocalDto(e.getId(), 
				e.getUsername(), 
				e.getEmail(), 
				e.getImageProfile(), 
				e.getRole().getId(),
				e.getLastSession());
	}
	
	public static UserLocalRowDto transformToUserLocalRowDto(UserLocal e) {
		if(e == null) return null;
		return UserLocalRowDto.builder()
				.id(e.getId())
				.active(e.getActive())
				.email(e.getEmail())
				.firstName(e.getFirstName())
				.lastName(e.getLastName())
				.lastSession(e.getLastSession())
				.username(e.getUsername())
				.groupName(e.getRole().getName())
				.build();
	}
	
	public static UserLocalDetailsDto transformToUserLocalDetailsDto(UserLocal e) {
		if(e == null) return null;
		var dto = new UserLocalDetailsDto();
		dto.setEmail(e.getEmail());
		dto.setFirstName(e.getFirstName());
		dto.setRole(new SimpleDto(e.getRole().getId(), e.getRole().getName(), ""));
		dto.setId(e.getId());
		dto.setLastName(e.getLastName());
		dto.setUsername(e.getUsername());
		dto.setActive(e.getActive());
		dto.setType2FA(e.getType2FA());
		return dto;
	}
	
	public static UserLocal transformToUserLocal(RequestSaveUserDto request) {
		var userToSave = new UserLocal();
		userToSave.setActive(request.getActive());
		userToSave.setEmail(request.getEmail());
		userToSave.setFirstName(request.getFirstName());
		userToSave.setImageProfile("");
		userToSave.setIsDeleted(false);
		userToSave.setLastName(request.getLastName());
		userToSave.setUsername(request.getUsername());
		return userToSave;
	}
}
