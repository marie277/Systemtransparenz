package control.edit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import view.ApplicationView;
import view.ModelView;

//Klasse zur Speicherung der Daten einer Anwendung, welche Teil einer Beziehung ist
public class ApplicationInRelation {
	
	private ApplicationView applicationView;
	private static int number = 0;
	private IntegerProperty id;
	private BooleanProperty relationDirectionProperty;
	
	//Konstruktor
	public ApplicationInRelation(ApplicationView applicationView) {
		this.applicationView = applicationView;
		int applicationId = ApplicationInRelation.number++;
		this.id = new SimpleIntegerProperty(this, "id", applicationId);
	}
	
	//Getter-Methode für das Property der Beziehungspfeil-Richtung
	public BooleanProperty getRelationDirectionProperty() {
		if(this.relationDirectionProperty == null) {
			this.relationDirectionProperty = new SimpleBooleanProperty(this, "relationDirection", false);
		}
		return this.relationDirectionProperty;
	}
	
	//Getter-Methode für die zugehörige Anwendungs-Ansicht
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	//Methode zum Hinzufügen einer an einer Beziehung beteiligten Anwendung aus einem XML-Dokument, welches als Modell dargestellt wird
	public static ApplicationInRelation importFromXML(Element item, ModelView modelView) {
		ApplicationView applicationView = null;
		ApplicationInRelation applicationInRelation = null;
        for (ApplicationView aV : modelView.getApplications()) {
        	String applicationName = aV.getApplicationModel().getApplicationName();
            if (applicationName.equals(item.getAttribute("Anwendungsname"))) {
                applicationView = aV;
                break;
            }
        }
        applicationInRelation = new ApplicationInRelation(applicationView);
        return applicationInRelation;
	}
	
	//Methode zur Erstellung einer Anwendung als Teil einer Beziehung als XML-Element, welches in einer Datei exportiert werden kann
	public Element createXMLElement(Document doc) {
		Element element = doc.createElement("Beziehungsteilnehmer");
		String applicationName = this.getApplicationView().getApplicationModel().getApplicationName();
	    element.setAttribute("Anwendungsname", applicationName);
	    return element;
	}
	
	//Setter-Methode für die zugehörige Anwendungs-Ansicht
	public void setApplicationView(ApplicationView applicationView) {
		this.applicationView = applicationView;
	}
	
	//Getter-Methode für das Property der zugehörigen ID
	public IntegerProperty getIdProperty() {
		return this.id;
	}
	
}
