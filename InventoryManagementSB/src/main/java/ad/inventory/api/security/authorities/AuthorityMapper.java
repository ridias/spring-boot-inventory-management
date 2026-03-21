package ad.inventory.api.security.authorities;

public class AuthorityMapper {

	private AuthorityMapper() {
		
	}

	public static AuthorityDto transform(Authority entity) {
		var dto = new AuthorityDto();
		dto.setPermission(entity.getPermission());
		dto.setActiveCreate(entity.getActiveCreate());
		dto.setActiveRead(entity.getActiveRead());
		dto.setActiveUpdate(entity.getActiveUpdate());
		dto.setActiveDelete(entity.getActiveDelete());
		return dto;
	}
}
