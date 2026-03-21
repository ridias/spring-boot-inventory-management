package ad.inventory.emails;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

@Service
public class EmailService extends BaseEmailService {

	public void sendEmailSync(String destinatary, String subject, String contentMessage) throws Exception {
		
		Session session = createEmailSession();
		Message message = createBaseMessage(session, destinatary, subject);
		
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(contentMessage, "text/html; charset=utf-8");
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        
        message.setContent(multipart);
        sendMessage(message);
	}
	
	@Async
	public CompletableFuture<Void> sendEmailAsync(String destinatary, String subject, String conetntMessage) throws Exception {
		this.sendEmailSync(destinatary, subject, conetntMessage);
		return CompletableFuture.completedFuture(null);
	}
}
