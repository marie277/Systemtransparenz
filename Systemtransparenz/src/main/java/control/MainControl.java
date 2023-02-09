package control;

import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import application.Main;
import control.dataExport.FileExportControl;
import control.fxml.ModelFXMLControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.ApplicationModel;
import view.ApplicationView;
import view.ElementView;
import view.ModelView;
import view.RelationView;

//Klasse für die zentrale Steuerung
public class MainControl {
	
	private static MainControl mainControl;
	public static ModelFXMLControl modelFXMLControl;
	private RelationView relationView;
	private ApplicationView applicationView;
	private ObservableList<ApplicationModel> applications;
	
	//Konstruktor
	public MainControl(ModelFXMLControl modelFXMLControl) {
		MainControl.modelFXMLControl = modelFXMLControl;
	}
	
	//Getter-Methode für die zentrale Steuerung
	public static MainControl getMainControl() {
		return MainControl.mainControl;
	}
		
	//Getter-Methode für die Modell-Ansichts-Steuerung
	public static MainControl getMainControl(ModelFXMLControl modelFXMLControl) {
		if(MainControl.mainControl == null) {
			MainControl.mainControl = new MainControl(modelFXMLControl);
		}
		return MainControl.mainControl;
	}
	
	//Getter-Methode für das Modell-Fenster
	public Window getMainWindow() {
		Window mainWindow = MainControl.modelFXMLControl.getTabPane().getScene().getWindow();
		return mainWindow;
	}
	
	//Getter-Methode für die Modell-Ansicht
	public ModelView getModelView() {
		ModelView modelView = MainControl.modelFXMLControl.getModelView();
		return modelView;
	}
	
	//Getter-Methode für eine Elements-Ansicht
	public ObjectProperty<ElementView> getElementView() {
		ObjectProperty<ElementView> elementView = MainControl.modelFXMLControl.getModelView().getSelectedElementProperty();
		return elementView;
	}
	
	//Getter-Methode für eine ausgewählte Elements-Ansicht
	public ElementView getSelectedElementView() {
		ElementView elementView = MainControl.modelFXMLControl.getModelView().getElementView();
        return elementView;
    }
	
	//Getter-Methode für die ausgewählten Anwendungen
	public ObservableList<ApplicationModel> getSelectedApplications(){
		return this.applications;
	}
	
	//Setter-Methode für die ausgewählten Anwendungen
	public void setSelectedApplications(ObservableList<ApplicationModel> applications) {
		this.applications = applications;
	}
	
	//Getter-Methode für eine Beziehungs-Ansicht
	public RelationView getRelationView() {
		return this.relationView;
	}
	
	//Setter-Methode für eine Beziehungsansicht
	public void setRelationView(RelationView relationView) {
		this.relationView = relationView;
	}
	
	//Getter-Methode für eine Anwendungsansicht
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	//Setter-Methode für eine Anwendungsansicht
	public void setApplicationView(ApplicationView applicationView) {
		this.applicationView = applicationView;
	}
	
	//Methode zur Anlage eines neuen Tabs in einer TabPane 
	public void importModelView(ModelView modelView) {
		ScrollPane scrollPane = new ScrollPane(modelView);
		String modelName = modelView.getModelName();
		Tab modelTab = new Tab(modelName, scrollPane);
		modelTab.textProperty().bind((ObservableValue<String>)modelView.getModelNameProperty());
		TabPane modelTabPane = MainControl.modelFXMLControl.getTabPane();
		modelTabPane.getTabs().add(modelTab);
		modelTabPane.getSelectionModel().select(modelTab);
	}
	
	//Methode zum Verlassen der Anwendung
	public void quit() {
		int numberOfTabs = MainControl.modelFXMLControl.getTabPane().getTabs().size();
		if(numberOfTabs > 0) {
			this.closeModel();
			if(numberOfTabs == 0) {
				System.exit(0);
			}
		}
		System.exit(0);
	}
	
	//Methode zum Öffnen eines Modells
	public void openModel() throws SAXException, IOException, ParserConfigurationException {
		FileExportControl.openModel(MainControl.mainControl);
	}

	//Methode zur Erstellung eines neuen Modells, welches benannt werden soll
	public void createModel() {
		ModelView modelView = new ModelView();
		modelView.setPrefWidth(1200.0);
		modelView.setPrefHeight(1800.0);
		ScrollPane scrollPane = new ScrollPane(modelView);
		String modelName = modelView.getModelName();
		Tab tab = new Tab(modelName, scrollPane);
		tab.textProperty().bind((ObservableValue<String>)modelView.getModelNameProperty());
		MainControl.modelFXMLControl.getTabPane().getTabs().add(tab);
		TextInputDialog input = new TextInputDialog("Neues Modell");
		input.setTitle("Modellnamen festlegen");
		input.setHeaderText("Bitte geben Sie den Namen des Modells an.");
		input.setContentText("Hier Modellnamen einfügen.");
		Optional<String> result = input.showAndWait();
		result.ifPresent(name -> {
			modelView.setModelName(name);
		});
	}

	//Methode zum Schließen eines Modells
	public void closeModel() {
		boolean isSaved = MainControl.modelFXMLControl.getModelView().getFileExportControl().isSaved();
		if(isSaved) {
			Tab tab = MainControl.modelFXMLControl.getTabPane().getSelectionModel().getSelectedItem();
			MainControl.modelFXMLControl.getTabPane().getTabs().remove(tab);
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
			Optional<ButtonType> result = alertConfirm.showAndWait();
			result.ifPresent(close -> {
				if(result.get() == justClose){
					Tab tab = MainControl.modelFXMLControl.getTabPane().getSelectionModel().getSelectedItem();
					MainControl.modelFXMLControl.getTabPane().getTabs().remove(tab);
				}
				else if(result.get() == saveAndClose) {
					try {
						this.saveModelAs();
						if(MainControl.modelFXMLControl.getModelView().getFileExportControl().isSaved()) {
							Tab tab = MainControl.modelFXMLControl.getTabPane().getSelectionModel().getSelectedItem();
							MainControl.modelFXMLControl.getTabPane().getTabs().remove(tab);
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
			});
		}
	}

	//Methode zum Speichern von (Änderungen an) einem Modell
	public void saveModel() throws TransformerException, IOException, ParserConfigurationException {
		MainControl.modelFXMLControl.getModelView().getModelControl().saveModel();
	}

	//Methode zum Speichern eines Modells oder Änderung von Speicherort oder -name
	public void saveModelAs() throws TransformerException, IOException, ParserConfigurationException {
		MainControl.modelFXMLControl.getModelView().getModelControl().saveModelAs();
	}

	//Methode zum Speichern eines Modells als Bild-Datei
	public void saveImage() {
		try {
			MainControl.modelFXMLControl.getModelView().getImageExportControl().saveImage();
			Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
			alertConfirm.setTitle("Speichern erfolgreich");
			alertConfirm.setHeaderText("Ihr Model wurde erfolgreich als Bild gespeichert");
			alertConfirm.show();
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

	//Methode zum Öffnen der Benutzeroberfläche für den Import von Anwendungen aus einer PostgreSQL-Datenbank
	public void importApplications() throws IOException, IllegalAccessException {
		if(this.getModelView() != null) {
			Stage applicationImport = new Stage();
	    	Parent loader = FXMLLoader.load(Main.class.getResource("connection.fxml"));
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

	//Methode zum Öffnen der Benutzeroberfläche für die Hilfs-Ansicht bzw. das Tutorial
	public void openHelp() throws IOException {
		Stage help = new Stage();
    	Parent loader = FXMLLoader.load(Main.class.getResource("help.fxml"));
    	Scene scene = new Scene(loader);
    	help.setTitle("Hilfe");
    	help.setScene(scene);
    	help.centerOnScreen();
    	help.show();
	}

	//Methode zum Öffnen der Benutzeroberfläche für das Anlegen einer Beziehung zwischen zwei Anwendungen
	public void addRelation() throws IOException, IllegalAccessException {
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

	//Methode zum Entfernen einer Beziehung aus dem Modell
	public void deleteRelation() throws IllegalAccessException {
		if(this.getModelView() != null) {
			ElementView elementView = MainControl.modelFXMLControl.getModelView().getElementView();
			if(elementView != null) {
				try {
					RelationView relationView = (RelationView)elementView;
					MainControl.modelFXMLControl.getModelView().getModelControl().removeRelationView(relationView);
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
	
	public void addApplication() throws IllegalAccessException, IOException {
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
    
  //Methode zum Hinzufügen einer Anwendung in das Modell
    public void addApplication(int applicationId, String applicationName, String applicationDescription, String applicationCategory, String applicationProducer, String applicationManager, String applicationDepartment, String applicationAdmin) throws IllegalArgumentException {
    	MainControl.modelFXMLControl.getModelView().getModelControl().addApplication(applicationId, applicationName, applicationDescription, applicationCategory, applicationProducer, applicationManager, applicationDepartment, applicationAdmin);
    }

    //Methode zum Löschen einer Anwendung aus dem Modell
	public void deleteApplication() throws IllegalAccessException {
		if(this.getModelView() != null) {
			ElementView elementView = MainControl.modelFXMLControl.getModelView().getElementView();
			if(elementView != null) {
				try {
					ApplicationView applicationView = (ApplicationView)elementView;
					MainControl.modelFXMLControl.getModelView().getModelControl().removeApplicationView(applicationView);
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

	//Methode zum Vergrößern der Ansicht
	public void zoomIn() {
		MainControl.modelFXMLControl.getModelView().getModelControl().zoomIn();
	}

	//Methode zum Verkleinern der Ansicht
	public void zoomOut() {
		MainControl.modelFXMLControl.getModelView().getModelControl().zoomOut();
	}

	//Methode zum Umbenennen des ausgewählten Modells
	public void renameModel() {
		TextInputDialog modelNameInput = new TextInputDialog();
		modelNameInput.setTitle("Modell umbenennen");
		modelNameInput.setHeaderText("Geben Sie einen neuen Namen ein:");
		modelNameInput.setContentText("Hier Namen eingeben");
		Optional<String> result = modelNameInput.showAndWait();
		result.ifPresent(e -> {
			this.getModelView().setModelName(e);
		});
	}

	//Methode zum Öffnen der Benutzeroberfläche für den Import von Anwendungen aus einer PostgreSQL-Datenbank
	public void importData() throws IOException, IllegalAccessException {
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

	public void exportApplications() throws IOException, IllegalAccessException {
		if(this.getModelView() != null) {
			Stage applicationImport = new Stage();
	    	Parent loader = FXMLLoader.load(Main.class.getResource("connectExport.fxml"));
	    	Scene scene = new Scene(loader);
	    	applicationImport.setTitle("Anwendungen exportieren");
	    	applicationImport.setScene(scene);
	    	applicationImport.centerOnScreen();
	    	applicationImport.show();
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}

	public void exportData() throws IOException, IllegalAccessException {
		// TODO Auto-generated method stub
		if(this.getModelView() != null) {
			Stage applicationImport = new Stage();
	    	Parent loader = FXMLLoader.load(Main.class.getResource("export.fxml"));
	    	Scene scene = new Scene(loader);
	    	applicationImport.setTitle("Anwendungen exportieren");
	    	applicationImport.setScene(scene);
	    	applicationImport.centerOnScreen();
	    	applicationImport.show();
		}
		else {
			throw new IllegalAccessException("Achtung! Es wurde noch kein Modell erstellt.");
		}
	}
	
}
