package ad.inventory.api.wirehouses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ad.inventory.api.audit.AuditService;
import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.dtos.SimpleDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.InvalidRequest;
import ad.inventory.shared.exceptions.NotFound;

@Service
public class WirehouseService {

	@Autowired
	private WirehouseRepository wirehouseRepository;
	@Autowired
	private AuditService auditService;
	
	public ResponseDto<SimpleDto> getAll(){
		var result = new ArrayList<SimpleDto>();
		var resultSet = this.wirehouseRepository.findAll();
		for(int i = 0; i < resultSet.size(); i++) {
			var wirehouse = resultSet.get(i);
			result.add(new SimpleDto(wirehouse.getId(), wirehouse.getName(), ""));
		}
		
		return GeneratorResponse.ok(result);
	}
	
	public ResponseDto<WirehouseRowDto> getByPagination(
			RequestPaginationWirehouseDto request, 
			RequestSaveAudit requestAudit){
		
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var result = new ArrayList<WirehouseRowDto>();
		var specification = new WirehouseSpecification<Wirehouse>();
		var filters = specification.getFilters(request);
		int limit = request.getLimit();
		int page = request.getPage();
		if(limit <= 0 || limit > 100) limit = 10;
		if(page < 0) page = 0;
		var pageRequest = PageRequest.of(page, limit);
		
		var resultSet = this.wirehouseRepository.findAll(filters, pageRequest);
		if(!resultSet.getContent().isEmpty()) {
			var wirehouses = resultSet.getContent();
			for(int i = 0; i < wirehouses.size(); i++) {
				var wirehouse = wirehouses.get(i);
				var dto = WirehouseCreator.createAsRowDto(wirehouse);
				result.add(dto);
			}
		}
		
		this.auditService.saveAsReadAuditRequest(requestAudit, request.toString());
		return GeneratorResponse.ok(result);
	}
	
	public ResponseDto<Long> getTotal(
			RequestPaginationWirehouseDto request){
		
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var specification = new WirehouseSpecification<Wirehouse>();
		var filters = specification.getFilters(request);
		var total = this.wirehouseRepository.count(filters);
		return GeneratorResponse.ok(List.of(total));
	}
	
	public ResponseDto<WirehouseDetailsDto> getDetailsById(Long id, RequestSaveAudit requestAudit){
		
		if(id == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var wirehouseInDb = this.wirehouseRepository.findById(id);
		if(wirehouseInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_WIREHOUSE_NOT_FOUND_CODE, GeneralErrors.ERR_404_WIREHOUSE_NOT_FOUND_MSG));
		
		var detailsDto = WirehouseCreator.createAsDetailDto(wirehouseInDb.get());
		
		this.auditService.saveAsReadAuditRequest(requestAudit, "id=" + id);
		return GeneratorResponse.ok(List.of(detailsDto));
	}
	
	public ResponseDto<Boolean> save(RequestSaveWirehouseDto request, RequestSaveAudit requestAudit){
		if(request == null || requestAudit == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var wirehouseToSave = WirehouseCreator.create(request);

		var validator = new WirehouseValidator(wirehouseToSave);
		if(validator.isRequiredFieldsNull())
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_UNDEFINED_FIELDS_CODE, GeneralErrors.ERR_400_UNDEFINED_FIELDS_MSG));
		
		var codeError = validator.getCodeAfterValidation();
		var message = validator.getMessageAfterValidation();
		if(!message.equals("OK"))
			return GeneratorResponse.fail(new InvalidRequest(codeError, message));
		
		var campaingnSaved = this.wirehouseRepository.save(wirehouseToSave);
		
		this.auditService.saveAsCreateAuditRequest(requestAudit, campaingnSaved.toString());
		return GeneratorResponse.ok(List.of(true));
	}
	
	public ResponseDto<Boolean> update(RequestSaveWirehouseDto request, RequestSaveAudit requestAudit){
		if(request == null || requestAudit == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var wirehouseInDb = this.wirehouseRepository.findById(request.getId());
		if(wirehouseInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_WIREHOUSE_NOT_FOUND_CODE, GeneralErrors.ERR_404_WIREHOUSE_NOT_FOUND_MSG));
		
		var wirehouseToUpdate = wirehouseInDb.get();
		var previousParameters = wirehouseToUpdate.toString();
		
		WirehouseCreator.updateFromRequest(request, wirehouseToUpdate);
		
		var validator = new WirehouseValidator(wirehouseToUpdate);
		if(validator.isRequiredFieldsNull())
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_UNDEFINED_FIELDS_CODE, GeneralErrors.ERR_400_UNDEFINED_FIELDS_MSG));
		
		var codeError = validator.getCodeAfterValidation();
		var message = validator.getMessageAfterValidation();
		if(!message.equals("OK"))
			return GeneratorResponse.fail(new InvalidRequest(codeError, message));
		
		var campaingnUpdated = this.wirehouseRepository.save(wirehouseToUpdate);
		
		this.auditService.saveAsUpdateAuditRequest(requestAudit, previousParameters, campaingnUpdated.toString());
		return GeneratorResponse.ok(List.of(true));
	}
	
	public ResponseDto<Boolean> deleteById(Long id, RequestSaveAudit requestAudit){
		if(id == null || requestAudit == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		this.wirehouseRepository.updateAsDeletedById(id);
		this.auditService.saveAsDeleteAuditRequest(requestAudit, "id=" + id);
		return GeneratorResponse.ok(List.of(true));
	}
	
	
}
