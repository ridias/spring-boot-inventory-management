package ad.inventory.api.audit;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditRequest {

	private String query;
	private String parametersCreate;
	private String parametersUpdatePreviousState;
	private String parametersUpdateCurrentState;
	
	public AuditRequest(String query, 
			String parametersCreate,
			String parametersUpdatePreviousState,
			String parametersUpdateCurrentState) {
		this.query = query;
		this.parametersCreate = parametersCreate;
		this.parametersUpdateCurrentState = parametersUpdatePreviousState;
		this.parametersUpdatePreviousState = parametersUpdateCurrentState;
	}
	
	public String toJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("query", this.query);
		obj.addProperty("parametersCreate", this.parametersCreate);
		obj.addProperty("parametersUpdatePreviousState", this.parametersUpdatePreviousState);
		obj.addProperty("parametersUpdateCurrentState", this.parametersUpdateCurrentState);
		return obj.toString();
	}
}
