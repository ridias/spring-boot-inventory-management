package ad.inventory.api.system;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "c_system")
@Getter
@Setter
public class CSystem {

	@Id
	private String key;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String value;
	
	@Column(length = 255)
	private String description;
}
