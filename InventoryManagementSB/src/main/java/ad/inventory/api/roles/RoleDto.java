package ad.inventory.api.roles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {

	private Long id;
	private String name;
	private String description;
	private Integer total;
	
	public RoleDto(Long id, String name, String description, Integer total) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.total = total;
	}
}
