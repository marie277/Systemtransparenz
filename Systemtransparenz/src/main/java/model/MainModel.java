package model;

import control.fxml.ModelFXMLControl;
import javafx.collections.ObservableList;
import view.ApplicationView;
import view.RelationView;

//Klasse f�r die zentrale Steuerung
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
	
	//Getter-Methode f�r die zentrale Steuerung
	public static MainModel getMainModel() {
		return MainModel.mainModel;
	}
		
	//Getter-Methode f�r die Modell-Ansichts-Steuerung
	public static MainModel getMainModel(ModelFXMLControl modelFXMLControl) {
		if(MainModel.mainModel == null) {
			MainModel.mainModel = new MainModel(modelFXMLControl);
		}
		return MainModel.mainModel;
	}
	
	//Getter-Methode f�r die ausgew�hlten Anwendungen
	public ObservableList<ApplicationModel> getSelectedApplications(){
		return this.applications;
	}
	
	//Setter-Methode f�r die ausgew�hlten Anwendungen
	public void setSelectedApplications(ObservableList<ApplicationModel> applications) {
		this.applications = applications;
	}
	
	//Getter-Methode f�r eine Beziehungs-Ansicht
	public RelationView getRelationView() {
		return this.relationView;
	}
	
	//Setter-Methode f�r eine Beziehungsansicht
	public void setRelationView(RelationView relationView) {
		this.relationView = relationView;
	}
	
	//Getter-Methode f�r eine Anwendungsansicht
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	//Setter-Methode f�r eine Anwendungsansicht
	public void setApplicationView(ApplicationView applicationView) {
		this.applicationView = applicationView;
	}
	
}
