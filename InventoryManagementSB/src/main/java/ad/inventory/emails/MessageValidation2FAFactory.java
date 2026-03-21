package ad.inventory.emails;

import org.springframework.stereotype.Component;

@Component
public class MessageValidation2FAFactory {

	public EmailMessageGenerator create(String lang) {
		EmailMessageGenerator messageGenerator = new MessageValidation2FABase(MessageValidation2FABundle.EN);
		if(lang == null) {
			messageGenerator = new MessageValidation2FABase(MessageValidation2FABundle.EN);
		}else if(lang.equals("es")) {
			messageGenerator = new MessageValidation2FABase(MessageValidation2FABundle.ES);
		}else if(lang.equals("ca")) {
			messageGenerator = new MessageValidation2FABase(MessageValidation2FABundle.CAT);
		}
		
		return messageGenerator;
	}
}
