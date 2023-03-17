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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ApplicationModel;
import model.MainModel;

public class ImportFXMLControl implements Initializable {
	
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String dataScheme;
	private LinkedList<ApplicationModel> applications;
	private ObservableList<ApplicationModel> applicationsList;
	
	@FXML
	private ScrollPane scrollPane;
	@FXML
    private ComboBox<String> databases;
	@FXML
    private TableView<ApplicationModel> tableView;
	@FXML
    private TableColumn<ApplicationModel, String> applicationsColumn;
	@FXML
	private Button cancel;
	@FXML
	private Button load;
	@FXML
	private Button submit;
	
	
	//Methode zum Schließen des Import-Fensters
	@FXML
    private void cancel(ActionEvent event) {
        this.scrollPane.getScene().getWindow().hide();
    }
	
	//Methode zum Laden der Anwendungen aus der ausgewählten Datenbank
	@FXML
	private void load(ActionEvent event) {
		this.initializeDatabase();
		this.applicationsColumn.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
        applicationsList = FXCollections.observableArrayList();
        for(ApplicationModel aM : this.importApplications()) {
            applicationsList.add(aM);
        }
        this.applicationsColumn.getTableView().setItems(applicationsList);
	}
    
	//Methode zum Bestätigen der geladenen Anwendungen, welche dem geöffneten Modell hinzugefügt werden
	@FXML
    private void submit(ActionEvent event) {
		try {
	        if(this.importApplications().size() != 0) {
	        	for(ApplicationModel aM : this.applicationsList) {
	        		MainModel.modelFXMLControl.getModelView().getModelControl().addApplication(aM.getApplicationId(), aM.getApplicationName(), aM.getApplicationDescription(), aM.getApplicationCategory(), aM.getApplicationProducer(), aM.getApplicationManager(), aM.getApplicationDepartment(), aM.getApplicationAdmin());
	        	}
	        }
		} catch(Exception e){
			e.printStackTrace();
			if (!e.getClass().equals(NullPointerException.class)) {
	            Alert alertError = new Alert(Alert.AlertType.ERROR);
	            alertError.setTitle("Fehler!");
	            alertError.setHeaderText("Die Anwendungen sind bereits im Modell vorhanden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
    }
    
	//Methode zur Initialisierung der ausgewählten PostgreSQL-Datenbank
    public void initializeDatabase() {
    	this.hostUrl = "localhost";
    	this.portNumber = 5432;
    	this.userName = "postgres";
    	this.passWord = "pw369";
    	this.dataScheme = "public";
    	if(this.databases.getValue() == "Alle Anwendungen") {
	    	this.dataBase = "systemtransparenz";
    	}
    	else if(this.databases.getValue() == "Kernanwendungen") {
	    	this.dataBase = "itportfolio";
    	}
        try {
        	Driver driver = new org.postgresql.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostUrl + ":" + this.portNumber + "/" + this.dataBase + "?search_path=" + this.dataScheme, this.userName, this.passWord);
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
    
    //Methode zum Import der Anwendungen aus der ausgewählten Tabelle
    public LinkedList<ApplicationModel> importApplications() {
		try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.anwendungsid, a.anwendungsname, a.beschreibung, k.kategoriename, h.herstellername, mb.mitarbeitername, f.fachbereichname, ma.mitarbeitername FROM anwendung a, kategorie k, hersteller h, (anwendungsmanager am INNER JOIN mitarbeiter mb ON am.mitarbeiterid = mb.mitarbeiterid), fachbereich f, (administrator ad INNER JOIN mitarbeiter ma ON ad.mitarbeiterid = ma.mitarbeiterid) WHERE a.kategoriename = k.kategoriename AND a.herstellerid = h.herstellerid AND a.anwendungsmanagerid = am.anwendungsmanagerid AND a.fachbereichid = f.fachbereichid AND a.adminid = ad.adminid");
            ResultSet resultSet = preparedStatement.executeQuery();
            applications = new LinkedList<ApplicationModel>();
            while(resultSet.next()){
            	applications.add(new ApplicationModel(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8)));
            }
            return applications;
        } catch(Exception e) {
        	e.printStackTrace();
			if(!e.getClass().equals(SQLException.class)) {
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
		this.databases.getItems().addAll("Alle Anwendungen", "Kernanwendungen");
		this.databases.getSelectionModel().select(0);
	}

}
