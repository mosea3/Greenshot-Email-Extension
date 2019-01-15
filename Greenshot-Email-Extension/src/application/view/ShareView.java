package application.view;

import java.io.IOException;

import application.Args;
import application.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * implements a share view
 * 
 * @author andy.moser
 *
 */
public class ShareView implements View {

	private Stage stage;
	private Args param;

	public ShareView(Stage stage, Args param) throws IOException {
		this.stage = stage;
		this.param = param;
		show();
	}

	@Override
	public void show() throws IOException {

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("screen.fxml"));

		Scene scene = new Scene(root, 400, 400);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("screen.css").toExternalForm());

		stage.setScene(scene);
		stage.setTitle("Teilen via Email");
		stage.show();

	}

}
