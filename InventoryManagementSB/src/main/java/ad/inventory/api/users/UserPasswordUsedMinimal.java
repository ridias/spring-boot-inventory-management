package ad.inventory.api.users;

import java.time.LocalDateTime;

public interface UserPasswordUsedMinimal {

	Long getId();
	String getPasswordUsed();
	LocalDateTime getCreatedAt();
	Long getIdUser();
}
