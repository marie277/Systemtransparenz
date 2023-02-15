package view;

import control.ApplicationControl;
import control.ElementControl;
import control.edit.ApplicationBorderPane;
import control.edit.Move;
import control.edit.MoveControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ApplicationModel;

//Klasse zur Pr�sentation einer Anwendung in einem Modell, beerbt abstrakte Klasse ElementView
public class ApplicationView extends ElementView {
	
	private ModelView modelView;
	private ApplicationModel applicationModel;
	private ApplicationControl applicationControl;
	private ApplicationBorderPane applicationBorderPane;
	private BooleanProperty selectedProperty;
	private boolean isSelected;
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
        this.name.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getNameProperty());
        Label idLabel = new Label("ID: ");
        this.id = new Text("");
        this.id.textProperty().bind(((ObservableValue<? extends String>)this.applicationModel.getIdProperty().asString()));
        HBox hBoxId = new HBox();
        hBoxId.getChildren().addAll(idLabel, this.id);
        Label descriptionLabel = new Label("Beschreibung: ");
        this.description = new Text("");
        this.description.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getDescriptionProperty());
        HBox hBoxDescription = new HBox();
        hBoxDescription.getChildren().addAll(descriptionLabel, this.description);
        Label categoryLabel = new Label("Kategorie: ");
        this.category = new Text("");
        this.category.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getCategoryProperty());
        HBox hBoxCategory = new HBox();
        hBoxCategory.getChildren().addAll(categoryLabel, this.category);
        Label producerLabel = new Label("Hersteller: ");
        this.producer = new Text("");
        this.producer.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getProducerProperty());
        HBox hBoxProducer = new HBox();
        hBoxProducer.getChildren().addAll(producerLabel, this.producer);
        Label managerLabel = new Label("Anwendungsmanager: ");
        this.manager = new Text("");
        this.manager.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getManagerProperty());
        HBox hBoxManager = new HBox();
        hBoxManager.getChildren().addAll(managerLabel, this.manager);
        Label departmentLabel = new Label("Fachbereich: ");
        this.department = new Text("");
        this.department.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getDepartmentProperty());
        HBox hBoxDepartment = new HBox();
        hBoxDepartment.getChildren().addAll(departmentLabel, this.department);
        Label adminLabel = new Label("Admin: ");
        this.admin = new Text("");
        this.admin.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getAdminProperty());
        HBox hBoxAdmin = new HBox();
        hBoxAdmin.getChildren().addAll(adminLabel, this.admin);
        this.vbox = new VBox();
        vbox.getChildren().addAll(hBoxId, hBoxDescription, hBoxCategory, hBoxProducer, hBoxManager, hBoxDepartment, hBoxAdmin);
        this.titledPane = new TitledPane();
        this.titledPane.setContent(this.vbox);
        this.titledPane.setExpanded(false);
        this.applicationBorderPane.setCenter((Node)this.name);
        this.applicationBorderPane.setBottom(titledPane);
        MoveControl.makeRegionMoveable((Region)this.applicationBorderPane, (Region)this.getModelView(), (Move)this);
        this.applicationBorderPane.getStylesheets().add(this.getClass().getResource("/application/application.css").toExternalForm());
        this.applicationBorderPane.getSelectedProperty().bind(this.getSelectedProperty());
        this.applicationBorderPane.setOnMouseClicked(e -> {
            if (modelView != null) {
            	modelView.deselectElements();
                modelView.setElementView(this);
            }
            this.applicationControl.setSelected(true);
            e.consume();
        });
        this.applicationBorderPane.setOnMousePressed(e -> {
            if (modelView != null) {
            	modelView.deselectElements();
                modelView.setElementView(this);
            }
            this.applicationControl.setSelected(true);
            e.consume();
        });
        this.isSelected = false;
	}

	//Getter-Methode f�r das Property der Auswahl
	private BooleanProperty getSelectedProperty() {
		if(this.selectedProperty == null) {
			this.selectedProperty = new SimpleBooleanProperty(this, "selected", this.isSelected);
		}
		return this.selectedProperty;
	}

	//Getter-Methode f�r die zugeh�rige Steuerung der pr�sentierten Anwendung
	public ApplicationControl getApplicationControl() {
		return this.applicationControl;
	}

	//Getter-Methode f�r die Fl�che der pr�sentierten Anwendung
	@Override
	public Region getElementRegion() {
		return (Region)this.applicationBorderPane;
	}

	//Setter-Methode f�r die Auswahl der pr�sentierten Anwendung
	public void setSelected(boolean selected) {
		if(this.selectedProperty == null) {
			this.selectedProperty.set(selected);
		}
		else {
			this.isSelected = selected;
		}
	}

	//Getter-Methode f�r die gespeicherten Daten der pr�sentierten Anwendung
	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

	//Getter-Methode f�r die zugeh�rige Modell-Ansicht der pr�sentierten Anwendung
	public ModelView getModelView() {
		return this.modelView;
	}

	//Getter-Methode f�r den Text der pr�sentierten Anwendung
	public Text getText() {
		return this.name;
	}
	
	//Getter-Methode f�r das gegliederte Feld einer pr�sentierten Anwendung
	public BorderPane getApplicationBorderPane() {
		return this.applicationBorderPane;
	}
	
	//Setter-Methode f�r das gegliederte Feld einer pr�sentierten Anwendung
	public void setApplicationBorderPane(ApplicationBorderPane applicationBorderPane) {
		this.applicationBorderPane = applicationBorderPane;
	}

	//Getter-Methode f�r die Steuerung des pr�sentierten Elements
	@Override
	public ElementControl getElementControl() {
		return this.getApplicationControl();
	}

	//Methode f�r die Ansichts-Vergr��erung bzw. -Verkleinerung der pr�sentierten Anwendung
	@Override
	public void zoom(double factor) {
		this.applicationControl.zoom(factor);
	}

	//Methode zur Pr�fung, ob eine pr�sentierte Anwendung bewegbar ist
	@Override
	public boolean isMoveable() {
		return true;
	}

	//Setter-Methode zur Festlegung, ob die pr�sentierte Anwendung bewegt wurde
	@Override
	public void setMoved(double x, double y) {
		this.applicationControl.setMoved(x, y);
	}

	//Methode zur Bewegung der pr�sentierten Anwendung innerhalb des Modells
	@Override
	public void move(double x, double y) {
		this.applicationControl.move(x, y);
	}

	//Methode zum Hinzuf�gen einer Anwendung zur Pr�sentation
	@Override
	public void addElement(ElementView element) {
		if(element.getClass().equals(ApplicationView.class)) {
			this.getElements().add(element);
			return;
		}
	}

	//Methode zur Pr�fung, ob eine pr�sentierte Anwendung ausgew�hlt wurde
	public boolean isSelected() {
		if(this.selectedProperty == null) {
			return this.isSelected;
		}
		else {
			return this.selectedProperty.get();
		}
	}
	
	//Methode zur Pr�fung, ob eine bestimmte Anwendung der hier pr�sentierten entspricht
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
