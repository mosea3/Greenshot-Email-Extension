package application.controls;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
import javafx.scene.control.ChoiceBox;
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

	@FXML
	private ChoiceBox templateSelector;

	String parameters = System.getProperty("sun.java.command").toString().substring("Application.Main".length());

	Args args;

	/**
	 * sets up controlling
	 */
	public void Controller() {

		/**
		 * todo: check repeatedly threaded, if theres a new image in the clipboard, if
		 * yes, add it into attachments
		 * 
		 * todo: save images as pdf and send them
		 */

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

		templateSelector.setValue("Auftrag fertig");
		templateSelector.getItems().add("Auftrag fertig");
		templateSelector.getItems().add("letzte Rechnung");
		templateSelector.getItems().add("Rechnungskopie");
		templateSelector.getItems().add("Lieferscheinkopie");
		templateSelector.getItems().add("Prüfprotokollkopie");
		templateSelector.getItems().add("Dokument");

		try {
			scanTemplates();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("no Templates found");
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				emailInput.requestFocus();
			}
		});

		emailInput.requestFocus();
		/**
		 * todo: evaluate if Clipboard on init contains email adress: if yes, paste it
		 * into email field
		 */
	}

	private void scanTemplates() throws FileNotFoundException {
		Collection<File> all = new ArrayList<File>();
		File file = new File("greenshot-email-templates");
		File[] children = file.listFiles();

		if (children != null) {
			for (File child : children) {
				all.add(child);
				templateSelector.getItems().add(child.getName().toString());
				System.out.println(child.getName().toString());
			}
		}

	}

	private boolean sendMail() throws FileNotFoundException {
		String subject;
		String text;
		ArrayList<String> headers = new ArrayList<String>();

		switch (templateSelector.getValue().toString()) {

		case "Auftrag fertig":
			subject = "Ihr Auftrag ist fertig!";
			text = "Guten Tag," + "Ihr Auftrag wurde fertig gestellt." + "Der dazugehörige Abholschein wurde angehängt"
					+ "Bei Änderungen der Lieferungsmodalität (Abholzeit/Post/Spedition), bitten wir um kurze Rückmeldung."
					+ "vielen Dank!" + "ihre Härterei Blessing AG";
			headers.addAll(Arrays.asList("Accept-Language: de-CH", "Accept-Language: fr-CH", "Accept-Language: es-ES",
					"Reply-To: info@blessing.ch", "X-Priority: 1 (Highest)", "X-MSMail-Priority: High",
					"Importance: High"));
			break;

		case "letzte Rechnung":
			// todo: add S/MIME Signature option
			subject = "letzte Rechnung";
			text = "Guten Tag," + "Bitte beachten Sie Ihre neueste Rechung im Anhang." + "mit freundlichen Grüssen"
					+ "Härterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;

		case "Rechnungskopie":
			// todo: add S/MIME Signature option
			subject = "Rechnungskope";
			text = "Guten Tag," + "Wie besprochen, erhalten Sie die gewünschte Rechnungskopie im Anhang."
					+ "mit freundlichen Grüssen" + "Härterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;

		case "Lieferscheinkopie":
			// todo: add S/MIME Signature option
			subject = "Lieferscheinkopie";
			text = "Guten Tag," + "Wie besprochen, erhalten Sie die gewünschte Lieferscheinkopie im Anhang."
					+ "mit freundlichen Grüssen" + "Härterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;

		case "Prüfprotokollkopie":
			// todo: add S/MIME Signature option
			subject = "Lieferscheinkopie";
			text = "Guten Tag," + "Wie besprochen, erhalten Sie die gewünschte Prüfprotokollkopie im Anhang."
					+ "mit freundlichen Grüssen" + "Härterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;
		case "Dokument":
			// todo: add S/MIME Signature option
			subject = "Lieferscheinkopie";
			text = "Guten Tag," + "Wie besprochen, erhalten Sie das gewünschte Dokument im Anhang."
					+ "mit freundlichen Grüssen" + "Härterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;
		}
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
