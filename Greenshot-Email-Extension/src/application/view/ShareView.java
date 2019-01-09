package application.view;

import java.io.IOException;

import application.Args;
import application.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShareView implements View {

	private Stage stage;

	public ShareView(Stage stage, Args args) {
		this.stage = stage;
	}

	@Override
	public void show() throws IOException {

		// param.set("path", new
		// File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("screen.fxml"));
		Scene scene = new Scene(root, 400, 400);

		stage.setScene(scene);
		stage.setTitle("Teilen via Email");
		stage.show();

	}

}
