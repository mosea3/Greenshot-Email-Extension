package application;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {
	private String path;
	private String[] args;

	/**
	 * JCommander style commandline parameters.
	 */
	public static class Args {
		@Parameter(names = "-s", description = "source TPS file or folder containing TPS files.", converter = FileConverter.class, required = false)
		private File sourceFile;
		@Parameter(names = "-i", description = "displays TPS file information.")
		private boolean info;
		@Parameter(names = "-idx", description = "displays the record ids for the available indexes.")
		private boolean index;
		@Parameter(names = "-layout", description = "displays the file layout.")
		private String text;
		@Parameter(names = "-t", description = "transmits text.")
		private boolean stackTraces;
		@Parameter(names = "-sT", description = "target CSV file or folder to create CSV files in.", converter = FileConverter.class, required = false)
		private File targetFile;

	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Args params = new Args();
			JCommander cmd = new JCommander(params);

			this.path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Environment Test");
			alert.setHeaderText("This Program is spinning at:");
			alert.setContentText(path);

			alert.showAndWait();

			try {

				System.out.println(params.info);
				System.out.println(params.text);

			} catch (Exception e) {
				System.out.println("no command arguments passed");
			}

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
