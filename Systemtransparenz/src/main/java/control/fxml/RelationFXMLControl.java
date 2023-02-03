package control.fxml;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import model.ApplicationInRelation;
import model.ApplicationModel;
import model.AssociatesModel;
import model.RelationModel;
import view.ApplicationView;
import view.ModelView;
import view.RelationView;

//Klasse zur Steuerung der Erstellung einer Beziehung, implementiert Interface Initializable
public class RelationFXMLControl implements Initializable{
	
	private ModelView modelView;
	@FXML
    private ScrollPane scrollPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TableColumn<ApplicationModel, String> availableApplications;
    @FXML
    private Button select;
    @FXML
    private Button remove;
    @FXML
    private ComboBox<String> relationTypes;
    @FXML
    private ComboBox<String> incomingRelation;
    @FXML
    private TableColumn<ApplicationModel, String> selectedApplications;
    @FXML
    private Button cancel;
    @FXML
    private Button submit;
    @FXML
    private LinkedList<AssociatesModel> associates;
    
    //Methode zum Schließen des Fensters zur Anlage einer Beziehung
    @FXML
    private void cancel(ActionEvent event) {
        this.scrollPane.getScene().getWindow().hide();
    }
    
    //Methode zum Bestätigen der erstellten Beziehung
    @FXML
    private void submit(ActionEvent event) throws Exception {
    	MainControl.getMainControl().setSelectedApplications((ObservableList<ApplicationModel>)this.selectedApplications.getTableView().getItems());
    	if (MainControl.getMainControl().getSelectedApplications().size() == 2) {
    		ApplicationInRelation firstApplication = this.associates.get(0).getApplicationInRelation();
    		ApplicationInRelation secondApplication = this.associates.get(1).getApplicationInRelation();
    		String relationType = this.relationTypes.getValue();
    		String arrowDirection = this.incomingRelation.getValue();
    		if((!relationType.equals("Verknüpft mit") && arrowDirection.equals("keine")) || (relationType.equals("Verknüpft mit") && !arrowDirection.equals("keine"))) {
    			this.borderPane.getScene().getWindow().hide();
    			Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Fehler!");
                alertError.setHeaderText("Der Beziehungstyp Verknüpft mit erfordert keine eingehende Anwendung, die beiden übrigen schon.");
                alertError.show();
    		}
	    	else {
	    		boolean arrowIncoming = arrowDirection.equals(firstApplication.getApplicationView().getApplicationModel().getApplicationName());
	    		RelationModel relationModel = new RelationModel(firstApplication, secondApplication, relationType, arrowIncoming);
	    		LinkedList<RelationView> relations = MainControl.getMainControl().getModelView().getRelations();
	    		if(relations.size() == 0) {
		    		MainControl.getMainControl().getModelView().getModelControl().addRelationView(relationModel);
		            LinkedList<ApplicationInRelation> applications = MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications();
		            double x = 0.0;
		            double y = 0.0;
		            for (ApplicationInRelation applicationInRelation : applications) {
		                x += applicationInRelation.getApplicationView().getElementRegion().getLayoutX();
		                y += applicationInRelation.getApplicationView().getElementRegion().getLayoutY();
		            }
		            x /= 2.0;
		            y /= 2.0;
		            MainControl.getMainControl().getModelView().getRelations().getLast().move(x, y);
		            this.borderPane.getScene().getWindow().hide();
	    		}
	    		else {
		    		boolean isDuplicate = false;
		    		for(int i = 0; i < relations.size(); i++) {
		    			RelationView relationView = MainControl.getMainControl().getModelView().getRelations().get(i);
		            	if((relationView.getRelationModel().getApplications().getFirst().getApplicationView().getApplicationModel().getApplicationName().equals(relationModel.getApplications().getFirst().getApplicationView().getApplicationModel().getApplicationName())
		            			&& relationView.getRelationModel().getApplications().get(1).getApplicationView().getApplicationModel().getApplicationName().equals(relationModel.getApplications().get(1).getApplicationView().getApplicationModel().getApplicationName()))
		            			|| (relationView.getRelationModel().getApplications().getFirst().getApplicationView().getApplicationModel().getApplicationName().equals(relationModel.getApplications().get(1).getApplicationView().getApplicationModel().getApplicationName())
		            			&& relationView.getRelationModel().getApplications().get(1).getApplicationView().getApplicationModel().getApplicationName().equals(relationModel.getApplications().getFirst().getApplicationView().getApplicationModel().getApplicationName()))) {
		            		isDuplicate = true;
		            		this.borderPane.getScene().getWindow().hide();
		            		Alert alertError = new Alert(Alert.AlertType.ERROR);
		                    alertError.setTitle("Fehler!");
		                    alertError.setHeaderText("Die ausgewählte Beziehung ist bereits vorhanden.");
		                    alertError.show();
		            	}
		            	else {
		            		isDuplicate = false;
		            	}
		            }
			    	if(!isDuplicate) {
			    		MainControl.getMainControl().getModelView().getModelControl().addRelationView(relationModel);
			            LinkedList<ApplicationInRelation> applications = MainControl.getMainControl().getModelView().getRelations().getLast().getRelationModel().getApplications();
			            double x = 0.0;
			            double y = 0.0;
			            for (ApplicationInRelation applicationInRelation : applications) {
			                x += applicationInRelation.getApplicationView().getElementRegion().getLayoutX();
			                y += applicationInRelation.getApplicationView().getElementRegion().getLayoutY();
			            }
			            x /= 2.0;
			            y /= 2.0;
			            MainControl.getMainControl().getModelView().getRelations().getLast().move(x, y);
			            this.borderPane.getScene().getWindow().hide();
			    	}	
			    }
			}
    	}
    	else {
    		Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Fehler!");
            alertError.setHeaderText("Es müssen genau zwei Anwendungen ausgewählt werden.");
            alertError.show();
    	}
    }
    
    //Methode zur Auswahl von einer verfügbaren Anwendung als Teil der zu erstellenden Beziehung
    @FXML
    private void select(ActionEvent event) throws Exception {
    	ApplicationModel applicationModel = this.availableApplications.getTableView().getSelectionModel().getSelectedItem();
        if (applicationModel != null) {
            this.availableApplications.getTableView().getItems().remove(applicationModel);
            this.selectedApplications.getTableView().getItems().add(applicationModel);
            this.incomingRelation.getItems().addAll(applicationModel.getApplicationName());
            String applicationName = applicationModel.getApplicationName();
            Text text = new Text(applicationName);
            AssociatesModel associatesModel = new AssociatesModel(text);
            this.associates.add(associatesModel);
        }
    }
    
    //Methode zum Entfernen einer Anwendung aus der Auswahl
    @FXML
    private void remove(ActionEvent event) throws Exception {
    	ApplicationModel applicationModel = this.selectedApplications.getTableView().getSelectionModel().getSelectedItem();
        if (applicationModel != null) {
            this.selectedApplications.getTableView().getItems().remove(applicationModel);
            this.availableApplications.getTableView().getItems().add(applicationModel);  
        }
    }
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.modelView = MainControl.getMainControl().getModelView();
		this.associates = new LinkedList<AssociatesModel>();
		this.relationTypes.getItems().addAll("Verknüpft mit", "Hat", "Nutzt");
		this.relationTypes.getSelectionModel().select(0);
		this.incomingRelation.getItems().add("keine");
		this.incomingRelation.getSelectionModel().select(0);
        this.availableApplications.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
		this.selectedApplications.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
        ObservableList<ApplicationModel> applications = FXCollections.observableArrayList();
        for (ApplicationView applicationView : this.modelView.getApplications()) {
            applications.add(applicationView.getApplicationModel());
        }
        this.availableApplications.getTableView().setItems(applications);
        this.availableApplications.getTableView().getSelectionModel().select(0);
        this.availableApplications.getTableView().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && this.availableApplications.getTableView().getSelectionModel().getSelectedItem() != null) {
                try {
					this.select(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });
        this.selectedApplications.getTableView().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && this.selectedApplications.getTableView().getSelectionModel().getSelectedItem() != null) {
                try {
					this.remove(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });
	}

}
