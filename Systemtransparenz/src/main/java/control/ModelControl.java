package control;

import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import control.edit.ApplicationInRelation;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import model.ApplicationModel;
import model.RelationModel;
import view.ApplicationView;
import view.ElementView;
import view.ModelView;
import view.RelationView;

//Klasse zur Steuerung von Modell-Ansichten
public class ModelControl {
	
	private ModelView modelView;
	private int zoomCounter;
	private LinkedList<ElementView> objects;
	private Region region;
	private ElementView elementView;
	private boolean isMovable;
	private int x;
	private int y;
	
	//Konstruktor
	public ModelControl(ModelView modelView) {
		this.modelView = modelView;
	}
	
	//Getter-Methode für die Modell-Ansicht
	public ModelView getModelView() {
		return this.modelView;
	}

	//Methode zum Speichern des Modells
	public void saveModel() throws TransformerException, IOException, ParserConfigurationException {
		this.modelView.getFileExportControl().saveFile();
	}

	//Methode zum Speichern des Modells als Datei
	public void saveModelAs() throws TransformerException, IOException, ParserConfigurationException {
		this.modelView.getFileExportControl().saveFileAs();
	}

	//Methode zum Entfernen einer Beziehungs-Ansicht aus dem Modell
	public void removeRelationView(RelationView relationView) {
		relationView.getRelationControl().removeApplications();
		this.modelView.removeElement(relationView);
	}

	//Methode zum Hinzufügen eines XML-Elements aus einer Datei in das Modell
	public static ModelView importXMLElement(Element item) {
		ModelView modelView = new ModelView();
		String modelName = item.getAttribute("Modellname");
		modelView.setModelName(modelName);
		modelView.setPrefHeight(Double.parseDouble(item.getAttribute("Modellhöhe")));
		modelView.setMinHeight(Double.parseDouble(item.getAttribute("Modellhöhe")));
		modelView.setPrefWidth(Double.parseDouble(item.getAttribute("Modellweite")));
		modelView.setMinWidth(Double.parseDouble(item.getAttribute("Modellweite")));
		NodeList applications = item.getElementsByTagName("Anwendung");
		for(int i=0; i<applications.getLength(); i++) {
			ApplicationControl.importXMLElement((Element)applications.item(i), modelView);
		}
		NodeList relations = item.getElementsByTagName("Beziehung");
		for(int i=0; i<relations.getLength(); i++) {
			RelationControl.importXMLElement((Element)relations.item(i), modelView);
		}
		return modelView;
	}

	//Methode zum Erstellen eines XML-Elements zum Speichern in einer Datei
	public Element createXMLElement(Document doc) {
		for(ApplicationView applicationView : this.modelView.getApplications()) {
			//this.modelView.getZoomControl().removeObject(applicationView);
			this.modelView.getModelControl().removeObject(applicationView);
		}
		for(RelationView relationView : this.modelView.getRelations()) {
			//this.modelView.getZoomControl().removeObject(relationView);
			this.modelView.getModelControl().removeObject(relationView);
		}
		Element model = doc.createElement("Modell");
		try {
			String modelName = this.modelView.getModelName();
			model.setAttribute("Modellname", modelName);
			String widthAttribute = new StringBuilder().append(this.modelView.getPrefWidth()).toString();
			model.setAttribute("Modellweite", widthAttribute);
			String heightAttribute = new StringBuilder().append(this.modelView.getPrefHeight()).toString();
			model.setAttribute("Modellhöhe", heightAttribute);
			//String zoomAttribute = new StringBuilder().append(this.modelView.getZoomControl().getZoomCounter()).toString();
			String zoomAttribute = new StringBuilder().append(this.modelView.getModelControl().getZoomCounter()).toString();
			model.setAttribute("Zoom", zoomAttribute);
			Element applications = doc.createElement("Anwendungen");
			Element relations = doc.createElement("Beziehungen");
			for(ApplicationView applicationView: this.modelView.getApplications()) {
				applications.appendChild(applicationView.getApplicationControl().createXMLElement(doc));
			}
			for(RelationView relationView: this.modelView.getRelations()) {
				relations.appendChild(relationView.getRelationControl().createXMLElement(doc));
			}
			model.appendChild(applications);
			model.appendChild(relations);
			for(ApplicationView applicationView : this.modelView.getApplications()) {
				//this.modelView.getZoomControl().addObject(applicationView);
				this.modelView.getModelControl().addObject(applicationView);
			}
			for(RelationView relationView : this.modelView.getRelations()) {
				//this.modelView.getZoomControl().addObject(relationView);
				this.modelView.getModelControl().addObject(relationView);
			};
		}
		catch(Exception e) {
			e.printStackTrace();
			for(ApplicationView applicationView : this.modelView.getApplications()) {
				//this.modelView.getZoomControl().addObject(applicationView);
				this.modelView.getModelControl().addObject(applicationView);
			}
			for(RelationView relationView : this.modelView.getRelations()) {
				//this.modelView.getZoomControl().addObject(relationView);
				this.modelView.getModelControl().addObject(relationView);
			}
		}
		return model;
	}
	
	//Methode zum Hinzufügen einer Beziehungs-Ansicht zum Modell
	public void addRelationView(RelationModel relationModel) {
		RelationView relationView = new RelationView(relationModel, this.modelView);
		this.modelView.addElement(relationView);
	}
	
	//Methode zum Entfernen einer Anwendungs-Ansicht aus dem Modell
	public void removeApplicationView(ApplicationView applicationView) throws IllegalAccessException {
		LinkedList<RelationView> relationViews = new LinkedList<RelationView>();
        for (RelationView relationView : this.modelView.getRelations()) {
            if (relationView.getRelationControl().isPartOfRelation(applicationView)) {
            	relationView.getRelationControl().removeApplication(applicationView);
                relationViews.add(relationView);
            }
        }
        for (RelationView relationView : relationViews) {
            this.removeRelationView(relationView);
        }
		this.modelView.removeElement(applicationView);
	}

	//Methode zum Hinzufügen einer Anwendungs-Ansicht in das Modell
	public void addApplication(int applicationId, String applicationName, String applicationDescription, String applicationCategory, String applicationProducer, String applicationManager, String applicationDepartment, String applicationAdmin) {
		for (ApplicationView applicationView : this.modelView.getApplications()) {
            if (applicationView.getApplicationModel().getApplicationId() == applicationId) {
                throw new IllegalArgumentException("Achtung! Es ist bereits eine Anwendung mit dieser ID vorhanden.");
            }
        }
		ApplicationModel applicationModel = new ApplicationModel(applicationId, applicationName, applicationDescription, applicationCategory, applicationProducer, applicationManager, applicationDepartment, applicationAdmin);
        ApplicationView applicationView = new ApplicationView(applicationModel, this.modelView);
        this.modelView.addElement(applicationView);;
	}

	//Methode zum Umbenennen einer Anwendungs-Ansicht im Modell
	public void renameApplication(ApplicationView applicationView, String applicationName) {
        applicationView.getApplicationControl().changeApplicationName(applicationName);
	}

	//Methode zur Änderung des Beziehungstyps einer Beziehung in dem Modell
	public void editRelation(RelationView relationView, String relationType, String relationDirection) {
		ApplicationInRelation firstApplication = relationView.getRelationModel().getApplications().get(0);
		ApplicationInRelation secondApplication = relationView.getRelationModel().getApplications().get(1);
		boolean arrowIncoming = false;
		if(relationDirection.equals(firstApplication.getApplicationView().getApplicationModel().getApplicationName())) {
			arrowIncoming = true;
		}
		this.removeRelationView(relationView);
		RelationModel relationModel = new RelationModel(firstApplication, secondApplication, relationType, arrowIncoming);
		this.addRelationView(relationModel);	
	}

	//Methode zur Änderung der ID einer Anwendung
	public void changeApplicationId(ApplicationView applicationView, String applicationId) {
		for (ApplicationView applicationViewModel : this.modelView.getApplications()) {
            if (applicationViewModel.getApplicationModel().getApplicationName().equals(applicationId) && !applicationViewModel.equals(applicationView)) {
                throw new IllegalArgumentException("Achtung! Es ist bereits eine Anwendung mit dieser ID vorhanden.");
            }
        }
        applicationView.getApplicationControl().changeApplicationId(applicationId);
	}

	//Methode zur Änderung der Beschreibung einer Anwendung
	public void changeApplicationDescription(ApplicationView applicationView, String applicationDescription) {
		applicationView.getApplicationControl().changeApplicationDescription(applicationDescription);
	}

	//Methode zur Änderung der Kategory einer Anwendung
	public void changeApplicationCategory(ApplicationView applicationView, String applicationCategory) {
		applicationView.getApplicationControl().changeApplicationCategory(applicationCategory);
	}

	//Methode zur Änderung des Produzenten einer Anwendung
	public void changeApplicationProducer(ApplicationView applicationView, String applicationProducer) {
		applicationView.getApplicationControl().changeApplicationProducer(applicationProducer);
	}
	
	//Methode zur Änderung des Managers einer Anwendung
	public void changeApplicationManager(ApplicationView applicationView, String applicationManager) {
		applicationView.getApplicationControl().changeApplicationManager(applicationManager);
	}

	//Methode zur Änderung des Fachbereichs einer Anwendung
	public void changeApplicationDepartment(ApplicationView applicationView, String applicationDepartment) {
		applicationView.getApplicationControl().changeApplicationDepartment(applicationDepartment);
	}

	//Methode zur Änderung des Admins einer Anwendung
	public void changeApplicationAdmin(ApplicationView applicationView, String applicationAdmin) {
		applicationView.getApplicationControl().changeApplicationAdmin(applicationAdmin);
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public void setModelView(ModelView modelView) {
		this.modelView = modelView;
	}
	
	public void setElementView(ElementView elementView) {
		this.elementView = elementView;
	}
	
	//Methode zur Steuerung der Bewegung von Elementen
	public static void makeRegionMoveable(Region region, ModelView modelView, ElementView elementView) {
		ModelControl moveControl = new ModelControl(modelView);
		moveControl.setRegion(region);
		moveControl.setModelView(modelView);
		moveControl.setElementView(elementView);
		region.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			boolean isInside = e.getY() > 5.0 && e.getY() < moveControl.region.getHeight() - 5.0 && e.getX() > 5.0 && e.getX() < moveControl.region.getWidth() - 5.0;
			if(isInside) {
				moveControl.isMovable = true;
			}
			else {
				moveControl.isMovable = false;
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
			if (moveControl.isMovable && moveControl.elementView.isMoveable()) {
	            Parent parent = moveControl.modelView.getParent();
	            double layoutX = 0.0;
	            double layoutY = 0.0;
	            do {
	                layoutX += parent.getLayoutX();
	                layoutY += parent.getLayoutY();
	                parent = parent.getParent();
	            } while (parent.getParent() != null);
	            int newLayoutX = (int)(e.getSceneX() - layoutX - moveControl.region.getWidth() / 2.0);
	            int newLayoutY = (int)(e.getSceneY() - layoutY - moveControl.region.getHeight() / 2.0);
	            if (newLayoutX > moveControl.x && newLayoutY > moveControl.y) {
	            	moveControl.elementView.move(newLayoutX, newLayoutY);
	            }
	            else if (newLayoutX > moveControl.x) {
	            	moveControl.elementView.move(newLayoutX, moveControl.y);
	            }
	            else if (newLayoutY > moveControl.y) {
	            	moveControl.elementView.move(moveControl.x, newLayoutY);
	            }
	            else {
	            	moveControl.elementView.move(moveControl.x, moveControl.y);
	            }
	            moveControl.region.setCursor(Cursor.MOVE);
	            e.consume();
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
			boolean isInside = e.getY() > 5.0 && e.getY() < moveControl.region.getHeight() - 5.0 && e.getX() > 5.0 && e.getX() < moveControl.region.getWidth() - 5.0;
			if(isInside && moveControl.isMovable) {
				moveControl.region.setCursor(Cursor.MOVE);
			}
			else {
				moveControl.region.setCursor(Cursor.DEFAULT);
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
			moveControl.isMovable = false;
			moveControl.region.setCursor(Cursor.DEFAULT);
		});
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
