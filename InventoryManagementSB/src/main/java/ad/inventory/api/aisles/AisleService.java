package ad.inventory.api.aisles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AisleService {

	@Autowired
	private AisleRepository aisleRepository;
}
