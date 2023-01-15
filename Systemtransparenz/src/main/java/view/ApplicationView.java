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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import model.ApplicationModel;

//Klasse zur Pr�sentation einer Anwendung in einem Modell, beerbt abstrakte Klasse ElementView
public class ApplicationView extends ElementView {
	
	private ModelView modelView;
	private ApplicationModel applicationModel;
	private ApplicationControl applicationControl;
	private ApplicationBorderPane applicationBorderPane;
	private ApplicationBorderPane applicationBorderPaneLabel;
	private BooleanProperty selectedProperty;
	private boolean isSelected;
	private Text text;

	//Konstruktor
	public ApplicationView(ApplicationModel applicationModel, ModelView modelView) {
		this.modelView = modelView;
		this.applicationModel = applicationModel;
        this.applicationControl = new ApplicationControl(this);
        this.applicationBorderPane = new ApplicationBorderPane();
        this.applicationBorderPane.setId("applicationBorderPane");
        this.applicationBorderPane.getStyleClass().add("applicationBorderPane");
        this.applicationBorderPane.setPrefSize(40.0, 40.0);
        this.text = new Text("");
        this.text.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.getNameProperty());
        this.applicationBorderPaneLabel = new ApplicationBorderPane((Node)this.text);
        this.applicationBorderPaneLabel.setId("applicationBorderPaneLabel");
        this.applicationBorderPane.setCenter((Node)this.applicationBorderPaneLabel);
        this.applicationBorderPane.setCenter((Node)this.text);
        MoveControl.makeRegionMoveable((Region)this.applicationBorderPane, (Region)this.getModelView(), (Move)this);
        this.applicationBorderPane.getStylesheets().add(this.getClass().getResource("/application/application.css").toExternalForm());
        this.applicationBorderPaneLabel.getSelectedProperty().bind(this.getSelectedProperty());
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
		return this.text;
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
