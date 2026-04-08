package ad.inventory.api.aisles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/aisles")
public class AisleController {

	@Autowired
	private AisleService aisleService;
}
