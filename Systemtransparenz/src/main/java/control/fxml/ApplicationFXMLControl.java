package control.fxml;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.postgresql.Driver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import model.ApplicationModel;
import model.MainModel;

public class ApplicationFXMLControl implements Initializable {
	
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String dataScheme;
	private LinkedList<String> categories;
	private LinkedList<String> producers;
	private LinkedList<String> managers;
	private LinkedList<String> departments;
	private LinkedList<String> admins;
	
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private TextField id;
	@FXML
	private TextField name;
	@FXML
	private TextField description;
	@FXML
	private ComboBox<String> category;
	@FXML
	private ComboBox<String> producer;
	@FXML
	private ComboBox<String> manager;
	@FXML
	private ComboBox<String> department;
	@FXML
	private ComboBox<String> admin;
	@FXML
	private Button cancel;
	@FXML
	private Button submit;
	
	//Methode zum Schließen des Import-Fensters
	@FXML
    private void cancel(ActionEvent event) {
        this.scrollPane.getScene().getWindow().hide();
    }
    
	//Methode zum Bestätigen der geladenen Anwendungen, welche dem geöffneten Modell hinzugefügt werden
	@FXML
    private void submit(ActionEvent event) {
		try {
			int applicationId = Integer.parseInt(this.id.getText());
			String applicationName = this.name.getText();
			String applicationDescription = this.description.getText();
			String applicationCategory = this.category.getValue();
			String applicationProducer = this.producer.getValue();
			String applicationManager = this.manager.getValue();
			String applicationDepartment = this.department.getValue();
			String applicationAdmin = this.admin.getValue();
			ApplicationModel applicationModel = new ApplicationModel(applicationId, 
					applicationName, applicationDescription, applicationCategory, 
					applicationProducer, applicationManager, applicationDepartment, 
					applicationAdmin);
			MainModel.modelFXMLControl.getModelView().getModelControl()
			.addApplication(applicationModel.getApplicationId(), 
					applicationModel.getApplicationName(), applicationModel
					.getApplicationDescription(), applicationModel.getApplicationCategory(),
					applicationModel.getApplicationProducer(), applicationModel
					.getApplicationManager(), applicationModel.getApplicationDepartment(),
					applicationModel.getApplicationAdmin());
		}
		catch(Exception e){
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
	            Alert alertError = new Alert(Alert.AlertType.ERROR);
	            alertError.setTitle("Fehler!");
	            alertError.setHeaderText("Die gewünschte Anwendung konnte nicht angelegt werden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
    }
	
	//Methode zur Herstellung einer Datenbank-Verbindung
	public void initializeDatabase() {
    	this.hostUrl = "localhost";
    	this.portNumber = 5432;
    	this.userName = "postgres";
    	this.passWord = "pw369";
    	this.dataBase = "systemtransparenz";
    	this.dataScheme = "public";
        try {
        	Driver driver = new org.postgresql.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostUrl + ":" + this.portNumber + "/" + this.dataBase + "?search_path=" + this.dataScheme, this.userName, this.passWord);
        } catch(Exception e){
        	e.printStackTrace();
        	if(!e.getClass().equals(IllegalArgumentException.class)) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Fehler!");
                alertError.setHeaderText("Es konnte keine Datenbank-Verbindung hergestellt werden.");
                alertError.show();
            }  
        }
	}
    
    //Methode zum Import der Anwendungen aus der ausgewählten Tabelle
    public void importValues() {
		try {
            PreparedStatement categoryPreparedStatement = connection.prepareStatement("SELECT k.kategoriename FROM kategorie k;");
            ResultSet categoryResultSet = categoryPreparedStatement.executeQuery();
            categories = new LinkedList<String>();
            while(categoryResultSet.next()){
            	categories.add(categoryResultSet.getString(1));
            }
            PreparedStatement producerPreparedStatement = connection.prepareStatement("SELECT h.herstellername FROM hersteller h;");
            ResultSet producerResultSet = producerPreparedStatement.executeQuery();
            producers = new LinkedList<String>();
            while(producerResultSet.next()){
            	producers.add(producerResultSet.getString(1));
            }
            PreparedStatement managerPreparedStatement = connection.prepareStatement("SELECT mb.mitarbeitername FROM (anwendungsmanager am INNER JOIN mitarbeiter mb ON am.mitarbeiterid = mb.mitarbeiterid);");
            ResultSet managerResultSet = managerPreparedStatement.executeQuery();
            managers = new LinkedList<String>();
            while(managerResultSet.next()){
            	managers.add(managerResultSet.getString(1));
            }
            PreparedStatement departmentPreparedStatement = connection.prepareStatement("SELECT f.fachbereichname FROM fachbereich f;");
            ResultSet departmentResultSet = departmentPreparedStatement.executeQuery();
            departments = new LinkedList<String>();
            while(departmentResultSet.next()){
            	departments.add(departmentResultSet.getString(1));
            }
            PreparedStatement adminPreparedStatement = connection.prepareStatement("SELECT mb.mitarbeitername FROM (administrator ad INNER JOIN mitarbeiter mb ON ad.mitarbeiterid = mb.mitarbeiterid);");
            ResultSet adminResultSet = adminPreparedStatement.executeQuery();
            admins = new LinkedList<String>();
            while(adminResultSet.next()){
            	admins.add(adminResultSet.getString(1));
            }
        } catch(Exception e) {
        	e.printStackTrace();
			if(!e.getClass().equals(SQLException.class)) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Fehler!");
                alertError.setHeaderText("Es konnten keine Werte geladen werden.");
                alertError.show();
            }
		}
	}
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initializeDatabase();
		this.importValues();
		for(String category : this.categories) {
			this.category.getItems().add(category);
		}
		this.category.getSelectionModel().select(0);
		for(String producer : this.producers) {
			this.producer.getItems().add(producer);
		}
		this.producer.getSelectionModel().select(0);
		for(String manager : this.managers) {
			this.manager.getItems().add(manager);
		}
		this.manager.getSelectionModel().select(0);
		for(String department : this.departments) {
			this.department.getItems().add(department);
		}
		this.department.getSelectionModel().select(0);
		for(String admin : this.admins) {
			this.admin.getItems().add(admin);
		}
		this.admin.getSelectionModel().select(0);
	}

}
