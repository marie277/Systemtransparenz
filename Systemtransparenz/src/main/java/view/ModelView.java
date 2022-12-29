package view;

import java.util.Collection;
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
import javafx.scene.Node;

public class ModelView extends Pane implements Zoom {
	
	private LinkedList<ApplicationView> applications;
	private LinkedList<RelationView> relations;
	private ModelControl modelControl;
	private StringProperty modelName;
	private ObjectProperty<ElementView> elementView;
	private FileExportControl fileExportControl;
	private ZoomControl zoomControl;
	private ImageExportControl imageExportControl;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModelView() {
		(this.zoomControl = new ZoomControl()).addObject(this);
		this.modelName = (StringProperty)new SimpleStringProperty((Object)this, "modelName", "Neues Modell");
		this.elementView = (ObjectProperty<ElementView>)new SimpleObjectProperty((Object)this, "elementView", (Object)null);
		this.fileExportControl = new FileExportControl(this);
		this.imageExportControl = new ImageExportControl(this);
		this.modelControl = new ModelControl(this);
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
		this.setOnMouseClicked(e -> this.deselectElements());
		//this.getStylesheets().add((String)this.getClass().getResource("relationLine.css").toExternalForm());
	}
	
	void deselectElements() {
		for(final ApplicationView applicationView : this.applications) {
			applicationView.getApplicationControl().isSelected(false);
		}
		for(final RelationView relationView : this.relations) {
			relationView.getRelationControl().isSelected(false);
		}
		this.setElementView(null);
	}

	void setElementView(ElementView elementView) {
		this.elementView.set(elementView);
	}

	public ElementView getElementView() {
		return (ElementView)this.elementView.get();
	}

	public String getModelName() {
		return (String)this.modelName.get();
	}

	public StringProperty modelName() {
		return this.modelName;
	}

	public void setModelName(String modelName) {
		this.modelName.set(modelName);
	}

	public FileExportControl getFileExportControl() {
		return this.fileExportControl;
	}

	public ModelControl getModelControl() {
		return this.modelControl;
	}

	public ImageExportControl getImageExportControl() {
		return this.imageExportControl;
	}
	
	public ObjectProperty<ElementView> selectedElementProperty(){
		return this.elementView;
	}

	public LinkedList<ApplicationView> getApplications() {
		return this.applications;
	}

	public ZoomControl getZoomControl() {
		return this.zoomControl;
	}

	@Override
	public void zoom(double factor) {
		this.setPrefWidth(this.getPrefWidth()*factor);
		this.setPrefHeight(this.getPrefHeight()*factor);
	}

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
			for(RelationLineView rLV : relationView.getRelationNodes()) {
				this.getChildren().removeAll((Collection<? extends Node>)rLV.getNodes());
			}
		}
        this.getChildren().remove((Object)elementView.getElementRegion());
        this.zoomControl.removeObject((Zoom)elementView);
        if (this.getElementView().equals(elementView)) {
            this.setElementView(null);
        }
        this.fileExportControl.setSaved(false);
	}

	public LinkedList<RelationView> getRelations() {
		return this.relations;
	}

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
			for(RelationLineView rLV : relationView.getRelationNodes()) {
				this.getChildren().addAll(0, (Collection<? extends Node>)rLV.getNodes());
			}
		}
        this.getChildren().add(this.getChildren().size(), elementView.getElementRegion());
        this.zoomControl.addObject(elementView);
        this.fileExportControl.setSaved(false);
	}

	public ObjectProperty<ElementView> elementViewProperty() {
		return this.elementView;
	}

}
	
	
	
	
	
	


	