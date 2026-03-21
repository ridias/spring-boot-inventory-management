package ad.inventory.api.security.authorities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class AuthorityId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idGroup;
	private Long idPermission;
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		AuthorityId authoId = (AuthorityId) o;
		return idGroup.equals(authoId.idGroup) &&
			idPermission.equals(authoId.idPermission) ;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idGroup, idPermission);
	}
}
