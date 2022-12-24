package control.fxml;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.ApplicationModel;
import view.ApplicationView;
import view.ModelView;

public class RelationFXMLControl implements Initializable{
	
	private ModelView modelView;
	@FXML
    private ScrollPane scroll_pane;
    @FXML
    private BorderPane border_pane;
    @FXML
    private TableColumn<ApplicationModel, String> available_applications;
    @FXML
    private Button select;
    @FXML
    private Button remove;
    @FXML
    private TableColumn<ApplicationModel, String> selected_applications;
    @FXML
    private Button cancel;
    @FXML
    private Button submit;
    
    @FXML
    void cancel(ActionEvent event) {
        this.scroll_pane.getScene().getWindow().hide();
    }
    
    @FXML
    void submit(ActionEvent event) {
        this.setSelectedApplications();
        if (MainControl.getMainControl().getSelectedApplications().size() == 2) {
            try {
                this.scroll_pane.setContent((Node)this.loadNextFXMLFile());
            }
            catch (IOException e) {
                this.showException(e);
            }
        }
        else {
            this.showException(new Exception("Es müssen genau zwei Anwendungen ausgewählt werden."));
        }
    }
    
    private Parent loadNextFXMLFile() throws IOException {
        Parent nextFXMLFile = (Parent)FXMLLoader.load(Main.class.getResource("relation2.fxml"));
        return nextFXMLFile;
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
    
    private void setSelectedApplications() {
        MainControl.getMainControl().setApplications((ObservableList<ApplicationModel>)this.selected_applications.getTableView().getItems());
    }
    
    @FXML
    void select(ActionEvent event) {
        if (this.available_applications.getTableView().getSelectionModel().getSelectedItem() != null) {
            ApplicationModel applicationModel = (ApplicationModel)this.available_applications.getTableView().getSelectionModel().getSelectedItem();
            this.available_applications.getTableView().getItems().remove(applicationModel);
            this.selected_applications.getTableView().getItems().add(applicationModel);
        }
    }
    
    @FXML
    void remove(ActionEvent event) {
        if (this.selected_applications.getTableView().getSelectionModel().getSelectedItem() != null) {
        	ApplicationModel applicationModel = (ApplicationModel)this.selected_applications.getTableView().getSelectionModel().getSelectedItem();
            this.selected_applications.getTableView().getItems().remove(applicationModel);
            this.available_applications.getTableView().getItems().add(applicationModel);
        }
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.modelView = MainControl.getMainControl().getModelView();
        this.available_applications.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
		this.selected_applications.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
        ObservableList<ApplicationModel> applications = FXCollections.observableArrayList();
        for (ApplicationView aV : this.modelView.getApplications()) {
            applications.add(aV.getApplicationModel());
            
        }
        
        this.available_applications.getTableView().setItems((ObservableList<ApplicationModel>)applications);
        this.available_applications.getTableView().getSelectionModel().select(0);
        this.available_applications.getTableView().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && this.available_applications.getTableView().getSelectionModel().getSelectedItem() != null) {
                this.select(null);
            }
        });
        this.selected_applications.getTableView().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && this.selected_applications.getTableView().getSelectionModel().getSelectedItem() != null) {
                this.remove(null);
            }
        });
        //HBox.setHgrow((Node)this.available_applications.getTableView(), Priority.ALWAYS);
        //HBox.setHgrow((Node)this.selected_applications.getTableView(), Priority.ALWAYS);
	}

}
