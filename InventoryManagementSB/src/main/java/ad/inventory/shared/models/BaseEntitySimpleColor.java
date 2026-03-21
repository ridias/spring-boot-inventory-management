package ad.inventory.shared.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntitySimpleColor {

	@Column(length = 10, nullable = false)
	private String color;
	@Column(name = "font_color", length = 10, nullable = false)
	private String fontColor;
}
