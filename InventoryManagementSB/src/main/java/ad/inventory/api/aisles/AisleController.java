package ad.inventory.api.aisles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ad.inventory.shared.ControllerHelper;
import ad.inventory.shared.dtos.ResponseDto;

@RestController
@RequestMapping("api/v1/aisles")
public class AisleController {

	@Autowired
	private AisleService aisleService;
	
	@GetMapping("/all")
	public ResponseEntity<ResponseDto<Aisle>> getAll(
			@CookieValue("inventoryMngToken") String token){
		
		var response = this.aisleService.getAll();
		if(!response.isSuccess())
			return new ResponseEntity<>(response, 
				ControllerHelper.getHttpStatusByCode(response.getErr().getCode()));
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
