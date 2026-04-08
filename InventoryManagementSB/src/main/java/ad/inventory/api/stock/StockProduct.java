package ad.inventory.api.stock;

import ad.inventory.api.aisles.Aisle;
import ad.inventory.api.levels.Level;
import ad.inventory.api.products.Product;
import ad.inventory.api.racks.Rack;
import ad.inventory.api.wirehouses.Wirehouse;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products_have_stock")
@Getter
@Setter
public class StockProduct {

	@EmbeddedId
	private StockProductId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_product", nullable = false)
	@MapsId("idProduct")
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_aisle", nullable = false)
	@MapsId("idAisle")
	private Aisle aisle;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_rack", nullable = false)
	@MapsId("idRack")
	private Rack rack;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_wirehouse", nullable = false)
	@MapsId("idWirehouse")
	private Wirehouse wirehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_level", nullable = false)
	@MapsId("idLevel")
	private Level level;
	
	@Column(name = "min_reserved_stock", nullable = false)
	private Integer minReservedStock;
	
	@Column(name = "stock", nullable = false)
	private Integer stock;
	
    @Column(name = "price_buy", nullable = false)
    private Double priceBuy;
    
    @Column(name = "price_sell", nullable = false)
    private Double priceSell;
	
	
	
	
}
