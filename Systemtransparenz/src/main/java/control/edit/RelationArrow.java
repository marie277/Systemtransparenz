package control.edit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Polygon;

public class RelationArrow extends Polygon {
	
	private BooleanProperty selectedProperty;
	private BooleanProperty relationDirectionProperty;
	
	//Konstruktor
	public RelationArrow() {
		
	}
	
	//Getter-Methode für das Property der Auswahl
	public BooleanProperty getSelectedProperty() {
		if(this.selectedProperty == null) {
			this.selectedProperty = new SimpleBooleanProperty(this, "selected", false);
		}
		return this.selectedProperty;
	}
	
	public BooleanProperty getRelationDirectionProperty() {
		if(this.relationDirectionProperty == null) {
			this.relationDirectionProperty = new SimpleBooleanProperty(this, "relationDirection", false);
		}
		return this.relationDirectionProperty;
	}
}
