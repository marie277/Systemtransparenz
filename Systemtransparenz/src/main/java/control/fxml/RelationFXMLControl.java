package control.fxml;

import java.io.IOException;
import java.net.URL;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import model.ApplicationInRelation;
import model.ApplicationModel;
import model.AssociatesModel;
import model.RelationModel;
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
    private ComboBox<String> relationtype;
    @FXML
    private TableColumn<ApplicationModel, String> selected_applications;
    @FXML
    private Button cancel;
    @FXML
    private Button submit;
    @FXML
    private LinkedList<AssociatesModel> associates;
    
    @FXML
    void cancel(ActionEvent event) {
        this.scroll_pane.getScene().getWindow().hide();
    }
    
    @FXML
    void submit(ActionEvent event) {
    	this.setSelectedApplications();
    	if (MainControl.getMainControl().getSelectedApplications().size() == 2) {
            this.addRelation();
        }
        else {
            this.showException(new Exception("Es müssen genau zwei Anwendungen ausgewählt werden."));
        }
        
        /*if (MainControl.getMainControl().getSelectedApplications().size() == 2) {
            try {
                this.scroll_pane.setContent((Node)this.loadNextFXMLFile());
            }
            catch (IOException e) {
                this.showException(e);
            }
        }
        else {
            this.showException(new Exception("Es müssen genau zwei Anwendungen ausgewählt werden."));
        }*/
    }
    
    private void addRelation() {
    	try {
            RelationModel relationModel = new RelationModel(this.associates.getFirst().getApplicationInRelation(), this.associates.get(1).getApplicationInRelation());
            /*for (int i = 2; i < this.associates.size(); ++i) {
                relationModel.getApplications().addLast(this.associates.get(i).getApplicationInRelation());
            }*/
            MainControl.getMainControl().getModelView().getModelControl().addRelationView(relationModel);
            double x = 0.0;
            double y = 0.0;
            if (MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications().size() == 2
            		&& MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications().getFirst().getApplicationView().equals(MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications().getLast().getApplicationView())) {
                x = MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications().getFirst().getApplicationView().getElementRegion().getLayoutX() + 50.0;
                y = MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications().getFirst().getApplicationView().getElementRegion().getLayoutY() + 50.0;
            }
            else {
                for (ApplicationInRelation applicationInRelation : MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications()) {
                    x += applicationInRelation.getApplicationView().getElementRegion().getLayoutX();
                    y += applicationInRelation.getApplicationView().getElementRegion().getLayoutY();
                }
                x /= MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications().size();
                y /= MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications().size();
            }
            MainControl.getMainControl().getModelView().getRelations().getLast().move(x, y);
            this.border_pane.getScene().getWindow().hide();
        }
        catch (Exception e) {
            this.showException(e);
        }
    }
    
    /*private Parent loadNextFXMLFile() throws IOException {
        Parent nextFXMLFile = (Parent)FXMLLoader.load(Main.class.getResource("relation2.fxml"));
        return nextFXMLFile;
    }*/
    
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
            this.associates.add(new AssociatesModel(new Text(applicationModel.getApplicationName())));
        }
    }
    
    @FXML
    void remove(ActionEvent event) throws Exception {
        if (this.selected_applications.getTableView().getSelectionModel().getSelectedItem() != null) {
        	ApplicationModel applicationModel = (ApplicationModel)this.selected_applications.getTableView().getSelectionModel().getSelectedItem();
            this.selected_applications.getTableView().getItems().remove(applicationModel);
            this.available_applications.getTableView().getItems().add(applicationModel);
            for(AssociatesModel aM : this.associates) {
            	if(aM.getApplicationInRelation().getApplicationView().getApplicationModel().getApplicationName().equals(applicationModel.getApplicationName())) {
            		this.associates.remove(aM);
            	}
            }
        }
    }
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.associates = new LinkedList<AssociatesModel>();
		this.relationtype.getItems().add("Verknüpft mit");
		relationtype.getSelectionModel().select(0);
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
                try {
					this.remove(null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
	}

}
