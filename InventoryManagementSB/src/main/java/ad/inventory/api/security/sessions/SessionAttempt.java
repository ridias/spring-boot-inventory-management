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
@Table(name = "t_session_attempts")
@Getter
@Setter
public class SessionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attempt_time", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime attemptTime;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "success", nullable = false)
    private Boolean success;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserLocal user;

}
