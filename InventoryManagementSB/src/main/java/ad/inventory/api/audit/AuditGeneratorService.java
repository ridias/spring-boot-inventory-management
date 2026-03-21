package ad.inventory.api.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.api.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditGeneratorService {

	@Autowired
	private JwtUtils jwtUtils;
	
	public RequestSaveAudit createRequestSaveAudit(String token, HttpServletRequest req, Long idAction) {
		Long idUserLogged = jwtUtils.getUserIdFromJwtToken(token);
        String ip = req.getRemoteAddr();
        String browser = req.getHeader("User-Agent");
        
        return new RequestSaveAudit(idAction, idUserLogged, ip, "", browser, "{}");
	}
}
