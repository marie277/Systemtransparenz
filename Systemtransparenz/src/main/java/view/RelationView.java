package view;

import java.util.LinkedList;

import control.ElementControl;
import control.RelationControl;
import control.edit.ApplicationBorderPane;
import control.edit.Move;
import control.edit.MoveControl;
import control.edit.RelationLine;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import model.ApplicationInRelation;
import model.RelationModel;

//Klasse zur Präsentation einer Beziehung in einem Modell, beerbt abstrakte Klasse ElementView
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
		this.relationText.textProperty().bind((ObservableValue<? extends String>)this.relationModel.getRelationTypeProperty());
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
		this.applicationBorderPane.setPrefSize(0.0, 0.0);
		for(ApplicationInRelation applicationInRelation : this.relationModel.getApplications()) {
			boolean relationDirection = this.relationModel.getRelationDirection();
			String relationType = this.relationModel.getRelationType();
			RelationLineView relationLineView = new RelationLineView(applicationInRelation, relationType, relationDirection);
			
			if(relationType == "Nutzt") {
				relationLineView.getRelationLine().setStyle("-fx-stroke-dash-array: 2 12 12 2; ");
			}
			this.relationNodes.add(relationLineView);
			DoubleProperty endX = relationLineView.getRelationLine().endXProperty();
			DoubleProperty layoutX = this.getElementRegion().layoutXProperty();
			DoubleBinding width = this.getElementRegion().prefWidthProperty().divide(2.0);
			DoubleProperty endY = relationLineView.getRelationLine().endYProperty();
			DoubleProperty layoutY = this.getElementRegion().layoutXProperty();
			DoubleBinding height = this.getElementRegion().prefHeightProperty().divide(2.0);
			endX.bind((ObservableValue<? extends Number>)layoutX.add((ObservableNumberValue)width));
			endY.bind((ObservableValue<? extends Number>)layoutY.add((ObservableNumberValue)height));
			
		}
		for(ApplicationInRelation applicationInRelation : this.relationModel.getApplications()) {
			applicationInRelation.getApplicationView().getElementRegion().boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
                for (RelationLineView rLV : this.relationNodes) {
                    if (rLV.getApplicationInRelation().equals(applicationInRelation)) {
                        rLV.getRelationHub();
                    }
                }
            });
        }
		MoveControl.makeRegionMoveable(this.getElementRegion(), (Region)this.getModelView(), (Move)this);
		this.relationText.layoutBoundsProperty().addListener((o, oldVal, newVal) -> {
            for (RelationLineView rLV : this.relationNodes) {
            	rLV.getRelationHub();
            }
        });
		this.selected = false;
	}

	//Getter-Methode für die Steuerung der präsentierten Beziehung
	public RelationControl getRelationControl() {
		return this.relationControl;
	}

	//Getter-Methode für die Knoten der Beziehung
	public LinkedList<RelationLineView> getRelationNodes() {
		return this.relationNodes;
	}

	//Getter-Methode für die Fläche der präsentierten Beziehung
	@Override
	public Region getElementRegion() {
		return (Region)this.applicationBorderPane;
	}

	//Getter-Methode für die Steuerung des präsentierten Elements
	@Override
	public ElementControl getElementControl() {
		return this.getRelationControl();
	}

	//Methode für die Ansichts-Vergrößerung bzw. -Verkleinerung der präsentierten Beziehung
	@Override
	public void zoom(double factor) {
		this.relationControl.zoom(factor);
	}

	//Methode zur Prüfung, ob eine präsentierte Beziehung bewegbar ist
	@Override
	public boolean isMoveable() {
		return true;
	}

	//Methode zur Festlegung, ob die präsentierte Beziehung bewegt wurde
	@Override
	public void setMoved(double x, double y) {
		this.relationControl.setMoved(x, y);
	}

	//Methode zur Bewegung der präsentierten Beziehung innerhalb des Modells
	@Override
	public void move(double x, double y) {
		this.relationControl.move(x, y);
	}

	//Methode zum Hinzufügen einer Beziehung zur Präsentation
	@Override
	public void addElement(ElementView element) {
		if(element.getClass().equals(RelationView.class)) {
			this.getElements().add(element);
			return;
		}
	}

	//Getter-Methode für die gespeicherten Daten der präsentierten Beziehung
	public RelationModel getRelationModel() {
		return this.relationModel;
	}

	//Methode zur Prüfung, ob eine präsentierte Beziehung ausgewählt wurde
	public boolean isSelected() {
		if(this.selectedProperty != null) {
			return this.selectedProperty.get();
		}
		else {
			return this.selected;
		}
	}

	//Setter-Methode für die Auswahl der präsentierten Beziehung
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
	
	//Methode zum Hinzufügen einer Anwendung als Beziehungs-Teilnehmer
	public void addPartOfRelation(ApplicationView applicationView) {
		this.relationControl.addPartOfRelation(applicationView);
	}
	
	//Getter-Methode für den Beziehungs-Text
	public Text getRelationText() {
		return this.relationText;
	}
	
	//Methode zur Prüfung, ob eine bestimmte Beziehung der hier präsentierten entspricht
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
