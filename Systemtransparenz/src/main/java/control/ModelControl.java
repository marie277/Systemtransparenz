package control;

import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import control.edit.ApplicationInRelation;
import javafx.event.EventHandler;
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
	private LinkedList<ElementView> elementViews;
	private Region region;
	private ElementView elementView;
	private boolean isMovable;
	private int x;
	private int y;
	private static final double deduction = 5.0;
	
	//Konstruktor
	public ModelControl(ModelView modelView) {
		this.modelView = modelView;
	}
	
	//Getter-Methode für die Modell-Ansicht
	public ModelView getModelView() {
		return this.modelView;
	}
	
	//Getter-Methode für den Zähler der Ansichts-Vergrößerungen bzw. -Verkleinerungen
	public int getZoomCounter() {
		return this.zoomCounter;
	}
	
	//Setter-Methode für die Region einer Elements-Ansicht
	public void setRegion(Region region) {
		this.region = region;
	}
	
	//Setter-Methode für die Modell-Ansicht
	public void setModelView(ModelView modelView) {
		this.modelView = modelView;
	}
	
	//Setter-Methode für die Elements-Ansicht
	public void setElementView(ElementView elementView) {
		this.elementView = elementView;
	}
	
	//Setter-Methode für den Zähler der Ansichts-Vergrößerungen bzw. -Verkleinerungen
	public void setZoomCounter(int zoomCounter) {
		this.zoomCounter = zoomCounter;
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
		for(ApplicationView applicationView : this.modelView.getApplications()) {
            if(applicationView.getApplicationModel().getApplicationId() == applicationId) {
                throw new IllegalArgumentException("Achtung! Es ist bereits eine Anwendung mit dieser ID vorhanden.");
            }
        }
		ApplicationModel applicationModel = new ApplicationModel(applicationId, applicationName, applicationDescription, applicationCategory, applicationProducer, applicationManager, applicationDepartment, applicationAdmin);
        ApplicationView applicationView = new ApplicationView(applicationModel, this.modelView);
        this.modelView.addElement(applicationView);;
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
	
	//Methode zum Umbenennen einer Anwendungs-Ansicht im Modell
	public void changeApplicationName(ApplicationView applicationView, String applicationName) {
        applicationView.getApplicationControl().changeApplicationName(applicationName);
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
	
	//Methode zur Steuerung der Bewegung von Elementen
	public static void makeRegionMoveable(Region region, ModelView modelView, ElementView elementView) {
		ModelControl modelControl = new ModelControl(modelView);
		modelControl.setRegion(region);
		modelControl.setModelView(modelView);
		modelControl.setElementView(elementView);
		region.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				boolean fitsHeight = event.getY() > deduction && event.getY() < modelControl.region.getHeight() - deduction;
				boolean fitsWidth = event.getX() > deduction && event.getX() < modelControl.region.getWidth() - deduction;
				if(fitsHeight && fitsWidth) {
					modelControl.isMovable = true;
				}
				else {
					modelControl.isMovable = false;
				}
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (modelControl.isMovable && modelControl.elementView.isMoveable()) {
		            Parent parent = modelControl.modelView.getParent();
		            double layoutX = parent.getLayoutX();
		            double layoutY = parent.getLayoutY();
		            while(parent.getParent() != null) {
		            	parent = parent.getParent();
		            	layoutX += parent.getLayoutX();
		                layoutY += parent.getLayoutY();
		            }
		            int newLayoutX = (int)(event.getSceneX() - layoutX - modelControl.region.getWidth() / 2.0);
		            int newLayoutY = (int)(event.getSceneY() - layoutY - modelControl.region.getHeight() / 2.0);
		            if (newLayoutX > modelControl.x && newLayoutY > modelControl.y) {
		            	modelControl.elementView.move(newLayoutX, newLayoutY);
		            }
		            else if (newLayoutX > modelControl.x) {
		            	modelControl.elementView.move(newLayoutX, modelControl.y);
		            }
		            else if (newLayoutY > modelControl.y) {
		            	modelControl.elementView.move(modelControl.x, newLayoutY);
		            }
		            else {
		            	modelControl.elementView.move(modelControl.x, modelControl.y);
		            }
		            event.consume();
				}
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				modelControl.region.setCursor(Cursor.DEFAULT);
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				modelControl.isMovable = false;
				modelControl.region.setCursor(Cursor.DEFAULT);
			}
		});
	}
	
	//Methode zur Initialisierung einer Liste von Elements-Ansichten
	public void initializeElementViewList() {
		this.elementViews = new LinkedList<ElementView>();
	}

	//Methode zum Berücksichtigen eines Elements bei der Ansichts-Vergrößerung bzw. -Verkleinerung
	public void addObject(ElementView elementView) {
		this.elementViews.add(elementView);
		double factor;
		if(this.zoomCounter>=1) {
			factor = 1.2;
		}
		else {
			factor = 0.8;
		}
		for(int i=0; i<this.zoomCounter; i++) {
			elementView.zoom(factor);
		}
	}

	//Methode zum Entfernen eines berücksichtigten Elements bei der Ansichts-Vergrößerung bzw. -Verkleinerung
	public void removeObject(ElementView elementView) {
		this.elementViews.remove(elementView);
		double factor;
		if(this.zoomCounter>=1) {
			factor = 0.8;
		}
		else {
			factor = 1.2;
		}
		for(int i=0; i<this.zoomCounter; i++) {
			elementView.zoom(factor);
		}
	}
	
	//Methode zur Durchführung einer Ansichts-Vergrößerung
	public void zoomIn() {
		this.zoomCounter++;
		for(ElementView elementView : this.elementViews) {
			elementView.zoom(1.2);
		}
	}

	//Methode zur Durchführung einer Ansichts-Verkleinerung
	public void zoomOut() {
		if(this.zoomCounter>=-10) {
			this.zoomCounter--;
			for(ElementView elementView : this.elementViews) {
				elementView.zoom(0.8);
			}
		}
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
			this.modelView.getModelControl().removeObject(applicationView);
		}
		for(RelationView relationView : this.modelView.getRelations()) {
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
				this.modelView.getModelControl().addObject(applicationView);
			}
			for(RelationView relationView : this.modelView.getRelations()) {
				this.modelView.getModelControl().addObject(relationView);
			};
		}
		catch(Exception e) {
			e.printStackTrace();
			for(ApplicationView applicationView : this.modelView.getApplications()) {
				this.modelView.getModelControl().addObject(applicationView);
			}
			for(RelationView relationView : this.modelView.getRelations()) {
				this.modelView.getModelControl().addObject(relationView);
			}
		}
		return model;
	}
}
