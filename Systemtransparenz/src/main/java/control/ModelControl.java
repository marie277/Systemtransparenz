package control;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import control.edit.Zoom;
import model.ApplicationModel;
import model.RelationModel;
import view.ApplicationView;
import view.ModelView;
import view.RelationView;

//Klasse zur Steuerung von Modell-Ansichten
public class ModelControl {
	
	private ModelView modelView;
	
	//Konstruktor
	public ModelControl(ModelView modelView) {
		this.modelView = modelView;
	}
	
	//Getter-Methode f?r die Modell-Ansicht
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

	//Methode zum Entfernen einer Anwendungs-Ansicht aus dem Modell
	public void removeApplicationView(ApplicationView applicationView) {
		this.modelView.removeElement(applicationView);
	}

	//Methode zum Vergr??ern der Modell-Ansicht
	public void zoomIn() {
		this.modelView.getZoomControl().zoomIn();
	}

	//Methode zum Verkleinern der Modell-Ansicht
	public void zoomOut() {
		this.modelView.getZoomControl().zoomOut();
	}

	//Methode zum Hinzuf?gen eines XML-Elements aus einer Datei in das Modell
	public static ModelView importXMLElement(Element item) {
		ModelView modelView = new ModelView();
		String modelName = item.getAttribute("Modellname");
		modelView.setModelName(modelName);
		modelView.setPrefHeight(Double.parseDouble(item.getAttribute("Modellhoehe")));
		modelView.setMinHeight(Double.parseDouble(item.getAttribute("Modellhoehe")));
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
			this.modelView.getZoomControl().removeObject((Zoom)applicationView);
		}
		for(RelationView relationView : this.modelView.getRelations()) {
			this.modelView.getZoomControl().removeObject((Zoom)relationView);
		}
		Element model = doc.createElement("Modell");
		try {
			String modelName = this.modelView.getModelName();
			model.setAttribute("Modellname", modelName);
			String widthAttribute = new StringBuilder().append(this.modelView.getPrefWidth()).toString();
			model.setAttribute("Modellweite", widthAttribute);
			String heightAttribute = new StringBuilder().append(this.modelView.getPrefHeight()).toString();
			model.setAttribute("Modellhoehe", heightAttribute);
			String zoomAttribute = new StringBuilder().append(this.modelView.getZoomControl().getZoomCounter()).toString();
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
				this.modelView.getZoomControl().addObject((Zoom)applicationView);
			}
			for(RelationView relationView : this.modelView.getRelations()) {
				this.modelView.getZoomControl().addObject((Zoom)relationView);
			};
		}
		catch(Exception e) {
			e.printStackTrace();
			for(ApplicationView applicationView : this.modelView.getApplications()) {
				this.modelView.getZoomControl().addObject((Zoom)applicationView);
			}
			for(RelationView relationView : this.modelView.getRelations()) {
				this.modelView.getZoomControl().addObject((Zoom)relationView);
			}
		}
		return model;
	}
	
	//Methode zum Hinzuf?gen einer Beziehungs-Ansicht zum Modell
	public void addRelationView(RelationModel relationModel) {
		RelationView relationView = new RelationView(relationModel, this.modelView);
		this.modelView.addElement(relationView);
	}
	
	//Methode zum Entfernen einer Anwendungs-Ansicht aus dem Modell
	public void removeApplication(ApplicationView applicationView) {
		this.modelView.removeElement(applicationView);
	}

	//Methode zum Hinzuf?gen einer Anwendungs-Ansicht in das Modell
	public void addApplication(String applicationName) {
		for (ApplicationView applicationView : this.modelView.getApplications()) {
            if (applicationView.getApplicationModel().getApplicationName().equals(applicationName)) {
                throw new IllegalArgumentException("Achtung! Es ist bereits eine Anwendung mit diesem Namen vorhanden.");
            }
        }
		ApplicationModel applicationModel = new ApplicationModel(applicationName);
        ApplicationView applicationView = new ApplicationView(applicationModel, this.modelView);
        this.modelView.addElement(applicationView);;
	}

	//Methode zum Umbenennen einer Anwendungs-Ansicht im Modell
	public void renameApplication(ApplicationView applicationView, String applicationName) {
		for (ApplicationView applicationViewModel : this.modelView.getApplications()) {
            if (applicationViewModel.getApplicationModel().getApplicationName().equals(applicationName) && !applicationViewModel.equals(applicationView)) {
                throw new IllegalArgumentException("Achtung! Es ist bereits eine Anwendung mit diesem Namen vorhanden.");
            }
        }
        applicationView.getApplicationControl().renameApplication(applicationName);
	}

	//Methode zur ?nderung des Beziehungstyps einer Beziehung in dem Modell
	public void editRelationType(RelationView relationView, String selectedItem) {
		relationView.getRelationControl().setRelationText(selectedItem);
	}

	//Methode zur Pr?fung, ob eine Modell-Ansicht mit der gesteuerten ?bereinstimmt
	@Override
	public boolean equals(Object object) {
		if(super.equals(object) && this.modelView.equals(((ModelControl) object).getModelView())) {
			return true;
		}
		else {
			return false;
		}
	}
}
