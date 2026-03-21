package ad.inventory.api.audit;

import java.time.LocalDateTime;

import ad.inventory.shared.dtos.SimpleDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditRowDto {

	private Long id;
	private LocalDateTime data;
	private String action;
	private SimpleDto user;
	private String ip;
	private String browser;
}
