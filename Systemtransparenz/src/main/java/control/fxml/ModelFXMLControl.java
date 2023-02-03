package control.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import control.MainControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import view.ApplicationView;
import view.ElementView;
import view.ModelView;
import view.RelationView;

//Klasse zur Steuerung der Erstellung und Bearbeitung eines Modells, implementiert Interface Initializable
public class ModelFXMLControl implements Initializable{

	@FXML
	private MenuItem createModel;
	@FXML
	private MenuItem openModel;
	@FXML
	private MenuItem saveModel;
	@FXML
	private MenuItem saveModelAs;
	@FXML
	private MenuItem renameModel;
	@FXML
	private MenuItem closeModel;
	@FXML
	private MenuItem saveImage;
	@FXML
	private MenuItem quit;
	@FXML
	private MenuItem importApplications;
	@FXML
	private MenuItem importData;
	@FXML
	private MenuItem addRelation;
	@FXML
	private MenuItem deleteRelation;
	@FXML
	private MenuItem addApplication;
	@FXML
	private MenuItem deleteApplication;
	@FXML
	private MenuItem zoomIn;
	@FXML
	private MenuItem zoomOut;
	@FXML
	private MenuItem openHelp;
	@FXML
	private Button createApplication;
	@FXML
	private Button removeApplication;
	@FXML
	private Button createRelation;
	@FXML
	private Button removeRelation;
	@FXML
	private TabPane model;
	@FXML
	private ScrollPane details;
	
	private MainControl mainControl;
	private ObjectProperty<ModelView> modelView;
	private ObjectProperty<ElementView> elementView;
	
	//Methode zum Verlassen der Anwendung
	@FXML
	public void quit(ActionEvent event) {
		this.mainControl.quit();
	}
	
	//Methode zum Ändern eines Modell-Namens
	@FXML
	public void renameModel(ActionEvent event) {
		this.mainControl.renameModel();
	}
	
	//Methode zum Öffnen eines Modells
	@FXML
	public void openModel(ActionEvent event) {
		try {
			this.mainControl.openModel();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Erstellen eines Modells
	@FXML
	public void createModel(ActionEvent event) {
		this.mainControl.createModel();
	}
	
	//Methode zum Schließen eines Modells
	@FXML
	public void closeModel(ActionEvent event) {
		this.mainControl.closeModel();
	}
	
	//Methode zum Speichern eines Modells
	@FXML
	public void saveModel(ActionEvent event) {
		try {
			this.mainControl.saveModel();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Speichern eines Modells unter Auswahl des Pfads
	@FXML
	public void saveModelAs(ActionEvent event) {
		try {
			this.mainControl.saveModelAs();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Speichern eines Modells als Bild-Datei
	@FXML
	public void saveImage(ActionEvent event) {
		try {
			this.mainControl.saveImage();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Öffnen des Fensters für den Anwendungs-Import
	@FXML
	public void openApplicationImport(ActionEvent event) {
		try {
			this.mainControl.importApplications();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Öffnen des Fensters für die Hilfestellung
	@FXML
	public void openHelp(ActionEvent event) {
		try {
			this.mainControl.openHelp();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Öffnen des Fensters für das Anlegen einer Beziehung
	@FXML
	public void addRelation(ActionEvent event) {
		try {
			this.mainControl.addRelation();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Entfernen einer ausgewählten Beziehung
	@FXML
	public void deleteRelation(ActionEvent event) {
		try {
			this.mainControl.deleteRelation();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Anlegen einer neuen Anwendung
	@FXML
	public void addApplication(ActionEvent event) {
		try {
			this.mainControl.addApplication();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Löschen einer ausgewählten Anwendung
	@FXML
	public void deleteApplication(ActionEvent event) {
		try {
			this.mainControl.deleteApplication();
		} catch(Exception e) {
			e.printStackTrace();
			if(!e.getClass().equals(NullPointerException.class)) {
				Alert alertError = new Alert(Alert.AlertType.ERROR);
				alertError.setTitle("Fehler!");
				alertError.setHeaderText(e.getMessage());
				alertError.show();
			}
		}
	}
	
	//Methode zum Vergrößern der Modell-Ansicht
	@FXML
	public void zoomIn(ActionEvent event) {
		this.mainControl.zoomIn();
	}
	
	//Methode zum Verkleinern der Modell-Ansicht
	@FXML
	public void zoomOut(ActionEvent event) {
		this.mainControl.zoomOut();
	}
	
	//Methode zum Öffnen des Fensters für den Anwendungs-Import
	@FXML
	public void importApplications(ActionEvent event) throws IOException, IllegalAccessException {
		this.mainControl.importApplications();
	}
	
	//Methode zum Öffnen des Fensters für den Anwendungs-Import
	@FXML
	public void importData(ActionEvent event) throws IOException, IllegalAccessException {
		this.mainControl.importData();
	}
	
	//Getter-Methode für die Fläche des aktuellen Modells 
	public TabPane getTabPane(){
		return this.model;
	}
	
	//Getter-Methode für die Modell-Ansicht
	public ModelView getModelView() {
		return this.modelView.get();
	}

	//Methode zum Deaktivieren von Menüpunkten
	private void setDisable(boolean b) {
		this.saveModel.setDisable(b);
		this.saveModelAs.setDisable(b);
		this.renameModel.setDisable(b);
		this.closeModel.setDisable(b);
		this.saveImage.setDisable(b);
		this.importApplications.setDisable(b);
		this.addRelation.setDisable(b);
		this.deleteRelation.setDisable(b);
		this.addApplication.setDisable(b);
		this.deleteApplication.setDisable(b);
		this.zoomIn.setDisable(b);
		this.zoomOut.setDisable(b);
	}
	
	//Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.mainControl = MainControl.getMainControl(this);
        this.model.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Tab modelTab = newValue;
                ScrollPane scrollPane = (ScrollPane) modelTab.getContent();
                ModelView modelViewTab = (ModelView) scrollPane.getContent();
                this.modelView.set(modelViewTab);
            }
            else {
                this.modelView.set(null);
            }
        });
        this.modelView = new SimpleObjectProperty<ModelView>(this, "selected", null);
        this.modelView.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
            	this.elementView.unbind();
                this.elementView.bind(newValue.getSelectedElementProperty());
                ContextMenu menu = new ContextMenu();
        		this.model.setContextMenu(menu);
        		this.setDisable(false);
            }
            else {
                this.elementView.unbind();
                this.elementView.set(null);
                this.model.setContextMenu(null);
        		this.setDisable(true);
            }
        });
        this.elementView = new SimpleObjectProperty<ElementView>(this, "selected", null);
        this.elementView.addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue == null) {
                    this.details.setContent(new BorderPane());
                }
                else if (newValue.getClass().equals(ApplicationView.class)) {
                    Parent parent = (Parent)FXMLLoader.load(Main.class.getResource("editApplication.fxml"));
                    this.details.setContent(parent);
                }
                else if (newValue.getClass().equals(RelationView.class)) {
                    Parent parent = (Parent)FXMLLoader.load(Main.class.getResource("editRelation.fxml"));
                    this.details.setContent(parent);
                }
            } catch (Exception e) {
            	e.printStackTrace();
    			if(!e.getClass().equals(NullPointerException.class)) {
    				Alert alertError = new Alert(Alert.AlertType.ERROR);
    				alertError.setTitle("Fehler!");
    				alertError.setHeaderText(e.getMessage());
    				alertError.show();
    			}
            }
        });
        this.model.setContextMenu(null);
		this.setDisable(true);
	}

}
