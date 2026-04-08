package ad.inventory.api.units;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/units")
public class UnitController {

	@Autowired
	private UnitService unitService;
}
