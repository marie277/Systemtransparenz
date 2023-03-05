package view;

import java.util.LinkedList;

import control.ElementControl;
import control.RelationControl;
import control.edit.ApplicationBorderPane;
import control.edit.ApplicationInRelation;
import control.ModelControl;
import control.edit.RelationNode;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import model.RelationModel;

//Klasse zur Präsentation einer Beziehung in einem Modell, beerbt abstrakte Klasse ElementView
public class RelationView extends ElementView{
	
	private RelationControl relationControl;
	private LinkedList<RelationNode> relationNodes;
	private ApplicationBorderPane applicationBorderPane;
	private RelationModel relationModel;
	private Text relationText;
	private BooleanProperty selectedProperty;
	private boolean selected;
	
	//Konstruktor
	public RelationView(RelationModel relationModel, ModelView modelView) {
		this.relationModel = relationModel;
		this.modelView = modelView;
		this.relationControl = new RelationControl(this);
		this.relationText = new Text("");
		this.relationText.textProperty().bind(this.relationModel.getRelationTypeProperty());
		this.relationNodes = new LinkedList<RelationNode>();
		this.applicationBorderPane = new ApplicationBorderPane();
		this.relationText.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				select(event);
			}
		});
		this.relationText.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				select(event);
			}
		});
		this.applicationBorderPane.setCenter((Node)this.relationText);
		this.applicationBorderPane.setPrefSize(10.0, 10.0);
		for(ApplicationInRelation applicationInRelation : this.relationModel.getApplications()) {
			boolean relationDirection = this.relationModel.getRelationDirection();
			String relationType = this.relationModel.getRelationType();
			RelationNode relationNode = new RelationNode(applicationInRelation, relationType, relationDirection);
			if(relationType == "Nutzt") {
				relationNode.getRelationLine().setStyle("-fx-stroke-dash-array: 6 6 ; ");
			}
			this.relationNodes.add(relationNode);
			DoubleProperty endX = relationNode.getRelationLine().endXProperty();
			DoubleProperty layoutX = this.getElementRegion().layoutXProperty();
			DoubleBinding width = this.getElementRegion().prefWidthProperty().divide(2.0);
			DoubleProperty endY = relationNode.getRelationLine().endYProperty();
			DoubleProperty layoutY = this.getElementRegion().layoutXProperty();
			DoubleBinding height = this.getElementRegion().prefHeightProperty().divide(2.0);
			endX.bind(layoutX.add(width));
			endY.bind(layoutY.add(height));
			applicationInRelation.getApplicationView().getElementRegion().boundsInParentProperty().addListener(new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					for (RelationNode rN : relationNodes) {
	                    if (rN.getApplicationInRelation().equals(applicationInRelation)) {
	                        rN.getRelationHub();
	                        rN.getRelationLine().setOnMouseClicked(new EventHandler<MouseEvent>() {
	                			@Override
	                			public void handle(MouseEvent event) {
	                				select(event);
	                			}
	                		});
	                		rN.getRelationLine().setOnMousePressed(new EventHandler<MouseEvent>() {
	                			@Override
	                			public void handle(MouseEvent event) {
	                				select(event);
	                			}
	                		});
	                		rN.getRelationArrow().setOnMouseClicked(new EventHandler<MouseEvent>() {
	                			@Override
	                			public void handle(MouseEvent event) {
	                				select(event);
	                			}
	                		});
	                		rN.getRelationArrow().setOnMousePressed(new EventHandler<MouseEvent>() {
	                			@Override
	                			public void handle(MouseEvent event) {
	                				select(event);
	                			}
	                		});
	                    }
	                }
				}
			});
        }
		ModelControl.makeRegionMoveable(this.getElementRegion(), this.getModelView(), this);
		this.relationText.layoutBoundsProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				for (RelationNode rN : relationNodes) {
	            	rN.getRelationHub();
	            }
			}
        });
		this.selected = false;
	}
	
	//Getter-Methode für die gespeicherten Daten der präsentierten Beziehung
	public RelationModel getRelationModel() {
		return this.relationModel;
	}
	
	//Getter-Methode für die Steuerung der präsentierten Beziehung
	public RelationControl getRelationControl() {
		return this.relationControl;
	}
	
	//Getter-Methode für den Beziehungs-Text
	public Text getRelationText() {
		return this.relationText;
	}

	//Getter-Methode für die Knoten der Beziehung
	public LinkedList<RelationNode> getRelationNodes() {
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
	
	//Getter-Methode für das Property der Auswahl
	public BooleanProperty getSelectedProperty() {
		if(this.selectedProperty != null) {
			return this.selectedProperty;
		}
		else {
			this.selectedProperty = new SimpleBooleanProperty(this, "selected", this.selected);
			return this.selectedProperty;
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
		for(RelationNode relationNode : this.relationNodes) {
			relationNode.setSelected(selected);
		}
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
	
	//Methode zur Auswahl einer Beziehung durch den Nutzer
	private void select(MouseEvent event) {
		if (modelView != null) {
        	modelView.deselectElements();
            modelView.setElementView(RelationView.this);
        }
		relationControl.setSelected(true);
        event.consume();
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

	//Methode zum Hinzufügen einer Anwendung als Beziehungs-Teilnehmer
	public void addPartOfRelation(ApplicationView applicationView) {
		this.relationControl.addPartOfRelation(applicationView);
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
