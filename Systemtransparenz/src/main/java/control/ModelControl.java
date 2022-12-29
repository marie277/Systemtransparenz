package control;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import control.edit.Zoom;
import model.ApplicationInRelation;
import model.ApplicationModel;
import model.RelationModel;
import view.ApplicationView;
import view.ModelView;
import view.RelationView;

public class ModelControl {
	private ModelView modelView;
	public ModelControl(ModelView modelView) {
		this.modelView = modelView;
	}

	public void saveModel() throws TransformerException, IOException, ParserConfigurationException {
		this.modelView.getFileExportControl().saveFile();
	}

	public void saveModelAs() throws TransformerException, IOException, ParserConfigurationException {
		this.modelView.getFileExportControl().saveFileAs();
	}

	public void removeRelationView(RelationView relationView) {
		relationView.getRelationControl().removeApplications();
		this.modelView.removeElement(relationView);
	}

	public void removeApplicationView(ApplicationView applicationView) {
		this.modelView.removeElement(applicationView);
	}

	public void zoomIn() {
		this.modelView.getZoomControl().zoomIn();
	}

	public void zoomOut() {
		this.modelView.getZoomControl().zoomOut();
	}

	public static ModelView importXMLElement(Element item) {
		ModelView modelView = new ModelView();
		String modelName = item.getAttribute("Modellname");
		modelView.setModelName(modelName);
		double modelHeight = Double.parseDouble(item.getAttribute("Modellhoehe"));
		modelView.setPrefHeight(modelHeight);
		modelView.setMinHeight(modelHeight);
		double modelWidth = Double.parseDouble(item.getAttribute("Modellweite"));
		modelView.setPrefWidth(modelWidth);
		modelView.setMinWidth(modelWidth);
		File imageLocation = new File(item.getAttribute("SpeicherortBild"));
		modelView.getFileExportControl().setFileLocation(imageLocation);
		NodeList applications = item.getElementsByTagName("Anwendung");
		for(int i=0; i<applications.getLength(); ++i) {
			ApplicationControl.importXMLElement((Element)applications.item(i), modelView);
		}
		NodeList relations = item.getElementsByTagName("Beziehung");
		for(int i=0; i<relations.getLength(); ++i) {
			RelationControl.importXMLElement((Element)relations.item(i), modelView);
		}
		return modelView;
	}

	public Element createXMLElement(Document doc) {
		this.removeObjects();
		Element model = doc.createElement("Modell");
		try {
			model.setAttribute("Modellname", this.modelView.getModelName());
			model.setAttribute("Modellweite", new StringBuilder().append(this.modelView.getPrefWidth()).toString());
			model.setAttribute("Modellhoehe", new StringBuilder().append(this.modelView.getPrefHeight()).toString());
			//model.setAttribute("SpeicherortBild", this.modelView.getImageExportControl().getImageLocation().getAbsolutePath());
			model.setAttribute("Zoom", new StringBuilder().append(this.modelView.getZoomControl().getZoomCounter()).toString());
			Element applications = doc.createElement("Anwendungen");
			for(ApplicationView aV: this.modelView.getApplications()) {
				applications.appendChild(aV.getApplicationControl().createXMLElement(doc));
			}
			model.appendChild(applications);
			Element relations = doc.createElement("Beziehungen");
			for(RelationView rV: this.modelView.getRelations()) {
				relations.appendChild(rV.getRelationControl().createXMLElement(doc));
			}
			model.appendChild(relations);
			this.addObjects();
		}
		catch(Exception e) {
			e.printStackTrace();
			this.addObjects();
		}
		return model;
	}

	private void addObjects() {
		for(ApplicationView applicationView : this.modelView.getApplications()) {
			this.modelView.getZoomControl().addObject((Zoom)applicationView);
		}
		for(RelationView relationView : this.modelView.getRelations()) {
			this.modelView.getZoomControl().addObject((Zoom)relationView);
		}
	}

	private void removeObjects() {
		for(ApplicationView applicationView : this.modelView.getApplications()) {
			this.modelView.getZoomControl().removeObject((Zoom)applicationView);
		}
		for(RelationView relationView : this.modelView.getRelations()) {
			this.modelView.getZoomControl().removeObject((Zoom)relationView);
		}
	}
	
	public ModelView getModelView() {
		return this.modelView;
	}
	
	public void createRelationView(ApplicationInRelation firstApplication, ApplicationInRelation secondApplication) {
		RelationView relationView = new RelationView(new RelationModel(firstApplication, secondApplication), this.modelView);
		this.modelView.addElement(relationView);
	}
	
	public void addRelationView(RelationModel relationModel) {
		RelationView relationView = new RelationView(relationModel, this.modelView);
		this.modelView.addElement(relationView);
	}
	
	public boolean removeApplicationFromRelation(ApplicationInRelation applicationInRelation, RelationView relationView){
		boolean isPartOfRelation = relationView.getRelationModel().getApplications().contains(applicationInRelation);
		int numberOfApplications = relationView.getRelationModel().getApplications().size();
		
		if(isPartOfRelation && numberOfApplications == 2) {
			relationView.getRelationControl().removePartOfRelation(applicationInRelation);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object object) {
		return super.equals(object) && this.modelView.equals(((ModelControl) object).getModelView());
	}

	public void removeApplication(ApplicationView applicationView) {
		this.modelView.removeElement(applicationView);
	}

	public void addApplication(String applicationName) {
		for (ApplicationView aV : this.modelView.getApplications()) {
            if (aV.getApplicationModel().getApplicationName().equals(applicationName)) {
                throw new IllegalArgumentException("Es ist bereits eine Anwendung mit diesem Namen vorhanden.");
            }
        }
        ApplicationView aV = new ApplicationView(new ApplicationModel(applicationName), this.modelView);
        this.modelView.addElement(aV);;
	}

	public void renameApplication(ApplicationView applicationView, String applicationName) {
		for (ApplicationView aV : this.modelView.getApplications()) {
            if (aV.getApplicationModel().getApplicationName().equals(applicationName) && !aV.equals(applicationView)) {
                throw new IllegalArgumentException("Achtung! Es ist bereits eine Anwendung mit diesem Namen vorhanden.");
            }
        }
        applicationView.getApplicationControl().renameApplication(applicationName);
	}

	public void editRelationType(RelationView relationView, String selectedItem) {
		relationView.getRelationControl().setRelationText(selectedItem);
	}

}
