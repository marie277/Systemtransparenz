package control.edit;

import java.util.LinkedList;

public class ZoomControl {

	private LinkedList<Zoom> objects;
	private int zoomCounter;
	
	public ZoomControl() {
		this.zoomCounter = 0;
		this.objects = new LinkedList<Zoom>();
	}
	
	public int getZoomCounter() {
		return this.zoomCounter;
	}

	public void setZoomCounter(int zoomCounter) {
		this.zoomCounter = zoomCounter;
	}

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

	public void removeObject(Zoom zoom) {
		this.objects.add(zoom);
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

	private void zoom(double factor) {
		for(Zoom zoom: this.objects) {
			zoom.zoom(factor);
		}
	}
	
	public void zoomIn() {
		this.zoomCounter++;
		this.zoom(1.2);
	}

	public void zoomOut() {
		if(this.zoomCounter>=-10) {
			this.zoomCounter--;
			this.zoom(0.8);
		}
	}

}
