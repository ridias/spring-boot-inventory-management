package ad.inventory.api.users;

import ad.inventory.shared.dtos.RequestPaginationDto;
import lombok.Getter;

@Getter
public class RequestPaginationUserDto extends RequestPaginationDto {

	private final Long idGroup;
	private final String active;
	private final String orderById;
	private final String orderByUsername;
	private final String orderByEmail;
	private final String orderByFirstName;
	private final String orderByLastName;
	private final String orderByGroup;
	private final String orderByActive;
	private final String orderByLastSession;
	
	public RequestPaginationUserDto(int limit, 
			int page, 
			String search,
			Long idGroup,
			String active,
			String orderById,
			String orderByUsername,
			String orderByEmail,
			String orderByFirstName,
			String orderByLastName,
			String orderByGroup,
			String orderByActive,
			String orderByLastSession) {
		super(limit, page, search);
		this.idGroup = idGroup;
		this.active = active;
		this.orderById = orderById;
		this.orderByUsername = orderByUsername;
		this.orderByEmail = orderByEmail;
		this.orderByFirstName = orderByFirstName;
		this.orderByLastName = orderByLastName;
		this.orderByGroup = orderByGroup;
		this.orderByActive = orderByActive;
		this.orderByLastSession = orderByLastSession;
	}

	public String toString() {		
		return "limit=" + this.getLimit() + 
				"&page=" + this.getPage() + 
				"&search=" + this.getSearch() +
				"&idGroup=" + idGroup + 
				"&active=" + active +
				"&orderById=" + orderById + 
				"&orderByUsername=" + orderByUsername + 
				"&orderByEmail=" + orderByEmail + 
				"&orderByFirstName=" + orderByFirstName + 
				"&orderByLastName=" + orderByLastName + 
				"&orderByGroup=" + orderByGroup +
				"&orderByActive=" + orderByActive + 
				"&orderByLastSession=" + orderByLastSession;
	}
}
