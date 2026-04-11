package ad.inventory.api.units;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;

@Service
public class UnitService {

	@Autowired
	private UnitRepository unitRepository;
	
	public ResponseDto<Unit> getAll(){
		return GeneratorResponse.ok(this.unitRepository.findAll());
	}
}
