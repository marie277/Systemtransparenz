package view;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import control.edit.RelationLine;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import model.ApplicationInRelation;

//Klasse zur Präsentation einer Beziehungslinie
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
		this.calculateCenterPoint();
		this.relationNodes = new LinkedList<Node>();
		relationNodes.add((Node)this.relationLine);
		for(Node node : this.relationNodes) {
			node.setOnMouseClicked(e -> {
				this.setSelected(true);
				e.consume();
			});
		}
	}
	
	//Setter-Methode für die Auswahl einer Beziehungslinie
	public void setSelected(boolean selected) {
		this.getSelectedProperty().set(selected);
	}

	//Methode zum Berechnen der Koordinaten des Mittelpunktes der Beziehungslinie
	public void calculateCenterPoint() {
		Point2D applicationCenterPoint = new Point2D(this.relationLine.getStartX(), this.relationLine.getStartY());
		Point2D lineCenterPoint = new Point2D(this.relationLine.getEndX(), this.relationLine.getEndY());
		double m = (lineCenterPoint.getY() - applicationCenterPoint.getY()) / (lineCenterPoint.getX() - applicationCenterPoint.getX());
        double b = applicationCenterPoint.getY() - (lineCenterPoint.getY() - applicationCenterPoint.getY()) / (lineCenterPoint.getX() - applicationCenterPoint.getX()) * applicationCenterPoint.getX();
        double x = 0.0;
        double y = 0.0;
        if (this.relationLine.getStartY() > this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = this.applicationInRelation.getApplicationView().getLayout().getY();
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartY() < this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = this.applicationInRelation.getApplicationView().getLayout().getY() + this.applicationInRelation.getApplicationView().getHeight();
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            x = this.applicationInRelation.getApplicationView().getLayout().getX();
            y = m * x + b;
        }
        else {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() + this.applicationInRelation.getApplicationView().getWidth();
            y = m * x + b;
        }
	}

	//Getter-Methode für das Property der Auswahl
	private BooleanProperty getSelectedProperty() {
		if(this.selectedProperty == null) {
			this.selectedProperty = new SimpleBooleanProperty(this, "selected", false);
		}
		return this.selectedProperty;
	}

	//Getter-Methode für die Knoten der Beziehung
	public LinkedList<Node> getRelationNodes() {
		return this.relationNodes;
	}

	//Getter-Methode für eine an der Beziehung beteiligte Anwendung
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

	//Methode zum Auflösen der Beziehungslinie
	public void unbindRelation() {
		this.relationLine.getSelectedProperty().unbind();
	}

	//Getter-Methode für die präsentierte Beziehungslinie
	public RelationLine getRelationLine() {
		return this.relationLine;
	}

	//Methode zum Hinzufügen einer Beziehungslinie aus einem XML-Dokument
	public static RelationLineView importFromXML(Element element, ModelView modelView) {
		ApplicationInRelation applicationInRelation = ApplicationInRelation.importFromXML((Element)element.getElementsByTagName("Beziehungsteilnehmer").item(0), modelView);
		RelationLineView relationLineView = new RelationLineView(applicationInRelation);
		return relationLineView;
	}
	
	//Methode zur Prüfung, ob eine Beziehungslinie ausgewählt wurde
	private boolean isSelected() {
		if(this.selectedProperty != null && this.getSelectedProperty().get()) {
			return true;
		}
		else {
			return false;
		}
	}
		
	//Methode zur Prüfung, ob eine Beziehungslinie mit der hier präsentierten übereinstimmt
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
