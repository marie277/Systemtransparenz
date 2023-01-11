package view;

import java.util.LinkedList;

import control.ElementControl;
import control.edit.Move;
import control.edit.Zoom;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;

//Abstrakte Klasse zur Pr�sentation eines Elements in einem Modell, implementiert Interfaces Zoom und Move
public abstract class ElementView implements Zoom, Move {

	protected ModelView modelView;
	private LinkedList<ElementView> elements;
	private double differenceWidth;
	private double differenceHeight;
	
	//Konstruktor
	public ElementView() {
		this.elements = new LinkedList<ElementView>();
	}
	
	//Setter-Methode f�r die zugeh�rige Modell-Ansicht
	public void setModelView(ModelView modelView) {
		this.modelView = modelView;
	}

	//Abstrakte Getter-Methode f�r die Fl�che des pr�sentierten Elements
	public abstract Region getElementRegion();
	
	//Abstrakte Getter-Methode f�r die Steuerung des pr�sentierten Elements
	public abstract ElementControl getElementControl();
	
	//Abstrakte Methode f�r die Ansichts-Vergr��erung bzw. -Verkleinerung des pr�sentierten Elements
	public abstract void zoom(double factor);
	
	//Abstrakte Methode zur Pr�fung, ob eine pr�sentierte Anwendung bewegbar ist
	public abstract boolean isMoveable();
	
	//Abstrakte Methode zur Festlegung, ob das pr�sentierte Element bewegt wurde
	public abstract void setMoved(double x, double y);
	
	//Abstrakte Methode zur Bewegung des pr�sentierten Elements innerhalb des Modells
	public abstract void move(double x, double y);
	
	//Abstrakte Methode zum Hinzuf�gen eines Elements zur Pr�sentation
	public abstract void addElement(ElementView element);
	
	//Methode zum Entfernen eines pr�sentierten Elements
	public void removeElement(ElementView elementView) {
		if(!containsElement(elementView)) {
			for(ElementView elementView1 : this.elements) {
				elementView1.removeElement(elementView);
			}
		}
		else {
			this.elements.remove(elementView);
		}
	}

	//Setter-Methode f�r die X, Y-Koordinaten eines pr�sentierten Elements
	public void setLayout(double x, double y) {
		this.getElementRegion().setLayoutX(x);
		if(this.getElementRegion().getLayoutX() + this.getWidth() > this.getModelView().getPrefWidth()) {
			this.getModelView().setPrefWidth(this.getModelView().getPrefWidth() + 100.0);
		}
		this.getElementRegion().setLayoutY(y);
		if(this.getElementRegion().getLayoutY() + this.getHeight() > this.getModelView().getPrefHeight()) {
			this.getModelView().setPrefHeight(this.getModelView().getPrefHeight() + 100.0);
		}
	}
	
	//Getter-Methode f�r die X, Y-Koordinaten eines pr�sentierten Elements
	public Point2D getLayout() {
		double x = this.getElementRegion().getLayoutX();
		double y = this.getElementRegion().getLayoutY();
		Point2D coordinates = new Point2D(x, y);
		return coordinates;
	}

	//Getter-Methode f�r die H�he eines pr�sentierten Elements
	public double getHeight() {
		return this.getElementRegion().getPrefHeight();
	}

	//Getter-Methode f�r die zugeh�rige Modell-Ansicht des pr�sentierten Elements
	public ModelView getModelView() {
		return this.modelView;
	}

	//Getter-Methode f�r die Weite eines pr�sentierten Elements
	public double getWidth() {
		return this.getElementRegion().getPrefWidth();
	}

	//Setter-Methode f�r die Weite und H�he eines pr�sentierten Elements
	public void setSize(double x, double y) {
		this.getElementRegion().setPrefSize(x, y);
		if(this.getElementRegion().getLayoutX() + this.getWidth() > this.getModelView().getPrefWidth()) {
			this.getModelView().setPrefWidth(this.getModelView().getPrefWidth() + 100.0);
		}
		if(this.getElementRegion().getLayoutY() + this.getHeight() > this.getModelView().getPrefHeight()) {
			this.getModelView().setPrefHeight(this.getModelView().getPrefHeight() + 100.0);
		}
		this.getModelView().getFileExportControl().setSaved(false);
	}

	//Getter-Methode f�r die pr�sentierten Elemente
	public LinkedList<ElementView> getElements() {
		return this.elements;
	}
	
	//Methode zur Pr�fung, ob ein Element in der Pr�sentation vorhanden ist
	public boolean containsElement(ElementView element) {
		if(this.elements.contains(element)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Methode zur Pr�fung, ob eine Elements-Ansicht der hier pr�sentierten entspricht
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		ElementView elementView = (ElementView)object;
		if(this.getModelView() != null && elementView.getModelView() != null ) {
			if(this.getModelView().equals(elementView.getModelView()) && this.getHeight() == elementView.getHeight() && this.getWidth() == elementView.getWidth() && this.getLayout().equals(elementView.getLayout()) && this.getElementRegion().equals(elementView.getElementRegion())) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(this.getModelView() == null && elementView.getModelView() == null && (this.getHeight() == elementView.getHeight() && this.getWidth() == elementView.getWidth() && this.getLayout().equals(elementView.getLayout()) && this.getElementRegion().equals(elementView.getElementRegion())) && this.differenceWidth == elementView.getDifferenceWidth() && this.differenceHeight == elementView.getDifferenceHeight() && this.getElements() == elementView.getElements()) {
			return true;
		}
		else {
			return false;
		}
	}

	//Getter-Methode f�r den Unterschied in der Weite eines pr�sentierten Elements
	public double getDifferenceWidth() {
		return this.differenceWidth;
	}
	
	//Getter-Methode f�r den Unterschied in der H�he eines pr�sentierten Elements
	public double getDifferenceHeight() {
		return this.differenceHeight;
	}
	
	//Setter-Methode f�r den Unterschied in der Weite eines pr�sentierten Elements
	public void setDifferenceWidth(double differenceWidth) {
		this.differenceWidth = differenceWidth;
	}
	
	//Setter-Methode f�r den Unterschied in der H�he eines pr�sentierten Elements
	public void setDifferenceHeight(double differenceHeight) {
		this.differenceHeight = differenceHeight;
	}

}
