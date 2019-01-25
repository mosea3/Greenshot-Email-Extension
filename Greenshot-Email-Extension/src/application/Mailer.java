package application;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Extremely simple helper class to send Emails using BFH's smtp server. based
 * on Mailer of JavaFX professor Philipp Locher from
 * https://web.ti.bfh.ch/~lhp2/pm/src/Mailer.java
 */
public class Mailer {

	public static final String HOST = "futura.metanet.ch";
	public static final String PORT = "465";

	/**
	 * Sends an email with attachment using specified smtp server futura.metanet.ch
	 * 
	 * @param login      The login (eg. 'abc2')
	 * @param password   The password
	 * @param fromEmail  The from email must match with the login (eg.
	 *                   'andy.moser@blessing.ch')
	 * @param toEmail    The to email (eg. 'samichlaus@wald.ch')
	 * @param subject    The subject of the message.
	 * @param text       The message
	 * @param attachment the URI to the Attachment
	 * @param debug      = false, whether debug output into console activated or not
	 * @throws MessagingException, FileNotFoundException
	 */
	public static void send(final String login, final String password, final String fromEmail, final String toEmail,
			final String subject, final String text, final List attachment, final Boolean debug)
			throws MessagingException, FileNotFoundException {

		Properties props = new Properties();
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");

		if (debug) {
			props.put("mail.debug", "true");
		}

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(login, password);
			}
		};

		Session session = Session.getInstance(props, auth);

		Message message = new MimeMessage(session);
		System.out.println(fromEmail);
		InternetAddress efromEmail = new InternetAddress(fromEmail);

		message.setFrom(efromEmail);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
		message.setSubject(subject);

		BodyPart messageBodyPart = new MimeBodyPart();

		messageBodyPart.setText(text);

		Multipart multipart = new MimeMultipart();

		multipart.addBodyPart(messageBodyPart);

		// iterate through all attachments
		for (int i = 0; i < attachment.size(); i++) {
			messageBodyPart = new MimeBodyPart();
			System.out.println("add:" + attachment.get(i).toString());
			String filename = (String) attachment.get(i);

			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			String[] tokens = filename.split(".+?/(?=[^/]+$)");
			filename = tokens[tokens.length - 1];
			System.out.println("send as" + "Anhang" + (i + 1) + ".jpg");
			messageBodyPart.setFileName("Anhang" + (i + 1) + ".jpg"); // Hoomans love cardinality
			multipart.addBodyPart(messageBodyPart);
		}

		message.setContent(multipart);
		if (debug) {
			System.out.println("debug: ready for sending");
		}
		Transport.send(message);
		System.out.println(toEmail);

		if (debug) {
			System.out.println("debug: saving on Sent");
		}
		try {
			copyIntoSent(session, message);
		} catch (Exception e) {
			System.out.println("Houston: we have a problem!" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * based on user1260928 of stackoverflow on
	 * https://stackoverflow.com/questions/29669575/javamail-copy-message-to-sent-folder
	 * 
	 * 
	 * 
	 * @param session
	 * @param msg
	 * @throws MessagingException
	 */
	private static void copyIntoSent(Session session, Message msg) throws MessagingException {

		Store store = session.getStore("imap");
		store.connect(Mailer.HOST, Credentials.username, Credentials.password);

		Folder folder = store.getFolder("Inbox.Gesendet.Screenshots");
		if (!folder.exists()) {
			folder.create(Folder.HOLDS_MESSAGES);
		}
		folder.open(Folder.READ_WRITE);
		folder.appendMessages(new Message[] { msg });

		folder.close(false);
	}
}