package ad.inventory.api.actions;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.shared.GeneratorResponse;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.dtos.SimpleDto;

@Service
public class ActionService {

	@Autowired
	private ActionRepository actionRepository;
	
	public ResponseDto<SimpleDto> getAll(){
		var result = new ArrayList<SimpleDto>();
		var actions = this.actionRepository.findAllByIsDeletedOrderByNameAsc(false);
		
		for(int i = 0; i < actions.size(); i++) {
			var action = actions.get(i);
			result.add(new SimpleDto(action.getId(), action.getName(), action.getKeyTranslate()));
		}
		
		return GeneratorResponse.ok(result);
	}
}
