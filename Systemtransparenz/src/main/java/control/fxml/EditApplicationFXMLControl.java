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

public class EditApplicationFXMLControl implements Initializable {
	
	private ApplicationView applicationView;
    @FXML
    private TextField applicationName;
    @FXML
    private Button submit;
    
    @FXML
    void editApplication(ActionEvent event) {
        try {
            if (!this.applicationName.getText().equals(this.applicationView.getApplicationModel().getApplicationName())) {
                this.renameApplication();
            }
        }
        catch (Exception e) {
            this.showException(e);
        }
    }
    
    private void renameApplication() throws IllegalArgumentException {
        this.applicationView.getModelView().getModelControl().renameApplication(this.applicationView, this.applicationName.getText());
    }
    
    private void showException(Exception e) {
        if (!e.getClass().equals(NullPointerException.class)) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fehler!");
            error.setHeaderText(e.getMessage());
            error.show();
        }
        e.printStackTrace();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        this.applicationView = (ApplicationView) MainControl.getMainControl().getSelectedElementView();
        this.applicationName.setText(this.applicationView.getApplicationModel().getApplicationName());
	}
}
