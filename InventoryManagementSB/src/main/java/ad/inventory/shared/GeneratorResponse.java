package ad.inventory.shared;

import java.util.ArrayList;
import java.util.List;

import ad.inventory.shared.dtos.ResponseDto;
import ad.inventory.shared.exceptions.CustomError;

public class GeneratorResponse {

	private GeneratorResponse() {
		
	}
	
	public static <T> ResponseDto<T> ok(List<T> result){
		var response = new ResponseDto<T>();
		response.setSuccess(true);
		response.setItems(result);
		response.setErr(null);
		return response;
	}
	
	public static <T> ResponseDto<T> fail(CustomError ex){
		var response = new ResponseDto<T>();
		response.setSuccess(false);
		response.setItems(new ArrayList<>());
		response.setErr(ex);
		return response;
	}
}
