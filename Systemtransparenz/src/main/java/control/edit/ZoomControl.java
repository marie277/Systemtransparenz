package control.edit;

import java.util.LinkedList;

import view.ElementView;

//Klasse zur Steuerung der Ansichtsvergrößerung von Elementen
public class ZoomControl {

	private int zoomCounter;
	private LinkedList<ElementView> objects;
	
	//Konstruktor
	public ZoomControl() {
		
	}
	
	public void setZoomCounter(int zoomCounter) {
		this.zoomCounter = zoomCounter;
	}
	
	public void initializeObjectList() {
		this.objects = new LinkedList<ElementView>();
	}
	//Getter-Methode für den Zähler der Ansichts-Vergrößerungen bzw. -Verkleinerungen
	public int getZoomCounter() {
		return this.zoomCounter;
	}

	//Methode zum Berücksichtigen eines Elements bei der Ansichts-Vergrößerung bzw. -Verkleinerung
	public void addObject(ElementView zoom) {
		this.objects.add(zoom);
		double factor;
		if(this.zoomCounter>=1) {
			factor = 1.2;
		}
		else {
			factor = 0.8;
		}
		for(int i=0; i<this.zoomCounter; i++) {
			zoom.zoom(factor);
		}
	}

	//Methode zum Entfernen eines berücksichtigten Elements bei der Ansichts-Vergrößerung bzw. -Verkleinerung
	//public void removeObject(Zoom zoom) {
	public void removeObject(ElementView zoom) {
		this.objects.remove(zoom);
		double factor;
		if(this.zoomCounter>=1) {
			factor = 0.8;
		}
		else {
			factor = 1.2;
		}
		for(int i=0; i<this.zoomCounter; i++) {
			zoom.zoom(factor);
		}
	}
	
	//Methode zur Durchführung einer Ansichts-Vergrößerung
	public void zoomIn() {
		this.zoomCounter++;
		for(ElementView zoom: this.objects) {
			zoom.zoom(1.2);
		}
	}

	//Methode zur Durchführung einer Ansichts-Verkleinerung
	public void zoomOut() {
		if(this.zoomCounter>=-10) {
			this.zoomCounter--;
			for(ElementView zoom: this.objects) {
				zoom.zoom(0.8);
			}
		}
	}

}
