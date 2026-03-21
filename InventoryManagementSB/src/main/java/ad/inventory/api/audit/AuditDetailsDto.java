package ad.inventory.api.audit;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditDetailsDto {

	private LocalDateTime data;
	private String action;
	private String username;
	private String ip;
	private String browser;
	private String platform;
	private String query;
	private String parametersCreate;
	private String parametersUpdatePrevious;
	private String parametersUpdateActual;
}
