package control.fxml;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import control.edit.ApplicationInRelation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.ApplicationModel;
import model.MainModel;
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
    private LinkedList<ApplicationInRelation> applicationsInRelation;
    
    //Methode zum Schließen des Fensters zur Anlage einer Beziehung
    @FXML
    private void cancel(ActionEvent event) {
        this.scrollPane.getScene().getWindow().hide();
    }
    
    //Methode zum Bestätigen der erstellten Beziehung
    @FXML
    private void submit(ActionEvent event) throws Exception {
    	MainModel.getMainModel().setSelectedApplications((ObservableList<ApplicationModel>)this.selectedApplications.getTableView().getItems());
    	if (MainModel.getMainModel().getSelectedApplications().size() == 2) {
    		ApplicationInRelation firstApplication = this.applicationsInRelation.get(0);
    		ApplicationInRelation secondApplication = this.applicationsInRelation.get(1);
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
	    		LinkedList<RelationView> relations = MainModel.modelFXMLControl.getModelView().getRelations();
	    		if(relations.size() == 0) {
	    			this.addRelationView(relationModel, 0.0, 0.0);
	    		}
	    		else {
		    		boolean isDuplicate = false;
		    		for(int i = 0; i < relations.size(); i++) {
		    			RelationView relationView = MainModel.modelFXMLControl.getModelView().getRelations().get(i);
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
			    		this.addRelationView(relationModel, 0.0, 0.0);
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
    
    //Methode zum Hinzufügen der Beziehung zur Modell-Steuerung und Platzierung in der Modell-Ansicht
    private void addRelationView(RelationModel relationModel, double x, double y) {
    	MainModel.modelFXMLControl.getModelView().getModelControl()
    	.addRelationView(relationModel);
        LinkedList<ApplicationInRelation> applications = MainModel.modelFXMLControl
        		.getModelView().getRelations().getLast().getRelationModel().getApplications();
        for(ApplicationInRelation applicationInRelation : applications) {
            x += applicationInRelation.getApplicationView().getElementRegion().getLayoutX();
            y += applicationInRelation.getApplicationView().getElementRegion().getLayoutY();
        }
        x /= 2.0;
        y /= 2.0;
        MainModel.modelFXMLControl.getModelView().getRelations().getLast().move(x, y);
        this.borderPane.getScene().getWindow().hide();
    }
    
    //Methode zur Auswahl von einer verfügbaren Anwendung als Teil der zu erstellenden Beziehung
    @FXML
    private void select(ActionEvent event) throws Exception {
    	ApplicationModel applicationModel = this.availableApplications.getTableView().getSelectionModel().getSelectedItem();
        if(applicationModel != null) {
            this.availableApplications.getTableView().getItems().remove(applicationModel);
            this.selectedApplications.getTableView().getItems().add(applicationModel);
            this.incomingRelation.getItems().addAll(applicationModel.getApplicationName());
            ApplicationView applicationView = null;
            LinkedList<ApplicationView> applicationViews = MainModel.modelFXMLControl.getModelView().getApplications();
            for(ApplicationView aV : applicationViews) {
                if (aV.getApplicationModel().equals(applicationModel)) {
                    applicationView = aV;
                }
            }
            ApplicationInRelation applicationInRelation;
            if (applicationView != null) {
            	applicationInRelation = new ApplicationInRelation(applicationView);
            	this.applicationsInRelation.add(applicationInRelation);
            }
            else {
            	throw new Exception("Achtung! Es sind keine Anwendungen vorhanden.");
            }
        }
    }
    
    //Methode zum Entfernen einer Anwendung aus der Auswahl
    @FXML
    private void remove(ActionEvent event) throws Exception {
    	ApplicationModel applicationModel = this.selectedApplications.getTableView().getSelectionModel().getSelectedItem();
        if(applicationModel != null) {
            this.selectedApplications.getTableView().getItems().remove(applicationModel);
            this.availableApplications.getTableView().getItems().add(applicationModel);  
        }
    }
    
    //Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.modelView = MainModel.modelFXMLControl.getModelView();
		this.applicationsInRelation = new LinkedList<ApplicationInRelation>();
		this.relationTypes.getItems().addAll("Verknüpft mit", "Hat", "Nutzt");
		this.relationTypes.getSelectionModel().select(0);
		this.incomingRelation.getItems().add("keine");
		this.incomingRelation.getSelectionModel().select(0);
        this.availableApplications.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
		this.selectedApplications.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("applicationName"));
        ObservableList<ApplicationModel> applications = FXCollections.observableArrayList();
        for(ApplicationView applicationView : this.modelView.getApplications()) {
            applications.add(applicationView.getApplicationModel());
        }
        this.availableApplications.getTableView().setItems(applications);
        this.availableApplications.getTableView().getSelectionModel().select(0);
        this.availableApplications.getTableView().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getClickCount() == 2 && availableApplications.getTableView().getSelectionModel().getSelectedItem() != null) {
	                try {
						select(null);
					} catch(Exception e) {
						e.printStackTrace();
					}
	            }
			}
        });
        this.selectedApplications.getTableView().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getClickCount() == 2 && selectedApplications.getTableView().getSelectionModel().getSelectedItem() != null) {
	                try {
	                	remove(null);
					} catch(Exception e) {
						e.printStackTrace();
					}
	            }
			}
        });
	}

}
