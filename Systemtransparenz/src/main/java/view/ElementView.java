package view;

import java.util.LinkedList;

import control.ElementControl;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;

//Abstrakte Klasse zur Präsentation eines Elements in einem Modell
public abstract class ElementView {

	protected ModelView modelView;
	private LinkedList<ElementView> elementViews;
	private double differenceWidth;
	private double differenceHeight;
	
	//Konstruktor
	public ElementView() {
		this.elementViews = new LinkedList<ElementView>();
	}
	
	//Getter-Methode für die präsentierten Elemente
	public LinkedList<ElementView> getElements() {
		return this.elementViews;
	}
	
	//Getter-Methode für die zugehörige Modell-Ansicht des präsentierten Elements
	public ModelView getModelView() {
		return this.modelView;
	}
	
	//Getter-Methode für den Unterschied in der Weite eines präsentierten Elements
	public double getDifferenceWidth() {
		return this.differenceWidth;
	}
	
	//Getter-Methode für den Unterschied in der Höhe eines präsentierten Elements
	public double getDifferenceHeight() {
		return this.differenceHeight;
	}
	
	//Setter-Methode für die zugehörige Modell-Ansicht
	public void setModelView(ModelView modelView) {
		this.modelView = modelView;
	}
		
	//Setter-Methode für den Unterschied in der Weite eines präsentierten Elements
	public void setDifferenceWidth(double differenceWidth) {
		this.differenceWidth = differenceWidth;
	}
	
	//Setter-Methode für den Unterschied in der Höhe eines präsentierten Elements
	public void setDifferenceHeight(double differenceHeight) {
		this.differenceHeight = differenceHeight;
	}

	//Methode zur Prüfung, ob ein Element in der Präsentation vorhanden ist
	public boolean containsElement(ElementView element) {
		if(this.elementViews.contains(element)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Abstrakte Getter-Methode für die Fläche des präsentierten Elements
	public abstract Region getElementRegion();
	
	//Abstrakte Getter-Methode für die Steuerung des präsentierten Elements
	public abstract ElementControl getElementControl();
	
	//Abstrakte Methode für die Ansichts-Vergrößerung bzw. -Verkleinerung des präsentierten Elements
	public abstract void zoom(double factor);
	
	//Abstrakte Methode zur Prüfung, ob eine präsentierte Anwendung bewegbar ist
	public abstract boolean isMoveable();
	
	//Abstrakte Methode zur Festlegung, ob das präsentierte Element bewegt wurde
	public abstract void setMoved(double x, double y);
	
	//Abstrakte Methode zur Bewegung des präsentierten Elements innerhalb des Modells
	public abstract void move(double x, double y);
	
	//Abstrakte Methode zum Hinzufügen eines Elements zur Präsentation
	public abstract void addElement(ElementView element);
	
	//Methode zum Entfernen eines präsentierten Elements
	public void removeElement(ElementView elementView) {
		if(!containsElement(elementView)) {
			for(ElementView eV : this.elementViews) {
				eV.removeElement(elementView);
			}
		}
		else {
			this.elementViews.remove(elementView);
		}
	}

	//Setter-Methode für die X, Y-Koordinaten eines präsentierten Elements
	public void setLayout(double x, double y) {
		this.getElementRegion().setLayoutX(x);
		double extension = 100.0;
		if(this.getElementRegion().getLayoutX() + this.getElementRegion().getPrefWidth() > this.getModelView().getPrefWidth()) {
			this.getModelView().setPrefWidth(this.getModelView().getPrefWidth() + extension);
		}
		this.getElementRegion().setLayoutY(y);
		if(this.getElementRegion().getLayoutY() + this.getElementRegion().getPrefHeight() > this.getModelView().getPrefHeight()) {
			this.getModelView().setPrefHeight(this.getModelView().getPrefHeight() + extension);
		}
	}
	
	//Getter-Methode für die X, Y-Koordinaten eines präsentierten Elements
	public Point2D getLayout() {
		double x = this.getElementRegion().getLayoutX();
		double y = this.getElementRegion().getLayoutY();
		Point2D coordinates = new Point2D(x, y);
		return coordinates;
	}

	//Setter-Methode für die Weite und Höhe eines präsentierten Elements
	public void setSize(double x, double y) {
		this.getElementRegion().setPrefSize(x, y);
		double extension = 100.0;
		if(this.getElementRegion().getLayoutX() + this.getElementRegion().getPrefWidth() > this.getModelView().getPrefWidth()) {
			this.getModelView().setPrefWidth(this.getModelView().getPrefWidth() + extension);
		}
		if(this.getElementRegion().getLayoutY() + this.getElementRegion().getPrefHeight() > this.getModelView().getPrefHeight()) {
			this.getModelView().setPrefHeight(this.getModelView().getPrefHeight() + extension);
		}
		this.getModelView().getFileExportControl().setSaved(false);
	}
	
	//Methode zur Prüfung, ob eine Elements-Ansicht der hier präsentierten entspricht
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		ElementView elementView = (ElementView)object;
		if(this.getModelView() != null && elementView.getModelView() != null ) {
			if(this.getModelView().equals(elementView.getModelView()) && this.getElementRegion().getPrefHeight() == elementView.getElementRegion().getPrefHeight() && this.getElementRegion().getPrefWidth() == elementView.getElementRegion().getPrefWidth() && this.getLayout().equals(elementView.getLayout()) && this.getElementRegion().equals(elementView.getElementRegion())) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(this.getModelView() == null && elementView.getModelView() == null && (this.getElementRegion().getPrefHeight() == elementView.getElementRegion().getPrefHeight() && this.getElementRegion().getPrefWidth() == elementView.getElementRegion().getPrefWidth() && this.getLayout().equals(elementView.getLayout()) && this.getElementRegion().equals(elementView.getElementRegion())) && this.differenceWidth == elementView.getDifferenceWidth() && this.differenceHeight == elementView.getDifferenceHeight() && this.getElements() == elementView.getElements()) {
			return true;
		}
		else {
			return false;
		}
	}

}
