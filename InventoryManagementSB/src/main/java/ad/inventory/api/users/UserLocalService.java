package ad.inventory.api.users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import ad.inventory.api.audit.AuditService;
import ad.inventory.api.audit.RequestSaveAudit;
import ad.inventory.api.roles.RoleRepository;
import ad.inventory.api.system.CSystemService;
import ad.inventory.shared.DateTimeFormatHelper;
import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.Duplicate;
import ad.inventory.shared.exceptions.Forbidden;
import ad.inventory.shared.exceptions.InvalidParameter;
import ad.inventory.shared.exceptions.InvalidRequest;
import ad.inventory.shared.exceptions.NotFound;

public class UserLocalService {

	@Autowired
	private UserLocalRepository userLocalRepository;
	@Autowired
	private RoleRepository userGroupRepository;
	@Autowired
	private AuditService auditService;
	@Autowired
	private CSystemService systemService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserPasswordUsedService userPasswordUsedService;
	
	public ResponseDto<UserLocalDto> getAll(){
		var users = this.userLocalRepository.findByIsDeletedOrderByUsernameAsc(false);
		var result = new ArrayList<UserLocalDto>();
		
		for(int i = 0; i < users.size(); i++) {
			var user = users.get(i);
			result.add(UserLocalMapper.transformToUserLocalDto(user));
		}
		
		return GeneratorResponse.ok(result);
	}
	
	public ResponseDto<UserLocalRowDto> getAllByPagination(RequestPaginationUserDto request, 
			RequestSaveAudit requestAudit){
		
		var specification = new UserLocalSpecification<UserLocal>();
		var filters = specification.getAll(request);
		int limit = request.getLimit();
		int page = request.getPage();
		if(limit <= 0) limit = 10;
		if(page < 0) page = 0;
		var pageRequest = PageRequest.of(page, limit);
		
		var resultSet = this.userLocalRepository.findAll(filters, pageRequest);
		List<UserLocalRowDto> result = getResultFromPageUsers(resultSet);
		this.saveAuditationPagination(request, requestAudit);
		return GeneratorResponse.ok(result);
	}
	
	public ResponseDto<Long> getAllTotal(RequestPaginationUserDto request){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var specification = new UserLocalSpecification<UserLocal>();
		var filters = specification.getAll(request);
		long total = this.userLocalRepository.count(filters);
		return GeneratorResponse.ok(List.of(total));
	}
	
	public ResponseDto<UserLocalDetailsDto> getDetailsById(Long id){
		if(id == null)
			return GeneratorResponse.fail(new InvalidParameter(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userLocalRepository.findById(id);
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		if(userInDb.get().getIsDeleted())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var user = userInDb.get();
		var userDetails = UserLocalMapper.transformToUserLocalDetailsDto(user);		
		return GeneratorResponse.ok(List.of(userDetails));
	}
	
	public ResponseDto<UserLocalDetailsDto> getDetailsById(Long id, RequestSaveAudit requestAudit){
		if(id == null)
			return GeneratorResponse.fail(new InvalidParameter(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userLocalRepository.findById(id);
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var user = userInDb.get();
		var userDetails = UserLocalMapper.transformToUserLocalDetailsDto(user);
		
		this.auditService.saveAsReadAuditRequest(requestAudit, "id=" + user.getUsername());
		
		return GeneratorResponse.ok(List.of(userDetails));
	}
	
	public ResponseDto<UserLocalDto> getDetailsProfileById(Long id){
		if(id == null)
			return GeneratorResponse.fail(new InvalidParameter(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userLocalRepository.findById(id);
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		if(userInDb.get().getIsDeleted())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var user = UserLocalMapper.transformToUserLocalDto(userInDb.get());
		return GeneratorResponse.ok(List.of(user));
	}
	
	public ResponseDto<RenovationTimeDto> isRenovationPassword(Long idUser){
		if(idUser == null) {
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		}
		
		var lastRenovationInDb = this.userLocalRepository.getLastPasswordRenovationById(idUser);
		if(lastRenovationInDb.isEmpty()) {
			return GeneratorResponse.fail(new NotFound(
					GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		}
		
		var response = new RenovationTimeDto();
		var renovationTime = lastRenovationInDb.get(0);
		response.setLastRenovation(renovationTime.getLastPasswordRenovation());
		response.setIsRenovationTime(false);
		
		if(renovationTime.getLastPasswordRenovation() == null) {
			response.setIsRenovationTime(true);
			return GeneratorResponse.ok(List.of(response));
		}
		
		var daysRenovationConfig = this.systemService.getDaysPasswordRenovation();
		var daysRenovation = Integer.parseInt(daysRenovationConfig.getName());
		var now = LocalDateTime.now();
		var limit = renovationTime.getLastPasswordRenovation().plusDays(daysRenovation);
		if(now.isAfter(limit) || now.equals(limit)) {
			response.setIsRenovationTime(true);
			return GeneratorResponse.ok(List.of(response));
		}
		
		return GeneratorResponse.ok(List.of(response));
	}
	
	public ResponseDto<Boolean> save(RequestSaveUserDto request, RequestSaveAudit requestAudit){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var usernamesInDb = this.userLocalRepository.findAllByUsername(request.getUsername());
		var emailsInDb = this.userLocalRepository.findAllByEmail(request.getEmail());
		if(!usernamesInDb.isEmpty())
			return GeneratorResponse.fail(new Duplicate(GeneralErrors.ERR_400_USER_DUPLICATE_USERNAME_CODE, GeneralErrors.ERR_400_USER_DUPLICATE_USERNAME_MSG));
		if(!emailsInDb.isEmpty())
			return GeneratorResponse.fail(new Duplicate(GeneralErrors.ERR_400_USER_DUPLICATE_EMAIL_CODE, GeneralErrors.ERR_400_USER_DUPLICATE_EMAIL_MSG));
		
		var userToSave = UserLocalMapper.transformToUserLocal(request);
		userToSave.setPassword(request.getPassword());
		userToSave.setCreatedAt(DateTimeFormatHelper.formatLocalDateTimeNow("yyyy-MM-dd HH:mm:ss.SSS"));
		userToSave.setLastSession(userToSave.getCreatedAt());
		userToSave.setType2FA(request.getType2FA());
		
		var userValidator = new UserValidator(userToSave);
		if(userValidator.isRequiredFieldsNull())
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_UNDEFINED_FIELDS_CODE, GeneralErrors.ERR_400_UNDEFINED_FIELDS_MSG));
		
		var code = userValidator.getCodeAfterValidation();
		var message = userValidator.getMessageAfterValidation();
		if(!message.equals("OK"))
			return GeneratorResponse.fail(new InvalidRequest(code, message));
		
		userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
		
		var userGroupInDb = userGroupRepository.findById(request.getIdRole());
		if(userGroupInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_CODE, GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_MSG));
		
		userToSave.setRole(userGroupInDb.get());

		var userSaved = this.userLocalRepository.save(userToSave);
		
		this.auditService.saveAsCreateAuditRequest(requestAudit,  userSaved.toString());
		
		return GeneratorResponse.ok(List.of(true));
	}
	
	public ResponseDto<Boolean> updateImage(RequestSaveUserImageDto request, Long idUser){
		if(request == null || idUser == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userLocalRepository.findById(idUser);
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var user = userInDb.get();
		user.setImageProfile(request.getImageProfile());
		this.userLocalRepository.save(user);
		return GeneratorResponse.ok(List.of(true));
	}

	public ResponseDto<Boolean> update(RequestSaveUserDto request, RequestSaveAudit requestAudit){
		if(request == null)
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userLocalRepository.findById(request.getId());
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(
					GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var previousParameters = userInDb.get().toString();
		var usernamesInDb = this.userLocalRepository.findAllByUsername(request.getUsername());
		var emailsInDb = this.userLocalRepository.findAllByEmail(request.getEmail());
		
		var responseUsernameInUse = this.isUsernameInUse(request, usernamesInDb);
		if(responseUsernameInUse != null) return responseUsernameInUse;
		
		var responseEmailInUse = this.isEmailInUse(request, emailsInDb);
		if(responseEmailInUse != null) return responseEmailInUse;
		
		var userToUpdate = userInDb.get();
		userToUpdate.setActive(request.getActive());
		userToUpdate.setEmail(request.getEmail());
		userToUpdate.setFirstName(request.getFirstName());
		userToUpdate.setIsDeleted(false);
		userToUpdate.setLastName(request.getLastName());
		userToUpdate.setUsername(request.getUsername());
		userToUpdate.setId(userInDb.get().getId());
		userToUpdate.setCreatedAt(userInDb.get().getCreatedAt());
		userToUpdate.setLastSession(userInDb.get().getLastSession());
		userToUpdate.setPassword(userInDb.get().getPassword());
		userToUpdate.setType2FA(request.getType2FA());
		
		var userValidator = new UserValidator(userToUpdate);
		if(userValidator.isRequiredFieldsNull())
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_UNDEFINED_FIELDS_CODE, GeneralErrors.ERR_400_UNDEFINED_FIELDS_MSG));
		
		var code = userValidator.getCodeAfterValidationNoPassword();
		var message = userValidator.getMessageAfterValidationNoPassword();
		if(!message.equals("OK"))
			return GeneratorResponse.fail(new InvalidRequest(code, message));
		
		var responsePasswordChanged = this.updatePasswordIfNotNull(request, userToUpdate);
		if(responsePasswordChanged != null) return responsePasswordChanged;
		
		var responseGroupChanged = this.updateGroupIfChanged(request, userToUpdate);
		if(responseGroupChanged != null) return responseGroupChanged;
		
		var userUpdated = this.userLocalRepository.save(userToUpdate);
		
		this.auditService.saveAsUpdateAuditRequest(requestAudit, previousParameters, userUpdated.toString());
		
		return GeneratorResponse.ok(List.of(true));
	}
	
	public ResponseDto<Boolean> updatePasswordAsRenovation(RequestPasswordAsRenovationDto request, RequestSaveAudit requestAudit){
		if(request == null || requestAudit == null) {
			return GeneratorResponse.fail(new InvalidRequest(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		}
		
		var password = request.getPassword();
		var idUser = request.getId();
		
		var userValidator = new UserValidator(null);
		if(!userValidator.isPasswordValid(password))
			return GeneratorResponse.fail(new InvalidRequest(
					GeneralErrors.ERR_400_USER_PASSWORD_VALID_CODE, GeneralErrors.ERR_400_USER_PASSWORD_VALID_MSG));
		
		if(this.userPasswordUsedService.isPasswordUsed(password, idUser))
			return GeneratorResponse.fail(new InvalidParameter(
					GeneralErrors.ERR_400_USER_PASSWORD_ALREADY_USED_CODE, GeneralErrors.ERR_400_USER_PASSWORD_ALREADY_USED_MSG)); 
		
		String passwordHashed = this.passwordEncoder.encode(password);
		this.userLocalRepository.updatePasswordById(passwordHashed, idUser);
		this.userPasswordUsedService.save(passwordHashed, idUser);
		this.userLocalRepository.updateLastRenovationPassword(LocalDateTime.now(), idUser);
		
		this.auditService.saveAsReadAuditRequest(requestAudit, "id=" + idUser + "&renovation_pass=changed");
		
		return GeneratorResponse.ok(List.of(true));
	}
	
	
	public ResponseDto<Boolean> deleteById(Long id, Long idUserLoggedIn, RequestSaveAudit requestAudit){
		if(id == null)
			return GeneratorResponse.fail(new InvalidParameter(GeneralErrors.ERR_400_BAD_REQUEST_CODE, GeneralErrors.ERR_400_BAD_REQUEST_MSG));
		
		var userInDb = this.userLocalRepository.findById(id);
		if(userInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		var userLoggedInDb = this.userLocalRepository.findById(idUserLoggedIn);
		if(userLoggedInDb.isEmpty())
			return GeneratorResponse.fail(new NotFound(GeneralErrors.ERR_404_USER_NOT_FOUND_CODE, GeneralErrors.ERR_404_USER_NOT_FOUND_MSG));
		
		if((userLoggedInDb.get().getRole().getName().equals("SUPERADMIN") && userInDb.get().getRole().getName().equals("COUNTELLIS")) ||
				(userLoggedInDb.get().getRole().getName().equals("ADMIN") && userInDb.get().getRole().getName().equals("SUPERADMIN")) ||
				(userLoggedInDb.get().getRole().getName().equals("ADMIN") && userInDb.get().getRole().getName().equals("COUNTELLIS"))){
			return GeneratorResponse.fail(new Forbidden(GeneralErrors.ERR_403_SERVICE_ACCESS_RESTRICTED_CODE, GeneralErrors.ERR_403_SERVICE_ACCESS_RESTRICTED_MSG));
		}
		
		String currentUsername = userInDb.get().getUsername();
		String currentEmail = userInDb.get().getEmail();
		String endDelete = "#deleted";
		
		currentUsername = currentUsername.length() + endDelete.length() >= 50 ?
				currentUsername.substring(0, 40) + endDelete : currentUsername + endDelete;
		currentEmail = currentEmail.length() + endDelete.length() >= 100 ?
				currentEmail.substring(0, 90) + endDelete : currentEmail + endDelete;
		
		this.userLocalRepository.updateUsernameAndEmailById(currentUsername, currentEmail, id);
		this.userLocalRepository.updateAsDeleted(id);
		
		this.auditService.saveAsDeleteAuditRequest(requestAudit, "id=" + id + "&email=" + userInDb.get().getEmail());
		
		return GeneratorResponse.ok(List.of(true));
	}
	
	public Map<String, UserLocal> getAllGroupedAsName(){
		var users = this.userLocalRepository.findAll();
		var hashmap = new HashMap<String, UserLocal>();
		
		for(int i = 0; i < users.size(); i++) {
			var user = users.get(i);
			String name = user.getUsername();
			if(!hashmap.containsKey(name)) {
				hashmap.put(name, user);
			}
		}
		return hashmap;
	}
	
	protected List<UserLocalRowDto> getResultFromPageUsers(Page<UserLocal> resultSet) {
		List<UserLocalRowDto> result = new ArrayList<>();
		if(!resultSet.getContent().isEmpty()) {
			var users = resultSet.getContent();
			users = users.stream()
					.filter(z -> !z.getIsDeleted())
					.toList();
			for(int i = 0; i < users.size(); i++) {
				var user = users.get(i);
				var userDto = UserLocalMapper.transformToUserLocalRowDto(user);
				result.add(userDto);
			}
		}
		
		return result;
	}
	
	protected void saveAuditationPagination(RequestPaginationUserDto request, 
			RequestSaveAudit requestAudit) {
		
		this.auditService.saveAsReadAuditRequest(requestAudit, request.toString());
	}
	
	protected ResponseDto<Boolean> isEmailInUse(
			RequestSaveUserDto request,
			List<UserLocal> emailsInDb){
		for(int i = 0; i < emailsInDb.size(); i++) {
			var user = emailsInDb.get(i);
			if(user.getEmail().equals(request.getEmail()) && !user.getId().equals(request.getId())) {
				return GeneratorResponse.fail(new Duplicate(
						GeneralErrors.ERR_400_USER_DUPLICATE_EMAIL_CODE, GeneralErrors.ERR_400_USER_DUPLICATE_EMAIL_MSG));
			}
		}
		
		return null;
	}
	
	protected ResponseDto<Boolean> isUsernameInUse(
			RequestSaveUserDto request,
			List<UserLocal> usernamesInDb){
		for(int i = 0; i < usernamesInDb.size(); i++) {
			var user = usernamesInDb.get(i);
			if(user.getUsername().equals(request.getUsername()) && !user.getId().equals(request.getId())) {
				return GeneratorResponse.fail(new Duplicate(
						GeneralErrors.ERR_400_USER_DUPLICATE_USERNAME_CODE, GeneralErrors.ERR_400_USER_DUPLICATE_USERNAME_MSG));
			}
		}
		
		return null;
	}
	
	protected ResponseDto<Boolean> updatePasswordIfNotNull(RequestSaveUserDto request, UserLocal userToUpdate){
		if(request.getPassword() != null) {
			var userValidator = new UserValidator(null);
			if(!userValidator.isPasswordValid(request.getPassword()))
				return GeneratorResponse.fail(new InvalidRequest(
						GeneralErrors.ERR_400_USER_PASSWORD_VALID_CODE, GeneralErrors.ERR_400_USER_PASSWORD_VALID_MSG));
			
			if(this.userPasswordUsedService.isPasswordUsed(request.getPassword(), userToUpdate.getId())) {
				return GeneratorResponse.fail(new InvalidParameter(
						GeneralErrors.ERR_400_USER_PASSWORD_ALREADY_USED_CODE, GeneralErrors.ERR_400_USER_PASSWORD_ALREADY_USED_MSG)); 
			}
			
			userToUpdate.setLastPasswordRenovation(LocalDateTime.now());
			userToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		
		return null;
	}
	
	protected ResponseDto<Boolean> updateGroupIfChanged(
			RequestSaveUserDto request, 
			UserLocal userToUpdate){
		if(request.getIdRole() != null && !request.getIdRole().equals(userToUpdate.getRole().getId())) {
			var userGroupInDb = userGroupRepository.findById(request.getIdRole());
			if(userGroupInDb.isEmpty())
				return GeneratorResponse.fail(new NotFound(
						GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_CODE, GeneralErrors.ERR_404_GROUP_USERS_NOT_FOUND_MSG));
			
			userToUpdate.setRole(userGroupInDb.get());
		}
		
		return null;
	}
}
