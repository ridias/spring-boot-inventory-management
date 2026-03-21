package ad.inventory.emails;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public abstract class BaseEmailService {

	@Value("${inventory.mail.smtp.host}")
	protected String smtpHost;
	@Value("${inventory.mail.smtp.port}")
	protected String smtpPort;
	@Value("${inventory.mail.smtp.username}")
	protected String smtpUsername;
	@Value("${inventory.mail.smtp.password}")
	protected String smtpPassword;
	
	protected Properties createSmtpProperties() {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.host", smtpHost);
		prop.put("mail.smtp.port", smtpPort);
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.ssl.checkserveridentity", "true");
        return prop;
	}
	
    protected Session createEmailSession() {
        Properties prop = createSmtpProperties();
        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });
    }
    
    protected Message createBaseMessage(Session session, String destinatary, String subject) 
            throws AddressException, MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUsername));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatary));
        message.setSubject(subject);
        return message;
    }
    
    protected void sendMessage(Message message) throws MessagingException {
        Transport.send(message);
    }
}
