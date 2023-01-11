package control.edit;

import java.util.LinkedList;

//Klasse zur Steuerung der Ansichtsvergr��erung von Elementen
public class ZoomControl {

	private int zoomCounter;
	private LinkedList<Zoom> objects;
	
	//Konstruktor
	public ZoomControl() {
		this.zoomCounter = 0;
		this.objects = new LinkedList<Zoom>();
	}
	
	//Getter-Methode f�r den Z�hler der Ansichts-Vergr��erungen bzw. -Verkleinerungen
	public int getZoomCounter() {
		return this.zoomCounter;
	}

	//Methode zum Ber�cksichtigen eines Elements bei der Ansichts-Vergr��erung bzw. -Verkleinerung
	public void addObject(Zoom zoom) {
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

	//Methode zum Entfernen eines ber�cksichtigten Elements bei der Ansichts-Vergr��erung bzw. -Verkleinerung
	public void removeObject(Zoom zoom) {
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
	
	//Methode zur Durchf�hrung einer Ansichts-Vergr��erung
	public void zoomIn() {
		this.zoomCounter++;
		for(Zoom zoom: this.objects) {
			zoom.zoom(1.2);
		}
	}

	//Methode zur Durchf�hrung einer Ansichts-Verkleinerung
	public void zoomOut() {
		if(this.zoomCounter>=-10) {
			this.zoomCounter--;
			for(Zoom zoom: this.objects) {
				zoom.zoom(0.8);
			}
		}
	}

}
