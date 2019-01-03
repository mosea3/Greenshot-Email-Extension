package application;

import java.net.URL;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import application.Credentials;


public class Controller {
	private Mailer mailer;
	@FXML 
	private Button SendButton;
	
	@FXML
	private TextField emailInput;
	
	public void Controller(){
	
		this.mailer = new Mailer();
	}
	
	@FXML
	private void initialize() 
	{
	}

	private boolean sendMail() {
		try {
			Mailer.send(Credentials.username, Credentials.password, Credentials.username, emailInput.getText(), "Test", "Guten Tag, " + Credentials.username + " teilt diese Bildschirmausgabe mit Ihnen");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
		
	@FXML
	private void SendButtonPressed() 
	{
		System.out.println("sendbutton pressed");
		sendMail();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Greenshot:Teilen via E-Mail");
		alert.setHeaderText("Erfolgreich!");
		alert.setContentText("Woohoo! Du hast erfolgreich ein Screenshot mit " + emailInput.getText() + " geteilt.");

		alert.showAndWait();
	}
}
