package view;

import java.util.LinkedList;
import control.ModelControl;
import control.dataExport.FileExportControl;
import control.dataExport.ImageExportControl;
import control.edit.RelationNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

//Klasse zur Pr�sentation eines Modells und beerbt Klasse Pane 
public class ModelView extends Pane {
	
	private ModelControl modelControl;
	private StringProperty modelName;
	private ObjectProperty<ElementView> elementView;
	private LinkedList<ApplicationView> applications;
	private LinkedList<RelationView> relations;
	private FileExportControl fileExportControl;
	private ImageExportControl imageExportControl;
	
	//Konstruktor
	public ModelView() {
		this.modelName = new SimpleStringProperty(this, "modelName", "Neues Modell");
		this.elementView = new SimpleObjectProperty<ElementView>(this, "elementView", null);
		this.modelControl = new ModelControl(this);
		this.modelControl.setZoomCounter(0);
		this.modelControl.initializeElementViewList();
		this.fileExportControl = new FileExportControl(this);
		this.imageExportControl = new ImageExportControl(this);
		this.applications = new LinkedList<ApplicationView>();
		this.relations = new LinkedList<RelationView>();
		this.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if(event.getDeltaY() > 0.0) {
					modelControl.zoomIn();
				}
				else {
					modelControl.zoomOut();
				}
			}
		});
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				deselectElements();
			}
		});
	}
	
	//Getter-Methode f�r die ausgew�hlte Elements-Ansicht im Modell
	public ElementView getElementView() {
		return this.elementView.get();
	}
	
	//Getter-Methode f�r die im Modell enthaltenen Beziehungs-Ansichten
	public LinkedList<RelationView> getRelations() {
		return this.relations;
	}
	
	//Getter-Methode f�r den Modell-Namen
	public String getModelName() {
		return this.modelName.get();
	}

	//Getter-Methode f�r das Property des Modell-Namens
	public StringProperty getModelNameProperty() {
		return this.modelName;
	}
	
	//Getter-Methode f�r die Steuerung des Datei-Exports
	public FileExportControl getFileExportControl() {
		return this.fileExportControl;
	}

	//Getter-Methode f�r die Steuerung der Modell-Ansicht
	public ModelControl getModelControl() {
		return this.modelControl;
	}

	//Getter-Methode f�r die Steuerung des Bild-Exports
	public ImageExportControl getImageExportControl() {
		return this.imageExportControl;
	}
	
	//Getter-Methode f�r das Property des im Modell ausgew�hlten Elements
	public ObjectProperty<ElementView> getSelectedElementProperty(){
		return this.elementView;
	}

	//Getter-Methode f�r die im Modell enthaltenen Anwendungs-Ansichten
	public LinkedList<ApplicationView> getApplications() {
		return this.applications;
	}
	
	//Setter-Methode f�r die ausgew�hlte Elements-Ansicht im Modell
	public void setElementView(ElementView elementView) {
		this.elementView.set(elementView);
	}
	
	//Setter-Methode f�r den Modell-Namen
	public void setModelName(String modelName) {
		this.modelName.set(modelName);
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

	//Methode f�r die Ansichts-Vergr��erung bzw. -Verkleinerung des pr�sentierten Modells
	public void zoom(double factor) {
		this.setPrefWidth(this.getPrefWidth()*factor);
		this.setPrefHeight(this.getPrefHeight()*factor);
	}

	//Methode zum Hinzuf�gen einer Elements-Ansicht zum Modell
	public void addElement(ElementView elementView) {
		if(elementView.getClass().equals(ApplicationView.class)) {
            this.applications.add((ApplicationView)elementView);
        }
        
        else if(elementView.getClass().equals(RelationView.class)) {
        	RelationView relationView = (RelationView)elementView;
			for(RelationNode relationNode : relationView.getRelationNodes()) {
				this.getChildren().addAll(0, relationNode.getRelationNodes());
			}
            this.relations.add((RelationView) elementView);
        }
		elementView.setModelView(this);
        this.getChildren().add(this.getChildren().size(), elementView.getElementRegion());
        this.modelControl.addObject(elementView);
        this.fileExportControl.setSaved(false);
	}

	//Methode zum Entfernen einer Elements-Ansicht aus dem Modell
	public void removeElement(ElementView elementView) {
		if(elementView.getClass().equals(ApplicationView.class)) {
            this.applications.remove(elementView);
        }
        else if(elementView.getClass().equals(RelationView.class)) {
        	RelationView relationView = (RelationView)elementView;
			for(RelationNode relationNode : relationView.getRelationNodes()) {
				this.getChildren().removeAll(relationNode.getRelationNodes());
			}
            this.relations.remove(elementView);
        }
		elementView.setModelView(null);
        this.getChildren().remove(elementView.getElementRegion());
        this.modelControl.removeObject(elementView);
        if (this.getElementView().equals(elementView)) {
            this.setElementView(null);
        }
        this.fileExportControl.setSaved(false);
	}
}
