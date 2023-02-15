package control.edit;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.ApplicationInRelation;

//Klasse zur Präsentation einer Beziehung als Linie, beerbt Klasse Line
public class RelationLine extends Line {
	
	private BooleanProperty selected;
	private BooleanProperty relationDirectionProperty;
	
	private Polygon triangle;
	private Pane parent;
	private ApplicationInRelation applicationInRelation;


	/*public RelationLine(Pane parent, ApplicationInRelation applicationInRelation) {
		//this.applicationInRelation = applicationInRelation;
	    this.parent = parent;
	    parent.getChildren().addAll(this);
	    canvas();
	}

	private DoubleBinding dx;
	private DoubleBinding dy;*/

	/*private void canvas(){
		
	    dx = startXProperty().add(endXProperty().negate());
	    dy = startYProperty().add(endYProperty().negate());
	    triangle = new Polygon(getStartX(), getEndY(), getStartX() + 16, getStartY() - 8, getStartX() + 16, getStartY() + 8);
	    Rotate rotate = new Rotate(0,0,0,1,Rotate.Z_AXIS);
	    triangle.getTransforms().add(rotate);
	    dx.addListener((observable, oldValue, newValue) -> {
	        rotate.setAngle(getAngle(dy.doubleValue(), newValue.doubleValue()));
	    });
	    dy.addListener((observable, oldValue, newValue) -> {
	        rotate.setAngle(getAngle(newValue.doubleValue(), dx.doubleValue()));
	    });
	    triangle.layoutXProperty().bind(startXProperty());
	    triangle.layoutYProperty().bind(startYProperty());
	    parent.getChildren().add(triangle);
	}*/
	
/*private void canvas(){
	   
	    dx = endXProperty().add(startXProperty().negate());
	    dy = endYProperty().add(startYProperty().negate());
	    triangle = new Polygon(getEndX(), getEndY(), getEndX() - 16, getEndY() + 8, getEndX() - 16, getEndY() - 8);
	    Rotate rotate = new Rotate(0,0,0,1,Rotate.Z_AXIS);
	    triangle.getTransforms().add(rotate);
	    dx.addListener((observable, oldValue, newValue) -> {
	        rotate.setAngle(getAngle(dy.doubleValue(), newValue.doubleValue()));
	    });
	    dy.addListener((observable, oldValue, newValue) -> {
	        rotate.setAngle(getAngle(newValue.doubleValue(), dx.doubleValue()));
	    });
	    triangle.layoutXProperty().bind(endXProperty());
	    triangle.layoutYProperty().bind(endYProperty());
	    parent.getChildren().add(triangle);
	}

	private double getAngle(double dy ,double dx){
	    return Math.toDegrees(Math.atan2(dy, dx));
	}*/

	//Konstruktor
	public RelationLine() {
		
	}
	
	//Getter-Methode für das Property der Auswahl
	public BooleanProperty getSelectedProperty() {
		if(this.selected == null) {
			this.selected = new SimpleBooleanProperty(this, "selected", false);
		}
		return this.selected;
	}

		public BooleanProperty getRelationDirectionProperty() {
		if(this.relationDirectionProperty == null) {
			this.relationDirectionProperty = new SimpleBooleanProperty(this, "relationDirection", false);
		}
		return this.relationDirectionProperty;
	}
	
}
