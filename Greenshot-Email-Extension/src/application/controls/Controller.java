package application.controls;

import java.io.FileNotFoundException;

import javax.mail.MessagingException;

import application.Credentials;
import application.Mailer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller for Greenshot Emailextension
 * 
 * @author andy.moser
 *
 *         todo: implement initial actions: list attached image todo: list
 *         attached image*s* todo: implement controls for settings
 */
public class Controller {
	private Mailer mailer;
	@FXML
	private Button SendButton;

	@FXML
	private TextField emailInput;

	public void Controller() {

		this.mailer = new Mailer();
	}

	@FXML
	private void initialize() {
	}

	private boolean sendMail() throws FileNotFoundException {
		try {
			Mailer.send(Credentials.username, Credentials.password, Credentials.username, emailInput.getText(), "Test",
					"Guten Tag, " + Credentials.username + " teilt diese Bildschirmausgabe mit Ihnen", "H:\test.png",
					false);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	@FXML
	private void SendButtonPressed() {
		System.out.println("sendbutton pressed");
		try {
			sendMail();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Greenshot:Teilen via E-Mail");
		alert.setHeaderText("Erfolgreich!");
		alert.setContentText("Woohoo! Du hast erfolgreich ein Screenshot mit " + emailInput.getText() + " geteilt.");

		alert.showAndWait();
	}
}
