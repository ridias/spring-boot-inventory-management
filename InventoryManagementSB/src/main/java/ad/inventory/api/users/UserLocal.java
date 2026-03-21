package ad.inventory.api.users;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ad.inventory.api.roles.Role;
import ad.inventory.shared.env.GeneralVariables;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLocal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8489800018981511093L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = GeneralVariables.COLUMN_TIMESTAMP_WITH_TIME_ZONE)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at", columnDefinition = GeneralVariables.COLUMN_TIMESTAMP_WITH_TIME_ZONE)
    private LocalDateTime deletedAt;

    @Column(name = "last_session", nullable = false, columnDefinition = GeneralVariables.COLUMN_TIMESTAMP_WITH_TIME_ZONE)
    private LocalDateTime lastSession;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name = "last_email_sent", columnDefinition = GeneralVariables.COLUMN_TIMESTAMP_WITH_TIME_ZONE)
    private LocalDateTime lastEmailSent;

    @Column(name = "recovery_token", length = 255)
    private String recoveryToken;

    @Column(name = "type_2fa")
    private Integer type2FA;

    @Column(name = "code_2fa", length = 20)
    private String code2fa;

    @Column(name = "last_time_generated_2fa", columnDefinition = GeneralVariables.COLUMN_TIMESTAMP_WITH_TIME_ZONE)
    private LocalDateTime lastTimeGenerated2fa;

    @Column(name = "retries_2fa")
    private Integer retries2fa;

    @Column(name = "last_password_renovation", columnDefinition = GeneralVariables.COLUMN_TIMESTAMP_WITH_TIME_ZONE)
    private LocalDateTime lastPasswordRenovation;
    
    @Column(name = "imageprofile", columnDefinition = "text")
    private String imageProfile;

    @ManyToOne
    @JoinColumn(name = "id_group", nullable = false)
    private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.getName()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
