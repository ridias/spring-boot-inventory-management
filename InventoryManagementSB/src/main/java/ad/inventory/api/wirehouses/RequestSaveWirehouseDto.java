package ad.inventory.api.wirehouses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSaveWirehouseDto {

	private Long id;
	private String name;
	private Boolean active;
	private String address;
	private String emailResponsible;
	private String phone;
	
}
