package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import view.ApplicationView;
import view.ModelView;

public class ApplicationInRelation {
	
	private static int idGenerator;
	private ApplicationView applicationView;
	private BooleanProperty relationType;
	private boolean typeOfRelation;
	private ReadOnlyIntegerWrapper id;
	
	public ApplicationInRelation(ApplicationView applicationView) {
		this.applicationView = applicationView;
		int applicationId = ApplicationInRelation.idGenerator++;
		this.id = new ReadOnlyIntegerWrapper((Object)this, "id", applicationId);
	}
	
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	public static ApplicationInRelation importApplicationFromRelationFromXML(Element item, ModelView modelView) {
		ApplicationView applicationView = null;
        for (ApplicationView aV : modelView.getApplications()) {
            if (aV.getApplicationModel().getApplicationName().equals(item.getAttribute("BeziehungsteilnehmerAnwendung"))) {
                applicationView = aV;
                break;
            }
        }
        ApplicationInRelation applicationInRelation = new ApplicationInRelation(applicationView);
        applicationInRelation.setTypeOfRelation(Boolean.parseBoolean(item.getAttribute("BeziehungsteilnehmerTyp")));
        return applicationInRelation;
	}
	
	void setTypeOfRelation(boolean typeOfRelation) {
		if(this.relationType != null) {
			this.relationType.set(typeOfRelation);
		}
		else {
			this.typeOfRelation = typeOfRelation;
		}
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public Element createXMLElement(Document doc) {
		Element element = doc.createElement("Beziehungsteilnehmer");
	    element.setAttribute("BeziehungsteilnehmerAnwendung", this.getApplicationView().getApplicationModel().getApplicationName());
	    element.setAttribute("BeziehungsteilnehmerTyp", new StringBuilder().append(this.isRelationType()).toString());
	    return element;
	}
	
	private boolean isRelationType() {
		if(this.relationType == null) {
			return this.typeOfRelation;
		}
		else {
			return this.relationType.get();
		}
	}
	
	static {
		ApplicationInRelation.idGenerator = 0;
	}
	
	
	public void setApplicationView(ApplicationView applicationView) {
		this.applicationView = applicationView;
	}
	
	public BooleanProperty relationTypeProperty() {
		if(this.relationType != null) {
			this.relationType = (BooleanProperty)new SimpleBooleanProperty((Object)this, "typeOfRelation", this.typeOfRelation);
		}
		return this.relationType;
	}
	
	public final int getId() {
		return this.id.get();
	}
	
	public ReadOnlyIntegerProperty idProperty() {
		return this.id.getReadOnlyProperty();
	}
	
	 @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        }
        ApplicationInRelation applicationInRelation = (ApplicationInRelation)object;
        return this.getApplicationView().equals(applicationInRelation.getApplicationView()) && /*this.isRelationType() == applicationInRelation.isRelationType() &&*/ this.getId() == applicationInRelation.getId();
    }
	
}
