package ad.inventory.api.wirehouses;

public class WirehouseValidator {

	private Wirehouse wirehouse;
	
	public WirehouseValidator(Wirehouse wirehouse) {
		this.wirehouse = wirehouse;
	}
	
	public boolean isRequiredFieldsNull() {
		if(this.wirehouse == null) return true;
		return this.wirehouse.getName() == null ||
				this.wirehouse.getActive() == null ||
				this.wirehouse.getName() == null ||
				this.wirehouse.getEmailResponsible() == null ||
				this.wirehouse.getIsDeleted() == null ||
				this.wirehouse.getAddress() == null;
	}
	
	public int getCodeAfterValidation() {
		/*if(this.wirehouse == null)
			return GeneralErrors.ERR_400_wirehouse_UNDEFINED_CODE;
		if(!isNameValid())
			return GeneralErrors.ERR_400_wirehouse_INVALID_NAME_CODE;
		*/
		
		return 0;
		
	}
	
	public String getMessageAfterValidation() {
		/*if(this.wirehouse == null)
			return GeneralErrors.ERR_400_wirehouse_UNDEFINED_MSG;
		if(!isNameValid())
			return GeneralErrors.ERR_400_wirehouse_INVALID_NAME_MSG;
		*/
		
		return "OK";
	}
}
