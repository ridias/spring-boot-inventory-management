package ad.inventory.api.audit;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import ad.inventory.api.actions.Action;
import ad.inventory.api.users.UserLocal;
import io.hypersistence.utils.hibernate.type.json.JsonType;

@Entity
@Table(name = "logs_auditation")
@Getter
@Setter
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "executed_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT now()")
    private LocalDateTime executedAt;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false, length = 45)
	private String ip;
	
	@Column(nullable = false, length = 255)
	private String browser;
	
	@Column(nullable = false, columnDefinition = "jsonb")
	@Type(JsonType.class)
	private String request;
	
    @Formula("request::text")
    private String requestAsText;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_action", nullable = false)
	private Action action;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_user", nullable = true)
	private UserLocal user;
}
