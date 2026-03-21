package ad.inventory.api.actions;

import ad.inventory.shared.models.BaseEntitySimpleTranslation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_actions")
@Getter
@Setter
public class Action extends BaseEntitySimpleTranslation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 255, nullable = false)
	private String name;
	
}
