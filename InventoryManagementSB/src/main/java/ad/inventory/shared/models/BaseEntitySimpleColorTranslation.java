package ad.inventory.shared.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntitySimpleColorTranslation extends BaseEntitySimpleColor {

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;
	@Column(name = "key_translate", length = 255)
	private String keyTranslate;
	
}
