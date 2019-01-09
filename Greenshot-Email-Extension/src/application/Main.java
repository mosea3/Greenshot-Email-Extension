package application;

import application.view.ConsoleShareView;

//public class Main extends Application {
public class Main {
	Args param;

	/**
	 * @Override public void start(Stage primaryStage) { try {
	 * 
	 *           } catch (Exception e) { e.printStackTrace(); } }
	 * 
	 * 
	 **/
	public static void main(String[] args) {
		Args param = new Args(args);

		new ConsoleShareView(param);

		// launch("test");
	}
}
