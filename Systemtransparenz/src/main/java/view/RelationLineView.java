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
import javafx.scene.Node;
import model.ApplicationInRelation;

//Klasse zur Präsentation einer Beziehungslinie
public class RelationLineView {

	private BooleanProperty selectedProperty;
	private BooleanProperty arrowDirectionProperty;
	private RelationLine relationLine;
	private ApplicationInRelation applicationInRelation;
	private LinkedList<Node> relationNodes;
	private RelationArrow circle;
	
	//Konstruktor
	public RelationLineView(ApplicationInRelation applicationInRelation, String relationType, boolean arrowDirection) {
		this.applicationInRelation = applicationInRelation;
		this.relationLine = new RelationLine();
		this.relationLine.getSelectedProperty().bind((ObservableValue<? extends Boolean>)this.getSelectedProperty());
		DoubleProperty layoutX = this.applicationInRelation.getApplicationView().getElementRegion().layoutXProperty();
		DoubleBinding width = this.applicationInRelation.getApplicationView().getElementRegion().prefWidthProperty().divide(2.0);
		DoubleProperty layoutY = this.applicationInRelation.getApplicationView().getElementRegion().layoutYProperty();
		DoubleBinding height = this.applicationInRelation.getApplicationView().getElementRegion().prefHeightProperty().divide(2.0);
		this.relationLine.startXProperty().bind((ObservableValue<? extends Number>)layoutX.add((ObservableNumberValue)width));
		this.relationLine.startYProperty().bind((ObservableValue<? extends Number>)layoutY.add((ObservableNumberValue)height));
		if(relationType == "Nutzt") {
			this.relationLine.setStyle("-fx-stroke-dash-array: 2 12 12 2; ");
		}
		(this.circle = new RelationArrow()).setId("circle");
		if((relationType.equals("Nutzt") || relationType.equals("Hat")) && ((arrowDirection == true && this.getApplicationInRelation().getIdProperty().get() % 2 == 0) || (arrowDirection == false && this.getApplicationInRelation().getIdProperty().get() % 2 == 1) )) {
			//this.circle.setRadius(5.0);
			this.circle.getPoints().addAll(new Double[]{
		            0.0, 0.0,
		            20.0, 10.0,
		            10.0, 20.0 });
			/*this.circle.getPoints().addAll(new Double[]{
		            20.0, 10.0,
		            10.0, 20.0,
		            0.0, 0.0 });*/
			this.circle.getSelectedProperty().bind((ObservableValue<? extends Boolean>)this.getSelectedProperty());
			this.circle.getArrowDirectionProperty().bind((ObservableValue<? extends Boolean>)this.getArrowDirectionProperty());
	        this.getArrowDirectionProperty().bind((ObservableValue<? extends Boolean>)this.applicationInRelation.getArrowDirectionProperty());
		}
        this.calculateCenterPoint();
		this.relationNodes = new LinkedList<Node>();
	    relationNodes.add((Node)this.relationLine);
	    relationNodes.add((Node)this.circle);
		for(Node node : this.relationNodes) {
			node.setOnMouseClicked(e -> {
				this.setSelected(true);
				e.consume();
			});
		}	
	}

	private BooleanProperty getArrowDirectionProperty() {
		if(this.arrowDirectionProperty == null) {
			this.arrowDirectionProperty = new SimpleBooleanProperty(this, "arrowDirection", false);
		}
		return this.arrowDirectionProperty;
	}

	public void calculateCenterPoint() {
        Point2D entitaetsMittelPunkt = new Point2D(this.relationLine.getStartX(), this.relationLine.getStartY());
        Point2D rauteMittelPunkt = new Point2D(this.relationLine.getEndX(), this.relationLine.getEndY());
        boolean kannKreisRechts = false;
        boolean kannKreisLinks = false;
        boolean kannKreisUnten = false;
        boolean kannKreisOben = false;
        double newX = 0.0;
        double newY = 0.0;
        /*double m = (rauteMittelPunkt.getY() - entitaetsMittelPunkt.getY()) / (rauteMittelPunkt.getX() - entitaetsMittelPunkt.getX());
        double b = entitaetsMittelPunkt.getY() - (rauteMittelPunkt.getY() - entitaetsMittelPunkt.getY()) / (rauteMittelPunkt.getX() - entitaetsMittelPunkt.getX()) * entitaetsMittelPunkt.getX();
        if (this.relationLine.getStartY() > this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            newY = this.applicationInRelation.getApplicationView().getLayout().getY() - this.circle.getRadius() * 2.0;
            newX = (newY - b) / m;
        }
        else if (this.relationLine.getStartY() < this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            newY = this.applicationInRelation.getApplicationView().getLayout().getY() + this.applicationInRelation.getApplicationView().getHeight() + this.circle.getRadius() * 4.0;
            newX = (newY - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            newX = this.applicationInRelation.getApplicationView().getLayout().getX() - this.circle.getRadius() * 2.0;
            newY = m * newX + b;
        }
        else {
            newX = this.applicationInRelation.getApplicationView().getLayout().getX() + this.applicationInRelation.getApplicationView().getWidth() + this.circle.getRadius() * 3.0;
            newY = m * newX + b;
        }
        kannKreisUnten = (this.relationLine.getStartY() < this.relationLine.getEndY());
        kannKreisLinks = (this.relationLine.getStartX() > this.relationLine.getEndX());
        kannKreisOben = !kannKreisUnten;
        kannKreisRechts = !kannKreisLinks;
        Point2D centerPoint = new Point2D(newX, newY);
        this.circle.setCenterX(centerPoint.getX());
        this.circle.setCenterY(centerPoint.getY());*/
        double m = (rauteMittelPunkt.getY() - entitaetsMittelPunkt.getY()) / (rauteMittelPunkt.getX() - entitaetsMittelPunkt.getX());
        double b = entitaetsMittelPunkt.getY() - (rauteMittelPunkt.getY() - entitaetsMittelPunkt.getY()) / (rauteMittelPunkt.getX() - entitaetsMittelPunkt.getX()) * entitaetsMittelPunkt.getX();
        if (this.relationLine.getStartY() > this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            newY = this.applicationInRelation.getApplicationView().getLayout().getY() - this.circle.getLayoutBounds().getHeight() * 2.0;
            newX = (newY - b) / m;
        }
        else if (this.relationLine.getStartY() < this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            newY = this.applicationInRelation.getApplicationView().getLayout().getY() + this.applicationInRelation.getApplicationView().getHeight() + this.circle.getLayoutBounds().getHeight() * 4.0;
            newX = (newY - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            newX = this.applicationInRelation.getApplicationView().getLayout().getX() - this.circle.getLayoutBounds().getHeight() * 2.0;
            newY = m * newX + b;
        }
        else {
            newX = this.applicationInRelation.getApplicationView().getLayout().getX() + this.applicationInRelation.getApplicationView().getWidth() + this.circle.getLayoutBounds().getHeight() * 3.0;
            newY = m * newX + b;
        }
        
        kannKreisUnten = (this.relationLine.getStartY() < this.relationLine.getEndY());
        kannKreisLinks = (this.relationLine.getStartX() > this.relationLine.getEndX());
        kannKreisOben = !kannKreisUnten;
        kannKreisRechts = !kannKreisLinks;
        Point2D centerPoint = new Point2D(newX, newY);
        //this.circle.setCenterX(centerPoint.getX());
        this.circle.setLayoutX(centerPoint.getX());
        //this.circle.setCenterY(centerPoint.getY());
        this.circle.setLayoutY(centerPoint.getY());
        /*double newBezX = this.circle.getCenterX();
        double newBezY = this.circle.getCenterY();
        if (kannKreisUnten) {
            newBezY += 4.0 * this.circle.getRadius();
        }
        if (kannKreisOben) {
            newBezY -= 3.0 * this.circle.getRadius();
        }
        if (kannKreisRechts) {
            newBezX += 3.0 * this.circle.getRadius();
        }
        if (kannKreisLinks) {
            newBezX -= 4.0 * this.circle.getRadius();
        }
        //this.beziehungstyp.setLayoutX(newBezX);
        //this.beziehungstyp.setLayoutY(newBezY);
        /*double newKonX = this.circle.getCenterX();
        double newKonY = this.circle.getCenterY();
        if (kannKreisOben && kannKreisLinks) {
            newKonY -= 3.0 * this.circle.getRadius();
            newKonX -= 1.0 * this.circle.getRadius();
        }
        else if (kannKreisOben && kannKreisRechts) {
            newKonY -= 3.0 * this.circle.getRadius();
            newKonX -= 1.0 * this.circle.getRadius();
        }
        else if (kannKreisOben) {
            newKonY -= 3.0 * this.circle.getRadius();
            newKonX -= 3.0 * this.circle.getRadius();
        }
        else if (kannKreisUnten && kannKreisLinks) {
            newKonY += 4.5 * this.circle.getRadius();
            newKonX -= 1.8 * this.circle.getRadius();
        }
        else if (kannKreisUnten && kannKreisRechts) {
            newKonY += 4.0 * this.circle.getRadius();
            newKonX -= 3.0 * this.circle.getRadius();
        }
        else {
            newKonY += 4.0 * this.circle.getRadius();
            newKonX -= 3.0 * this.circle.getRadius();
        }
        /*this.konnektivitaet.setLayoutX(newKonX);
        this.konnektivitaet.setLayoutY(newKonY);*/
    }
	
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
		this.relationLine.startXProperty().unbind();
        this.relationLine.startYProperty().unbind();
        this.circle.getSelectedProperty().unbind();
        this.circle.getArrowDirectionProperty().unbind();
	}

	//Getter-Methode für die präsentierte Beziehungslinie
	public RelationLine getRelationLine() {
		return this.relationLine;
	}

	//Methode zum Hinzufügen einer Beziehungslinie aus einem XML-Dokument
	public static RelationLineView importFromXML(Element element, ModelView modelView) {
		ApplicationInRelation applicationInRelation = ApplicationInRelation.importFromXML((Element)element.getElementsByTagName("Beziehungsteilnehmer").item(0), modelView);
		String relationType = "";
		boolean arrowDirection = true;
		RelationLineView relationLineView = new RelationLineView(applicationInRelation, relationType, arrowDirection);
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
