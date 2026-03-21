package ad.inventory.api.roles;

import ad.inventory.shared.dtos.RequestPaginationDto;
import lombok.Getter;

@Getter
public class RequestPaginationRoleDto extends RequestPaginationDto {

	private final String orderById;
	private final String orderByName;
	private final String orderByDescription;
	private final String orderByTotalUsers;
	
	public RequestPaginationRoleDto(int limit, 
			int page, 
			String search,
			String orderById,
			String orderByName,
			String orderByDescription,
			String orderByTotalUsers) {
		super(limit, page, search);
		this.orderById = orderById;
		this.orderByName = orderByName;
		this.orderByDescription = orderByDescription;
		this.orderByTotalUsers = orderByTotalUsers; 
	}
	
	public String toString() {
		return "limit=" + this.getLimit() + 
				"&page=" + this.getPage() + 
				"&search=" + this.getSearch() +
				"&orderById=" + orderById + 
				"&orderByName=" + orderByName + 
				"&orderByDescription=" + orderByDescription + 
				"&orderByTotalUsers=" + orderByTotalUsers;
	}

	
}
