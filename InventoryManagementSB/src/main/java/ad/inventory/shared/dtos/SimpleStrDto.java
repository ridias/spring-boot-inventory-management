package ad.inventory.shared.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleStrDto {

	private String id;
	private String name;
	
	public SimpleStrDto(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
