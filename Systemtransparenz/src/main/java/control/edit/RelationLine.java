package control.edit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Line;

//Klasse zur Präsentation einer Beziehung als Linie, beerbt Klasse Line
public class RelationLine extends Line {
	
	private BooleanProperty selected;
	
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
	
}
