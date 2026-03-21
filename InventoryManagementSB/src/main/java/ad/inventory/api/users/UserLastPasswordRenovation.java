package ad.inventory.api.users;

import java.time.LocalDateTime;

public interface UserLastPasswordRenovation {

	Long getId();
	LocalDateTime getLastPasswordRenovation();
}
