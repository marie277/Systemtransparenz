package control;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import view.ElementView;

public abstract class ElementControl {
	
	private ElementView elementView;
	
	public ElementControl(ElementView elementView) {
		this.elementView = elementView;
	}
	
	public ElementView getElementView() {
		return this.elementView;
	}
	
	public abstract void refresh();
	
	public void move(double x, double y) {
		double xDifference = x - elementView.getWidthHeight().getX();
		double yDifference = y - elementView.getWidthHeight().getY();
		elementView.setDifferenceWidth(elementView.getDifferenceWidth() + xDifference);
		elementView.setDifferenceHeight(elementView.getDifferenceHeight()+ yDifference);
		this.elementView.setWidthHeight(x, y);
		this.elementView.getModelView().getFileExportControl().setSaved(false);
		for(ElementView eV : this.elementView.getElements()) {
			eV.isMoved(x, y);
		}
	}
	
	public void setWidthHeight(double width, double height) {
		this.elementView.setSize(width, height);
		this.elementView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public void addElement(ElementView element) {
		this.elementView.addElement(element);
		element.setDifferenceWidth(element.getWidthHeight().getX() - this.elementView.getWidthHeight().getX());
		element.setDifferenceHeight(element.getWidthHeight().getY() - this.elementView.getWidthHeight().getY());
		this.elementView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public void removeElement(ElementView element) {
		this.elementView.removeElement(element);
		this.elementView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public boolean containsElement(ElementView elementView) {
		if(this.elementView.containsElement(elementView)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static void importXMLSettings(Element item, ElementView elementView) {
		elementView.setSize(Double.parseDouble(item.getAttribute("Weite")), Double.parseDouble(item.getAttribute("Hoehe")));
		elementView.setWidthHeight(Double.parseDouble(item.getAttribute("X-Koordinate")), Double.parseDouble(item.getAttribute("Y-Koordinate")));
		
	}

	public Element createXMLElement(Document doc) {
		Element element = setXMLAttributes(doc);
		Element parent = doc.createElement("XML-Element");
		parent.appendChild(element);
		Element children = doc.createElement("Kind-Elemente");
		for(ElementView eV : this.elementView.getElements()) {
			children.appendChild(eV.getElementControl().createXMLElement(doc));
		}
		parent.appendChild(children);
		return parent;
	}

	private Element setXMLAttributes(Document doc) {
		Element element = doc.createElement("Element");
		element.setAttribute("X-Koordinate", new StringBuilder().append(this.elementView.getElementRegion().getLayoutX()).toString());
		element.setAttribute("Y-Koordinate", new StringBuilder().append(this.elementView.getElementRegion().getLayoutY()).toString());
		element.setAttribute("Weite", new StringBuilder().append(this.elementView.getElementRegion().getWidth()).toString());
		element.setAttribute("Hoehe", new StringBuilder().append(this.elementView.getElementRegion().getHeight()).toString());
		return element;
	}
	
	public void zoom(double factor) {
		this.elementView.setSize(this.elementView.getWidth()*factor, this.elementView.getHeight()*factor);
		this.elementView.setWidthHeight(this.elementView.getWidthHeight().getX()*factor, this.elementView.getWidthHeight().getY()*factor);
		this.elementView.getModelView().getFileExportControl().setSaved(false);
		this.elementView.setDifferenceWidth(this.elementView.getDifferenceWidth() * factor);
		this.elementView.setDifferenceHeight(this.elementView.getDifferenceHeight() * factor);
	}
	
	public void isMoved(double x, double y) {
		this.elementView.setWidthHeight(x + this.elementView.getDifferenceWidth(), y + this.elementView.getDifferenceHeight());
		for(ElementView eV : this.elementView.getElements()) {
			eV.isMoved(this.elementView.getWidthHeight().getX(), this.elementView.getWidthHeight().getY());
		}
	}
	
	public boolean equals(Object object) {
		if(!super.equals(object)  ) {
			return false;
		}
		return this.elementView.equals(((ElementControl) object).getElementView());
	}

}
