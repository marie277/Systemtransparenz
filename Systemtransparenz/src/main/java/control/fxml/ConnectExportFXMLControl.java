package control.fxml;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ApplicationModel;
import view.ApplicationView;
import view.ModelView;

public class ConnectExportFXMLControl implements Initializable {
	
	private static ConnectExportFXMLControl connectExportFXMLControl;
	private ModelView modelView;
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String sqlStatement;
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
	private TextField statement;
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
	
	//Konstruktor
	public ConnectExportFXMLControl() {
		
	}
	
	//Statische Getter-Methode für die Steuerung des Datenbank-Imports
	public static ConnectExportFXMLControl getConnectExportFXMLControl(){
        if (connectExportFXMLControl == null) {
        	connectExportFXMLControl = new ConnectExportFXMLControl();
        }
        return connectExportFXMLControl;
    }
	
	//Methode zum Schließen des Import-Fensters
	@FXML
    private void cancel(ActionEvent event) {
        this.scrollPane.getScene().getWindow().hide();
    }
	
	//Methode zum Laden der Anwendungen aus der ausgewählten Datenbank
	@FXML
	private void load(ActionEvent event) {
		this.initializePostgresqlDatabase();
		this.applicationsColumn.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
       
        //im Modell vorhandene Anwendungen
        applicationsList = FXCollections.observableArrayList();
        for (ApplicationView applicationView : this.modelView.getApplications()) {
        	applicationsList.add(applicationView.getApplicationModel());
        }
        
        this.applicationsColumn.getTableView().setItems((ObservableList<ApplicationModel>)applicationsList);
        
	}
    
	//Methode zum Bestätigen der geladenen Anwendungen, welche dem geöffneten Modell hinzugefügt werden
	@FXML
    private void submit(ActionEvent event) {
		try {
	        for(ApplicationModel applicationModel : this.applicationsList) {
	        	this.exportApplications(applicationModel);
	        }
		}
		catch (Exception e){
			e.printStackTrace();
			if (!e.getClass().equals(NullPointerException.class)) {
	            Alert alertError = new Alert(Alert.AlertType.ERROR);
	            alertError.setTitle("Fehler!");
	            alertError.setHeaderText("Es sind keine Anwendungen in der ausgewählten Tabelle vorhanden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
    }
    
	//Methode zur Initialisierung der ausgewählten PostgreSQL-Datenbank
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
    
    
    //Methode zum Export der Anwendungen aus der ausgewählte Tabelle
    public boolean exportApplications(ApplicationModel applicationModel) {
    	try {
    		this.sqlStatement = this.statement.getText();
    		PreparedStatement preparedStatement = connection.prepareStatement(this.sqlStatement);
            preparedStatement.execute();
        } catch (Exception e) {
        	e.printStackTrace();
			if (!e.getClass().equals(SQLException.class)) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Fehler!");
                alertError.setHeaderText("Es konnten keine Anwendungen importiert werden.");
                alertError.show();
            }
			return false;
        }
        return true;
	}
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String defaultStatement = "INSERT INTO anwendung (anwendungsid, anwendungsname, beschreibung, kategoriename, herstellerid, anwendungsmanagerid, fachbereichid, adminid)"
				+"VALUES (?, ?, ?,"
				+"(SELECT kategoriename from kategorie WHERE kategorie.kategoriename = ?),"
				+"(SELECT herstellerid from hersteller WHERE hersteller.herstellername = ?),"
				+"(SELECT anwendungsmanagerid from (anwendungsmanager INNER JOIN mitarbeiter ON anwendungsmanager.mitarbeiterid = mitarbeiter.mitarbeiterid) WHERE mitarbeiter.mitarbeitername = ?),"
				+"(SELECT fachbereichid from fachbereich WHERE fachbereich.fachbereichname = ?),"
				+"(SELECT adminid from (administrator INNER JOIN mitarbeiter ON administrator.mitarbeiterid = mitarbeiter.mitarbeiterid) WHERE mitarbeiter.mitarbeitername = ?))"
				+"ON CONFLICT (anwendungsid) DO UPDATE "
				+"SET anwendungsname = excluded.anwendungsname,"
				+"beschreibung = excluded.beschreibung,"
				+"kategoriename = excluded.kategoriename,"
				+"herstellerid = excluded.herstellerid,"
				+"anwendungsmanagerid = excluded.anwendungsmanagerid,"
				+"fachbereichid = excluded.fachbereichid,"
				+"adminid = excluded.adminid;";
		this.statement.setText(defaultStatement);
	}
}
