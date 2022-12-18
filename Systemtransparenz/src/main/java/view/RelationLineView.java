package view;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import control.edit.RelationLine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import model.ApplicationInRelation;

public class RelationLineView {

	private BooleanProperty selected;
	private RelationLine relationLine;
	private ApplicationInRelation applicationInRelation;
	private LinkedList<Node> relationNodes;
	
	public RelationLineView(ApplicationInRelation applicationInRelation) {
		this.applicationInRelation = applicationInRelation;
		this.relationLine = new RelationLine();
		this.relationLine.selectedProperty().bind((ObservableValue<? extends Boolean>)this.selectedProperty());
		this.relationLine.startXProperty().bind((ObservableValue<? extends Number>)this.applicationInRelation.getApplicationView().getElementRegion().layoutXProperty().add((ObservableNumberValue)this.applicationInRelation.getApplicationView().getElementRegion().prefWidthProperty().divide(2.0)));
		this.relationLine.startYProperty().bind((ObservableValue<? extends Number>)this.applicationInRelation.getApplicationView().getElementRegion().layoutYProperty().add((ObservableNumberValue)this.applicationInRelation.getApplicationView().getElementRegion().prefHeightProperty().divide(2.0)));
		this.calculateCenterPoint();
		(this.relationNodes = new LinkedList<Node>()).add((Node)this.relationLine);
		for(final Node node : this.relationNodes) {
			node.setOnMouseClicked(e -> {
				this.setSelected(true);
				e.consume();
			});
		}
	}
	
	void setSelected(boolean selected) {
		this.selectedProperty().set(selected);
	}

	public void calculateCenterPoint() {
		Point2D applicationCenterPoint = new Point2D(this.relationLine.getStartX(), this.relationLine.getStartY());
		Point2D lineCenterPoint = new Point2D(this.relationLine.getEndX(), this.relationLine.getEndY());
		double m = (lineCenterPoint.getY() - applicationCenterPoint.getY()) / (lineCenterPoint.getX() - applicationCenterPoint.getX());
        double b = applicationCenterPoint.getY() - (lineCenterPoint.getY() - applicationCenterPoint.getY()) / (lineCenterPoint.getX() - applicationCenterPoint.getX()) * applicationCenterPoint.getX();
        double x = 0.0;
        double y = 0.0;
        if (this.relationLine.getStartY() > this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = this.applicationInRelation.getApplicationView().getWidthHeight().getY();
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartY() < this.relationLine.getEndY() && Math.abs(this.relationLine.getStartX() - this.relationLine.getEndX()) < Math.abs(this.relationLine.getStartY() - this.relationLine.getEndY())) {
            y = this.applicationInRelation.getApplicationView().getWidthHeight().getY() + this.applicationInRelation.getApplicationView().getHeight();
            x = (y - b) / m;
        }
        else if (this.relationLine.getStartX() > this.relationLine.getEndX()) {
            x = this.applicationInRelation.getApplicationView().getWidthHeight().getX();
            y = m * x + b;
        }
        else {
            x = this.applicationInRelation.getApplicationView().getWidthHeight().getX() + this.applicationInRelation.getApplicationView().getWidth();
            y = m * x + b;
        }
        //Point2D centerPoint = new Point2D(x,y);
	}

	private BooleanProperty selectedProperty() {
		if(this.selected == null) {
			this.selected = (BooleanProperty)new SimpleBooleanProperty((Object)this, "selected", false);
		}
		return this.selected;
	}

	public LinkedList<Node> getNodes() {
		return this.relationNodes;
	}

	public ApplicationInRelation getApplicationInRelation() {
		return this.applicationInRelation;
	}

	public Element createXMLElement(Document doc) {
		Element relationXML = doc.createElement("Beziehungslinie");
		relationXML.appendChild(this.getApplicationInRelation().createXMLElement(doc));
		return relationXML;
	}

	public void unbindRelation() {
		this.relationLine.selectedProperty().unbind();
	}

	public RelationLine getRelationLine() {
		return this.relationLine;
	}

	public static RelationLineView importRelationFromXML(Element element, ModelView modelView) {
		ApplicationInRelation applicationInRelation = ApplicationInRelation.importApplicationFromRelationFromXML((Element)element.getElementsByTagName("Beziehungsteilnehmer").item(0), modelView);
		RelationLineView relationLineView = new RelationLineView(applicationInRelation);
		return relationLineView;
	}
	
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		RelationLineView rLV = (RelationLineView)object;
		return this.isSelected() == rLV.isSelected()  && this.getApplicationInRelation().equals(rLV.getApplicationInRelation()) && this.getNodes().equals(rLV.getNodes()); 
	}

	private boolean isSelected() {
		return this.selected != null && this.selectedProperty().get();
	}
	
}
