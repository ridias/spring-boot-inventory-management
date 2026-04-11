package ad.inventory.api.racks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;

@Service
public class RackService {

	@Autowired
	private RackRepository rackRepository;
	
	public ResponseDto<Rack> getAll(){
		return GeneratorResponse.ok(this.rackRepository.findAll());
	}
}
