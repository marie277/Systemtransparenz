package control.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import application.Main;
import control.dataExport.FileExportControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.MainModel;
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
	private MenuItem exportApplications;
	@FXML
	private MenuItem exportData;
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
	
	private MainModel mainModel;
	private ObjectProperty<ModelView> modelView;
	private ObjectProperty<ElementView> elementView;
	
	//Getter-Methode für die Daten eines Modells
	public MainModel getMainModel() {
		return this.mainModel;
	}

	//Setter-Methode für die Daten eines Modells
	public void setMainModel(MainModel mainModel) {
		this.mainModel = mainModel;
	}
	
	//Methode zum Verlassen der Anwendung
	@FXML
	public void quit(ActionEvent event) {
		int numberOfTabs = this.getTabPane().getTabs().size();
		if(numberOfTabs > 0) {
			this.closeModel(event);
			if(numberOfTabs == 0) {
				System.exit(0);
			}
		}
		System.exit(0);
	}
	
	//Methode zum Ändern eines Modell-Namens
	@FXML
	public void changeModelName(ActionEvent event) {
		TextInputDialog modelNameInput = new TextInputDialog();
		modelNameInput.setTitle("Modell umbenennen");
		modelNameInput.setHeaderText("Geben Sie einen neuen Namen ein:");
		modelNameInput.setContentText("Hier Namen eingeben");
		modelNameInput.showAndWait();
		this.getModelView().setModelName(modelNameInput.getEditor().getText());
	}
	
	//Methode zum Öffnen eines Modells
	@FXML
	public void openModel(ActionEvent event) throws SAXException, IOException, ParserConfigurationException {
		FileExportControl.openModel();
	}
	
	//Methode zum Erstellen eines Modells
	@FXML
	public void createModel(ActionEvent event) {
		ModelView modelView = new ModelView();
		modelView.setPrefWidth(1200.0);
		modelView.setPrefHeight(1800.0);
		ScrollPane scrollPane = new ScrollPane(modelView);
		String modelName = modelView.getModelName();
		Tab tab = new Tab(modelName, scrollPane);
		tab.textProperty().bind((ObservableValue<String>)modelView.getModelNameProperty());
		this.getTabPane().getTabs().add(tab);
		TextInputDialog input = new TextInputDialog("Neues Modell");
		input.setTitle("Modellnamen festlegen");
		input.setHeaderText("Bitte geben Sie den Namen des Modells an.");
		input.setContentText("Hier Modellnamen einfügen.");
		input.showAndWait();
		modelView.setModelName(input.getEditor().getText());
	}
	
	//Methode zum Schließen eines Modells
	@FXML
	public void closeModel(ActionEvent event) {
		boolean isSaved = this.getModelView().getFileExportControl().isSaved();
		if(isSaved) {
			Tab tab = this.getTabPane().getSelectionModel().getSelectedItem();
			MainModel.modelFXMLControl.getTabPane().getTabs().remove(tab);
		}
		else {
			Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
			alertConfirm.setTitle("Model schließen");
			alertConfirm.setHeaderText("Möchten Sie das Modell wirklich schließen?");
			ButtonType saveAndClose = new ButtonType("Speichern und schließen");
			ButtonType justClose = new ButtonType("Schließen ohne speichern");
			ButtonType cancel = new ButtonType("Abbrechen");
			ButtonType[] buttons = new ButtonType[3];
			buttons[0] = saveAndClose;
			buttons[1] = justClose;
			buttons[2] = cancel;
			alertConfirm.getButtonTypes().setAll(buttons);
			alertConfirm.showAndWait();
			if(alertConfirm.getResult() == justClose) {
				Tab tab = this.getTabPane().getSelectionModel().getSelectedItem();
				this.getTabPane().getTabs().remove(tab);
			}
			else if(alertConfirm.getResult() == saveAndClose) {
				try {
					this.saveModelAs(event);
					if(this.getModelView().getFileExportControl().isSaved()) {
						Tab tab = this.getTabPane().getSelectionModel().getSelectedItem();
						this.getTabPane().getTabs().remove(tab);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					if(!e.getClass().equals(NullPointerException.class)) {
						Alert alertError = new Alert(Alert.AlertType.ERROR);
						alertError.setTitle("Fehler!");
						alertError.setHeaderText(e.getMessage());
						alertError.show();
					}
				}
			}
		}
	}
	
	//Methode zum Speichern eines Modells
	@FXML
	public void saveModel(ActionEvent event) throws TransformerException, IOException, ParserConfigurationException {
		this.getModelView().getModelControl().saveModel();
	}
	
	//Methode zum Speichern eines Modells unter Auswahl des Pfads
	@FXML
	public void saveModelAs(ActionEvent event) throws TransformerException, IOException, ParserConfigurationException {
		this.getModelView().getModelControl().saveModelAs();
	}
	
	//Methode zum Speichern eines Modells als Bild-Datei
	@FXML
	public void saveImage(ActionEvent event) throws IOException {
		this.getModelView().getImageExportControl().saveImage();
		Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
		alertConfirm.setTitle("Speichern erfolgreich");
		alertConfirm.setHeaderText("Ihr Model wurde erfolgreich als Bild gespeichert");
		alertConfirm.show();
	}
	
	//Methode zum Öffnen des Fensters für die Hilfestellung
	@FXML
	public void openHelp(ActionEvent event) throws IOException {
		Stage help = new Stage();
    	Parent loader = FXMLLoader.load(Main.class.getResource("help.fxml"));
    	Scene scene = new Scene(loader);
    	help.setTitle("Hilfe");
    	help.setScene(scene);
    	help.centerOnScreen();
    	help.show();
	}
	
	//Methode zum Öffnen des Fensters für das Anlegen einer Beziehung
	@FXML
	public void addRelation(ActionEvent event) throws IllegalAccessException, IOException {
		if(this.getModelView() != null) {
			int numberOfApplications = this.getModelView().getApplications().size();
			if(numberOfApplications > 1) {
				Stage addRelationStage = new Stage();
				Parent loader = FXMLLoader.load(Main.class.getResource("relation.fxml"));
				Scene addRelationScene = new Scene(loader);
				addRelationStage.initModality(Modality.APPLICATION_MODAL);
				addRelationStage.setTitle("Beziehung hinzufügen");
				addRelationStage.setScene(addRelationScene);
				addRelationStage.centerOnScreen();
				addRelationStage.show();
			}
			else {
				throw new IllegalAccessException("Achtung! Es sind nicht mindestens zwei Anwendungen vorhanden.");
			}
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
	
	//Methode zum Entfernen einer ausgewählten Beziehung
	@FXML
	public void deleteRelation(ActionEvent event) throws IllegalAccessException {
		if(this.getModelView() != null) {
			ElementView elementView = MainModel.modelFXMLControl.getModelView().getElementView();
			if(elementView != null) {
				try {
					RelationView relationView = (RelationView)elementView;
					MainModel.modelFXMLControl.getModelView().getModelControl().removeRelationView(relationView);
				} catch(Exception e) {
					e.printStackTrace();
					if(!e.getClass().equals(NullPointerException.class)) {
						Alert alertError = new Alert(Alert.AlertType.ERROR);
						alertError.setTitle("Fehler!");
						alertError.setHeaderText("Achtung! Das ausgewählte Element ist keine Beziehung.");
						alertError.show();
					}
				}
			}
			else {
				throw new IllegalAccessException("Achtung! Es wurde keine Beziehung ausgewählt.");
			}
		}
		else {
			
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
	
	//Methode zum Anlegen einer neuen Anwendung
	@FXML
	public void addApplication(ActionEvent event) throws IllegalAccessException, IOException {
		if(this.getModelView() != null) {
			Stage addRelationStage = new Stage();
			Parent loader = FXMLLoader.load(Main.class.getResource("application.fxml"));
			Scene addApplicationScene = new Scene(loader);
			addRelationStage.initModality(Modality.APPLICATION_MODAL);
			addRelationStage.setTitle("Anwendung hinzufügen");
			addRelationStage.setScene(addApplicationScene);
			addRelationStage.centerOnScreen();
			addRelationStage.show();
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
	
	//Methode zum Löschen einer ausgewählten Anwendung
	@FXML
	public void deleteApplication(ActionEvent event) throws IllegalAccessException {
		if(this.getModelView() != null) {
			ElementView elementView = MainModel.modelFXMLControl.getModelView().getElementView();
			if(elementView != null) {
				try {
					ApplicationView applicationView = (ApplicationView)elementView;
					MainModel.modelFXMLControl.getModelView().getModelControl().removeApplicationView(applicationView);
				} catch(Exception e) {
					e.printStackTrace();
					if(!e.getClass().equals(NullPointerException.class)) {
						Alert alertError = new Alert(Alert.AlertType.ERROR);
						alertError.setTitle("Fehler!");
						alertError.setHeaderText("Achtung! Das ausgewählte Element ist keine Anwendung.");
						alertError.show();
					}
				}
			}
			else {
				throw new IllegalAccessException("Achtung! Es wurde keine Anwendung ausgewählt.");
			}
		}
		else {
			
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
	
	//Methode zum Vergrößern der Modell-Ansicht
	@FXML
	public void zoomIn(ActionEvent event) {
		this.getModelView().getModelControl().zoomIn();
	}
	
	//Methode zum Verkleinern der Modell-Ansicht
	@FXML
	public void zoomOut(ActionEvent event) {
		this.getModelView().getModelControl().zoomOut();
	}
	
	//Methode zum Öffnen des Fensters für den Anwendungs-Import
	@FXML
	public void importApplications(ActionEvent event) throws IOException, IllegalAccessException {
		if(this.getModelView() != null) {
			Stage applicationImport = new Stage();
	    	Parent loader = FXMLLoader.load(Main.class.getResource("connectImport.fxml"));
	    	Scene scene = new Scene(loader);
	    	applicationImport.setTitle("Anwendungen importieren");
	    	applicationImport.setScene(scene);
	    	applicationImport.centerOnScreen();
	    	applicationImport.show();
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
	
	//Methode zum Öffnen des Fensters für den Anwendungs-Import
	@FXML
	public void importData(ActionEvent event) throws IOException, IllegalAccessException {
		if(this.getModelView() != null) {
			Stage applicationImport = new Stage();
	    	Parent loader = FXMLLoader.load(Main.class.getResource("import.fxml"));
	    	Scene scene = new Scene(loader);
	    	applicationImport.setTitle("Anwendungen importieren");
	    	applicationImport.setScene(scene);
	    	applicationImport.centerOnScreen();
	    	applicationImport.show();
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
	
	//Methode zum Öffnen des Fensters für den Anwendungs-Export
	@FXML
	public void exportApplications(ActionEvent event) throws IOException, IllegalAccessException {
		if(this.getModelView() != null) {
			Stage applicationExport = new Stage();
	    	Parent loader = FXMLLoader.load(Main.class.getResource("connectExport.fxml"));
	    	Scene scene = new Scene(loader);
	    	applicationExport.setTitle("Anwendungen exportieren");
	    	applicationExport.setScene(scene);
	    	applicationExport.centerOnScreen();
	    	applicationExport.show();
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
		
	//Methode zum Öffnen des Fensters für den Anwendungs-Import
	@FXML
	public void exportData(ActionEvent event) throws IOException, IllegalAccessException {
		if(this.getModelView() != null) {
			Stage applicationExport = new Stage();
	    	Parent loader = FXMLLoader.load(Main.class.getResource("export.fxml"));
	    	Scene scene = new Scene(loader);
	    	applicationExport.setTitle("Anwendungen exportieren");
	    	applicationExport.setScene(scene);
	    	applicationExport.centerOnScreen();
	    	applicationExport.show();
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
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
		this.setMainModel(MainModel.getMainModel(this));
        this.model.getSelectionModel().selectedItemProperty().addListener(
    		new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
					if (newValue != null) {
    	                Tab modelTab = (Tab) newValue;
    	                ScrollPane scrollPane = (ScrollPane)modelTab.getContent();
    	                ModelView modelViewTab = (ModelView)scrollPane.getContent();
    	                modelView.set(modelViewTab);
    	            }
    	            else {
    	                modelView.set(null);
    	            }
				}
    		});
        this.modelView = new SimpleObjectProperty<ModelView>(this, "selected", null);
        this.modelView.addListener(
    		new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
					if (newValue != null) {
		            	elementView.unbind();
		                elementView.bind(((ModelView) newValue).getSelectedElementProperty());
		                ContextMenu menu = new ContextMenu();
		        		model.setContextMenu(menu);
		        		setDisable(false);
		            }
		            else {
		                elementView.unbind();
		                elementView.set(null);
		                model.setContextMenu(null);
		        		setDisable(true);
		            }
				}
    		});
        this.elementView = new SimpleObjectProperty<ElementView>(this, "selected", null);
        this.elementView.addListener(
    		new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
					try {
		                if (newValue == null) {
		                    details.setContent(new BorderPane());
		                }
		                else if (newValue.getClass().equals(ApplicationView.class)) {
		                    Parent parent = (Parent)FXMLLoader.load(Main.class.getResource("editApplication.fxml"));
		                    details.setContent(parent);
		                }
		                else if (newValue.getClass().equals(RelationView.class)) {
		                    Parent parent = (Parent)FXMLLoader.load(Main.class.getResource("editRelation.fxml"));
		                    details.setContent(parent);
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
				}
    		});
        this.model.setContextMenu(null);
		this.setDisable(true);
	}

}
