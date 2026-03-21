package ad.inventory.api.users;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenovationTimeDto {
	
	private Boolean isRenovationTime;
	private LocalDateTime lastRenovation;
	
}
