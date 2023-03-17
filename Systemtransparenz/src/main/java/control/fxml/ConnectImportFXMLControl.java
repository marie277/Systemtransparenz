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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ApplicationModel;
import model.MainModel;

//Klasse zur Steuerung des Imports von Anwendungen aus einer Datenbank, implementiert Interface Initializable
public class ConnectImportFXMLControl implements Initializable {
	
	private Connection connection = null;
	private String userName;
	private String passWord;
	private String hostUrl;
	private int portNumber;
	private String dataBase;
	private String dataScheme;
	private String applicationTable;
	private String categoryTable;
	private String producerTable;
	private String departmentTable;
	private String managerTable;
	private String adminTable;
	private String employeeTable;
	private String sqlStatement;
	private String applicationIdAttribute;
	private String applicationNameAttribute;
	private String descriptionAttribute;
	private String applicationCategoryNameAttribute;
	private String categoryNameAttribute;
	private String applicationProducerIdAttribute;
	private String producerIdAttribute;
	private String producerNameAttribute;
	private String applicationDepartmentIdAttribute;
	private String departmentIdAttribute;
	private String departmentNameAttribute;
	private String employeeIdAttribute;
	private String employeeNameAttribute;
	private String applicationManagerIdAttribute;
	private String managerIdAttribute;
	private String applicationAdminIdAttribute;
	private String adminIdAttribute;
	private String managerEmployeeIdAttribute;
	private String adminEmployeeIdAttribute;
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
	private TextField applicationCategoryName;
	@FXML
	private TextField categoryName;
	@FXML
	private TextField applicationProducerId;
	@FXML
	private TextField producerId;
	@FXML
	private TextField producerName;
	@FXML
	private TextField applicationDepartmentId;
	@FXML
	private TextField departmentId;
	@FXML
	private TextField departmentName;
	@FXML
	private TextField employeeId;
	@FXML
	private TextField employeeName;
	@FXML
	private TextField applicationManagerId;
	@FXML
	private TextField managerId;
	@FXML
	private TextField applicationAdminId;
	@FXML
	private TextField adminId;
	@FXML
	private TextField managerEmployeeId;
	@FXML
	private TextField adminEmployeeId;
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
		} catch (Exception e){
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
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
    
    //Methode zum Import der Anwendungen aus der ausgewählten Tabelle
    public LinkedList<ApplicationModel> importApplications() {
		try {
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
			this.applicationCategoryNameAttribute = this.applicationCategoryName.getText();
			this.categoryNameAttribute = this.categoryName.getText();
			this.applicationProducerIdAttribute = this.applicationProducerId.getText();
			this.producerIdAttribute = this.producerId.getText();
			this.producerNameAttribute = this.producerName.getText();
			this.applicationDepartmentIdAttribute = this.applicationDepartmentId.getText();
			this.departmentIdAttribute = this.departmentId.getText();
			this.departmentNameAttribute = this.departmentName.getText();
			this.employeeIdAttribute = this.employeeId.getText();
			this.employeeNameAttribute = this.employeeName.getText();
			this.applicationManagerIdAttribute = this.applicationManagerId.getText();
			this.managerIdAttribute = this.managerId.getText();
			this.applicationAdminIdAttribute = this.applicationAdminId.getText();
			this.adminIdAttribute = this.adminId.getText();
			this.managerEmployeeIdAttribute = this.managerEmployeeId.getText();
			this.adminEmployeeIdAttribute = this.adminEmployeeId.getText();
			this.sqlStatement = "SELECT a." + this.applicationIdAttribute + ", a." + this.applicationNameAttribute + ", a."
			+ this.descriptionAttribute + ", k." + this.categoryNameAttribute + ", h." + this.producerNameAttribute 
					+ ", mb." + this.employeeNameAttribute + ", f." + this.departmentNameAttribute + ", ma." + this.employeeNameAttribute + " FROM " + this.applicationTable
					+ " a, " + this.categoryTable + " k, " + this.producerTable + " h, (" + this.managerTable
					+ " am INNER JOIN " + this.employeeTable + " mb ON am." + this.managerEmployeeIdAttribute + " = mb." + this.employeeIdAttribute + "), "
					+ this.departmentTable + " f, (" + this.adminTable + " ad INNER JOIN " + this.employeeTable + " ma ON ad."
					+ this.adminEmployeeIdAttribute + " = ma." + this.managerEmployeeIdAttribute + ") WHERE a." + this.applicationCategoryNameAttribute + " = k." + this.categoryNameAttribute
					+ " AND a." + this.applicationProducerIdAttribute + " = h." + this.producerIdAttribute + " AND a." + this.applicationManagerIdAttribute + " = am." + this.managerIdAttribute
					+ " AND a." + this.applicationDepartmentIdAttribute + " = f." + this.departmentIdAttribute + " AND a." + this.applicationAdminIdAttribute + " = ad." + this.adminIdAttribute + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(this.sqlStatement);
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
		
	}

}
