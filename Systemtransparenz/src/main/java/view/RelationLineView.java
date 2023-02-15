package view;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import control.edit.RelationArrow;
import control.edit.RelationLine;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.ApplicationInRelation;

//Klasse zur Präsentation einer Beziehungslinie
public class RelationLineView {

	private BooleanProperty selectedProperty;
	private boolean relationDirection;
	private BooleanProperty relationDirectionProperty;
	private String relationType;
	private RelationLine relationLine;
	private RelationLine relationLine1;
	private RelationLine relationLine2;
	private ApplicationInRelation applicationInRelation;
	private LinkedList<Node> relationNodes;
	private RelationArrow relationArrow;
	private Pane pane;
	private DoubleBinding dx;
	private DoubleBinding dy;
	
	//Konstruktor
	public RelationLineView(ApplicationInRelation applicationInRelation, String relationType, boolean relationDirection) {
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
		double length = 20.0;
		double angle = 75.0;
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
		        rotate.setAngle(getAngle(dy.doubleValue(), newValue.doubleValue()));
		    });
		    dy.addListener((observable, oldValue, newValue) -> {
		        rotate.setAngle(getAngle(newValue.doubleValue(), dx.doubleValue()));
		    });
			this.relationArrow.getSelectedProperty().bind((ObservableValue<? extends Boolean>)this.getSelectedProperty());
			this.relationArrow.getRelationDirectionProperty().bind((ObservableValue<? extends Boolean>)this.getRelationDirectionProperty());
	        this.getRelationDirectionProperty().bind((ObservableValue<? extends Boolean>)this.applicationInRelation.getRelationDirectionProperty());
	        this.getRelationDirectionProperty().bind((ObservableValue<? extends Boolean>)this.applicationInRelation.getRelationDirectionProperty());
			this.relationNodes.add((Node)this.relationArrow);
		}
		for(Node node : this.relationNodes) {
			node.setOnMouseClicked(e -> {
				this.setSelected(true);
				e.consume();
			});
		}	
	}
	
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
            y = this.applicationInRelation.getApplicationView().getLayout().getY() + this.applicationInRelation.getApplicationView().getHeight() + this.relationArrow.getLayoutBounds().getHeight() * 4.0;
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() - this.relationArrow.getLayoutBounds().getHeight() * 2.0;
            y = m * x + b;
        }
        else {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() + this.applicationInRelation.getApplicationView().getWidth() + this.relationArrow.getLayoutBounds().getHeight() * 3.0;
            y = m * x + b;
        }
        relationHub = new Point2D(x, y);
        this.relationArrow.setLayoutX(relationHub.getX());
        this.relationArrow.setLayoutY(relationHub.getY());
        System.out.println(this.relationArrow.getLayoutX()+", "+ this.relationArrow.getLayoutY());
       
	}
	
	private void canvas() {
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
            y = this.applicationInRelation.getApplicationView().getLayout().getY() + this.applicationInRelation.getApplicationView().getHeight() + this.relationArrow.getLayoutBounds().getHeight() * 4.0;
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() - this.relationArrow.getLayoutBounds().getHeight() * 2.0;
            y = m * x + b;
        }
        else {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() + this.applicationInRelation.getApplicationView().getWidth() + this.relationArrow.getLayoutBounds().getHeight() * 3.0;
            y = m * x + b;
        }
        relationHub = new Point2D(x, y);
        this.relationArrow.setLayoutX(relationHub.getX());
        this.relationArrow.setLayoutY(relationHub.getY());
        System.out.println(this.relationArrow.getLayoutX()+", "+ this.relationArrow.getLayoutY());
	}

	private double getAngle(double dy ,double dx){
	    return Math.toDegrees(Math.atan2(dy, dx));
	}

	public String getRelationType() {
		return this.relationType;
	}
	
	public boolean getRelationDirection() {
		return this.relationDirection;
	}

	private BooleanProperty getRelationDirectionProperty() {
		if(this.relationDirectionProperty == null) {
			this.relationDirectionProperty = new SimpleBooleanProperty(this, "relationDirection", false);
		}
		return this.relationDirectionProperty;
	}

	/*public void getRelationHub() {
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
            y = this.applicationInRelation.getApplicationView().getLayout().getY() + this.applicationInRelation.getApplicationView().getHeight() + this.relationArrow.getLayoutBounds().getHeight() * 4.0;
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() - this.relationArrow.getLayoutBounds().getHeight() * 2.0;
            y = m * x + b;
        }
        else {
            x = this.applicationInRelation.getApplicationView().getLayout().getX() + this.applicationInRelation.getApplicationView().getWidth() + this.relationArrow.getLayoutBounds().getHeight() * 3.0;
            y = m * x + b;
        }
        relationHub = new Point2D(x, y);
        this.relationArrow.setLayoutX(relationHub.getX());
        this.relationArrow.setLayoutY(relationHub.getY());
        System.out.println(this.relationArrow.getLayoutX()+", "+ this.relationArrow.getLayoutY());
    }*/
	
	//Setter-Methode für die Auswahl einer Beziehungslinie
	public void setSelected(boolean selected) {
		this.getSelectedProperty().set(selected);
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

	//Methode zum Auflösen der Beziehungslinie
	public void unbindRelation() {
		this.relationLine.getSelectedProperty().unbind();
		this.relationLine.startXProperty().unbind();
        this.relationLine.startYProperty().unbind();
        if((relationType.equals("Nutzt") || relationType.equals("Hat")) && ((relationDirection == true && this.getApplicationInRelation().getIdProperty().get() % 2 == 0) || (relationDirection == false && this.getApplicationInRelation().getIdProperty().get() % 2 == 1) )) {
            this.relationArrow.layoutXProperty().unbind();
            this.relationArrow.layoutYProperty().unbind();
		}
    
        //this.relationArrow.getSelectedProperty().unbind();
        //this.relationArrow.getRelationDirectionProperty().unbind();
	}

	//Getter-Methode für die präsentierte Beziehungslinie
	public RelationLine getRelationLine() {
		return this.relationLine;
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
	public static RelationLineView importFromXML(Element element, ModelView modelView) {
		ApplicationInRelation applicationInRelation = ApplicationInRelation.importFromXML((Element)element.getElementsByTagName("Beziehungsteilnehmer").item(0), modelView);
		String relationType = element.getAttribute("Beziehungstyp");
		boolean relationDirection = Boolean.parseBoolean(element.getAttribute("Beziehungsrichtung"));
		RelationLineView relationLineView = new RelationLineView(applicationInRelation, relationType, relationDirection);
		return relationLineView;
	}
		
	//Methode zur Prüfung, ob eine Beziehungslinie mit der hier präsentierten übereinstimmt
	@Override
	public boolean equals(Object object) {
		RelationLineView relationLineView = (RelationLineView)object;
		if(super.equals(object) && this.getApplicationInRelation().equals(relationLineView.getApplicationInRelation()) && this.getRelationNodes().equals(relationLineView.getRelationNodes())) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
