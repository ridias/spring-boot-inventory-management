package ad.inventory.api.aisles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;

@Service
public class AisleService {

	@Autowired
	private AisleRepository aisleRepository;
	
	public ResponseDto<Aisle> getAll(){
		return GeneratorResponse.ok(this.aisleRepository.findAll());
	}
}
