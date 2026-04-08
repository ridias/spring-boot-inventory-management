package ad.inventory.api.stock;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class StockProductId implements Serializable {
	
	private static final long serialVersionUID = 2148052321615943081L;

	private Long idProduct;
	private Long idAisle;
	private Long idRack;
	private Long idWirehouse;
	private Long idLevel;
	
	public StockProductId() {
		
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		StockProductId id = (StockProductId) o;
		return idProduct.equals(id.idProduct) &&
				idAisle.equals(id.idAisle) &&
				idRack.equals(id.idRack) &&
				idWirehouse.equals(id.idWirehouse) &&
				idLevel.equals(id.idLevel);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idProduct, idAisle, idRack, idWirehouse, idLevel);
	}
	
}
