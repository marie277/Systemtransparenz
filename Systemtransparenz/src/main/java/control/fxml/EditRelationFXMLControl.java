package control.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import view.RelationView;

//Klasse zur Steuerung der Bearbeitung einer ausgewählten Beziehung zwischen zwei Anwendungen, implementiert Interface Initializable
public class EditRelationFXMLControl implements Initializable {
	
	private RelationView relationView;
    @FXML
    private ComboBox<String> relationType;
    @FXML
    private Button submit;
    
    //Methode zur Bearbeitung einer ausgewählten Anwendung
    @FXML
    private void editRelationType(ActionEvent event) {;
    	MainControl.getMainControl().getModelView().getModelControl().editRelationType(this.relationView, this.relationType.getSelectionModel().getSelectedItem());
    }
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        this.relationView = (RelationView)MainControl.getMainControl().getSelectedElementView();
        MainControl.getMainControl().setRelationView(this.relationView);
        this.relationType.getItems().addAll("Verknüpft mit");
        String relationText = this.relationView.getRelationText().getText();
        this.relationType.getSelectionModel().select(relationText);
	}

}
