package control.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import control.MainControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import model.ApplicationModel;

public class ApplicationFXMLControl implements Initializable {
	
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private TextField id;
	@FXML
	private TextField name;
	@FXML
	private TextField description;
	@FXML
	private TextField category;
	@FXML
	private TextField producer;
	@FXML
	private TextField manager;
	@FXML
	private TextField department;
	@FXML
	private TextField admin;
	@FXML
	private Button cancel;
	@FXML
	private Button submit;
	
	//Konstruktor
	public ApplicationFXMLControl() {
		
	}
	
	//Methode zum Schließen des Import-Fensters
	@FXML
    private void cancel(ActionEvent event) {
        this.scrollPane.getScene().getWindow().hide();
    }
    
	//Methode zum Bestätigen der geladenen Anwendungen, welche dem geöffneten Modell hinzugefügt werden
	@FXML
    private void submit(ActionEvent event) {
		try {
			int applicationId = Integer.parseInt(id.getText());
			String applicationName = name.getText();
			String applicationDescription = description.getText();
			String applicationCategory = category.getText();
			String applicationProducer = producer.getText();
			String applicationManager = manager.getText();
			String applicationDepartment = department.getText();
			String applicationAdmin = admin.getText();
			ApplicationModel applicationModel = new ApplicationModel(applicationId, applicationName, applicationDescription, applicationCategory, applicationProducer, applicationManager, applicationDepartment, applicationAdmin);
	        //MainControl.getMainControl().addApplication(applicationModel.getApplicationName());
			MainControl.getMainControl().addApplication(applicationModel.getApplicationId(), applicationModel.getApplicationName(), applicationModel.getApplicationDescription(), applicationModel.getApplicationCategory(), applicationModel.getApplicationProducer(), applicationModel.getApplicationManager(), applicationModel.getApplicationDepartment(), applicationModel.getApplicationAdmin());
	        
		}
		catch (Exception e){
			e.printStackTrace();
			if (!e.getClass().equals(NullPointerException.class)) {
	            Alert alertError = new Alert(Alert.AlertType.ERROR);
	            alertError.setTitle("Fehler!");
	            alertError.setHeaderText("Die gewünschte Anwendung konnte nicht angelegt werden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
    }
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
