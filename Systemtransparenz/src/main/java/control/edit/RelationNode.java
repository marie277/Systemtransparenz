package control.edit;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import view.ModelView;

//Klasse zur Präsentation einer Beziehungslinie
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
	private double x;
    private double y;
	
	//Konstruktor
	public RelationNode(ApplicationInRelation applicationInRelation, String relationType, boolean relationDirection) {
		this.applicationInRelation = applicationInRelation;
		this.relationType = relationType;
		this.relationDirection = relationDirection;
		this.relationLine = new RelationLine();
		this.relationLine.getSelectedProperty().bind(this.getSelectedProperty());
		DoubleProperty layoutX = this.applicationInRelation.getApplicationView().getElementRegion().layoutXProperty();
		DoubleBinding width = this.applicationInRelation.getApplicationView().getElementRegion().prefWidthProperty().divide(2.0);
		DoubleProperty layoutY = this.applicationInRelation.getApplicationView().getElementRegion().layoutYProperty();
		DoubleBinding height = this.applicationInRelation.getApplicationView().getElementRegion().prefHeightProperty().divide(2.0);
		this.relationLine.startXProperty().bind(layoutX.add(width));
		this.relationLine.startYProperty().bind(layoutY.add(height));
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
		if((relationType.equals("Nutzt") || relationType.equals("Hat")) && ((relationDirection == true 
				&& this.getApplicationInRelation().getIdProperty().get() % 2 == 0) || 
				(relationDirection == false 
				&& this.getApplicationInRelation().getIdProperty().get() % 2 == 1) )) {
			dx = this.relationLine.endXProperty().add(this.relationLine.startXProperty().negate());
		    dy = this.relationLine.endYProperty().add(this.relationLine.startYProperty().negate());
		    Rotate rotate = new Rotate(0,0,0,1,Rotate.Z_AXIS);
		    this.relationArrow.getTransforms().add(rotate);
		    dx.addListener(new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue,
						Object newValue) {
					rotate.setAngle(Math.toDegrees(Math.atan2(dy.doubleValue(), (double) newValue)));
				}
		    });
		    dy.addListener(new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, 
						Object newValue) {
					rotate.setAngle(Math.toDegrees(Math.atan2((double)newValue, dx.doubleValue())));
				}
		    });
			this.relationArrow.getSelectedProperty().bind(this.getSelectedProperty());
			this.relationArrow.getRelationDirectionProperty().bind(this.getRelationDirectionProperty());
	        this.getRelationDirectionProperty().bind(this.applicationInRelation.getRelationDirectionProperty());
			this.getRelationHub();
	        this.relationNodes.add((Node)this.relationArrow);
		}
	}
	
	//Getter-Methode für die präsentierte Beziehungslinie
	public RelationLine getRelationLine() {
		return this.relationLine;
	}
	
	//Getter-Methode für die präsentierte Beziehungslinie
	public RelationArrow getRelationArrow() {
		return this.relationArrow;
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
	
	//Setter-Methode für die Auswahl einer Beziehungslinie
	public void setSelected(boolean selected) {
		this.getSelectedProperty().set(selected);
	}
	
	//Methode zur Bestimmung der Position des Beziehungspfeils
	public void getRelationHub(){  
        double slope = (this.relationLine.getEndY() - this.relationLine.getStartY()) / (this.relationLine.getEndX() - this.relationLine.getStartX());
        double point = this.relationLine.getStartY() - (this.relationLine.getEndY() - this.relationLine.getStartY()) / (this.relationLine.getEndX() - this.relationLine.getStartX()) * this.relationLine.getStartX();
        double applicationLayoutX = this.applicationInRelation.getApplicationView().getLayout().getX();
        double applicationLayoutY = this.applicationInRelation.getApplicationView().getLayout().getY();
        double applicationPrefWidth = this.applicationInRelation.getApplicationView().getElementRegion().getPrefWidth();
        double applicationPrefHeight = this.applicationInRelation.getApplicationView().getElementRegion().getPrefHeight();
        if(this.relationLine.getStartY() > this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = applicationLayoutY;
            x = (y - point) / slope;
        }
        else if(this.relationLine.getStartY() < this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = applicationLayoutY + applicationPrefHeight;
            x = (y - point) / slope;
        }
        else if(this.relationLine.getStartX() > this.relationLine.getEndX()) {
            x = applicationLayoutX;
            y = slope * x + point;
        }
        else {
            x = applicationLayoutX + applicationPrefWidth;
            y = slope * x + point;
        }
        this.relationArrow.setLayoutX(x);
        this.relationArrow.setLayoutY(y);
	}

	//Getter-Methode für das Property der Beziehungsrichtung
	private BooleanProperty getRelationDirectionProperty() {
		if(this.relationDirectionProperty == null) {
			this.relationDirectionProperty = new SimpleBooleanProperty(this, "relationDirection", false);
		}
		return this.relationDirectionProperty;
	}

	//Methode zum Auflösen der Beziehungslinie
	public void unbindRelation() {
		this.relationLine.getSelectedProperty().unbind();
		this.relationLine.startXProperty().unbind();
        this.relationLine.startYProperty().unbind();
        this.relationArrow.getSelectedProperty().unbind();
        this.relationArrow.getRelationDirectionProperty().unbind();
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

	//Methode zum Hinzufügen einer Beziehungslinie aus einem XML-Dokument
	public static RelationNode importFromXML(Element element, ModelView modelView) {
		ApplicationInRelation applicationInRelation = ApplicationInRelation.importFromXML((Element)element.getElementsByTagName("Beziehungsteilnehmer").item(0), modelView);
		String relationType = element.getAttribute("Beziehungstyp");
		boolean relationDirection = Boolean.parseBoolean(element.getAttribute("Beziehungsrichtung"));
		RelationNode relationNode = new RelationNode(applicationInRelation, relationType, relationDirection);
		return relationNode;
	}
	
}
