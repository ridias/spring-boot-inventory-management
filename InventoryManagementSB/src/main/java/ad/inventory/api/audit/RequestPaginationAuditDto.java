package ad.inventory.api.audit;

import java.time.LocalDateTime;

import ad.inventory.shared.dtos.RequestPaginationDto;
import lombok.Getter;

@Getter
public class RequestPaginationAuditDto extends RequestPaginationDto {

	private final Long idUser;
	private final Long idAction;
	private final LocalDateTime start;
	private final LocalDateTime end;
	private final String orderByData;
	private final String orderByAction;
	private final String orderByUser;
	private final String orderByIp;
	private final String orderByBrowser;
	
	public RequestPaginationAuditDto(int limit, 
			int page, 
			String search,
			Long idUser,
			Long idAction,
			LocalDateTime start,
			LocalDateTime end,
			String orderByData,
			String orderByAction,
			String orderByUser,
			String orderByIp,
			String orderByBrowser) {
		super(limit, page, search);
		this.idUser = idUser;
		this.idAction = idAction;
		this.start = start;
		this.end = end;
		this.orderByData = orderByData;
		this.orderByAction = orderByAction;
		this.orderByUser = orderByUser;
		this.orderByIp = orderByIp;
		this.orderByBrowser = orderByBrowser;
	}
}
