package view;

import java.util.LinkedList;
import control.ModelControl;
import control.dataExport.FileExportControl;
import control.dataExport.ImageExportControl;
import control.edit.Zoom;
import control.edit.ZoomControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

//Klasse zur Präsentation eines Modells, beerbt Klasse Pane und implementiert Interface Zoom
public class ModelView extends Pane implements Zoom {
	
	private ModelControl modelControl;
	private StringProperty modelName;
	private ObjectProperty<ElementView> elementView;
	private LinkedList<ApplicationView> applications;
	private LinkedList<RelationView> relations;
	private FileExportControl fileExportControl;
	private ZoomControl zoomControl;
	private ImageExportControl imageExportControl;
	
	//Konstruktor
	public ModelView() {
		this.zoomControl = new ZoomControl();
		zoomControl.addObject(this);
		this.modelName = new SimpleStringProperty(this, "modelName", "Neues Modell");
		this.elementView = new SimpleObjectProperty<ElementView>(this, "elementView", null);
		this.modelControl = new ModelControl(this);
		this.fileExportControl = new FileExportControl(this);
		this.imageExportControl = new ImageExportControl(this);
		this.applications = new LinkedList<ApplicationView>();
		this.relations = new LinkedList<RelationView>();
		this.addEventFilter(ScrollEvent.ANY, e->{
			if(e.getDeltaY() > 0.0) {
				this.zoomControl.zoomIn();
			}
			else {
				this.zoomControl.zoomOut();
			}
		});
		this.setOnMouseClicked(e -> {
			this.deselectElements();
		});
	}
	
	//Methode zur Aufhebung der Auswahl von Elementen
	public void deselectElements() {
		for(ApplicationView applicationView : this.applications) {
			applicationView.getApplicationControl().setSelected(false);
		}
		for(RelationView relationView : this.relations) {
			relationView.getRelationControl().setSelected(false);
		}
		this.setElementView(null);
	}

	//Setter-Methode für die ausgewählte Elements-Ansicht im Modell
	public void setElementView(ElementView elementView) {
		this.elementView.set(elementView);
	}

	//Getter-Methode für die ausgewählte Elements-Ansicht im Modell
	public ElementView getElementView() {
		return this.elementView.get();
	}

	//Getter-Methode für den Modell-Namen
	public String getModelName() {
		return this.modelName.get();
	}

	//Getter-Methode für das Property des Modell-Namens
	public StringProperty getModelNameProperty() {
		return this.modelName;
	}

	//Setter-Methode für den Modell-Namen
	public void setModelName(String modelName) {
		this.modelName.set(modelName);
	}

	//Getter-Methode für die Steuerung des Datei-Exports
	public FileExportControl getFileExportControl() {
		return this.fileExportControl;
	}

	//Getter-Methode für die Steuerung der Modell-Ansicht
	public ModelControl getModelControl() {
		return this.modelControl;
	}

	//Getter-Methode für die Steuerung des Bild-Exports
	public ImageExportControl getImageExportControl() {
		return this.imageExportControl;
	}
	
	//Getter-Methode für das Property des im Modell ausgewählten Elements
	public ObjectProperty<ElementView> getSelectedElementProperty(){
		return this.elementView;
	}

	//Getter-Methode für die im Modell enthaltenen Anwendungs-Ansichten
	public LinkedList<ApplicationView> getApplications() {
		return this.applications;
	}

	//Getter-Methode für die Steuerung der Ansichtsvergrößerung bzw. -verkleinerung
	public ZoomControl getZoomControl() {
		return this.zoomControl;
	}

	//Methode für die Ansichts-Vergrößerung bzw. -Verkleinerung des präsentierten Modells
	@Override
	public void zoom(double factor) {
		this.setPrefWidth(this.getPrefWidth()*factor);
		this.setPrefHeight(this.getPrefHeight()*factor);
	}

	//Methode zum Entfernen einer Elements-Ansicht aus dem Modell
	public void removeElement(ElementView elementView) {
		if (elementView.getClass().equals(ApplicationView.class)) {
            this.applications.remove(elementView);
        }
        else if (elementView.getClass().equals(RelationView.class)) {
            this.relations.remove(elementView);
        }
		elementView.setModelView(null);
		if(elementView.getClass().equals(RelationView.class)) {
			RelationView relationView = (RelationView)elementView;
			for(RelationLineView relationLineView : relationView.getRelationNodes()) {
				this.getChildren().removeAll(relationLineView.getRelationNodes());
			}
		}
        this.getChildren().remove(elementView.getElementRegion());
        this.zoomControl.removeObject(elementView);
        if (this.getElementView().equals(elementView)) {
            this.setElementView(null);
        }
        this.fileExportControl.setSaved(false);
	}

	//Getter-Methode für die im Modell enthaltenen Beziehungs-Ansichten
	public LinkedList<RelationView> getRelations() {
		return this.relations;
	}

	//Methode zum Hinzufügen einer Elements-Ansicht zum Modell
	public void addElement(ElementView elementView) {
		if (elementView.getClass().equals(ApplicationView.class)) {
            this.applications.add((ApplicationView)elementView);
        }
        
        else if (elementView.getClass().equals(RelationView.class)) {
            this.relations.add((RelationView) elementView);
        }
		elementView.setModelView(this);
		if(elementView.getClass().equals(RelationView.class)) {
			RelationView relationView = (RelationView)elementView;
			for(RelationLineView relationLineView : relationView.getRelationNodes()) {
				this.getChildren().addAll(0, relationLineView.getRelationNodes());
			}
		}
        this.getChildren().add(this.getChildren().size(), elementView.getElementRegion());
        this.zoomControl.addObject(elementView);
        this.fileExportControl.setSaved(false);
	}

}
