package ad.inventory.api.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.dtos.SimpleDto;


@RestController
@RequestMapping("api/v1/actions")
public class ActionController {

	@Autowired
	private ActionService actionService;

	@GetMapping("/all")
	public ResponseEntity<ResponseDto<SimpleDto>> getAll(){
		var response = this.actionService.getAll();
		if(!response.isSuccess())
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
