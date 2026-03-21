package ad.inventory.api.security.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;

@Service
public class PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;
	
	public ResponseDto<Permission> getAll(){
		var permissions = this.permissionRepository.findAll();
		return GeneratorResponse.ok(permissions);
	}
}
