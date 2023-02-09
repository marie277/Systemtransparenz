package control.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import view.ApplicationView;

//Klasse zur Steuerung der Bearbeitung einer ausgewählten Anwendung, implementiert Interface Initializable
public class EditApplicationFXMLControl implements Initializable {
	
	private ApplicationView applicationView;
    @FXML
    private TextField applicationName;
    @FXML
    private TextField applicationId;
    @FXML
    private TextField applicationDescription;
    @FXML
    private TextField applicationCategory;
    @FXML
    private TextField applicationProducer;
    @FXML
    private TextField applicationManager;
    @FXML
    private TextField applicationDepartment;
    @FXML
    private TextField applicationAdmin;
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
        	if (!this.applicationId.getText().equals(String.valueOf(applicationIdView))) {
                this.applicationView.getModelView().getModelControl().changeApplicationId(this.applicationView, this.applicationId.getText());
            }
            if (!this.applicationName.getText().equals(applicationNameView)) {
                this.applicationView.getModelView().getModelControl().renameApplication(this.applicationView, this.applicationName.getText());
            }
            if (!this.applicationDescription.getText().equals(applicationDescriptionView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationDescription(this.applicationView, this.applicationDescription.getText());
            }
            if (!this.applicationCategory.getText().equals(applicationCategoryView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationCategory(this.applicationView, this.applicationCategory.getText());
            }
            if (!this.applicationProducer.getText().equals(applicationProducerView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationProducer(this.applicationView, this.applicationProducer.getText());
            }
            if (!this.applicationManager.getText().equals(applicationManagerView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationManager(this.applicationView, this.applicationManager.getText());
            }
            if (!this.applicationDepartment.getText().equals(applicationDepartmentView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationCategory(this.applicationView, this.applicationDepartment.getText());
            }
            if (!this.applicationAdmin.getText().equals(applicationAdminView)) {
                this.applicationView.getModelView().getModelControl().changeApplicationAdmin(this.applicationView, this.applicationAdmin.getText());
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        	if (!e.getClass().equals(NullPointerException.class)) {
                Alert alertEerror = new Alert(Alert.AlertType.ERROR);
                alertEerror.setTitle("Fehler!");
                alertEerror.setHeaderText(e.getMessage());
                alertEerror.show();
            }
        }
    }
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        this.applicationView = (ApplicationView)MainControl.getMainControl().getSelectedElementView();
        String applicationNameView = this.applicationView.getApplicationModel().getApplicationName();
        this.applicationName.setText(applicationNameView);
        int applicationIdView = this.applicationView.getApplicationModel().getApplicationId();
    	this.applicationId.setText(Integer.toString(applicationIdView));
    	String applicationDescriptionView = this.applicationView.getApplicationModel().getApplicationDescription();
    	this.applicationDescription.setText(applicationDescriptionView);
    	String applicationCategoryView = this.applicationView.getApplicationModel().getApplicationCategory();
    	this.applicationCategory.setText(applicationCategoryView);
    	String applicationProducerView = this.applicationView.getApplicationModel().getApplicationProducer();
    	this.applicationProducer.setText(applicationProducerView);
    	String applicationManagerView = this.applicationView.getApplicationModel().getApplicationManager();
    	this.applicationManager.setText(applicationManagerView);
    	String applicationDepartmentView = this.applicationView.getApplicationModel().getApplicationDepartment();
    	this.applicationDepartment.setText(applicationDepartmentView);
    	String applicationAdminView = this.applicationView.getApplicationModel().getApplicationAdmin();
    	this.applicationAdmin.setText(applicationAdminView);
	}
}
