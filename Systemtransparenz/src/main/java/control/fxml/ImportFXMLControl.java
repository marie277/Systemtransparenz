package control.fxml;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.ApplicationModel;

public class ImportFXMLControl implements Initializable{
	
	DatabaseFXMLControl databaseFXMLControl = DatabaseFXMLControl.getDatabaseFXMLControl();
	
    @FXML
    private ListView<String> applicationsList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*ObservableList<ApplicationModel> applications = DatabaseFXMLControl.getDatabaseFXMLControl().getApplications();
		for(ApplicationModel aM : applications) {
			System.out.print(aM);
			applicationsList.getItems().add(aM.getApplicationName());
			
		}*/
	}

}
