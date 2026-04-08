package ad.inventory.api.racks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RackService {

	@Autowired
	private RackRepository rackRepository;
}
