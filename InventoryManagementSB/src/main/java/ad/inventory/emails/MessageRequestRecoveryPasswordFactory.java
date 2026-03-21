package ad.inventory.emails;

import org.springframework.stereotype.Component;

@Component
public class MessageRequestRecoveryPasswordFactory {

	public EmailMessageGenerator create(String lang) {
		EmailMessageGenerator messageGenerator = new MessageRequestRecoveryPasswordBase(MessageRequestRecoveryPasswordBundle.EN);
		if(lang == null) {
			messageGenerator = new MessageRequestRecoveryPasswordBase(MessageRequestRecoveryPasswordBundle.EN);
		}else if(lang.equals("es")) {
			messageGenerator = new MessageRequestRecoveryPasswordBase(MessageRequestRecoveryPasswordBundle.ES);
		}else if(lang.equals("ca")) {
			messageGenerator = new MessageRequestRecoveryPasswordBase(MessageRequestRecoveryPasswordBundle.CAT);
		}
		
		return messageGenerator;
	}
}
