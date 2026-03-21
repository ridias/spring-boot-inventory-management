package ad.inventory.emails;

import lombok.Getter;

@Getter
public enum MessageValidation2FABundle {

	EN(
		"en",
		"InvMag 2FA Code",
	    "Login Verification Code",
	    "Session Verification",
	    "Dear <strong>user</strong>,",
	    "You have successfully logged into your <strong>InvMag</strong> account. "
	    	+ "To complete the process and verify your identity, please enter the following two-factor authentication (2FA) code:",
	    "Note:",
	    "This code expires in <strong>120 seconds</strong>. Once expired, you will need to log in again to receive a new code.",
	    "If you did not perform this action, we recommend that you immediately change your password and contact our support team.",
	    "Thank you for trusting us.",
	    "Sincerely,",
	    "The InvMag Team",
	    "This is an automatic email. If you have questions or need help, please contact our customer support service."
	),
	ES(
		"es",
		"InvMag Código 2FA",
	    "Código de verificación de inicio de sesión",
	    "Verificación de Sesión",
	    "Estimado/a <strong>usuario/a</strong>,",
	    "Has iniciado sesión correctamente en tu cuenta de <strong>InvMag Platform</strong>. "
	    	+ "Para completar el proceso y verificar tu identidad, por favor introduce el siguiente código de doble factor (2FA):",
	    "Atención:",
	    "Este código expira en <strong>120 segundos</strong>. Una vez expirado, deberás volver a iniciar sesión para recibir uno nuevo.",
	    "Si no has realizado esta acción, te recomendamos que cambies tu contraseña inmediatamente y contactes con nuestro equipo de soporte.",
	    "Gracias por confiar en nosotros.",
	    "Atentamente,",
	    "El equipo de InvMag Platform",
	    "Este es un correo automático. Si tienes dudas o necesitas ayuda, ponte en contacto con nuestro servicio de atención al cliente."	
	),
	CAT(
		"ca",
		"InvMag Codi 2FA",
	    "Codi de verificació d'inici de sessió",
	    "Verificació de Sessió",
	    "Benvolgut/da <strong>usuari/ària</strong>,",
	    "Has iniciat sessió correctament al teu compte de <strong>InvMag Platform</strong>. "
	    	+ "Per completar el procés i verificar la teva identitat, si us plau, introdueix el següent codi de doble factor (2FA):",
	    "Atenció:",
	    "Aquest codi expira en <strong>120 segons</strong>. Un cop expirat, hauràs de tornar a iniciar sessió per rebre un nou codi.",
	    "Si no has realitzat aquesta acció, et recomanem que canviïs immediatament la teva contrasenya i contactis amb el nostre equip de suport.",
	    "Gràcies per confiar en nosaltres.",
	    "Atentament,",
	    "L'equip de InvMag Platform",
	    "Aquest és un correu automàtic. Si tens dubtes o necessites ajuda, posa’t en contacte amb el nostre servei d’atenció al client."	
	);
	
	private final String lang;
	private final String subject;
    private final String title;
    private final String header;
    private final String greeting;
    private final String bodyIntro;
    private final String noteLabel;
    private final String expirationText;
    private final String warningText;
    private final String closing;
    private final String farewell;
    private final String team;
    private final String footerNote;
    
    MessageValidation2FABundle(
    		String lang,
            String subject,
            String title,
            String header,
            String greeting,
            String bodyIntro,
            String noteLabel,
            String expirationText,
            String warningText,
            String closing,
            String farewell,
            String team,
            String footerNote
    ) {
    	this.lang = lang;
        this.subject = subject;
        this.title = title;
        this.header = header;
        this.greeting = greeting;
        this.bodyIntro = bodyIntro;
        this.noteLabel = noteLabel;
        this.expirationText = expirationText;
        this.warningText = warningText;
        this.closing = closing;
        this.farewell = farewell;
        this.team = team;
        this.footerNote = footerNote;
    }
	
}
