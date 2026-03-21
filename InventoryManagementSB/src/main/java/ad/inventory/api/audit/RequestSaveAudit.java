package ad.inventory.api.audit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSaveAudit {

	private Long idUser;
	private Long idAction;
	private String request;
	private String ip;
	private String description;
	private String browser;
	private String codeTranslator;
	
	public RequestSaveAudit(Long idAction, Long idUser, String ip, String description,
			String browser, String request) {
		
		this.idUser = idUser;
		this.idAction = idAction;
		this.request = request;
		this.ip = ip;
		this.description = description;
		this.browser = browser;
		this.codeTranslator = "";
	}
	
	public RequestSaveAudit(Long idAction, Long idUser, String ip, String description,
			String browser, String request, String codeTranslator) {
		
		this.idUser = idUser;
		this.idAction = idAction;
		this.request = request;
		this.ip = ip;
		this.description = description;
		this.browser = browser;
		this.codeTranslator = codeTranslator;
	}
}
