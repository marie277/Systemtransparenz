package view;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import control.edit.RelationLine;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import model.ApplicationInRelation;

//Klasse zur Pr�sentation einer Beziehungslinie
public class RelationLineView {

	private BooleanProperty selectedProperty;
	private RelationLine relationLine;
	private ApplicationInRelation applicationInRelation;
	private LinkedList<Node> relationNodes;
	
	//Konstruktor
	public RelationLineView(ApplicationInRelation applicationInRelation) {
		this.applicationInRelation = applicationInRelation;
		this.relationLine = new RelationLine();
		this.relationLine.getSelectedProperty().bind((ObservableValue<? extends Boolean>)this.getSelectedProperty());
		DoubleProperty layoutX = this.applicationInRelation.getApplicationView().getElementRegion().layoutXProperty();
		DoubleBinding width = this.applicationInRelation.getApplicationView().getElementRegion().prefWidthProperty().divide(2.0);
		DoubleProperty layoutY = this.applicationInRelation.getApplicationView().getElementRegion().layoutYProperty();
		DoubleBinding height = this.applicationInRelation.getApplicationView().getElementRegion().prefHeightProperty().divide(2.0);
		this.relationLine.startXProperty().bind((ObservableValue<? extends Number>)layoutX.add((ObservableNumberValue)width));
		this.relationLine.startYProperty().bind((ObservableValue<? extends Number>)layoutY.add((ObservableNumberValue)height));
		//this.calculateCenterPoint();
		this.relationNodes = new LinkedList<Node>();
		relationNodes.add((Node)this.relationLine);
		for(Node node : this.relationNodes) {
			node.setOnMouseClicked(e -> {
				this.setSelected(true);
				e.consume();
			});
		}
	}
	
	//Setter-Methode f�r die Auswahl einer Beziehungslinie
	public void setSelected(boolean selected) {
		this.getSelectedProperty().set(selected);
	}

	//Getter-Methode f�r das Property der Auswahl
	private BooleanProperty getSelectedProperty() {
		if(this.selectedProperty == null) {
			this.selectedProperty = new SimpleBooleanProperty(this, "selected", false);
		}
		return this.selectedProperty;
	}

	//Getter-Methode f�r die Knoten der Beziehung
	public LinkedList<Node> getRelationNodes() {
		return this.relationNodes;
	}

	//Getter-Methode f�r eine an der Beziehung beteiligte Anwendung
	public ApplicationInRelation getApplicationInRelation() {
		return this.applicationInRelation;
	}

	//Methode zur Erstellung einer Beziehungslinie als XML-Element, welches in einer Datei exportiert werden kann
	public Element createXMLElement(Document doc) {
		Element relationXML = doc.createElement("Beziehungslinie");
		Element applicationInRelation = this.getApplicationInRelation().createXMLElement(doc);
		relationXML.appendChild(applicationInRelation);
		return relationXML;
	}

	//Methode zum Aufl�sen der Beziehungslinie
	public void unbindRelation() {
		this.relationLine.getSelectedProperty().unbind();
	}

	//Getter-Methode f�r die pr�sentierte Beziehungslinie
	public RelationLine getRelationLine() {
		return this.relationLine;
	}

	//Methode zum Hinzuf�gen einer Beziehungslinie aus einem XML-Dokument
	public static RelationLineView importFromXML(Element element, ModelView modelView) {
		ApplicationInRelation applicationInRelation = ApplicationInRelation.importFromXML((Element)element.getElementsByTagName("Beziehungsteilnehmer").item(0), modelView);
		RelationLineView relationLineView = new RelationLineView(applicationInRelation);
		return relationLineView;
	}
	
	//Methode zur Pr�fung, ob eine Beziehungslinie ausgew�hlt wurde
	private boolean isSelected() {
		if(this.selectedProperty != null && this.getSelectedProperty().get()) {
			return true;
		}
		else {
			return false;
		}
	}
		
	//Methode zur Pr�fung, ob eine Beziehungslinie mit der hier pr�sentierten �bereinstimmt
	@Override
	public boolean equals(Object object) {
		RelationLineView relationLineView = (RelationLineView)object;
		if(super.equals(object) && this.isSelected() == relationLineView.isSelected() && this.getApplicationInRelation().equals(relationLineView.getApplicationInRelation()) && this.getRelationNodes().equals(relationLineView.getRelationNodes())) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
