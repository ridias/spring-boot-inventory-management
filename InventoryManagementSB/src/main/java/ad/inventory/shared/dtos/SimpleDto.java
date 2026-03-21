package ad.inventory.shared.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimpleDto {

	private final Long id;
	private final String name;
	private final String keyTranslate;
	
	
}
