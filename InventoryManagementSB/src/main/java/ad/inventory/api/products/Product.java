package ad.inventory.api.products;

import java.time.LocalDateTime;

import ad.inventory.api.categories.Category;
import ad.inventory.api.units.Unit;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(nullable = false, length = 50)
    private String sku;
    
    @Column(nullable = false, length = 500)
    private String description;
    
    @Column(nullable = false)
    private Boolean active;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    
    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime deletedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unit")
    private Unit unit;
}
