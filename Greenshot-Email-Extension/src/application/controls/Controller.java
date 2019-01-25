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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Controller for Greenshot Emailextension
 * 
 * @author andy.moser
 *
 *         todo: implement initial actions: list attached image todo: list
 *         attached image*s* todo: implement controls for settings todo: make
 *         list deletable implement preview for listed images put initial
 *         clipboard content into clipboard after execution
 * 
 */
public class Controller {
	private Mailer mailer;
	@FXML
	private Button SendButton, addImageButton;

	@FXML
	private TextField emailInput;

	@FXML
	private ListView<String> filesList;

	@FXML
	private ChoiceBox templateSelector;

	@FXML
	private Hyperlink infolink;

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
		// get all parameters

		String[] param = parameters.trim().split("\\s+");
		this.args = new Args(param);

		infolink.setText("v0.9");

		ObservableList<String> items = FXCollections.observableArrayList(args.get("file"));
		filesList.setItems(items);

		emailInput.setPromptText("Emailadresse eingeben");

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

		emailInput.requestFocus();/**
									 * todo: evaluate if Clipboard on init contains email adress: if yes, paste it
									 * into email field
									 */

		emailInput.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					SendButtonPressed();
				}
			}

		});

		addImageButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.getImageFromClipboard());
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

		text = "Guten Tag, " + Credentials.username + " teilt diese Bildschirmausgabe mit Ihnen";
		subject = "info@blessing.ch hat ein Dokument mit Ihnen geteilt.";

		switch (templateSelector.getValue().toString()) {

		case "Auftrag fertig":
			subject = "Ihr Auftrag ist fertig!";
			text = "Guten Tag,\n" + "Ihr Auftrag wurde fertig gestellt.\n"
					+ "Der dazugehöriger Abholschein wurde angehängt in jpg-Format.\n"
					+ "Bitte nehmen Sie oder Ihre Vertretung ebendiesen Abholschein mit.\n\n"
					+ "Bei Änderungen der Lieferungsmodalität (Abholzeit/Post/Spedition),\n"
					+ "bitten wir um kurze Rückmeldung auf info@blessing.ch oder \n"
					+ "oder telefonisch an +41 34 426 10 80. \n \n" + "vielen Dank!\n"
					+ "\n \n ihre Härterei Blessing AG";
			headers.addAll(Arrays.asList("Accept-Language: de-CH", "Accept-Language: fr-CH", "Accept-Language: es-ES",
					"Reply-To: info@blessing.ch", "X-Priority: 1 (Highest)", "X-MSMail-Priority: High",
					"Importance: High"));
			break;

		case "letzte Rechnung":
			// todo: add S/MIME Signature option
			subject = "letzte Rechnung";
			text = "Guten Tag,\n" + "Bitte beachten Sie Ihre neueste Rechung im Anhang im jpg-Format.\n"
					+ "mit freundlichen Grüssen\n\n" + "Härterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;

		case "Rechnungskopie":
			// todo: add S/MIME Signature option
			subject = "Rechnungskopie";
			text = "Guten Tag,\n"
					+ "Wie besprochen, erhalten Sie die gewünschte Rechnungskopie im Anhang im jpg-Format.\n"
					+ "mit freundlichen Grüssen\n" + "\nHärterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;

		case "Lieferscheinkopie":
			// todo: add S/MIME Signature option02

			subject = "Lieferscheinkopie";
			text = "Guten Tag,\n"
					+ "Wie besprochen, erhalten Sie die gewünschte Lieferscheinkopie im Anhang im jpg-Format.\n"
					+ "mit freundlichen Grüssen\n" + "\nHärterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;

		case "Prüfprotokollkopie":
			// todo: add S/MIME Signature option
			subject = "Prüfprotokollkopie";
			text = "Guten Tag,\n"
					+ "Wie besprochen, erhalten Sie die gewünschte Prüfprotokollkopie im Anhang im jpg-Format.\n"
					+ "mit freundlichen Grüssen\n" + "\nHärterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;
		case "Dokument":
			// todo: add S/MIME Signature option
			subject = "info@blessing.ch hat ein Dokument mit Ihnen geteilt";
			text = "Guten Tag,\n" + "\nWie besprochen, erhalten Sie das gewünschte Dokument im Anhang im jpg-Format.\n"
					+ "\nmit freundlichen Grüssen" + "\nHärterei Blessing AG";
			headers.add("Reply-To: info@blessing.ch");
			break;
		}
		try {
			Mailer.send(Credentials.username, Credentials.password, Credentials.username, emailInput.getText(), subject,
					text, filesList.getItems(), false);
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

	/**
	 * Get an image off the system clipboard.
	 * 
	 * @return Returns an Image if successful; otherwise returns null.
	 */
	public Image getImageFromClipboard() {
		@SuppressWarnings("restriction")

		String imagepath;

		Clipboard clipboard = Clipboard.getSystemClipboard();

		String empty = new String();

		String path = System.getProperty("user.home") + "\\AppData\\Local\\Temp\\test.jpg";

		try {
			imagepath = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
			if (imagepath.contains(".jpg")) {
				System.out.println(imagepath);
				System.out.println("Das war tatsächlich in Bild!");
				System.out.println(System.getenv("APPDATA"));
				System.out.println(System.getProperty("user.home") + "\\AppData\\Local\\Temp\\");

				File input = new File(imagepath);
				File outputFile = new File(path);

				filesList.getItems().add(imagepath);
				System.out.println("Bild " + outputFile);
			} else {
				System.out.println("not comply to img resource:" + imagepath);
			}

		} catch (

		NullPointerException e) {
			System.out.println("Das war kein Bild");
		}

		return null;
	}
}
