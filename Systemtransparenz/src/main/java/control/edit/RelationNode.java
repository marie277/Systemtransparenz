package control.edit;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import view.ModelView;

//Klasse zur Pr�sentation einer Beziehungslinie
public class RelationNode {

	private BooleanProperty selectedProperty;
	private boolean relationDirection;
	private BooleanProperty relationDirectionProperty;
	private String relationType;
	private RelationLine relationLine;
	private ApplicationInRelation applicationInRelation;
	private LinkedList<Node> relationNodes;
	private RelationArrow relationArrow;
	private DoubleBinding dx;
	private DoubleBinding dy;
	
	//Konstruktor
	public RelationNode(ApplicationInRelation applicationInRelation, String relationType, boolean relationDirection) {
		this.applicationInRelation = applicationInRelation;
		this.relationType = relationType;
		this.relationDirection = relationDirection;
		this.relationLine = new RelationLine();
		this.relationLine.getSelectedProperty().bind((ObservableValue<? extends Boolean>)this.getSelectedProperty());
		DoubleProperty layoutX = this.applicationInRelation.getApplicationView().getElementRegion().layoutXProperty();
		DoubleBinding width = this.applicationInRelation.getApplicationView().getElementRegion().prefWidthProperty().divide(2.0);
		DoubleProperty layoutY = this.applicationInRelation.getApplicationView().getElementRegion().layoutYProperty();
		DoubleBinding height = this.applicationInRelation.getApplicationView().getElementRegion().prefHeightProperty().divide(2.0);
		this.relationLine.startXProperty().bind((ObservableValue<? extends Number>)layoutX.add((ObservableNumberValue)width));
		this.relationLine.startYProperty().bind((ObservableValue<? extends Number>)layoutY.add((ObservableNumberValue)height));
		this.relationNodes = new LinkedList<Node>();
		this.relationNodes.add((Node)this.relationLine);
		this.relationArrow = new RelationArrow();
		double length = 10.0;
		double angle = 90.0;
		this.relationArrow.getPoints().addAll(new Double[]{
			0d, 0d,
			-(length * Math.tan(angle)), -length,
	        -(length * Math.tan(angle)), length,
	    });
		if((relationType.equals("Nutzt") || relationType.equals("Hat")) && ((relationDirection == true && this.getApplicationInRelation().getIdProperty().get() % 2 == 0) || (relationDirection == false && this.getApplicationInRelation().getIdProperty().get() % 2 == 1) )) {
			dx = this.relationLine.endXProperty().add(this.relationLine.startXProperty().negate());
		    dy = this.relationLine.endYProperty().add(this.relationLine.startYProperty().negate());
		    Rotate rotate = new Rotate(0,0,0,1,Rotate.Z_AXIS);
		    this.relationArrow.getTransforms().add(rotate);
		    dx.addListener((observable, oldValue, newValue) -> {
		        rotate.setAngle(Math.toDegrees(Math.atan2(dy.doubleValue(), newValue.doubleValue())));
		    });
		    dy.addListener((observable, oldValue, newValue) -> {
		        rotate.setAngle(Math.toDegrees(Math.atan2(newValue.doubleValue(), dx.doubleValue())));
		    });
			this.relationArrow.getSelectedProperty().bind((ObservableValue<? extends Boolean>)this.getSelectedProperty());
			this.relationArrow.getRelationDirectionProperty().bind((ObservableValue<? extends Boolean>)this.getRelationDirectionProperty());
	        this.getRelationDirectionProperty().bind((ObservableValue<? extends Boolean>)this.applicationInRelation.getRelationDirectionProperty());
	        this.getRelationDirectionProperty().bind((ObservableValue<? extends Boolean>)this.applicationInRelation.getRelationDirectionProperty());
			this.getRelationHub();
	        this.relationNodes.add((Node)this.relationArrow);
		}
		for(Node node : this.relationNodes) {
			node.setOnMouseClicked(e -> {
				this.setSelected(true);
				e.consume();
			});
		}	
	}
	
	//Methode zur Bestimmung der Position des Beziehungspfeils
	public void getRelationHub(){  
		Point2D relationHub = null;
        Point2D applicationHub = new Point2D(this.relationLine.getStartX(), this.relationLine.getStartY());
        Point2D relationIntersection = new Point2D(this.relationLine.getEndX(), this.relationLine.getEndY());
        double x = 0.0;
        double y = 0.0;
        double m = (relationIntersection.getY() - applicationHub.getY()) / (relationIntersection.getX() - applicationHub.getX());
        double b = applicationHub.getY() - (relationIntersection.getY() - applicationHub.getY()) / (relationIntersection.getX() - applicationHub.getX()) * applicationHub.getX();
        if (this.relationLine.getStartY() > this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = this.applicationInRelation.getApplicationView().getLayout().getY() - this.relationArrow.getLayoutBounds().getHeight() * 2.0;
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartY() < this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = this.applicationInRelation.getApplicationView().getLayout().getY() + this.applicationInRelation.getApplicationView().getElementRegion().getPrefHeight() + this.relationArrow.getLayoutBounds().getHeight() * 4.0;
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() - this.relationArrow.getLayoutBounds().getHeight() * 2.0;
            y = m * x + b;
        }
        else {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() + this.applicationInRelation.getApplicationView().getElementRegion().getPrefWidth() + this.relationArrow.getLayoutBounds().getHeight() * 3.0;
            y = m * x + b;
        }
        relationHub = new Point2D(x, y);
        this.relationArrow.setLayoutX(relationHub.getX());
        this.relationArrow.setLayoutY(relationHub.getY());
	}

	//Getter-Methode f�r das Property der Beziehungsrichtung
	private BooleanProperty getRelationDirectionProperty() {
		if(this.relationDirectionProperty == null) {
			this.relationDirectionProperty = new SimpleBooleanProperty(this, "relationDirection", false);
		}
		return this.relationDirectionProperty;
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

	//Methode zum Aufl�sen der Beziehungslinie
	public void unbindRelation() {
		this.relationLine.getSelectedProperty().unbind();
		this.relationLine.startXProperty().unbind();
        this.relationLine.startYProperty().unbind();
        this.relationArrow.getSelectedProperty().unbind();
        this.relationArrow.getRelationDirectionProperty().unbind();
	}

	//Getter-Methode f�r die pr�sentierte Beziehungslinie
	public RelationLine getRelationLine() {
		return this.relationLine;
	}
	
	//Getter-Methode f�r die pr�sentierte Beziehungslinie
	public RelationArrow getRelationArrow() {
		return this.relationArrow;
	}
	
	//Methode zur Erstellung einer Beziehungslinie als XML-Element, welches in einer Datei exportiert werden kann
	public Element createXMLElement(Document doc) {
		Element relationLine = doc.createElement("Beziehungslinie");
		relationLine.setAttribute("Beziehungstyp", this.relationType);
		relationLine.setAttribute("Beziehungsrichtung", String.valueOf(this.relationDirection));
		Element applicationInRelation = this.getApplicationInRelation().createXMLElement(doc);
		relationLine.appendChild(applicationInRelation);
		return relationLine;
	}

	//Methode zum Hinzuf�gen einer Beziehungslinie aus einem XML-Dokument
	public static RelationNode importFromXML(Element element, ModelView modelView) {
		ApplicationInRelation applicationInRelation = ApplicationInRelation.importFromXML((Element)element.getElementsByTagName("Beziehungsteilnehmer").item(0), modelView);
		String relationType = element.getAttribute("Beziehungstyp");
		boolean relationDirection = Boolean.parseBoolean(element.getAttribute("Beziehungsrichtung"));
		RelationNode relationLineView = new RelationNode(applicationInRelation, relationType, relationDirection);
		return relationLineView;
	}
		
	//Methode zur Pr�fung, ob eine Beziehungslinie mit der hier pr�sentierten �bereinstimmt
	@Override
	public boolean equals(Object object) {
		RelationNode relationLineView = (RelationNode)object;
		if(super.equals(object) && this.getApplicationInRelation().equals(relationLineView.getApplicationInRelation()) && this.getRelationNodes().equals(relationLineView.getRelationNodes())) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
