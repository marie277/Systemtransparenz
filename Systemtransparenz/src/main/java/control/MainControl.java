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

public class MainControl {
	private static MainControl mainControl;
	private static ModelFXMLControl modelFXMLControl;
	private ObservableList<ApplicationModel> applications;
	private RelationView relationView;
	private ApplicationView applicationView;
	
	public MainControl(ModelFXMLControl modelFXMLControl) {
		MainControl.modelFXMLControl = modelFXMLControl;
	}
	
	public static MainControl getMainControl(ModelFXMLControl modelFXMLControl) {
		if(MainControl.mainControl == null) {
			MainControl.mainControl = new MainControl(modelFXMLControl);
		}
		return MainControl.mainControl;
	}
	
	public static MainControl getMainControl() {
		return MainControl.mainControl;
	}

	public void importModelView(ModelView modelView) {
		final ScrollPane scrollPane = new ScrollPane(modelView);
		final Tab modelTab = new Tab(modelView.getModelName(), scrollPane);
		modelTab.textProperty().bind((ObservableValue<String>)modelView.modelName());
		TabPane modelTabPane = MainControl.modelFXMLControl.getTabPane();
		modelTabPane.getTabs().add(modelTab);
		modelTabPane.getSelectionModel().select(modelTab);
	}
	
	public Window getMainWindow() {
		return MainControl.modelFXMLControl.getTabPane().getScene().getWindow();
	}
	
	public ModelView getModelView() {
		return MainControl.modelFXMLControl.getModelView();
	}
	
	public ObjectProperty<ElementView> getElementView() {
		return MainControl.modelFXMLControl.getModelView().elementViewProperty();
	}
	
	public ObservableList<ApplicationModel> getSelectedApplications(){
		return this.applications;
	}
	
	public void setApplications(ObservableList<ApplicationModel> applications) {
		this.applications = applications;
	}
	
	public RelationView getRelationView() {
		return this.relationView;
	}
	
	public void setRelation(RelationView relationView) {
		this.relationView = relationView;
	}
	
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	public void setApplicationView(ApplicationView applicationView) {
		this.applicationView = applicationView;
	}
	
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

	public void openModel() throws SAXException, IOException, ParserConfigurationException {
		FileExportControl.openModel(MainControl.mainControl);
	}

	public void createModel() {
		ModelView modelView = new ModelView();
		modelView.setPrefHeight(2000.0);
		modelView.setPrefWidth(2000.0);
		ScrollPane scrollPane = new ScrollPane(modelView);
		Tab tab = new Tab(modelView.getModelName(), scrollPane);
		tab.textProperty().bind((ObservableValue<String>)modelView.modelName());
		MainControl.modelFXMLControl.getTabPane().getTabs().add(tab);
		final TextInputDialog input = new TextInputDialog("Neues Modell");
		input.setTitle("Modellnamen festlegen");
		input.setHeaderText("Bitte geben Sie den Namen des Modells an.");
		input.setContentText("Hier Modellnamen einf�gen.");
		final Optional<String> result = (Optional<String>)input.showAndWait();
		result.ifPresent(name -> modelView.setModelName(name));
	}

	public void closeModel() {
		if(MainControl.modelFXMLControl.getModelView().getFileExportControl().isSaved()) {
			Tab tab = MainControl.modelFXMLControl.getTabPane().getSelectionModel().getSelectedItem();
			MainControl.modelFXMLControl.getTabPane().getTabs().remove(tab);
		}
		else {
			Alert a = new Alert(Alert.AlertType.CONFIRMATION);
			a.setTitle("Model schlie�en");
			a.setHeaderText("M�chten Sie das Modell wirklich schlie�en?");
			ButtonType saveAndClose = new ButtonType("Speichern und schlie�en");
			ButtonType justClose = new ButtonType("Schlie�en ohne speichern");
			ButtonType cancel = new ButtonType("Abbrechen");
			a.getButtonTypes().setAll(new ButtonType[] {saveAndClose, justClose, cancel});
			Optional<ButtonType> result = a.showAndWait();
			result.ifPresent(e -> {
				if(result.get() == saveAndClose) {
					try {
						this.saveModelAs();
						if(MainControl.modelFXMLControl.getModelView().getFileExportControl().isSaved()) {
							Tab tab = MainControl.modelFXMLControl.getTabPane().getSelectionModel().getSelectedItem();
							MainControl.modelFXMLControl.getTabPane().getTabs().remove(tab);
						}
					}
					catch(Exception e1) {
						
					}
				}
				else if(result.get() == justClose){
					Tab tab = MainControl.modelFXMLControl.getTabPane().getSelectionModel().getSelectedItem();
					MainControl.modelFXMLControl.getTabPane().getTabs().remove(tab);
				}
			});
		}
	}

	public void saveModel() throws TransformerException, IOException, ParserConfigurationException {
		MainControl.modelFXMLControl.getModelView().getModelControl().saveModel();
	}

	public void saveModelAs() throws TransformerException, IOException, ParserConfigurationException {
		MainControl.modelFXMLControl.getModelView().getModelControl().saveModelAs();
	}

	public void saveImage() {
		try {
			MainControl.modelFXMLControl.getModelView().getImageExportControl().saveImage();
			Alert a = new Alert(Alert.AlertType.CONFIRMATION);
			a.setTitle("Speichern erfolgreich");
			a.setHeaderText("Ihr Model wurde erfolgreich als Bild gespeichert");
			a.show();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}

	private void showException(Exception e) {
		if(!e.getClass().equals(NullPointerException.class)) {
			Alert error = new Alert(Alert.AlertType.ERROR);
			error.setTitle("Fehler!");
			error.setHeaderText(e.getMessage());
			error.show();
		}
		e.printStackTrace();
	}

	public void importApplications() throws IOException {
		Stage applicationImport = new Stage();
    	applicationImport.setTitle("Anwendungen importieren");
    	Parent parent = FXMLLoader.load(Main.class.getResource("dataImport.fxml"));
    	final Scene scene = new Scene(parent);
    	applicationImport.setScene(scene);
    	applicationImport.centerOnScreen();
    	applicationImport.show();
	}

	public void openHelp() throws IOException {
		Stage help = new Stage();
    	help.setTitle("Hilfe");
    	final Parent parent = FXMLLoader.load(Main.class.getResource("help.fxml"));
    	final Scene scene = new Scene(parent);
    	help.setScene(scene);
    	help.centerOnScreen();
    	help.show();
	}

	public void addRelation() throws IOException, IllegalAccessException {
		int numberOfApplications = this.getModelView().getApplications().size();
		if(numberOfApplications >= 1) {
			Stage addRelationStage = new Stage();
			addRelationStage.initModality(Modality.APPLICATION_MODAL);
			addRelationStage.setTitle("Beziehung hinzuf�gen");
			Parent addRelationParent = FXMLLoader.load(Main.class.getResource("relation.fxml"));
			Scene addRelationScene = new Scene(addRelationParent);
			addRelationStage.setScene(addRelationScene);
			addRelationStage.centerOnScreen();
			addRelationStage.show();
			return;
		}
		throw new IllegalAccessException("Achtung! Es sind noch keine Anwendungen vorhanden.");
	}

	public void deleteRelation() throws IllegalAccessException {
		RelationView relationView = (RelationView)MainControl.modelFXMLControl.getModelView().getElementView();
		boolean relationClass = MainControl.modelFXMLControl.getModelView().getElementView().getClass().equals(RelationView.class);
		if(relationView == null || !relationClass) {
			throw new IllegalAccessException("Achtung! Es wurde keine Beziehung ausgew�hlt.");
		}
		MainControl.modelFXMLControl.getModelView().getModelControl().removeRelationView(relationView);
	}

	public void addApplication() throws IOException {
		Stage addApplicationStage = new Stage();
		addApplicationStage.initModality(Modality.APPLICATION_MODAL);
		addApplicationStage.setTitle("Anwendung hinzuf�gen");
		Parent addApplicationParent = FXMLLoader.load(Main.class.getResource("application.fxml"));
		Scene addRelationScene = new Scene(addApplicationParent);
		addApplicationStage.setScene(addRelationScene);
		addApplicationStage.centerOnScreen();
		addApplicationStage.show();
		return;
	}

	public void deleteApplication() throws IllegalAccessException {
		ApplicationView applicationView = (ApplicationView)MainControl.modelFXMLControl.getModelView().getElementView();
		boolean applicationClass = MainControl.modelFXMLControl.getModelView().getElementView().getClass().equals(ApplicationView.class);
		if(applicationView == null || !applicationClass) {
			throw new IllegalAccessException("Achtung! Es wurde keine Anwendung ausgew�hlt.");
		}
		MainControl.modelFXMLControl.getModelView().getModelControl().removeApplicationView(applicationView);
	}

	public void zoomIn() {
		MainControl.modelFXMLControl.getModelView().getModelControl().zoomIn();
	}

	public void zoomOut() {
		MainControl.modelFXMLControl.getModelView().getModelControl().zoomOut();
	}

	public void renameModel() {
		TextInputDialog modelNameInput = new TextInputDialog(this.getModelView().getModelName());
		modelNameInput.setTitle("Modell umbenennen");
		modelNameInput.setHeaderText("Geben Sie einen neuen Namen ein:");
		modelNameInput.setContentText("Hier Namen eingeben");
		String newModelName = modelNameInput.getResult();
		this.getModelView().setModelName(newModelName);
	}

}
