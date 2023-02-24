package control.edit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

//Klasse zur Präsentation einer Anwendung als gegliederte Fläche, beerbt Klasse BorderPane
public class ApplicationBorderPane extends BorderPane {

	private BooleanProperty selected;
	
	//Konstruktor
	public ApplicationBorderPane() {
		
	}
	
	//Zweiter Konstruktor
	public ApplicationBorderPane(Node node) {
		super(node);
	}
	
	//Getter-Methode für das Property der Auswahl
	public BooleanProperty getSelectedProperty() {
		if(this.selected == null) {
			this.selected = new SimpleBooleanProperty(this, "selected", false);
		}
		return this.selected;
	}

}
