package ad.inventory.api.wirehouses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/wirehouses")
public class WirehouseController {

	@Autowired
	private WirehouseService wirehouseService;
	
}
