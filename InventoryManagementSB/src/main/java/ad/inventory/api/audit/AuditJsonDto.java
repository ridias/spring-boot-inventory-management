package ad.inventory.api.audit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditJsonDto {

	private String query;
	private String parametersCreate;
	private String parametersUpdateCurrentState;
	private String parametersUpdatePreviousState;
}
