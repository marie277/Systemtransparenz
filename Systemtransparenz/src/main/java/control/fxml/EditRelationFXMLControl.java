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

public class EditRelationFXMLControl implements Initializable {
	
	private RelationView relationView;
    @FXML
    private ComboBox<String> relation_type;
    @FXML
    private Button submit;
    
    @FXML
    void editRelationType(ActionEvent event) {;
    	MainControl.getMainControl().getModelView().getModelControl().editRelationType(this.relationView, this.relation_type.getSelectionModel().getSelectedItem());
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        this.relationView = (RelationView)MainControl.getMainControl().getSelectedElementView();
        MainControl.getMainControl().setRelation(this.relationView);
        this.relation_type.getItems().addAll("Verknüpft mit");
        this.relation_type.getSelectionModel().select(this.relationView.getInterfaceText().getText());
	}

}
