package ad.inventory.emails;

import lombok.Getter;

@Getter
public enum MessageRequestRecoveryPasswordBundle {

	EN(
		"en",
	    "Password Reset",
	    "Password Reset Request",
	    "Dear <strong>customer</strong>,",
	    "We received a request to change the password for your <strong>InvMag Platform</strong> account. If you requested this change, please follow these steps to update your password:",
	    "Click on the following link to reset your password:",
	    "This link will be valid for the next <strong>30 minutes</strong>. If you don't complete the process within this time, you'll need to request another password reset.",
	    "If you didn't request this change, you can safely ignore this email; your current password will remain unchanged.",
	    "For your security, we recommend that you never share your password with anyone and always use a strong password.",
	    "If you have any questions or need assistance, please don't hesitate to contact our support team.",
	    "Sincerely,<br><strong>InvMag Platform Team</strong>"
	),
	ES(
		"es",
	    "Cambio de contraseña",
	    "Cambio de contraseña",
	    "Estimado/a <strong>cliente</strong>,",
	    "Hemos recibido una solicitud para cambiar la contraseña de tu cuenta en <strong>InvMag Platform</strong>. Si has solicitado este cambio, por favor sigue los siguientes pasos para actualizar tu contraseña:",
	    "Haz clic en el siguiente enlace para cambiar tu contraseña:",
	    "El enlace estará disponible durante los próximos <strong>30 minutos</strong>. Si no realizas el cambio en este tiempo, deberás solicitarlo nuevamente.",
	    "Si no has solicitado este cambio, puedes ignorar este correo electrónico; tu contraseña actual no se verá afectada.",
	    "Por tu seguridad, te recomendamos no compartir tu contraseña con nadie y asegurarte de utilizar una contraseña segura.",
	    "Si tienes alguna duda o necesitas asistencia, no dudes en contactar con nuestro equipo de soporte.",
	    "Atentamente,<br><strong>El equipo de InvMag Platform</strong>"
	),
	CAT(
		"ca",
	    "Canvi de contrasenya",
	    "Canvi de contrasenya",
	    "Benvolgut/da <strong>client/a</strong>,",
	    "Hem rebut una sol·licitud per canviar la contrasenya del teu compte a <strong>InvMag Platform</strong>. Si has demanat aquest canvi, si us plau, segueix els següents passos per actualitzar la teva contrasenya:",
	    "Fes clic en el següent enllaç per canviar la teva contrasenya:",
	    "L'enllaç estarà disponible durant els propers <strong>30 minuts</strong>. Si no fas el canvi en aquest temps, hauràs de sol·licitar-ne un de nou.",
	    "Si no has sol·licitat aquest canvi, pots ignorar aquest correu electrònic; la teva contrasenya actual no es veurà afectada.",
	    "Per la teva seguretat, et recomanem no compartir la teva contrasenya amb ningú i assegurar-te d'utilitzar una clau segura.",
	    "Si tens algun dubte o necessites assistència, no dubtis en contactar amb el nostre equip de suport.",
	    "Atentament,<br><strong>L'equip de InvMag Platform</strong>"
	);
	
    private final String languageCode;
    private final String title;
    private final String subject;
    private final String greeting;
    private final String intro;
    private final String step1;
    private final String step2;
    private final String noRequestWarning;
    private final String securityAdvice;
    private final String supportText;
    private final String closing;
	
    MessageRequestRecoveryPasswordBundle(
    	String languageCode,
        String title,
        String subject,
        String greeting,
        String intro,
        String step1,
        String step2,
        String noRequestWarning,
        String securityAdvice,
        String supportText,
        String closing
    ) {
        this.languageCode = languageCode;
        this.title = title;
        this.subject = subject;
        this.greeting = greeting;
        this.intro = intro;
        this.step1 = step1;
        this.step2 = step2;
        this.noRequestWarning = noRequestWarning;
        this.securityAdvice = securityAdvice;
        this.supportText = supportText;
        this.closing = closing;
    }
}
