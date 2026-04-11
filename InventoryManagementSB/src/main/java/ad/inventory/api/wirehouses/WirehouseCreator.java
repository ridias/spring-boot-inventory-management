package ad.inventory.api.wirehouses;

public class WirehouseCreator {

	private WirehouseCreator() {
		
	}
	
	public static WirehouseRowDto createAsRowDto(Wirehouse e) {
		var dto = new WirehouseRowDto();
		if(e == null) return dto;
		
		dto.setActive(e.getActive());
		dto.setAddress(e.getAddress());
		dto.setId(e.getId());
		dto.setName(e.getName());
		
		return dto;
	}
	
	public static WirehouseDetailsDto createAsDetailDto(Wirehouse e) {
		var dto = new WirehouseDetailsDto();
		if(e == null) return dto;
		
		dto.setActive(e.getActive());
		dto.setAddress(e.getAddress());
		dto.setEmailResponsible(e.getEmailResponsible());
		dto.setId(e.getId());
		dto.setName(e.getName());
		
		return dto;
	}
	
	public static Wirehouse create(RequestSaveWirehouseDto request) {
		var wirehouse = new Wirehouse();
		wirehouse.setActive(request.getActive());
		wirehouse.setAddress(request.getAddress());
		wirehouse.setEmailResponsible(request.getEmailResponsible());
		wirehouse.setIsDeleted(false);
		wirehouse.setName(request.getName());
		wirehouse.setPhone(request.getPhone());
		return wirehouse;
	}
	
	public static void updateFromRequest(RequestSaveWirehouseDto request, Wirehouse wirehouse) {
		if(request == null || wirehouse == null) return;
		
		wirehouse.setActive(request.getActive());
		wirehouse.setAddress(request.getAddress());
		wirehouse.setEmailResponsible(request.getEmailResponsible());
		wirehouse.setName(request.getName());
		wirehouse.setPhone(request.getPhone());
	}
}
