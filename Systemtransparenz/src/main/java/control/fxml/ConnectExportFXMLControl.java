package control.fxml;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	
	//Methode zum Laden der Anwendungen aus dem ausgewählten Modell
	@FXML
	private void load(ActionEvent event) throws SQLException {
		this.initializeDatabase();
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
		this.adminEmployeeIdAttribute = this.employeeIdA.getText();
		String categoryStatement = "CREATE TABLE IF NOT EXISTS " + this.dataScheme + "." + this.categoryTable + "(" + this.categoryNameAttribute + " TEXT PRIMARY KEY);";
		String producerStatement = "CREATE TABLE IF NOT EXISTS " + this.producerTable + "(" + this.producerIdAttribute  + " SERIAL PRIMARY KEY, " + this.producerNameAttribute + " TEXT NOT NULL UNIQUE);";
		String departmentStatement = "CREATE TABLE IF NOT EXISTS " + this.departmentTable + "(" + this.departmentIdAttribute + " SERIAL PRIMARY KEY, " + this.departmentNameAttribute + " TEXT NOT NULL UNIQUE);";
		String employeeStatement = "CREATE TABLE IF NOT EXISTS " + this.employeeTable + "(" + this.employeeIdAttribute + " SERIAL PRIMARY KEY, " + this.employeeNameAttribute + " TEXT NOT NULL UNIQUE);";
		String managerStatement = "CREATE TABLE IF NOT EXISTS " + this.managerTable + "(" + this.managerIdAttribute + " SERIAL PRIMARY KEY, " + this.managerEmployeeIdAttribute + " INT	NOT NULL UNIQUE, FOREIGN KEY(" + this.managerEmployeeIdAttribute + ") REFERENCES " + this.employeeTable + "(" + this.employeeIdAttribute + "));";
		String adminStatement = "CREATE TABLE IF NOT EXISTS " + this.adminTable + "(" + this.adminIdAttribute + " SERIAL PRIMARY KEY, " + this.adminEmployeeIdAttribute + " INT	NOT NULL UNIQUE, FOREIGN KEY(" + this.adminEmployeeIdAttribute + ") REFERENCES " + this.employeeTable + "(" + this.employeeIdAttribute + "));";
		String applicationStatement = "CREATE TABLE IF NOT EXISTS " + this.applicationTable + "(" + this.applicationIdAttribute + " INT PRIMARY KEY, " + this.applicationNameAttribute + " TEXT NOT NULL, " + this.descriptionAttribute + " TEXT, " + this.applicationCategoryNameAttribute + " TEXT NOT NULL, " + this.applicationProducerIdAttribute + " INT NOT NULL, " + this.applicationManagerIdAttribute + " INT	NOT NULL, " + this.applicationDepartmentIdAttribute	+ " INT	NOT NULL, " + this.applicationAdminIdAttribute + "	INT	NOT NULL, FOREIGN KEY(" + this.applicationCategoryNameAttribute + ") REFERENCES " + this.categoryTable + "(" + this.categoryNameAttribute + "), FOREIGN KEY(" + this.applicationProducerIdAttribute + ") REFERENCES " + this.producerTable + "(" + this.producerIdAttribute + "), FOREIGN KEY(" + this.applicationManagerIdAttribute + ") REFERENCES " + this.managerTable + "(" + this.managerIdAttribute + "), FOREIGN KEY(" + this.applicationDepartmentIdAttribute + ") REFERENCES " + this.departmentTable + "(" + this.departmentIdAttribute + "),FOREIGN KEY(" + this.applicationAdminIdAttribute + ") REFERENCES " + this.adminTable + "(" + this.adminIdAttribute + "));";
		ArrayList<String> statements = new ArrayList<>();
		statements.add(categoryStatement);
		statements.add(producerStatement);
		statements.add(departmentStatement);
		statements.add(employeeStatement);
		statements.add(managerStatement);
		statements.add(adminStatement);
		statements.add(applicationStatement);
		for(String statement : statements) {
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
	        preparedStatement.execute();
		}
		this.applicationsColumn.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
        applicationsList = FXCollections.observableArrayList();
        for(ApplicationView applicationView : this.modelView.getApplications()) {
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
			if(!e.getClass().equals(NullPointerException.class)) {
	            Alert alertError = new Alert(Alert.AlertType.ERROR);
	            alertError.setTitle("Fehler!");
	            alertError.setHeaderText("Es konnten keine Anwendungen in die ausgewählte Tabelle exportiert werden.");
	            alertError.show();
	        }
		}
		this.scrollPane.getScene().getWindow().hide();
		Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
		alertInformation.setTitle("Exportieren erfolgreich");
		alertInformation.setHeaderText("Ihre Anwendungen wurden erfolgreich in die ausgewählte Datenbank exportiert.");
		alertInformation.show();
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
            connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostUrl + ":" + this.portNumber + "/?", this.userName, this.passWord);
            PreparedStatement ps = connection.prepareStatement("SELECT datname FROM pg_database WHERE datistemplate = false;");
            ResultSet rs = ps.executeQuery();
            boolean databaseExists = false;
            while(rs.next()) {
                if(rs.getString(1).equals(this.dataBase)) {
                	databaseExists = true;
                	break;
                }
                else {
                	databaseExists = false;
                }
            }
            if(databaseExists == true) {
            	connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostUrl + ":" + this.portNumber + "/" + this.dataBase, this.userName, this.passWord);
            	Statement stmt = connection.createStatement();
            	stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + this.dataScheme + ";");
            	stmt.executeUpdate("SET SEARCH_PATH TO " + this.dataScheme + ";");
            }
            else {
            	Statement stmt = connection.createStatement();
            	stmt.executeUpdate("CREATE DATABASE " + this.dataBase + ";");
            	connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostUrl + ":" + this.portNumber + "/" + this.dataBase, this.userName, this.passWord);
            	Statement stmt1 = connection.createStatement();
            	stmt1.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + this.dataScheme + ";");
            	stmt1.executeUpdate("SET SEARCH_PATH TO " + this.dataScheme + ";");
            }
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
		String categoryStatement = "INSERT INTO " + this.categoryTable + "(" + this.categoryNameAttribute + ")"
				+ "VALUES (?) "
				+ "ON CONFLICT (" + this.categoryNameAttribute + ") DO UPDATE "
				+ "SET " + this.categoryNameAttribute + " = excluded." + this.categoryNameAttribute + ";";
		PreparedStatement categoryPreparedStatement = connection.prepareStatement(categoryStatement);
		categoryPreparedStatement.setString(1, applicationModel.getApplicationCategory());
		categoryPreparedStatement.execute();
		String producerStatement = "INSERT INTO " + this.producerTable + "("+ this.producerIdAttribute + ", " + this.producerNameAttribute + ")"
				+ "VALUES (default, ?) "
				+ "ON CONFLICT (" + this.producerNameAttribute + ") DO UPDATE "
				+ "SET " + this.producerNameAttribute + " = excluded." + this.producerNameAttribute + ";";
		PreparedStatement producerPreparedStatement = connection.prepareStatement(producerStatement);
		producerPreparedStatement.setString(1, applicationModel.getApplicationProducer());
		producerPreparedStatement.execute();
		String departmentStatement = "INSERT INTO " + this.departmentTable + "(" + this.departmentIdAttribute + ", " + this.departmentNameAttribute + ")"
				+ "VALUES (default, ?) "
				+ "ON CONFLICT (" + this.departmentNameAttribute + ") DO UPDATE "
				+ "SET " + this.departmentNameAttribute + " = excluded." + this.departmentNameAttribute + ";";
		PreparedStatement departmentPreparedStatement = connection.prepareStatement(departmentStatement);
		departmentPreparedStatement.setString(1, applicationModel.getApplicationDepartment());
		departmentPreparedStatement.execute();
		String employeeStatement = "INSERT INTO " + this.employeeTable + "(" + this.employeeIdAttribute + ", " + this.employeeNameAttribute + ")"
				+ "VALUES (default, ?) "
				+ "ON CONFLICT (" + this.employeeNameAttribute + ") DO UPDATE "
				+ "SET " + this.employeeNameAttribute + " = excluded." + this.employeeNameAttribute + ";";
		PreparedStatement employeePreparedStatement = connection.prepareStatement(employeeStatement);
		employeePreparedStatement.setString(1,  applicationModel.getApplicationManager());
		employeePreparedStatement.execute();
		employeePreparedStatement.setString(1,  applicationModel.getApplicationAdmin());
		employeePreparedStatement.execute();
		String statement4 = "INSERT INTO " + this.managerTable + "(" + this.managerIdAttribute + ", " + this.managerEmployeeIdAttribute + ")"
				+"VALUES (default, (SELECT " + this.employeeIdAttribute + " FROM " + this.employeeTable + " WHERE " + this.employeeNameAttribute + " = ?))"
				+ " ON CONFLICT (" + this.managerEmployeeIdAttribute + ") DO UPDATE "
				+ "SET " + this.managerEmployeeIdAttribute + " = excluded." + this.managerEmployeeIdAttribute +";";
		PreparedStatement preparedStatement5 = connection.prepareStatement(statement4);
		preparedStatement5.setString(1, applicationModel.getApplicationManager());
		preparedStatement5.execute();
		String statement5 = "INSERT INTO " + this.adminTable + "(" + this.adminIdAttribute + ", " + this.adminEmployeeIdAttribute + ")"
				+"VALUES (default, (SELECT " + this.employeeIdAttribute + " FROM " + this.employeeTable + " WHERE " + this.employeeNameAttribute + " = ?))"
				+ " ON CONFLICT (" + this.adminEmployeeIdAttribute + ") DO UPDATE "
				+ "SET " + this.adminEmployeeIdAttribute + " = excluded." + this.adminEmployeeIdAttribute + ";";
		PreparedStatement preparedStatement6 = connection.prepareStatement(statement5);
		preparedStatement6.setString(1, applicationModel.getApplicationAdmin());
		preparedStatement6.execute();
		this.sqlStatement = "INSERT INTO " + this.applicationTable + "(" + this.applicationIdAttribute + ", " + this.applicationNameAttribute + ", " + this.descriptionAttribute + ", " + this.applicationCategoryNameAttribute + ", " + this.applicationProducerIdAttribute + ", " + this.applicationManagerIdAttribute + ", " + this.applicationDepartmentIdAttribute + ", " + this.applicationAdminIdAttribute + ")"
				+"VALUES (?, ?, ?,"
				+"(SELECT " + this.categoryNameAttribute + " FROM " + this.categoryTable + " k WHERE k." + this.categoryNameAttribute + " = ?),"
				+"(SELECT " + this.producerIdAttribute + " FROM " + this.producerTable + " h WHERE h." + this.producerNameAttribute + " = ?),"
				+"(SELECT " + this.managerIdAttribute + " FROM (" + this.managerTable + " am INNER JOIN " + this.employeeTable + " ma ON am." + this.managerEmployeeIdAttribute + " = ma." + this.employeeIdAttribute + ") WHERE ma." + this.employeeNameAttribute + " = ?),"
				+"(SELECT " + this.departmentIdAttribute + " FROM " + this.departmentTable + " f WHERE f." + this.departmentNameAttribute + " = ?),"
				+"(SELECT " + this.adminIdAttribute + " FROM (" + this.adminTable + " ad INNER JOIN " + this.employeeTable + " mb ON ad." + this.adminEmployeeIdAttribute + " = mb." + this.employeeIdAttribute + ") WHERE mb." + this.employeeNameAttribute + "= ?))"
				+"ON CONFLICT (" + this.applicationIdAttribute + ") DO UPDATE "
				+"SET " + this.applicationNameAttribute + " = excluded." + this.applicationNameAttribute + ", "
				+ this.descriptionAttribute + " = excluded." + this.descriptionAttribute + ", "
				+ this.applicationCategoryNameAttribute + " = excluded." + this.applicationCategoryNameAttribute + ", "
				+ this.applicationProducerIdAttribute + " = excluded." + this.applicationProducerIdAttribute + ", "
				+ this.applicationManagerIdAttribute + " = excluded." + this.applicationManagerIdAttribute + ", "
				+ this.applicationDepartmentIdAttribute + " = excluded." + this.applicationDepartmentIdAttribute + ", "
				+ this.applicationAdminIdAttribute + " = excluded." + this.applicationAdminIdAttribute + ";";
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
