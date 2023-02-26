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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ApplicationModel;
import model.MainModel;
import view.ApplicationView;
import view.ModelView;

public class ConnectExportFXMLControl implements Initializable {
	
	private ModelView modelView;
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String sqlStatement;
	private String dataScheme;
	private String applicationTable;
	private String categoryTable;
	private String producerTable;
	private String departmentTable;
	private String managerTable;
	private String adminTable;
	private String employeeTable;
	private String applicationIdAttribute;
	private String applicationNameAttribute;
	private String descriptionAttribute;
	private String categoryNameAAttribute;
	private String categoryNameAttribute;
	private String producerIdAAttribute;
	private String producerIdAttribute;
	private String producerNameAttribute;
	private String departmentIdAAttribute;
	private String departmentIdAttribute;
	private String departmentNameAttribute;
	private String employeeIdAttribute;
	private String employeeNameAttribute;
	private String managerIdAAttribute;
	private String managerIdAttribute;
	private String adminIdAAttribute;
	private String adminIdAttribute;
	private String employeeIdMAttribute;
	private String employeeIdAAttribute;
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
	private TextField scheme;
	@FXML
	private TextField application;
	@FXML
	private TextField category;
	@FXML
	private TextField producer;
	@FXML
	private TextField department;
	@FXML
	private TextField manager;
	@FXML
	private TextField admin;
	@FXML
	private TextField employee;
	@FXML
	private TextField applicationId;
	@FXML
	private TextField applicationName;
	@FXML
	private TextField description;
	@FXML
	private TextField categoryNameA;
	@FXML
	private TextField categoryName;
	@FXML
	private TextField producerIdA;
	@FXML
	private TextField producerId;
	@FXML
	private TextField producerName;
	@FXML
	private TextField departmentIdA;
	@FXML
	private TextField departmentId;
	@FXML
	private TextField departmentName;
	@FXML
	private TextField employeeId;
	@FXML
	private TextField employeeName;
	@FXML
	private TextField managerIdA;
	@FXML
	private TextField managerId;
	@FXML
	private TextField adminIdA;
	@FXML
	private TextField adminId;
	@FXML
	private TextField employeeIdM;
	@FXML
	private TextField employeeIdA;
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
        for (ApplicationView applicationView : this.modelView.getApplications()) {
        	applicationsList.add(applicationView.getApplicationModel());
        }
        this.applicationsColumn.getTableView().setItems(applicationsList);    
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
	            alertError.setHeaderText("Es konnten keine Anwendungen in die ausgewählte Tabelle exportiert werden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
		Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
		alertConfirm.setTitle("Speichern erfolgreich");
		alertConfirm.setHeaderText("Ihre Anwendungen wurden erfolgreich in die ausgewählte Datenbank exportiert.");
		alertConfirm.show();
    }
    
	//Methode zur Initialisierung der ausgewählten PostgreSQL-Datenbank
    public void initializeDatabase() {
    	this.hostUrl = this.host.getText();
    	this.portNumber = Integer.parseInt(this.port.getText());
    	this.dataBase = this.database.getText();
    	this.dataScheme = this.scheme.getText();
    	this.userName = this.username.getText();
    	this.passWord = this.password.getText();
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
    
    //Methode zum Export der Anwendungen aus der ausgewählte Tabelle
    public void exportApplications(ApplicationModel applicationModel) throws SQLException {
    	this.applicationTable = this.application.getText();
		this.categoryTable = this.category.getText();
		this.producerTable = this.producer.getText();
		this.departmentTable = this.department.getText();
		this.managerTable = this.manager.getText();
		this.adminTable = this.admin.getText();
		this.employeeTable = this.employee.getText();
		this.applicationIdAttribute = this.applicationId.getText();
		this.applicationNameAttribute = this.applicationName.getText();
		this.descriptionAttribute = this.description.getText();
		this.categoryNameAAttribute = this.categoryNameA.getText();
		this.categoryNameAttribute = this.categoryName.getText();
		this.producerIdAAttribute = this.producerIdA.getText();
		this.producerIdAttribute = this.producerId.getText();
		this.producerNameAttribute = this.producerName.getText();
		this.departmentIdAAttribute = this.departmentIdA.getText();
		this.departmentIdAttribute = this.departmentId.getText();
		this.departmentNameAttribute = this.departmentName.getText();
		this.employeeIdAttribute = this.employeeId.getText();
		this.employeeNameAttribute = this.employeeName.getText();
		this.managerIdAAttribute = this.managerIdA.getText();
		this.managerIdAttribute = this.managerId.getText();
		this.adminIdAAttribute = this.adminIdA.getText();
		this.adminIdAttribute = this.adminId.getText();
		this.employeeIdMAttribute = this.employeeIdM.getText();
		this.employeeIdAAttribute = this.employeeIdA.getText();
		this.sqlStatement = "INSERT INTO " + this.applicationTable + "(" + this.applicationIdAttribute + ", " + this.applicationNameAttribute + ", " + this.descriptionAttribute + ", " + this.categoryNameAAttribute + ", " + this.producerIdAAttribute + ", " + this.managerIdAAttribute + ", " + this.departmentIdAAttribute + ", " + this.adminIdAAttribute + ")"
				+"VALUES (?, ?, ?,"
				+"(SELECT " + this.categoryNameAttribute + " FROM " + this.categoryTable + " k WHERE k." + this.categoryNameAttribute + " = ?),"
				+"(SELECT " + this.producerIdAttribute + " FROM " + this.producerTable + " h WHERE h." + this.producerNameAttribute + " = ?),"
				+"(SELECT " + this.managerIdAttribute + " FROM (" + this.managerTable + " am INNER JOIN " + this.employeeTable + " ma ON am." + this.employeeIdMAttribute + " = ma." + this.employeeIdAttribute + ") WHERE ma." + this.employeeNameAttribute + " = ?),"
				+"(SELECT " + this.departmentIdAttribute + " FROM " + this.departmentTable + " f WHERE f." + this.departmentNameAttribute + " = ?),"
				+"(SELECT " + this.adminIdAttribute + " FROM (" + this.adminTable + " ad INNER JOIN " + this.employeeTable + " mb ON ad." + this.employeeIdAAttribute + " = mb." + this.employeeIdAttribute + ") WHERE mb." + this.employeeNameAttribute + "= ?))"
				+"ON CONFLICT (" + this.applicationIdAttribute + ") DO UPDATE "
				+"SET " + this.applicationNameAttribute + " = excluded." + this.applicationNameAttribute + ", "
				+ this.descriptionAttribute + " = excluded." + this.descriptionAttribute + ", "
				+ this.categoryNameAAttribute + " = excluded." + this.categoryNameAAttribute + ", "
				+ this.producerIdAAttribute + " = excluded." + this.producerIdAAttribute + ", "
				+ this.managerIdAAttribute + " = excluded." + this.managerIdAAttribute + ", "
				+ this.departmentIdAAttribute + " = excluded." + this.departmentIdAAttribute + ", "
				+ this.adminIdAAttribute + " = excluded." + this.adminIdAAttribute + ";";
		PreparedStatement preparedStatement = connection.prepareStatement(this.sqlStatement);
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
	}
}
