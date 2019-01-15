package application.view;

import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.mail.MessagingException;

import application.Args;
import application.Credentials;
import application.Mailer;
import application.View;

/**
 * implements UI on command line for the fancy ones
 * 
 * @author andy.moser
 *
 */
public class ConsoleShareView implements View {
	Args args;

	public ConsoleShareView(Args args) {
		this.args = args;

		System.out.println(args.integrate().toString());

		show();
	}

	@Override
	public void show() {
		Scanner scanner = new Scanner(System.in);
		if (args.integrate().size() == 0) {
			// config execution

		} else if (args.get("send") == "true") {
			try {
				Boolean debug;
				debug = args.get("debug") == "true";

				System.out.println("Greenshot Email Erweiterung");
				System.out.println("Empfänger:");
				String reciever = scanner.nextLine();

				Mailer.send(Credentials.username, Credentials.password, Credentials.username, reciever, "Test",
						"Guten Tag, " + Credentials.username + " teilt diese Bildschirmausgabe mit Ihnen",
						args.get("file"), debug);

				System.out.println("erfolgreich mit " + reciever + " geteilt.");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.out.println("Anhang wurde nicht gefunden. Es werden keine leeren Anhänge gesendet.");
			}
		}
	}

}
