package ad.inventory.api.security.authorities;

import ad.inventory.api.roles.Role;
import ad.inventory.api.security.permissions.Permission;
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
@Table(name = "groups_have_permissions")
@Getter
@Setter
public class Authority {

	@EmbeddedId
	private AuthorityId id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_group", nullable = false)
	@MapsId("idGroup")
	private Role role;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_page", nullable = false)
	@MapsId("idPermission")
	private Permission permission;
	
	@Column(name = "active_create", nullable = false)
	private Boolean activeCreate;
	@Column(name = "active_read", nullable = false)
	private Boolean activeRead;
	@Column(name = "active_update", nullable = false)
	private Boolean activeUpdate;
	@Column(name = "active_delete", nullable = false)
	private Boolean activeDelete;
}
