package control.fxml;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ApplicationInRelation;
import model.ApplicationModel;
import model.AssociatesModel;
import model.RelationModel;

public class RelationTypeFXMLControl implements Initializable {
	
	@FXML
    private LinkedList<AssociatesModel> associates;
    @FXML
    private Text applications;
    @FXML
    private Text relationType;
    @FXML
    private Button cancel;
    @FXML
    private Button submit;
    @FXML
    private VBox applications_VBox;
    @FXML
    private VBox relationType_VBox;
    @FXML
    private BorderPane border_pane;
    @FXML
	private HBox applicationBox;
    
    @FXML
    void cancel(ActionEvent event) {
        this.border_pane.getScene().getWindow().hide();
    }
    
    @FXML
    void addRelation(ActionEvent event) {
    	try {
            RelationModel relationModel = new RelationModel(this.associates.getFirst().getApplicationInRelation(), this.associates.get(1).getApplicationInRelation());
            for (int i = 2; i < this.associates.size(); ++i) {
                relationModel.getApplications().addLast(this.associates.get(i).getApplicationInRelation());
            }
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
    
    private void showException(Exception e) {
        if (!e.getClass().equals(NullPointerException.class)) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Fehler!");
            error.setHeaderText(e.getMessage());
            error.show();
        }
        e.printStackTrace();
    }
    
    private void addApplication(ApplicationModel applicationModel, int row) {
        Text applicationName = new Text(applicationModel.getApplicationName());
        applicationName.setStyle("-fx-fill: white");
        HBox applicationBox = new HBox(new Node[] { (Node)applicationName });
        applicationBox.setPrefHeight(30.0);
        applicationBox.setMinHeight(30.0);
        applicationBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin((Node)applicationName, new Insets(5.0));
        //applicationBox.prefWidthProperty().bind(this.applicationBox.widthProperty());
        ComboBox<String> relationType = (ComboBox<String>)new ComboBox();
        relationType.getItems().add("Verknüpft mit");
        relationType.getSelectionModel().select(0);
        relationType.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox relationTypeBox = new HBox(new Node[] { (Node)relationType });
        relationTypeBox.setPrefHeight(30.0);
        relationTypeBox.setMinHeight(30.0);
        relationTypeBox.setAlignment(Pos.CENTER);
        HBox.setMargin((Node)relationType, new Insets(1.0));
        HBox.setHgrow((Node)relationType, Priority.ALWAYS);
        if (row % 2 == 0) {
            applicationName.setStyle("-fx-fill: black");
            applicationBox.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 5");
            relationTypeBox.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 5");
        }
        else {
        	applicationBox.setStyle("-fx-background-color: grey");
        	relationTypeBox.setStyle("-fx-background-color: grey");
        }
        this.applications_VBox.getChildren().add((Node)applicationBox);
        this.relationType_VBox.getChildren().add((Node)relationTypeBox);
        this.associates.add(new AssociatesModel(applicationName/*, relationType*/));
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        this.associates = new LinkedList<AssociatesModel>();
        ObservableList<ApplicationModel> applications = MainControl.getMainControl().getSelectedApplications();
        this.addApplication((ApplicationModel)applications.get(0), 1);
        this.addApplication((ApplicationModel)applications.get(1), 2);
	}

}
