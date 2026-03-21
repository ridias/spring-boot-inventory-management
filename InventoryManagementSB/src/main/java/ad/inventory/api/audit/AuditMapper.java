package ad.inventory.api.audit;

import ad.inventory.shared.dtos.SimpleDto;

public class AuditMapper {

private AuditMapper() {
		
	}

	public static AuditRowDto transformToAuditRowDto(Audit e) {
		var dto = new AuditRowDto();
		dto.setId(e.getId());
		dto.setAction(e.getAction().getKeyTranslate());
		dto.setBrowser(e.getBrowser());
		dto.setData(e.getExecutedAt());
		dto.setIp(e.getIp());
		
		if(e.getUser() != null) {
			dto.setUser(new SimpleDto(e.getUser().getId(), e.getUser().getUsername(), ""));
		}else {
			dto.setUser(null);
		}
		
		return dto;
	}
	
	public static AuditDetailsDto transformToAuditDetailsDto(Audit e) {
		var dto = new AuditDetailsDto();
		dto.setAction(e.getAction().getKeyTranslate());
		dto.setBrowser(e.getBrowser());
		dto.setData(e.getExecutedAt());
		dto.setIp(e.getIp());
		
		if(e.getUser() != null) {
			dto.setUsername(e.getUser().getUsername());
		}else {
			dto.setUsername(null);
		}
		
		return dto;
	}
}
