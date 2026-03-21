package ad.inventory.emails;

import java.util.Map;

public class MessageRequestRecoveryPasswordBase implements EmailMessageGenerator {

	private final MessageRequestRecoveryPasswordBundle bundle;
	
	public MessageRequestRecoveryPasswordBase(MessageRequestRecoveryPasswordBundle bundle) {
		this.bundle = bundle;
	}
	
	@Override
	public String getMessage(Map<String, String> properties) {
		String url = null;
		if(properties.containsKey("url")) {
			url = properties.get("url");
		}
		
		if(url == null || url.isBlank()) {
			return null;
		}
		
        return "<html lang=\"" + bundle.getLanguageCode() + "\">\r\n"
        + "<head>\r\n"
        + "    <meta charset=\"UTF-8\">\r\n"
        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
        + "    <title>" + bundle.getTitle() + "</title>\r\n"
        + "</head>\r\n"
        + "<body>\r\n"
        + "    <p>" + bundle.getGreeting() + "</p>\r\n"
        + "    <p>" + bundle.getIntro() + "</p>\r\n"
        + "    <ol>\r\n"
        + "        <li>" + bundle.getStep1() + "\r\n"
        + "        <p><a href=\"" + url + "\" target=\"_blank\">" + url + "</a></p></li>\r\n"
        + "        <li>" + bundle.getStep2() + "</li>\r\n"
        + "    </ol>\r\n"
        + "    <p>" + bundle.getNoRequestWarning() + "</p>\r\n"
        + "    <p>" + bundle.getSecurityAdvice() + "</p>\r\n"
        + "    <p>" + bundle.getSupportText() + "</p>\r\n"
        + "    <p>" + bundle.getClosing() + "</p>\r\n"
        + "</body>\r\n"
        + "</html>";
	}

	@Override
	public String getSubject() {
		return this.bundle.getSubject();
	}

}
