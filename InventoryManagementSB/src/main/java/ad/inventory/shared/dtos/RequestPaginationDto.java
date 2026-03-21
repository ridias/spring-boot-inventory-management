package ad.inventory.shared.dtos;

import lombok.Getter;

@Getter
public class RequestPaginationDto {

	private final int limit;
	private final int page;
	private final String search;
	
	public RequestPaginationDto(int limit, int page, String search) {
		this.limit = limit;
		this.page = page;
		this.search = search;
	}
}
