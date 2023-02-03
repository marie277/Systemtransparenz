package control.edit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class RelationArrow extends Polygon {
	
	private BooleanProperty selected;
	private BooleanProperty arrowDirectionProperty;
	
	//Konstruktor
	public RelationArrow() {
		
	}
	
	//Getter-Methode für das Property der Auswahl
	public BooleanProperty getSelectedProperty() {
		if(this.selected == null) {
			this.selected = new SimpleBooleanProperty(this, "selected", false);
		}
		return this.selected;
	}
	
	public BooleanProperty getArrowDirectionProperty() {
		if(this.arrowDirectionProperty == null) {
			this.arrowDirectionProperty = new SimpleBooleanProperty(this, "arrowDirection", false);
		}
		return this.arrowDirectionProperty;
	}
}
