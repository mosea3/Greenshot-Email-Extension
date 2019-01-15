package application.controls;

import java.io.FileNotFoundException;

import javax.mail.MessagingException;

import application.Args;
import application.Credentials;
import application.Mailer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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

	@FXML
	private ListView<String> filesList;

	String parameters = System.getProperty("sun.java.command").toString().substring("Application.Main".length());

	Args args;

	/**
	 * sets up controlling
	 */
	public void Controller() {

		String[] param = parameters.trim().split("\\s+");

		System.out.println(param);

		this.mailer = new Mailer();
		this.args = new Args(param);
	}

	/**
	 * initializes controlling of main share view
	 */
	@FXML
	private void initialize() {
		String[] param = parameters.trim().split("\\s+");
		this.args = new Args(param);
		System.out.println(this.args.integrate());

		ObservableList<String> items = FXCollections.observableArrayList(args.get("file"));
		filesList.setItems(items);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				emailInput.requestFocus();
			}
		});
	}

	private boolean sendMail() throws FileNotFoundException {
		try {
			Mailer.send(Credentials.username, Credentials.password, Credentials.username, emailInput.getText(), "Test",
					"Guten Tag, " + Credentials.username + " teilt diese Bildschirmausgabe mit Ihnen", args.get("file"),
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
		System.exit(0);
	}
}
