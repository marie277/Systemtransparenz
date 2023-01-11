package model;

import java.util.LinkedList;

import control.MainControl;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import view.ApplicationView;

//Klasse zur Speicherung der Daten der beiden Anwendungen, welche zu einer Beziehung gehören
public class AssociatesModel {
	
	private Text applicationName;
    
	//Konstruktor
    public AssociatesModel(Text applicationName) {
        this.applicationName = applicationName;
    }
    
    //Getter-Methode für die an einer Beziehung beteiligten Anwendungen
    public ApplicationInRelation getApplicationInRelation() throws Exception {
        ApplicationView applicationView = null;
        ApplicationModel applicationModel = null;
        ObservableList<ApplicationModel> applications = MainControl.getMainControl().getSelectedApplications();
        for (ApplicationModel applicationModelAss : applications) {
            if (applicationModelAss.getApplicationName().equals(this.applicationName.getText())) {
                applicationModel = applicationModelAss;
                
            }
        }
        LinkedList<ApplicationView> applicationViews = MainControl.getMainControl().getModelView().getApplications();
        for (ApplicationView applicationViewAss : applicationViews) {
            if (applicationViewAss.getApplicationModel().equals(applicationModel)) {
                applicationView = applicationViewAss;
            }
        }
        ApplicationInRelation applicationInRelation;
        if (applicationView != null) {
        	applicationInRelation = new ApplicationInRelation(applicationView);
        }
        else {
        	throw new Exception("Achtung! Es sind keine Anwendungen vorhanden.");
        }
        return applicationInRelation;
    }

}
