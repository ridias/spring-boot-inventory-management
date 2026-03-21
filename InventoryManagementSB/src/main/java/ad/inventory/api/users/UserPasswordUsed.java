package ad.inventory.api.users;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_password_used")
@Getter
@Setter
public class UserPasswordUsed {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "password_used", nullable = false, length = 1024)
	private String passwordUsed;
	
	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime createdAt;
	
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = true)
    private UserLocal user;
}
