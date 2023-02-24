package model;

import control.fxml.ModelFXMLControl;
import javafx.collections.ObservableList;
import view.ApplicationView;
import view.RelationView;

//Klasse für die zentrale Steuerung
public class MainModel {
	
	private static MainModel mainModel;
	public static ModelFXMLControl modelFXMLControl;
	private RelationView relationView;
	private ApplicationView applicationView;
	private ObservableList<ApplicationModel> applications;
	
	//Konstruktor
	public MainModel(ModelFXMLControl modelFXMLControl) {
		MainModel.modelFXMLControl = modelFXMLControl;
	}
	
	//Getter-Methode für die zentrale Steuerung
	public static MainModel getMainModel() {
		return MainModel.mainModel;
	}
		
	//Getter-Methode für die Modell-Ansichts-Steuerung
	public static MainModel getMainModel(ModelFXMLControl modelFXMLControl) {
		if(MainModel.mainModel == null) {
			MainModel.mainModel = new MainModel(modelFXMLControl);
		}
		return MainModel.mainModel;
	}
	
	//Getter-Methode für die ausgewählten Anwendungen
	public ObservableList<ApplicationModel> getSelectedApplications(){
		return this.applications;
	}
	
	//Setter-Methode für die ausgewählten Anwendungen
	public void setSelectedApplications(ObservableList<ApplicationModel> applications) {
		this.applications = applications;
	}
	
	//Getter-Methode für eine Beziehungs-Ansicht
	public RelationView getRelationView() {
		return this.relationView;
	}
	
	//Setter-Methode für eine Beziehungsansicht
	public void setRelationView(RelationView relationView) {
		this.relationView = relationView;
	}
	
	//Getter-Methode für eine Anwendungsansicht
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	//Setter-Methode für eine Anwendungsansicht
	public void setApplicationView(ApplicationView applicationView) {
		this.applicationView = applicationView;
	}
	
}
