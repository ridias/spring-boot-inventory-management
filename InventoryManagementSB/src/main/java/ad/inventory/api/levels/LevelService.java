package ad.inventory.api.levels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;

@Service
public class LevelService {

	@Autowired
	private LevelRepository levelRepository;
	
	public ResponseDto<Level> getAll(){
		return GeneratorResponse.ok(this.levelRepository.findAll());
	}
}
