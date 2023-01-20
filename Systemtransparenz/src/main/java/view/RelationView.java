package view;

import java.util.LinkedList;

import control.ElementControl;
import control.RelationControl;
import control.edit.ApplicationBorderPane;
import control.edit.Move;
import control.edit.MoveControl;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import model.ApplicationInRelation;
import model.RelationModel;

//Klasse zur Pr�sentation einer Beziehung in einem Modell, beerbt abstrakte Klasse ElementView
public class RelationView extends ElementView{
	
	//private ModelView modelView;
	private RelationControl relationControl;
	private LinkedList<RelationLineView> relationNodes;
	private ApplicationBorderPane applicationBorderPane;
	private RelationModel relationModel;
	private Text relationText;
	private BooleanProperty selectedProperty;
	private boolean selected;
	private StackPane stackPane;
	
	//Konstruktor
	public RelationView(RelationModel relationModel, ModelView modelView) {
		this.relationModel = relationModel;
		this.modelView = modelView;
		this.relationControl = new RelationControl(this);
		this.relationText = new Text("");
		this.relationText.textProperty().bind((ObservableValue<? extends String>)this.relationModel.getRelationNameProperty());
		this.stackPane = new StackPane();
		this.relationNodes = new LinkedList<RelationLineView>();
		this.applicationBorderPane = new ApplicationBorderPane();
		this.relationText.setOnMouseClicked(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.relationControl.setSelected(true);
			e.consume();
		});
		this.relationText.setOnMousePressed(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.relationControl.setSelected(true);
			e.consume();
		});
		
		this.stackPane.getChildren().add((Node)this.relationText);
		this.stackPane.setAlignment(Pos.CENTER);
		this.applicationBorderPane.setCenter((Node)this.stackPane);
		this.applicationBorderPane.setPrefSize(40.0, 40.0);
		for(ApplicationInRelation applicationInRelation : this.relationModel.getApplications()) {
			RelationLineView relationLineView = new RelationLineView(applicationInRelation);
			this.relationNodes.add(relationLineView);
			DoubleProperty endX = relationLineView.getRelationLine().endXProperty();
			DoubleProperty layoutX = this.getElementRegion().layoutXProperty();
			DoubleBinding width = this.getElementRegion().prefWidthProperty().divide(2.0);
			DoubleProperty endY = relationLineView.getRelationLine().endYProperty();
			DoubleProperty layoutY = this.getElementRegion().layoutXProperty();
			DoubleBinding height = this.getElementRegion().prefWidthProperty().divide(2.0);
			endX.bind((ObservableValue<? extends Number>)layoutX.add((ObservableNumberValue)width));
			endY.bind((ObservableValue<? extends Number>)layoutY.add((ObservableNumberValue)height));
		}
		MoveControl.makeRegionMoveable(this.getElementRegion(), (Region)this.getModelView(), (Move)this);
		this.selected = false;
	}

	//Getter-Methode f�r die Steuerung der pr�sentierten Beziehung
	public RelationControl getRelationControl() {
		return this.relationControl;
	}

	//Getter-Methode f�r die Knoten der Beziehung
	public LinkedList<RelationLineView> getRelationNodes() {
		return this.relationNodes;
	}

	//Getter-Methode f�r die Fl�che der pr�sentierten Beziehung
	@Override
	public Region getElementRegion() {
		return (Region)this.applicationBorderPane;
	}

	//Getter-Methode f�r die Steuerung des pr�sentierten Elements
	@Override
	public ElementControl getElementControl() {
		return this.getRelationControl();
	}

	//Methode f�r die Ansichts-Vergr��erung bzw. -Verkleinerung der pr�sentierten Beziehung
	@Override
	public void zoom(double factor) {
		this.relationControl.zoom(factor);
	}

	//Methode zur Pr�fung, ob eine pr�sentierte Beziehung bewegbar ist
	@Override
	public boolean isMoveable() {
		return true;
	}

	//Methode zur Festlegung, ob die pr�sentierte Beziehung bewegt wurde
	@Override
	public void setMoved(double x, double y) {
		this.relationControl.setMoved(x, y);
	}

	//Methode zur Bewegung der pr�sentierten Beziehung innerhalb des Modells
	@Override
	public void move(double x, double y) {
		this.relationControl.move(x, y);
	}

	//Methode zum Hinzuf�gen einer Beziehung zur Pr�sentation
	@Override
	public void addElement(ElementView element) {
		if(element.getClass().equals(RelationView.class)) {
			this.getElements().add(element);
			return;
		}
	}

	//Getter-Methode f�r die gespeicherten Daten der pr�sentierten Beziehung
	public RelationModel getRelationModel() {
		return this.relationModel;
	}

	//Methode zur Pr�fung, ob eine pr�sentierte Beziehung ausgew�hlt wurde
	public boolean isSelected() {
		if(this.selectedProperty != null) {
			return this.selectedProperty.get();
		}
		else {
			return this.selected;
		}
	}

	//Setter-Methode f�r die Auswahl der pr�sentierten Beziehung
	public void setSelected(boolean selected) {
		if(this.selectedProperty != null) {
			this.selectedProperty.set(selected);
		}
		else {
			this.selected = selected;
		}
		for(RelationLineView relationLineView : this.relationNodes) {
			relationLineView.setSelected(selected);
		}
	}
	
	public BooleanProperty getSelectedProperty() {
		if(this.selectedProperty != null) {
			return this.selectedProperty;
		}
		else {
			this.selectedProperty = new SimpleBooleanProperty(this, "selected", this.selected);
			return this.selectedProperty;
		}
	}
	
	//Methode zum Hinzuf�gen einer Anwendung als Beziehungs-Teilnehmer
	public void addPartOfRelation(ApplicationView applicationView) {
		this.relationControl.addPartOfRelation(applicationView);
	}
	
	//Getter-Methode f�r den Beziehungs-Text
	public Text getRelationText() {
		return this.relationText;
	}
	
	//Methode zur Pr�fung, ob eine bestimmte Beziehung der hier pr�sentierten entspricht
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		RelationView relationView = (RelationView)object;
		if(this.getRelationModel().equals(relationView.getRelationModel()) && this.getRelationNodes().equals(relationView.getRelationNodes()) && this.getElementRegion().equals(relationView.getElementRegion()) && this.isSelected() == relationView.isSelected()) {
			return true;
		}
		else {
			return false;
		}
	}

}
