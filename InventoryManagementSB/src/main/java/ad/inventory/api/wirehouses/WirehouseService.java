package ad.inventory.api.wirehouses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WirehouseService {

	@Autowired
	private WirehouseRepository wirehouseRepository;
}
