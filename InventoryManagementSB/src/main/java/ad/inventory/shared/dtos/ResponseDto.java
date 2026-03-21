package ad.inventory.shared.dtos;

import java.util.List;

import ad.inventory.shared.exceptions.CustomError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> {
	
	private boolean success;
	private List<T> items;
	private CustomError err;

}
