package ad.inventory.api.roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_groups")
@Getter
@Setter
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50, nullable = false)
	private String name;
	@Column(length = 100, nullable = false)
	private String description;
	@Column(columnDefinition = "BOOLEAN default false")
	private Boolean hidden;
	
	public String toString() {
		return "id=" + this.id + 
				"&name=" + this.name + 
				"&description=" + description;
	}
}
