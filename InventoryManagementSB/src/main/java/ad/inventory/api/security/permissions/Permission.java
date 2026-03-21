package ad.inventory.api.security.permissions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_pages")
@Getter
@Setter
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 255)
	private String name;
	@Column(nullable = false, length = 255)
	private String parent;
	@Column(name = "is_create", nullable = false)
	private Boolean isCreate;
	@Column(name = "is_read", nullable = false)
	private Boolean isRead;
	@Column(name = "is_update", nullable = false)
	private Boolean isUpdate;
	@Column(name = "is_delete", nullable = false)
	private Boolean isDelete;
}
