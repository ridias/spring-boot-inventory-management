package ad.inventory.emails;

import java.util.Map;

public interface EmailMessageGenerator {

	String getMessage(Map<String, String> properties);
	String getSubject();
}
