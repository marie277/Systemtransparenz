package control.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import model.MainModel;
import view.RelationView;

//Klasse zur Steuerung der Bearbeitung einer ausgewählten Beziehung zwischen zwei Anwendungen, implementiert Interface Initializable
public class EditRelationFXMLControl implements Initializable {
	
	private RelationView relationView;
    @FXML
    private ComboBox<String> relationType;
    @FXML
    private ComboBox<String> relationDirection;
    @FXML
    private Button submit;
    
    //Methode zur Bearbeitung einer ausgewählten Anwendung
    @FXML
    private void editRelation(ActionEvent event) {;
	    if((!relationType.getSelectionModel().getSelectedItem().equals("Verknüpft mit") && relationDirection.getSelectionModel().getSelectedItem().equals("keine")) || (relationType.getSelectionModel().getSelectedItem().equals("Verknüpft mit") && !relationDirection.getSelectionModel().getSelectedItem().equals("keine"))) {
			Alert alertError = new Alert(Alert.AlertType.ERROR);
	        alertError.setTitle("Fehler!");
	        alertError.setHeaderText("Der Beziehungstyp Verknüpft mit erfordert keine eingehende Anwendung, die beiden übrigen schon.");
	        alertError.show();
		}
	    else {
	    	MainModel.modelFXMLControl.getModelView().getModelControl().editRelation(this.relationView, this.relationType.getSelectionModel().getSelectedItem(), this.relationDirection.getSelectionModel().getSelectedItem());
	    }
    }
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.relationView = (RelationView)MainModel.modelFXMLControl.getModelView().getElementView();
        MainModel.getMainModel().setRelationView(this.relationView);
        this.relationType.getItems().addAll("Verknüpft mit", "Hat", "Nutzt");
        String relationText = this.relationView.getRelationText().getText();
        this.relationType.getSelectionModel().select(relationText);
        this.relationDirection.getItems().addAll("keine", this.relationView.getRelationModel()
        		.getApplications().get(0).getApplicationView().getApplicationModel().
        		getApplicationName(), this.relationView.getRelationModel().getApplications().get(1)
        		.getApplicationView().getApplicationModel().getApplicationName());
        String relationIncoming = "";
        if(this.relationType.getSelectionModel().getSelectedItem().equals("Hat") || 
        		this.relationType.getSelectionModel().getSelectedItem().equals("Nutzt")) {
	        if(this.relationView.getRelationModel().getRelationDirection() == true) {
	        	relationIncoming = this.relationView.getRelationModel().getApplications().get(0)
	        			.getApplicationView().getApplicationModel().getApplicationName();
	        }
	        else {
	        	relationIncoming = this.relationView.getRelationModel().getApplications().get(1)
	        			.getApplicationView().getApplicationModel().getApplicationName();
	        }
        }
        else {
        	relationIncoming = "keine";
        }
        this.relationDirection.getSelectionModel().select(relationIncoming);
	}

}
