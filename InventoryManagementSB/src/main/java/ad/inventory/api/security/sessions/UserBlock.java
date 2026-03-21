package ad.inventory.api.security.sessions;

import java.time.LocalDateTime;

import ad.inventory.api.users.UserLocal;
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
@Table(name = "t_users_blocks")
@Getter
@Setter
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_start_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime blockStartTime;

    @Column(name = "block_type", nullable = false, length = 20)
    private String blockType;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "ip", nullable = false, length = 45)
    private String ip;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserLocal user;

}
