package control.fxml;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

import application.Main;
import control.MainControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ApplicationModel;
import view.ApplicationView;

public class DatabaseFXMLControl implements Initializable {
	
	private static DatabaseFXMLControl databaseFXMLControl;
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String tableName;
	private LinkedList<ApplicationModel> returnValues;
	private ObservableList<ApplicationModel> applicationsList;
	
	@FXML
	private ScrollPane scroll_pane;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private TextField host;
	@FXML
	private TextField port;
	@FXML
	private TextField database;
	@FXML
	private TextField table;
	@FXML
    private TableColumn<ApplicationModel, String> applications;
	@FXML
	private Button cancel;
	@FXML
	private Button load;
	@FXML
	private Button submit;
	
	public DatabaseFXMLControl() {
		
	}
	
	public static DatabaseFXMLControl getDatabaseFXMLControl(){
        if (databaseFXMLControl == null) {
        	databaseFXMLControl = new DatabaseFXMLControl();
        }
        return databaseFXMLControl;
    }
	
	@FXML
    void cancel(ActionEvent event) {
        this.scroll_pane.getScene().getWindow().hide();
    }
	
	@FXML
	void load(ActionEvent event) {
		this.initializePostgresqlDatabase();
		//this.importApplications();
		this.applications.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
        applicationsList = FXCollections.observableArrayList();
        for (ApplicationModel aM : this.importApplications()) {
            applicationsList.add(aM);
            
        }
        this.applications.getTableView().setItems((ObservableList<ApplicationModel>)applicationsList);
	}
    
	@FXML
    void submit(ActionEvent event) {
        if (this.importApplications().size() != 0) {
        	for(ApplicationModel aM : this.applicationsList) {
        		MainControl.getMainControl().addApplication(aM.getApplicationName());
        	}
        	
        }
        else {
            this.showException(new Exception("Es sind keine Anwendungen in der ausgewählten Tabelle vorhanden."));
        }
    }
    /*@FXML
    void submit(ActionEvent event) {
        this.initializePostgresqlDatabase();
        this.getApplications();
        if (this.importApplications().size() != 0) {
            try {
                this.scroll_pane.setContent((Node)this.loadNextFXMLFile());
            }
            catch (IOException e) {
                this.showException(e);
            }
        }
        else {
            this.showException(new Exception("Es konnte keine Datenbank-Verbindung hergestellt werden."));
        }
    }*/
    
    /*private Parent loadNextFXMLFile() throws IOException {
        Parent nextFXMLFile = (Parent)FXMLLoader.load(Main.class.getResource("dataImport.fxml"));
        return nextFXMLFile;
    }*/
    
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
        } catch (SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (connection == null) {
                System.exit(-1);
            }
        }
    }
    
    private void showException(Exception e) {
        if (!e.getClass().equals(NullPointerException.class)) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fehler!");
            error.setHeaderText(e.getMessage());
            error.show();
        }
        e.printStackTrace();
    }
    
    public LinkedList<ApplicationModel> importApplications() {
		// TODO Auto-generated method stub
		try {
			this.tableName = this.table.getText();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet sqlReturnValues = stmt.executeQuery();

            returnValues = new LinkedList<>();

            while (sqlReturnValues.next()){
                returnValues.add(new ApplicationModel(sqlReturnValues.getString(2)));
            }
            for(ApplicationModel aM : returnValues) {
            	System.out.print(aM.getApplicationName());
            }
            return returnValues;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
		return null;
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
