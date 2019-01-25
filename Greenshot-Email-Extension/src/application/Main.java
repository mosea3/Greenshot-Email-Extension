package application;

import application.view.ConsoleShareView;
import application.view.ShareView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main Application Class
 * 
 * @author andy.moser
 *
 */
public class Main extends Application {
// public class Main {

	Args param;
	String parameters = System.getProperty("sun.java.command").toString()
			.substring(getClass().getName().toString().length());

	@Override
	public void start(Stage primaryStage) {
		try {

			String[] param = parameters.trim().split("\\s+");

			Args args = new Args(param);

			if (args.get("no-gui") == "true") {
				new ConsoleShareView(args);
			} else {
				System.out.println("start GUI...");
				new ShareView(primaryStage, args);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Greenshot Java Email-Sharing Extension");
		launch("test");
	}

	public static String getVersion() {
		return "v0.9";
	}
}
