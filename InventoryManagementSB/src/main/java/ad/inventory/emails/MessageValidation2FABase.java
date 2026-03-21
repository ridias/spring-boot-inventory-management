package ad.inventory.emails;

import java.util.Map;

public class MessageValidation2FABase implements EmailMessageGenerator {
	
	private final MessageValidation2FABundle bundle;
	
	public MessageValidation2FABase(MessageValidation2FABundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public String getMessage(Map<String, String> properties) {
		String codeVerification = null;
		
		if(properties.containsKey("code")) {
			codeVerification = properties.get("code");
		}
		
		if(codeVerification == null || codeVerification.isBlank()) {
			return null;
		}
		
		return this.buildValidationContent(codeVerification);
	}

	@Override
	public String getSubject() {
		return this.bundle.getSubject();
	}
	
	private String buildValidationContent(String code) {
	    return """
	<!DOCTYPE html>
	<html lang="%s">
	    <head>
	        <meta charset="UTF-8">
	        <meta name="viewport" content="width=device-width, initial-scale=1.0">
	        <title>%s</title>
	        <style>
	            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; }
	            .container { max-width: 600px; margin: 0 auto; padding: 20px; }
	            .header { color: #2c3e50; border-bottom: 1px solid #eee; padding-bottom: 10px; }
	            .details { background-color: #f8f9fa; padding: 20px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #3498db; }
	            .code-box {
	                font-size: 24px;
	                font-weight: bold;
	                background-color: #ecf0f1;
	                padding: 15px;
	                display: inline-block;
	                border-radius: 8px;
	                margin: 20px 0;
	                letter-spacing: 3px;
	            }
	            .footer { margin-top: 30px; font-size: 0.9em; color: #7f8c8d; text-align: center; }
	        </style>
	    </head>
	    <body>
	        <div class="container">
	            <div class="header">
	                <h2>%s</h2>
	            </div>
	            <p>%s</p>
	            <p>%s</p>
	            <div class="code-box">%s</div>
	            <p><strong>%s</strong> %s</p>
	            <p>%s</p>
	            <p>%s</p>
	            <div class="footer">
	                <p>%s</p>
	                <p><strong>%s</strong></p>
	                <p>%s</p>
	            </div>
	        </div>
	    </body>
	</html>
	""".formatted(
	        this.bundle.getLang(),
	        this.bundle.getTitle(),
	        this.bundle.getHeader(),
	        this.bundle.getGreeting(),
	        this.bundle.getBodyIntro(),
	        code,
	        this.bundle.getNoteLabel(),
	        this.bundle.getExpirationText(),
	        this.bundle.getWarningText(),
	        this.bundle.getClosing(),
	        this.bundle.getFarewell(),
	        this.bundle.getTeam(),
	        this.bundle.getFooterNote()
	    );
	}
}
