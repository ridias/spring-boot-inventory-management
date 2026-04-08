package ad.inventory.api.units;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitService {

	@Autowired
	private UnitRepository unitRepository;
}
