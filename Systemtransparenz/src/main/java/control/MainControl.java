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
	public static ModelFXMLControl modelFXMLControl;
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
		TextInputDialog input = new TextInputDialog("Neues Modell");
		input.setTitle("Modellnamen festlegen");
		input.setHeaderText("Bitte geben Sie den Namen des Modells an.");
		input.setContentText("Hier Modellnamen einfügen.");
		Optional<String> result = (Optional<String>)input.showAndWait();
		result.ifPresent(name -> modelView.setModelName(name));
	}

	public void closeModel() {
		if(MainControl.modelFXMLControl.getModelView().getFileExportControl().isSaved()) {
			Tab tab = MainControl.modelFXMLControl.getTabPane().getSelectionModel().getSelectedItem();
			MainControl.modelFXMLControl.getTabPane().getTabs().remove(tab);
		}
		else {
			Alert a = new Alert(Alert.AlertType.CONFIRMATION);
			a.setTitle("Model schließen");
			a.setHeaderText("Möchten Sie das Modell wirklich schließen?");
			ButtonType saveAndClose = new ButtonType("Speichern und schließen");
			ButtonType justClose = new ButtonType("Schließen ohne speichern");
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
    	Parent parent = FXMLLoader.load(Main.class.getResource("connection.fxml"));
    	Scene scene = new Scene(parent);
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
			addRelationStage.setTitle("Beziehung hinzufügen");
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
		boolean rightClass = MainControl.modelFXMLControl.getModelView().getElementView().getClass().equals(RelationView.class);
		if(relationView == null || !rightClass) {
			throw new IllegalAccessException("Achtung! Es wurde keine Beziehung ausgewählt.");
		}
		MainControl.modelFXMLControl.getModelView().getModelControl().removeRelationView(relationView);
	}

	/*public void addApplication() throws IOException {
		Stage addApplicationStage = new Stage();
		addApplicationStage.initModality(Modality.APPLICATION_MODAL);
		addApplicationStage.setTitle("Anwendung hinzufügen");
		Parent addApplicationParent = FXMLLoader.load(Main.class.getResource("application.fxml"));
		Scene addRelationScene = new Scene(addApplicationParent);
		addApplicationStage.setScene(addRelationScene);
		addApplicationStage.centerOnScreen();
		addApplicationStage.show();
		return;
	}*/
	
	public void addApplication() throws IllegalArgumentException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Anwendung erstellen");
        dialog.setHeaderText("Wählen Sie eine Bezeichnung für Ihre Anwendung.");
        dialog.setContentText("Hier die Bezeichnung für Ihre Anwendung einfügen.");
        Optional<String> result = (Optional<String>)dialog.showAndWait();
        result.ifPresent(neueBezeichung -> this.addApplication(neueBezeichung));
    }
    
    public void addApplication(double x, final double y) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Anwendung erstellen");
        dialog.setHeaderText("Wählen Sie eine Bezeichnung für Ihre Anwendung.");
        dialog.setContentText("Hier die Bezeichnung für Ihre Anwendung einfügen.");
        Optional<String> result = (Optional<String>)dialog.showAndWait();
        result.ifPresent(neueBezeichung -> this.addApplication(neueBezeichung, x, y));
    }
    
    public void addApplication(String neueBezeichung) throws IllegalArgumentException {
        MainControl.modelFXMLControl.getModelView().getModelControl().addApplication(neueBezeichung);
    }
    
    private void addApplication(String neueBezeichung, double x, double y) throws IllegalArgumentException {
        this.addApplication(neueBezeichung);
        MainControl.modelFXMLControl.getModelView().getApplications().getLast().move(x, y);
    }

	public void deleteApplication() throws IllegalAccessException {
		ApplicationView applicationView = (ApplicationView)MainControl.modelFXMLControl.getModelView().getElementView();
		boolean rightApplication = MainControl.modelFXMLControl.getModelView().getElementView().getClass().equals(ApplicationView.class);
		if(applicationView == null || !rightApplication) {
			throw new IllegalAccessException("Achtung! Es wurde keine Anwendung ausgewählt.");
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
		TextInputDialog modelNameInput = new TextInputDialog();
		modelNameInput.setTitle("Modell umbenennen");
		modelNameInput.setHeaderText("Geben Sie einen neuen Namen ein:");
		modelNameInput.setContentText("Hier Namen eingeben");
		Optional<String> newModelName = modelNameInput.showAndWait();
		newModelName.ifPresent(e -> this.getModelView().setModelName(e));
	}
	
	public ElementView getSelectedElementView() {
        return MainControl.modelFXMLControl.getModelView().getElementView();
    }

	/*public void createNewApplication(String applicationName) {
		// TODO Auto-generated method stub
		//System.out.print(applicationName);
		MainControl.modelFXMLControl.getModelView().getModelControl().addApplication(applicationName);
	}*/

}
