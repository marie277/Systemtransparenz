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
import javafx.scene.control.TextField;
import model.MainModel;
import view.ApplicationView;

//Klasse zur Steuerung der Bearbeitung einer ausgewählten Anwendung, implementiert Interface Initializable
public class EditApplicationFXMLControl implements Initializable {
	
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
	
	private ApplicationView applicationView;
    @FXML
    private TextField applicationName;
    @FXML
    private TextField applicationId;
    @FXML
    private TextField applicationDescription;
    @FXML
    private ComboBox<String> applicationCategory;
    @FXML
    private ComboBox<String> applicationProducer;
    @FXML
    private ComboBox<String> applicationManager;
    @FXML
    private ComboBox<String> applicationDepartment;
    @FXML
    private ComboBox<String> applicationAdmin;
    @FXML
    private Button submit;
    
    //Methode zur Bearbeitung einer ausgewählten Anwendung
    @FXML
    private void editApplication(ActionEvent event) {
    	int applicationIdView = this.applicationView.getApplicationModel().getApplicationId();
    	String applicationNameView = this.applicationView.getApplicationModel().getApplicationName();
    	String applicationDescriptionView = this.applicationView.getApplicationModel().getApplicationDescription();
    	String applicationCategoryView = this.applicationView.getApplicationModel().getApplicationCategory();
    	String applicationProducerView = this.applicationView.getApplicationModel().getApplicationProducer();
    	String applicationManagerView = this.applicationView.getApplicationModel().getApplicationManager();
    	String applicationDepartmentView = this.applicationView.getApplicationModel().getApplicationDepartment();
    	String applicationAdminView = this.applicationView.getApplicationModel().getApplicationAdmin();
        try {
        	if(!this.applicationId.getText().equals(String.valueOf(applicationIdView))) {
                this.applicationView.getModelView().getModelControl().changeApplicationId(this.applicationView,
                		this.applicationId.getText());
            }
            if(!this.applicationName.getText().equals(applicationNameView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationName(this.applicationView, 
                		this.applicationName.getText());
            }
            if(!this.applicationDescription.getText().equals(applicationDescriptionView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationDescription(this.applicationView, this.applicationDescription.getText());
            }
            if(!this.applicationCategory.getValue().equals(applicationCategoryView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationCategory(this.applicationView, this.applicationCategory.getValue());
            }
            if(!this.applicationProducer.getValue().equals(applicationProducerView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationProducer(this.applicationView, this.applicationProducer.getValue());
            }
            if(!this.applicationManager.getValue().equals(applicationManagerView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationManager(this.applicationView, this.applicationManager.getValue());
            }
            if(!this.applicationDepartment.getValue().equals(applicationDepartmentView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationDepartment(this.applicationView, this.applicationDepartment.getValue());
            }
            if(!this.applicationAdmin.getValue().equals(applicationAdminView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationAdmin(this.applicationView, this.applicationAdmin.getValue());
            }
        } catch(Exception e) {
        	e.printStackTrace();
        	if(!e.getClass().equals(NullPointerException.class)) {
                Alert alertEerror = new Alert(Alert.AlertType.ERROR);
                alertEerror.setTitle("Fehler!");
                alertEerror.setHeaderText(e.getMessage());
                alertEerror.show();
            }
        }
    }
    
    //Methode zur Herstellung der Datenbank-Verbindung
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
		this.applicationView = (ApplicationView)MainModel.modelFXMLControl.getModelView().getElementView();
        String applicationNameView = this.applicationView.getApplicationModel().getApplicationName();
        this.applicationName.setText(applicationNameView);
        int applicationIdView = this.applicationView.getApplicationModel().getApplicationId();
    	this.applicationId.setText(Integer.toString(applicationIdView));
    	String applicationDescriptionView = this.applicationView.getApplicationModel().getApplicationDescription();
    	this.applicationDescription.setText(applicationDescriptionView);
    	String applicationCategoryView = this.applicationView.getApplicationModel().getApplicationCategory();
    	String applicationProducerView = this.applicationView.getApplicationModel().getApplicationProducer();
    	String applicationManagerView = this.applicationView.getApplicationModel().getApplicationManager();
    	String applicationDepartmentView = this.applicationView.getApplicationModel().getApplicationDepartment();
    	String applicationAdminView = this.applicationView.getApplicationModel().getApplicationAdmin();
    	this.initializeDatabase();
		this.importValues();
		for(String category : this.categories) {
			this.applicationCategory.getItems().add(category);
		}
		this.applicationCategory.getSelectionModel().select(applicationCategoryView);
		for(String producer : this.producers) {
			this.applicationProducer.getItems().add(producer);
		}
		this.applicationProducer.getSelectionModel().select(applicationProducerView);
		for(String manager : this.managers) {
			this.applicationManager.getItems().add(manager);
		}
		this.applicationManager.getSelectionModel().select(applicationManagerView);
		for(String department : this.departments) {
			this.applicationDepartment.getItems().add(department);
		}
		this.applicationDepartment.getSelectionModel().select(applicationDepartmentView);
		for(String admin : this.admins) {
			this.applicationAdmin.getItems().add(admin);
		}
		this.applicationAdmin.getSelectionModel().select(applicationAdminView);
	}
}
