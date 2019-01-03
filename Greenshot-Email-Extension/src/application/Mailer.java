package application;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Extremely simple helper class to send Emails using BFH's smtp server. 
 * based on Mailer of JavaFX professor Philipp Locher from https://web.ti.bfh.ch/~lhp2/pm/src/Mailer.java
 */
public class Mailer {

	public static final String HOST = "smtp.bfh.ch";
	public static final String PORT = "587";
	
	
	/**
	 * Sends an email using BFH's smtp server smtp.bfh.ch.
	 * 
	 * @param login The login (eg. 'abc2')
	 * @param password The password
	 * @param fromEmail The from email must match with the login (eg. 'claus.amsberger@bfh.ch')
	 * @param toEmail The to email (eg. 'samichlaus@wald.ch')
	 * @param subject The subject of the message.
	 * @param message The message
	 * @throws MessagingException
	 */
	public static void send(final String login, final String password, final String fromEmail, final String toEmail, final String subject, final String text)
			throws MessagingException {

		Properties props = new Properties();
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(login, password);
			}
		};

		Session session = Session.getInstance(props, auth);

		// Create a default MimeMessage object.
        Message message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(fromEmail));

        // Set To: header field of the header.
        message.setRecipients(Message.RecipientType.TO,
           InternetAddress.parse(toEmail));

        // Set Subject: header field
        message.setSubject("Bildschirmausgabe");

        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        messageBodyPart.setText("Guten Tag, " + fromEmail + " teilt dieses Dokument mit Ihnen.");

        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        String filename = "H:\\data.png";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        message.setContent(multipart);

        // Send message
        Transport.send(message);
	}
}