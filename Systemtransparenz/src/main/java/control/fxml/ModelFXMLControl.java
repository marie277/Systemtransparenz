package control.fxml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import control.MainControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import view.ElementView;
import view.ModelView;

public class ModelFXMLControl implements Initializable{

	@FXML
	private MenuItem create_model;
	@FXML
	private MenuItem open_model;
	@FXML
	private MenuItem save_model;
	@FXML
	private MenuItem save_model_as;
	@FXML
	private MenuItem rename_model;
	@FXML
	private MenuItem close_model;
	@FXML
	private MenuItem save_image;
	@FXML
	private MenuItem quit;
	@FXML
	private MenuItem import_applications;
	@FXML
	private MenuItem add_relation;
	@FXML
	private MenuItem delete_relation;
	@FXML
	private MenuItem add_application;
	@FXML
	private MenuItem delete_application;
	@FXML
	private MenuItem zoom_in;
	@FXML
	private MenuItem zoom_out;
	@FXML
	private MenuItem open_help;
	@FXML
	private Button create_application;
	@FXML
	private Button remove_application;
	@FXML
	private Button create_relation;
	@FXML
	private Button remove_realtion;
	@FXML
	private TabPane model;
	
	private MainControl mainControl;
	private ObjectProperty<ModelView> modelView;
	private ObjectProperty<ElementView> elementView;
	
	@FXML
	public void quit(final ActionEvent event) {
		this.mainControl.quit();
	}
	
	@FXML
	public void renameModel(ActionEvent event) {
		this.mainControl.renameModel();
	}
	
	@FXML
	public void openModel(ActionEvent event) {
		try {
			this.mainControl.openModel();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void createModel(ActionEvent event) {
		this.mainControl.createModel();
	}
	
	@FXML
	public void closeModel(ActionEvent event) {
		this.mainControl.closeModel();
	}
	
	@FXML
	public void saveModel(ActionEvent event) {
		try {
			this.mainControl.saveModel();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void saveModelAs(ActionEvent event) {
		try {
			this.mainControl.saveModelAs();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	
	@FXML
	public void saveImage(ActionEvent event) {
		try {
			this.mainControl.saveImage();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void openApplicationImport(ActionEvent event) {
		try {
			this.mainControl.importApplications();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void openHelp(ActionEvent event) {
		try {
			this.mainControl.openHelp();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void addRelation(ActionEvent event) {
		try {
			this.mainControl.addRelation();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void deleteRelation(ActionEvent event) {
		try {
			this.mainControl.deleteRelation();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void addApplication(ActionEvent event) {
		try {
			this.mainControl.addApplication();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void deleteApplication(ActionEvent event) {
		try {
			this.mainControl.deleteApplication();
		}
		catch(Exception e) {
			this.showException(e);
		}
	}
	
	@FXML
	public void zoomIn(ActionEvent event) {
		this.mainControl.zoomIn();
	}
	
	@FXML
	public void zoomOut(ActionEvent event) {
		this.mainControl.zoomOut();
	}
	
	@FXML
	public void importApplications(ActionEvent event) throws IOException {
		this.mainControl.importApplications();
	}
	
	public TabPane getTabPane(){
		return this.model;
	}
	
	public ModelView getModelView() {
		return this.modelView.get();
	}
	
	private void showEmptyTabPane() {
		this.model.setContextMenu(null);
		setDisable(true);
	}
	
	private void showModelView() {
		ContextMenu menu = new ContextMenu();
		this.model.setContextMenu(menu);
		setDisable(false);
	}
	
	private void setDisable(boolean b) {
		this.save_model.setDisable(b);
		this.save_model_as.setDisable(b);
		this.rename_model.setDisable(b);
		this.close_model.setDisable(b);
		this.save_image.setDisable(b);
		this.import_applications.setDisable(b);
		this.add_relation.setDisable(b);
		this.delete_relation.setDisable(b);
		this.add_application.setDisable(b);
		this.delete_application.setDisable(b);
	}
	
	private void showException(Exception e) {
		if(!e.getClass().equals(NullPointerException.class)) {
			final Alert error = new Alert(Alert.AlertType.ERROR);
			error.setTitle("Fehler!");
			error.setHeaderText(e.getMessage());
			error.show();
		}
		e.printStackTrace();
	}
	
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
        this.elementView = (ObjectProperty<ElementView>)new SimpleObjectProperty<ElementView>((Object)this, "selektierteKomponente", (ElementView)null);
        (this.modelView = (ObjectProperty<ModelView>)new SimpleObjectProperty<ModelView>((Object)this, "aktivesProjekt", (ModelView)null)).addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.elementView.unbind();
                this.elementView.set(null);
                this.showEmptyTabPane();
            }
            else {
                this.elementView.unbind();
                this.elementView.bind((ObservableValue<ElementView>)newValue.selectedElementProperty());
                this.showModelView();
            }
        });
      
        this.showEmptyTabPane();
	}

}
