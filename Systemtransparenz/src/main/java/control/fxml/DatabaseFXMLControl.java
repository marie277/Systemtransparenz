package control.fxml;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ApplicationModel;

//Klasse zur Steuerung des Imports von Anwendungen aus einer Datenbank, implementiert Interface Initializable
public class DatabaseFXMLControl implements Initializable {
	
	private static DatabaseFXMLControl databaseFXMLControl;
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String tableName;
	private LinkedList<ApplicationModel> applications;
	private ObservableList<ApplicationModel> applicationsList;
	
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private TextField host;
	@FXML
	private TextField port;
	@FXML
	private TextField database;
	@FXML
	private TextField table;
	@FXML
    private TableColumn<ApplicationModel, String> applicationsColumn;
	@FXML
	private Button cancel;
	@FXML
	private Button load;
	@FXML
	private Button submit;
	
	//Konstruktor
	public DatabaseFXMLControl() {
		
	}
	
	//Statische Getter-Methode f�r die Steuerung des Datenbank-Imports
	public static DatabaseFXMLControl getDatabaseFXMLControl(){
        if (databaseFXMLControl == null) {
        	databaseFXMLControl = new DatabaseFXMLControl();
        }
        return databaseFXMLControl;
    }
	
	//Methode zum Schlie�en des Import-Fensters
	@FXML
    private void cancel(ActionEvent event) {
        this.scrollPane.getScene().getWindow().hide();
    }
	
	//Methode zum Laden der Anwendungen aus der ausgew�hlten Datenbank
	@FXML
	private void load(ActionEvent event) {
		this.initializePostgresqlDatabase();
		this.applicationsColumn.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
        applicationsList = FXCollections.observableArrayList();
        for (ApplicationModel aM : this.importApplications()) {
            applicationsList.add(aM);
        }
        this.applicationsColumn.getTableView().setItems((ObservableList<ApplicationModel>)applicationsList);
	}
    
	//Methode zum Best�tigen der geladenen Anwendungen, welche dem ge�ffneten Modell hinzugef�gt werden
	@FXML
    private void submit(ActionEvent event) {
		try {
	        if (this.importApplications().size() != 0) {
	        	for(ApplicationModel aM : this.applicationsList) {
	        		//MainControl.getMainControl().addApplication(aM.getApplicationName());
	        		MainControl.getMainControl().addApplication(aM.getApplicationId(), aM.getApplicationName(), aM.getApplicationDescription(), aM.getApplicationCategory(), aM.getApplicationProducer(), aM.getApplicationManager(), aM.getApplicationDepartment(), aM.getApplicationAdmin());
	        	}
	        }
		}
		catch (Exception e){
			e.printStackTrace();
			if (!e.getClass().equals(NullPointerException.class)) {
	            Alert alertError = new Alert(Alert.AlertType.ERROR);
	            alertError.setTitle("Fehler!");
	            alertError.setHeaderText("Es sind keine Anwendungen in der ausgew�hlten Tabelle vorhanden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
    }
    
	//Methode zur Initialisierung der ausgew�hlten PostgreSQL-Datenbank
    public void initializePostgresqlDatabase() {
    	this.hostUrl = this.host.getText();
    	this.portNumber = Integer.parseInt(this.port.getText());
    	this.dataBase = this.database.getText();
    	this.userName = this.username.getText();
    	this.passWord = this.password.getText();
    	
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://" + hostUrl + ":" + portNumber + "/" + dataBase, userName, passWord);
            System.out.println("DB connected");
        } catch(Exception e){
        	e.printStackTrace();
        	if (!e.getClass().equals(IllegalArgumentException.class)) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Fehler!");
                alertError.setHeaderText("Es konnte keine Datenbank-Verbindung hergestellt werden.");
                alertError.show();
            }  
        }
    }
    
    //Methode zum Import der Anwendungen aus der ausgew�hlten Tabelle
    public LinkedList<ApplicationModel> importApplications() {
		try {
			this.tableName = this.table.getText();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.anwendungsid, a.anwendungsname, a.beschreibung, k.kategoriename, h.herstellername, mb.mitarbeitername, f.fachbereichname, ma.mitarbeitername FROM " + tableName + " a, kategorie k, hersteller h, (anwendungsmanager am INNER JOIN mitarbeiter mb ON am.mitarbeiterid = mb.mitarbeiterid), fachbereich f, (administrator ad INNER JOIN mitarbeiter ma ON ad.mitarbeiterid = ma.mitarbeiterid) WHERE a.kategoriename = k.kategoriename AND a.herstellerid = h.herstellerid AND a.anwendungsmanagerid = am.anwendungsmanagerid AND a.fachbereichid = f.fachbereichid AND a.adminid = ad.adminid");
            ResultSet resultSet = preparedStatement.executeQuery();
            applications = new LinkedList<>();
            while (resultSet.next()){
            	applications.add(new ApplicationModel(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8)));
            }
            return applications;
        } catch(Exception e) {
        	e.printStackTrace();
			if (!e.getClass().equals(SQLException.class)) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Fehler!");
                alertError.setHeaderText("Es konnten keine Anwendungen importiert werden.");
                alertError.show();
            }
		}
		return null;
	}
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
