package control;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.scene.layout.Region;
import view.ElementView;

//Abstrakte Klasse zur Steuerung von Elements-Ansichten
public abstract class ElementControl {
	
	private ElementView elementView;
	
	//Konstruktor
	public ElementControl(ElementView elementView) {
		this.elementView = elementView;
	}
	
	//Getter-Methode für die Elements-Ansicht
	public ElementView getElementView() {
		return this.elementView;
	}
	
	//Abstrakte Methode zur Aktualisierung der Elements-Grenzen
	public abstract void refresh();
	
	//Setter-Methode zur Festlegung, ob eine Anwendung gespeichert wurde
	private void setSaved(boolean saved) {
		this.elementView.getModelView().getFileExportControl().setSaved(saved);
	}
	
	//Methode zur Bewegung einer Elements-Ansicht innerhalb des Modells
	public void move(double x, double y) {
		double differenceWidth = this.elementView.getDifferenceWidth();
		double differenceLayoutX = x - this.elementView.getLayout().getX();
		this.elementView.setDifferenceWidth(differenceWidth + differenceLayoutX);
		double differenceHeight = this.elementView.getDifferenceHeight();
		double differenceLayoutY = y - this.elementView.getLayout().getY();
		this.elementView.setDifferenceHeight(differenceHeight + differenceLayoutY);
		this.elementView.setLayout(x, y);
		this.setSaved(false);
		for(ElementView elementView : this.elementView.getElements()) {
			elementView.setMoved(x, y);
		}
	}
	
	//Methode zum Import der Attribute eines XML-Elements
	public static void importXMLSettings(Element item, ElementView elementView) {
		double width = Double.parseDouble(item.getAttribute("Weite"));
		double height =  Double.parseDouble(item.getAttribute("Höhe"));
		double x = Double.parseDouble(item.getAttribute("X-Koordinate"));
		double y = Double.parseDouble(item.getAttribute("Y-Koordinate"));
		elementView.setSize(width, height);
		elementView.setLayout(x, y);
	}

	//Methode zur Erstellung eines XML-Elements, welches in einer Datei exportiert werden kann
	public Element createXMLElement(Document doc) {
		Element element = setXMLAttributes(doc);
		Element parentElement = doc.createElement("XML-Element");
		parentElement.appendChild(element);
		return parentElement;
	}

	//Setter-Methode für die Attribute eines XML-Elements
	private Element setXMLAttributes(Document doc) {
		Element element = doc.createElement("Element");
		Region region = this.elementView.getElementRegion();
		String xAttribute = new StringBuilder().append(region.getLayoutX()).toString();
		element.setAttribute("X-Koordinate", xAttribute);
		String yAttribute = new StringBuilder().append(region.getLayoutY()).toString();
		element.setAttribute("Y-Koordinate", yAttribute);
		String widthAttribute = new StringBuilder().append(region.getWidth()).toString();
		element.setAttribute("Weite",widthAttribute);
		String heightAttribute = new StringBuilder().append(region.getHeight()).toString();
		element.setAttribute("Höhe", heightAttribute);
		return element;
	}
	
	//Methode zur Aktualisierung eines Elements
	public void zoom(double factor) {
		this.elementView.setSize(this.elementView.getElementRegion().getPrefWidth()*factor, this.elementView.getElementRegion().getPrefHeight()*factor);
		this.elementView.setLayout(this.elementView.getLayout().getX()*factor, this.elementView.getLayout().getY()*factor);
		this.elementView.setDifferenceWidth(this.elementView.getDifferenceWidth() * factor);
		this.elementView.setDifferenceHeight(this.elementView.getDifferenceHeight() * factor);
		this.setSaved(false);
	}
	
	//Setter-Methode zur Festlegung, ob eine Elements-Ansicht bewegt wurde
	public void setMoved(double x, double y) {
		double layoutX = x + this.elementView.getDifferenceWidth();
		double layoutY =  y + this.elementView.getDifferenceHeight();
		this.elementView.setLayout(layoutX, layoutY);
		for(ElementView elementView : this.elementView.getElements()) {
			elementView.setMoved(this.elementView.getLayout().getX(), this.elementView.getLayout().getY());
		}
	}

}
