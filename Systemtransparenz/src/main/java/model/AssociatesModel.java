package model;

import java.util.LinkedList;

import control.MainControl;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import view.ApplicationView;

public class AssociatesModel {
	
	private Text applicationName;
    private ComboBox<String> relationType;
    
    public AssociatesModel(Text applicationName/*, ComboBox<String> relationType*/) {
        this.applicationName = applicationName;
        //this.relationType = relationType;
    }
    
    public ApplicationInRelation getApplicationInRelation() throws Exception {
        ApplicationView aV = this.getApplicationView();
        if (aV == null) {
            throw new Exception("Achtung! Es sind keine Anwendungen vorhanden.");
        }
        ApplicationInRelation applicationInRelation = new ApplicationInRelation(aV);
        //applicationInRelation.setTypeOfRelation(((String)this.relationType.getSelectionModel().getSelectedItem()).equals("Verknüpft mit"));
        return applicationInRelation;
    }
    
    private ApplicationView getApplicationView() {
        ObservableList<ApplicationModel> applications = MainControl.getMainControl().getSelectedApplications();
        ApplicationModel applicationModel = null;
        for (ApplicationModel aM : applications) {
            if (aM.getApplicationName().equals(this.applicationName.getText())) {
                applicationModel = aM;
            }
        }
        LinkedList<ApplicationView> applicationViews = MainControl.getMainControl().getModelView().getApplications();
        for (ApplicationView aV : applicationViews) {
            if (aV.getApplicationModel().equals(applicationModel)) {
                return aV;
            }
        }
        return null;
    }
}
