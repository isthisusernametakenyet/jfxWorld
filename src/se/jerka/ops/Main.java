package se.jerka.ops;

import javafx.application.Application;
import javafx.stage.Stage;
import se.jerka.ops.ui.Gui;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		new Gui().createAndShow(stage);
	}
	
	public static void main(String[] arse) {
		launch(arse);
	}
	
}