package application;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {
	private String[] args;
	@Override
	public void start(Stage primaryStage) {
		try {
			
			String path =  new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
				    .toURI()).getPath();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Environment Test");
			alert.setHeaderText("This Program is spinning at:");
			alert.setContentText(path);

			alert.showAndWait();
			
			
			
			
			
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("screen.fxml"));

			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		launch(args);
	}
}
