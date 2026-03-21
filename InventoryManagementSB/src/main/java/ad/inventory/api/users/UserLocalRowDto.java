package ad.inventory.api.users;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserLocalRowDto {

	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String groupName;
	private Boolean active;
	private LocalDateTime lastSession;
}
