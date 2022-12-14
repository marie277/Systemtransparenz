package application;
	
import control.fxml.ModelFXMLControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

//Main-Klasse
public class Main extends Application {
	
	//Methode zum Starten der Anwendung
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
			loader.setController(new ModelFXMLControl());
			primaryStage.setScene(new Scene(loader.load()));
			primaryStage.setMaximized(true);
			primaryStage.setTitle("Optimierung Systemtransparenz");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Main-Methode
	public static void main(String[] args) {
		launch(args);
	}
}
