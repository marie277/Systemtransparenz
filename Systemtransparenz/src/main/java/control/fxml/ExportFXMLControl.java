package control.fxml;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import view.ApplicationView;
import view.ModelView;

public class ExportFXMLControl implements Initializable {
	
	private ModelView modelView;
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String dataScheme;
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
		this.applicationsColumn.setCellValueFactory(new PropertyValueFactory
				<ApplicationModel, String>("applicationName"));
        applicationsList = FXCollections.observableArrayList();
        for(ApplicationView applicationView : this.modelView.getApplications()) {
        	applicationsList.add(applicationView.getApplicationModel());
        }
        this.applicationsColumn.getTableView()
        .setItems((ObservableList<ApplicationModel>)applicationsList);
	}
    
	//Methode zum Bestätigen der geladenen Anwendungen, welche dem geöffneten Modell hinzugefügt werden
	@FXML
    private void submit(ActionEvent event) {
		try {
	        for(ApplicationModel applicationModel : this.applicationsList) {
	        	this.exportApplications(applicationModel);
	        }
		} catch(Exception e){
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
	            Alert alertError = new Alert(Alert.AlertType.ERROR);
	            alertError.setTitle("Fehler!");
	            alertError.setHeaderText("Es konnten keine Anwendungen in die ausgewählte Tabelle exportiert werden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
		Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
		alertInformation.setTitle("Speichern erfolgreich");
		alertInformation.setHeaderText("Ihre Anwendungen wurden erfolgreich in die ausgewählte Datenbank exportiert.");
		alertInformation.show();
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
            connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostUrl + ":" 
            + this.portNumber + "/" + this.dataBase + "?search_path=" + this.dataScheme,
            this.userName, this.passWord);
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
    
    //Methode zum Export der Anwendungen aus der ausgewählte Tabelle
    public void exportApplications(ApplicationModel applicationModel) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO anwendung (anwendungsid, anwendungsname, beschreibung,"
				+ " kategoriename, herstellerid, anwendungsmanagerid, fachbereichid, adminid)"
				+"VALUES (?, ?, ?,"
				+"(SELECT kategoriename from kategorie WHERE kategorie.kategoriename = ?),"
				+"(SELECT herstellerid from hersteller WHERE hersteller.herstellername = ?),"
				+"(SELECT anwendungsmanagerid from (anwendungsmanager INNER JOIN mitarbeiter "
				+ "ON anwendungsmanager.mitarbeiterid = mitarbeiter.mitarbeiterid) WHERE "
				+ "mitarbeiter.mitarbeitername = ?),"
				+"(SELECT fachbereichid from fachbereich WHERE fachbereich.fachbereichname "
				+ "= ?), (SELECT adminid from (administrator INNER JOIN mitarbeiter ON "
				+ "administrator.mitarbeiterid = mitarbeiter.mitarbeiterid) "
				+ "WHERE mitarbeiter.mitarbeitername = ?))"
				+"ON CONFLICT (anwendungsid) DO UPDATE "
				+"SET anwendungsname = excluded.anwendungsname,"
				+"beschreibung = excluded.beschreibung,"
				+"kategoriename = excluded.kategoriename,"
				+"herstellerid = excluded.herstellerid,"
				+"anwendungsmanagerid = excluded.anwendungsmanagerid,"
				+"fachbereichid = excluded.fachbereichid,"
				+"adminid = excluded.adminid;");
        preparedStatement.setInt(1, applicationModel.getApplicationId());
        preparedStatement.setString(2, applicationModel.getApplicationName());
        preparedStatement.setString(3, applicationModel.getApplicationDescription());
        preparedStatement.setString(4, applicationModel.getApplicationCategory());
        preparedStatement.setString(5, applicationModel.getApplicationProducer());
        preparedStatement.setString(6, applicationModel.getApplicationManager());
        preparedStatement.setString(7, applicationModel.getApplicationDepartment());
        preparedStatement.setString(8, applicationModel.getApplicationAdmin());
        preparedStatement.execute();
	}
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.modelView = MainModel.modelFXMLControl.getModelView();
		this.databases.getItems().addAll("Alle Anwendungen", "Kernanwendungen");
		this.databases.getSelectionModel().select(0);
	}

}
