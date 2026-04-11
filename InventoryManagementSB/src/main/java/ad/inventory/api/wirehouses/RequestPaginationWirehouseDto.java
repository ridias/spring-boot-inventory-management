package ad.inventory.api.wirehouses;

import ad.inventory.shared.dtos.RequestPaginationDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPaginationWirehouseDto extends RequestPaginationDto {

	private final Boolean active;
	private final String orderById;
	private final String orderByName;
	private final String orderByActive;
	
	public RequestPaginationWirehouseDto(
			int limit, 
			int page, 
			String search,
			Boolean active,
			String orderById,
			String orderByName,
			String orderByActive) {
		super(limit, page, search);
		
		this.active = active;
		this.orderByActive = orderByActive;
		this.orderById = orderById;
		this.orderByName = orderByName;
	}

}
