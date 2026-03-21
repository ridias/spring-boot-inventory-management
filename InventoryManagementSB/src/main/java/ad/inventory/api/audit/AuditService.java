package ad.inventory.api.audit;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ad.inventory.api.actions.ActionRepository;
import ad.inventory.api.users.UserLocalRepository;
import ad.inventory.shared.DateTimeFormatHelper;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.InvalidRequest;
import ad.inventory.shared.exceptions.NotFound;

@Service
public class AuditService {

	@Autowired
	private AuditRepository auditRepository;
	@Autowired
	private ActionRepository actionRepository;
	@Autowired
	private UserLocalRepository userRepository;
	
	private static final Logger log = LoggerFactory.getLogger(AuditService.class);
	
	public ResponseDto<AuditRowDto> getByPagination(RequestPaginationAuditDto request){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		List<AuditRowDto> result = new ArrayList<>();
		var specification = new AuditSpecification<Audit>();
		var filters = specification.getFiltered(request);
		int limit = request.getLimit();
		int page = request.getPage();
		if(limit <= 0) limit = 10;
		if(limit > 500) limit = 500;
		if(page < 0) page = 0;
		var pageRequest = PageRequest.of(page, limit);
		
		var resultSet = this.auditRepository.findAll(filters, pageRequest);
		if(!resultSet.getContent().isEmpty()) {
			var logs = resultSet.getContent();
			for(int i = 0; i < logs.size(); i++) {
				var audit = logs.get(i);
				result.add(AuditMapper.transformToAuditRowDto(audit));
			}
		}
		
		return GeneratorResponse.ok(result);
	}

	public ResponseDto<Long> getTotal(RequestPaginationAuditDto request){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var specification = new AuditSpecification<Audit>();
		var filters = specification.getFiltered(request);
		var total = this.auditRepository.count(filters);
		return GeneratorResponse.ok(List.of(total));
	}
	
	public ResponseDto<AuditDetailsDto> getDetailsById(Long id){
		if(id == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var logInDb = this.auditRepository.findById(id);
		
		if(logInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(
					GeneralErrors.ERR_404_NOT_FOUND_CODE, GeneralErrors.ERR_404_NOT_FOUND_MSG));
		
		var result = AuditMapper.transformToAuditDetailsDto(logInDb.get());
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AuditJsonDto data = objectMapper.readValue(logInDb.get().getRequest(), AuditJsonDto.class);
			result.setQuery(data.getQuery());
			result.setParametersCreate(data.getParametersCreate());
			result.setParametersUpdateActual(data.getParametersUpdateCurrentState());
			result.setParametersUpdatePrevious(data.getParametersUpdatePreviousState());
		}catch(Exception ex) {
			log.error("Audit not parsed, more details: ", ex);
		}
		
		return GeneratorResponse.ok(List.of(result));
	}
	
	public void saveAsReadAuditRequest(RequestSaveAudit requestAudit, String query) {
		var auditRequest = new AuditRequest(query, "", "", "");
		requestAudit.setDescription("");
		requestAudit.setRequest(auditRequest.toJson());
		this.saveAudit(requestAudit);
	}
	
	public void saveAsCreateAuditRequest(RequestSaveAudit requestAudit, String query) {
		var auditRequest = new AuditRequest("", query, "", "");
		requestAudit.setDescription("");
		requestAudit.setRequest(auditRequest.toJson());
		this.saveAudit(requestAudit);
	}
	
	public void saveAsUpdateAuditRequest(RequestSaveAudit requestAudit, String previousParameters, String currentParameters) {
		var auditRequest = new AuditRequest("", "", previousParameters, currentParameters);
		requestAudit.setDescription("");
		requestAudit.setRequest(auditRequest.toJson());
		this.saveAudit(requestAudit);
	}
	
	public void saveAsDeleteAuditRequest(RequestSaveAudit requestAudit, String query) {
		this.saveAsReadAuditRequest(requestAudit, query);
	}
	
	private void saveAudit(RequestSaveAudit request) {
		var audit = new Audit();
		
		var actionInDb = this.actionRepository.findById(request.getIdAction());
		if(actionInDb.isEmpty()) {
			log.warn("Auditoria no registrada, acció desconeguda -> " + request.getIdAction());
			return; 
		}
		
		var userInDb = this.userRepository.findById(request.getIdUser());
		if(userInDb.isEmpty()) {
			audit.setUser(null);
		}else {
			audit.setUser(userInDb.get());
		}
		
		var executedAt = DateTimeFormatHelper.formatLocalDateTimeNow("yyyy-MM-dd HH:mm:ss.SSS");
		
		audit.setId(null);
		audit.setAction(actionInDb.get());
		audit.setExecutedAt(executedAt);
		audit.setBrowser(request.getBrowser());
		audit.setDescription(request.getDescription());
		audit.setIp(request.getIp());
		audit.setRequest(request.getRequest());
		
		this.auditRepository.save(audit);
	}
}
