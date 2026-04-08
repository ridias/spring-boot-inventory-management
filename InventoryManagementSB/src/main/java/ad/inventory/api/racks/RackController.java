package ad.inventory.api.racks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/racks")
public class RackController {

	@Autowired
	private RackService rackService;
}
