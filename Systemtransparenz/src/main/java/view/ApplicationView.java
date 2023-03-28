package view;

import java.util.LinkedList;

import control.ApplicationControl;
import control.ElementControl;
import control.edit.ApplicationBorderPane;
import control.ModelControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ApplicationModel;

//Klasse zur Präsentation einer Anwendung in einem Modell, beerbt abstrakte Klasse ElementView
public class ApplicationView extends ElementView {
	
	private ModelView modelView;
	private ApplicationModel applicationModel;
	private ApplicationControl applicationControl;
	private ApplicationBorderPane applicationBorderPane;
	private BooleanProperty selectedProperty;
	private boolean isSelected;
	private Label idLabel;
	private Label descriptionLabel;
	private Label categoryLabel;
	private Label producerLabel;
	private Label managerLabel;
	private Label departmentLabel;
	private Label adminLabel;
	private Text name;
	private Text id;
	private Text description;
	private Text category;
	private Text producer;
	private Text manager;
	private Text department;
	private Text admin;
	private VBox vbox;
	private TitledPane titledPane;

	//Konstruktor
	public ApplicationView(ApplicationModel applicationModel, ModelView modelView) {
		this.modelView = modelView;
		this.applicationModel = applicationModel;
        this.applicationControl = new ApplicationControl(this);
        this.applicationBorderPane = new ApplicationBorderPane();
        this.applicationBorderPane.setId("applicationBorderPane");
        this.applicationBorderPane.getStyleClass().add("applicationBorderPane");
        this.applicationBorderPane.setPrefSize(200.0, 100.0);
        this.name = new Text("");
        this.name.textProperty().bind(this.applicationModel.getNameProperty());
        this.idLabel = new Label("ID: ");
        this.id = new Text("");
        this.id.textProperty().bind((this.applicationModel.getIdProperty().asString()));
        HBox hBoxId = new HBox();
        hBoxId.getChildren().addAll(idLabel, this.id);
        this.descriptionLabel = new Label("Beschreibung: ");
        this.description = new Text("");
        this.description.textProperty().bind(this.applicationModel.getDescriptionProperty());
        HBox hBoxDescription = new HBox();
        hBoxDescription.getChildren().addAll(descriptionLabel, this.description);
        this.categoryLabel = new Label("Kategorie: ");
        this.category = new Text("");
        this.category.textProperty().bind(this.applicationModel.getCategoryProperty());
        HBox hBoxCategory = new HBox();
        hBoxCategory.getChildren().addAll(categoryLabel, this.category);
        this.producerLabel = new Label("Hersteller: ");
        this.producer = new Text("");
        this.producer.textProperty().bind(this.applicationModel.getProducerProperty());
        HBox hBoxProducer = new HBox();
        hBoxProducer.getChildren().addAll(producerLabel, this.producer);
        this.managerLabel = new Label("Anwendungsmanager: ");
        this.manager = new Text("");
        this.manager.textProperty().bind(this.applicationModel.getManagerProperty());
        HBox hBoxManager = new HBox();
        hBoxManager.getChildren().addAll(managerLabel, this.manager);
        this.departmentLabel = new Label("Fachbereich: ");
        this.department = new Text("");
        this.department.textProperty().bind(this.applicationModel.getDepartmentProperty());
        HBox hBoxDepartment = new HBox();
        hBoxDepartment.getChildren().addAll(departmentLabel, this.department);
        this.adminLabel = new Label("Admin: ");
        this.admin = new Text("");
        this.admin.textProperty().bind(this.applicationModel.getAdminProperty());
        HBox hBoxAdmin = new HBox();
        hBoxAdmin.getChildren().addAll(adminLabel, this.admin);
        this.vbox = new VBox();
        vbox.getChildren().addAll(hBoxId, hBoxDescription, hBoxCategory,
        		hBoxProducer, hBoxManager, hBoxDepartment, hBoxAdmin);
        this.titledPane = new TitledPane();
        this.titledPane.setContent(this.vbox);
        this.titledPane.setExpanded(false);
        this.applicationBorderPane.setCenter((Node)this.name);
        this.applicationBorderPane.setBottom(titledPane);
        ModelControl.makeRegionMoveable((Region)this.applicationBorderPane,
        		this.getModelView(), this);
        this.applicationBorderPane.getStylesheets().add(this.getClass().getResource("/application/application.css").toExternalForm());
        this.applicationBorderPane.getSelectedProperty().bind(this.getSelectedProperty());
        this.applicationBorderPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(modelView != null) {
	            	modelView.deselectElements();
	                modelView.setElementView(ApplicationView.this);
	            }
				applicationControl.setSelected(true);
	            event.consume();
			}
            
        });
        this.applicationBorderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(modelView != null) {
	            	modelView.deselectElements();
	                modelView.setElementView(ApplicationView.this);
	            }
				applicationControl.setSelected(true);
	            event.consume();
			}
        });
        this.isSelected = false;
	}

	//Getter-Methode für das Property der Auswahl
	private BooleanProperty getSelectedProperty() {
		if(this.selectedProperty == null) {
			this.selectedProperty = new SimpleBooleanProperty(this, "selected", this.isSelected);
		}
		return this.selectedProperty;
	}

	//Getter-Methode für die zugehörige Steuerung der präsentierten Anwendung
	public ApplicationControl getApplicationControl() {
		return this.applicationControl;
	}

	//Getter-Methode für die Fläche der präsentierten Anwendung
	@Override
	public Region getElementRegion() {
		return (Region)this.applicationBorderPane;
	}

	//Setter-Methode für die Auswahl der präsentierten Anwendung
	public void setSelected(boolean selected) {
		if(this.selectedProperty == null) {
			this.selectedProperty.set(selected);
		}
		else {
			this.isSelected = selected;
		}
	}

	//Getter-Methode für die gespeicherten Daten der präsentierten Anwendung
	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

	//Getter-Methode für die zugehörige Modell-Ansicht der präsentierten Anwendung
	public ModelView getModelView() {
		return this.modelView;
	}

	//Getter-Methode für den Text der präsentierten Anwendung
	public LinkedList<Text> getAttributes() {
		LinkedList<Text> texts = new LinkedList<Text>();
		texts.add(this.name);
		texts.add(this.id);
		texts.add(this.description);
		texts.add(this.category);
		texts.add(this.producer);
		texts.add(this.manager);
		texts.add(this.department);
		texts.add(this.admin);
		return texts;
	}
	
	public LinkedList<Label> getAttributeLabels(){
		LinkedList<Label> labels = new LinkedList<Label>();
		labels.add(this.idLabel);
		labels.add(this.descriptionLabel);
		labels.add(this.categoryLabel);
		labels.add(this.producerLabel);
		labels.add(this.managerLabel);
		labels.add(this.departmentLabel);
		labels.add(this.adminLabel);
		return labels;
	}
	
	//Getter-Methode für das gegliederte Feld einer präsentierten Anwendung
	public BorderPane getApplicationBorderPane() {
		return this.applicationBorderPane;
	}
	
	//Setter-Methode für das gegliederte Feld einer präsentierten Anwendung
	public void setApplicationBorderPane(ApplicationBorderPane applicationBorderPane) {
		this.applicationBorderPane = applicationBorderPane;
	}

	//Getter-Methode für die Steuerung des präsentierten Elements
	@Override
	public ElementControl getElementControl() {
		return this.getApplicationControl();
	}

	//Methode für die Ansichts-Vergrößerung bzw. -Verkleinerung der präsentierten Anwendung
	@Override
	public void zoom(double factor) {
		this.applicationControl.zoom(factor);
	}

	//Methode zur Prüfung, ob eine präsentierte Anwendung bewegbar ist
	@Override
	public boolean isMoveable() {
		return true;
	}

	//Setter-Methode zur Festlegung, ob die präsentierte Anwendung bewegt wurde
	@Override
	public void setMoved(double x, double y) {
		this.applicationControl.setMoved(x, y);
	}

	//Methode zur Bewegung der präsentierten Anwendung innerhalb des Modells
	@Override
	public void move(double x, double y) {
		this.applicationControl.move(x, y);
	}

	//Methode zum Hinzufügen einer Anwendung zur Präsentation
	@Override
	public void addElement(ElementView element) {
		if(element.getClass().equals(ApplicationView.class)) {
			this.getElements().add(element);
			return;
		}
	}

	//Methode zur Prüfung, ob eine präsentierte Anwendung ausgewählt wurde
	public boolean isSelected() {
		if(this.selectedProperty == null) {
			return this.isSelected;
		}
		else {
			return this.selectedProperty.get();
		}
	}
	
	//Methode zur Prüfung, ob eine bestimmte Anwendung der hier präsentierten entspricht
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		ApplicationView applicationView = (ApplicationView)object;
		if(this.getApplicationModel().equals(applicationView.getApplicationModel()) && this.getModelView().equals(applicationView.getModelView()) && this.isSelected() == applicationView.isSelected()) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
