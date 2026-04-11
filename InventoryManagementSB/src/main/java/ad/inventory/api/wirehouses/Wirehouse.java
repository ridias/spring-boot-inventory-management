package ad.inventory.api.wirehouses;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wirehouses")
@Getter
@Setter
public class Wirehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false)
    private Boolean active;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    
    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime deletedAt;
    
    @Column(nullable = false)
    private String address;
    
    @Column(name = "email_responsible", nullable = false)
    private String emailResponsible;
    
    @Column(length = 20)
    private String phone;
    
}
