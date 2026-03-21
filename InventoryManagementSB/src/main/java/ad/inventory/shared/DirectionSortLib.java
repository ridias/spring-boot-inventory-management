package ad.inventory.shared;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class DirectionSortLib {

	private DirectionSortLib() {
		
	}

	public static Direction getDirectionSort(String value) {
		if(value == null) return Sort.Direction.ASC;
		return value.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
	}
}
