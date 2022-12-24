package view;

import java.util.LinkedList;

import control.ElementControl;
import control.edit.Move;
import control.edit.Zoom;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;

public abstract class ElementView implements Zoom, Move {

	protected ModelView modelView;
	private LinkedList<ElementView> elements;
	protected double differenceWidth;
	protected double differenceHeight;
	
	public ElementView() {
		this.elements = new LinkedList<ElementView>();
	}
	
	public void setModelView(ModelView modelView) {
		this.modelView = modelView;
	}

	public abstract Region getElementRegion();
	
	public abstract ElementControl getElementControl();
	
	public abstract void zoom(double factor);
	
	public abstract boolean isMoveable();
	
	public abstract void isMoved(final double x, final double y);
	
	public abstract void move(double x, double y);
	
	public abstract void addElement(final ElementView element);
	
	public void removeElement(ElementView elementView) {
		if(containsElement(elementView)) {
			this.elements.remove(elementView);
		}
		else {
			for(ElementView eV : this.elements) {
				eV.removeElement(elementView);
			}
		}
	}

	public void setWidthHeight(double x, double y) {
		this.getElementRegion().setLayoutX(x);
		this.getElementRegion().setLayoutY(y);
		if(this.getElementRegion().getLayoutX() + this.getWidth() > this.getModelView().getPrefWidth()) {
			this.getModelView().setPrefWidth(this.getModelView().getPrefWidth() + 100.0);
		}
		if(this.getElementRegion().getLayoutY() + this.getHeight() > this.getModelView().getPrefHeight()) {
			this.getModelView().setPrefHeight(this.getModelView().getPrefHeight() + 100.0);
		}
	}
	
	public Point2D getWidthHeight() {
		return new Point2D(this.getElementRegion().getLayoutX(), this.getElementRegion().getLayoutY());
	}

	public double getHeight() {
		return this.getElementRegion().getPrefHeight();
	}

	public ModelView getModelView() {
		return this.modelView;
	}

	public double getWidth() {
		return this.getElementRegion().getPrefWidth();
	}

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

	public LinkedList<ElementView> getElements() {
		return this.elements;
	}
	
	public boolean containsElement(final ElementView element) {
		return this.elements.contains(element);
	}
	
	public boolean equals(final Object object) {
		if(!super.equals(object)) {
			return false;
		}
		ElementView eV = (ElementView)object;
		if(this.getModelView() != null && eV.getModelView() != null ) {
			return this.getModelView().equals(eV.getModelView()) && this.getHeight() == eV.getHeight() && this.getWidth() == eV.getWidth() && this.getWidthHeight().equals((Object)eV.getWidthHeight()) && this.getElementRegion().equals(eV.getElementRegion());
		}
		return this.getModelView() == null && eV.getModelView() == null && (this.getHeight() == eV.getHeight() && this.getWidth() == eV.getWidth() && this.getWidthHeight().equals((Object)eV.getWidthHeight()) && this.getElementRegion().equals(eV.getElementRegion())) && this.differenceWidth == eV.getDifferenceWidth() && this.differenceHeight == eV.getDifferenceHeight() && this.getElements() == eV.getElements();
	}

	public double getDifferenceWidth() {
		return this.differenceWidth;
	}
	
	public double getDifferenceHeight() {
		return this.differenceHeight;
	}
	
	public void setDifferenceWidth(double differenzWeite) {
		this.differenceWidth = differenzWeite;
	}
	
	public void setDifferenceHeight(double differenzHoehe) {
		this.differenceHeight = differenzHoehe;
	}

}
