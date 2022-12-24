package control.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.ApplicationModel;
import view.ModelView;

public class ApplicationFXMLControl implements Initializable{

	private ModelView modelView;
	@FXML
	private TitledPane titled_pane;
	@FXML
	private TextField applicationid;
	@FXML
	private TextField applicationname;
	@FXML
	private TextField description;
	@FXML
	private TextField category;
	@FXML
	private TextField producer;
	@FXML
	private TextField applicationmanager;
	@FXML
	private TextField admin;
	@FXML
	private Button cancel;
	@FXML
	private Button submit;
	@FXML
	private HBox applicationBox;
	@FXML
	private LinkedList<ApplicationModel> applications;
	
	/*@FXML
    void cancel(ActionEvent event) {
        this.titled_pane.getScene().getWindow().hide();
    }
	
	@FXML
	void submit(ActionEvent event) throws IOException {
		this.createApplication();
	}
	
	private void createApplication() throws IOException {
		String applicationName = this.applicationname.getText();
		MainControl.getMainControl().createNewApplication(applicationName);
	}
	
	private void addApplicationToModelView(ApplicationModel applicationModel, int zeile) {
        Text applicationName = new Text(applicationModel.getApplicationName());
        applicationName.setStyle("-fx-fill: white");
        HBox applicationBox = new HBox(new Node[] { (Node)applicationName });
        applicationBox.setPrefHeight(30.0);
        applicationBox.setMinHeight(30.0);
        applicationBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin((Node)applicationName, new Insets(5.0));
        applicationBox.prefWidthProperty().bind((ObservableValue)this.applicationBox.widthProperty());
        if (zeile % 2 == 0) {
        	applicationName.setStyle("-fx-fill: black");
        	applicationBox.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 5");
        }
        else {
        	applicationBox.setStyle("-fx-background-color: grey");
        }
    }
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
