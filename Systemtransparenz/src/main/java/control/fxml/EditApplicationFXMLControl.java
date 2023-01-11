package control.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import view.ApplicationView;

//Klasse zur Steuerung der Bearbeitung einer ausgewählten Anwendung, implementiert Interface Initializable
public class EditApplicationFXMLControl implements Initializable {
	
	private ApplicationView applicationView;
    @FXML
    private TextField applicationName;
    @FXML
    private Button submit;
    
    //Methode zur Bearbeitung einer ausgewählten Anwendung
    @FXML
    private void editApplication(ActionEvent event) {
    	String applicationNameView = this.applicationView.getApplicationModel().getApplicationName();
        try {
            if (!this.applicationName.getText().equals(applicationNameView)) {
                this.applicationView.getModelView().getModelControl().renameApplication(this.applicationView, this.applicationName.getText());
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        	if (!e.getClass().equals(NullPointerException.class)) {
                Alert alertEerror = new Alert(Alert.AlertType.ERROR);
                alertEerror.setTitle("Fehler!");
                alertEerror.setHeaderText(e.getMessage());
                alertEerror.show();
            }
        }
    }
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        this.applicationView = (ApplicationView) MainControl.getMainControl().getSelectedElementView();
        String applicationNameView = this.applicationView.getApplicationModel().getApplicationName();
        this.applicationName.setText(applicationNameView);
	}
}
