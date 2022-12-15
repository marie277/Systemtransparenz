package control;

import java.util.Optional;

import control.dataExport.FileExportControl;
import control.fxml.ModelFXMLControl;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
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
	
	public ModelView getSelectedModel() {
		return MainControl.modelFXMLControl.getModelView();
	}
	
	public ElementView getSelectedElement() {
		return MainControl.modelFXMLControl.getModelView().getElementView();
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
		// TODO Auto-generated method stub
		int numberOfTabs = MainControl.modelFXMLControl.getTabPane().getTabs().size();
		if(numberOfTabs > 0) {
			this.closeModel();
			if(numberOfTabs == 0) {
				System.exit(0);
			}
		}
		System.exit(0);
	}

	public void openModel() {
		// TODO Auto-generated method stub
		FileExportControl.openModel(MainControl.mainControl);
	}

	public void createModel() {
		// TODO Auto-generated method stub
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
		input.setContentText("Hier Modellnamen einfügen.");
		final Optional<String> result = (Optional<String>)input.showAndWait();
		result.ifPresent(name -> modelView.setModelName(name));
	}

	public void closeModel() {
		// TODO Auto-generated method stub
		
	}

	public void saveModel() {
		// TODO Auto-generated method stub
		
	}

	public void saveModelAs() {
		// TODO Auto-generated method stub
		
	}

	public void saveImage() {
		// TODO Auto-generated method stub
		
	}

	public void importApplications() {
		// TODO Auto-generated method stub
		
	}

	public void openHelp() {
		// TODO Auto-generated method stub
		
	}

	public void addRelation() {
		// TODO Auto-generated method stub
		
	}

	public void deleteRelation() {
		// TODO Auto-generated method stub
		
	}

	public void addInterface() {
		// TODO Auto-generated method stub
		
	}

	public void deleteInterface() {
		// TODO Auto-generated method stub
		
	}

	public void zoomIn() {
		// TODO Auto-generated method stub
		
	}

	public void zoomOut() {
		// TODO Auto-generated method stub
		
	}

	public void renameModel() {
		// TODO Auto-generated method stub
		
	}

	public static MainControl getInstance(ModelFXMLControl modelFXMLControl) {
		// TODO Auto-generated method stub
		return null;
	}


}
