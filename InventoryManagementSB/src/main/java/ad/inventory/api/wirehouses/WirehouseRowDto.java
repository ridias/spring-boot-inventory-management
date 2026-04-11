package ad.inventory.api.wirehouses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WirehouseRowDto {

	private Long id;
	private String name;
	private Boolean active;
	private String address; 
	
}
