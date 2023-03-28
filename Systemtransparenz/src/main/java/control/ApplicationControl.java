package control;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.ApplicationModel;
import view.ApplicationView;
import view.ModelView;

//Klasse zur Steuerung einer Anwendungs-Ansicht, beerbt die abstrakte Klasse zur allgemeinen Elements-Steuerung
public class ApplicationControl extends ElementControl {

	private ApplicationView applicationView;
	private String applicationName;
	private int applicationId;
	private String applicationDescription;
	private String applicationCategory;
	private String applicationProducer;
	private String applicationManager;
	private String applicationDepartment;
	private String applicationAdmin;

	//Konstruktor
	public ApplicationControl(ApplicationView applicationView) {
		super(applicationView);
		this.applicationView = applicationView;
	}
	
	//Getter-Methode für die Anwendungs-Ansicht
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	//Setter-Methode für die Auswahl
	public void setSelected(boolean selected) {
		this.applicationView.setSelected(selected);
	}
	
	//Methode zur Umbenennung einer (importierten) Anwendung
	public void changeApplicationName(String applicationName) {
		this.applicationView.getApplicationModel().setApplicationName(applicationName);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	//Methode zur Änderung der ID einer Anwendung
	public void changeApplicationId(String applicationId) {
		this.applicationView.getApplicationModel().setApplicationId(Integer.parseInt(applicationId));
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	//Methode zur Änderung der Beschreibung einer Anwendung
	public void changeApplicationDescription(String applicationDescription) {
		this.applicationView.getApplicationModel().setApplicationDescription(applicationDescription);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	//Methode zur Änderung der Kategorie einer Anwendung
	public void changeApplicationCategory(String applicationCategory) {
		this.applicationView.getApplicationModel().setApplicationCategory(applicationCategory);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	//Methode zur Änderung des Herstellers einer Anwendung
	public void changeApplicationProducer(String applicationProducer) {
		this.applicationView.getApplicationModel().setApplicationProducer(applicationProducer);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	//Methode zur Änderung des Managers einer Anwendung
	public void changeApplicationManager(String applicationManager) {
		this.applicationView.getApplicationModel().setApplicationManager(applicationManager);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	//Methode zur Änderung des Fachbereichs einer Anwendung
	public void changeApplicationDepartment(String applicationDepartment) {
		this.applicationView.getApplicationModel().setApplicationDepartment(applicationDepartment);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	//Methode zur Änderung des Admins einer Anwendung
	public void changeApplicationAdmin(String applicationAdmin) {
		this.applicationView.getApplicationModel().setApplicationAdmin(applicationAdmin);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	//Methode zur Aktualisierung der Schriftgröße des Elements
	@Override
	public void zoom(double factor) {
		super.zoom(factor);
		for(Text text : this.applicationView.getAttributes()) {
			String fontName = text.getFont().getName();
			double fontSize = text.getFont().getSize();
			text.setFont(new Font(fontName, fontSize*factor));
		}
		for(Label label : this.applicationView.getAttributeLabels()) {
			String fontName = label.getFont().getName();
			double fontSize = label.getFont().getSize();
			label.setFont(new Font(fontName, fontSize*factor));
		}
	}

	//Methode zur Erstellung einer Anwendung als XML-Element, welches in einer Datei exportiert werden kann
	@Override
	public Element createXMLElement(Document doc) {
		Element element = super.createXMLElement(doc);
		Element application = doc.createElement("Anwendung");
		application.appendChild(element);
		this.applicationId = this.applicationView.getApplicationModel().getApplicationId();
		application.setAttribute("ID", String.valueOf(this.applicationId));
		this.applicationName = this.applicationView.getApplicationModel().getApplicationName();
		application.setAttribute("Anwendungsname", this.applicationName);
		this.applicationDescription = this.applicationView.getApplicationModel()
				.getApplicationDescription();
		application.setAttribute("Beschreibung", this.applicationDescription);
		this.applicationCategory = this.applicationView.getApplicationModel().getApplicationCategory();
		application.setAttribute("Kategorie", this.applicationCategory);
		this.applicationProducer = this.applicationView.getApplicationModel().getApplicationProducer();
		application.setAttribute("Hersteller", this.applicationProducer);
		this.applicationManager = this.applicationView.getApplicationModel().getApplicationManager();
		application.setAttribute("Anwendungsmanager", this.applicationManager);
		this.applicationDepartment = this.applicationView.getApplicationModel()
				.getApplicationDepartment();
		application.setAttribute("Fachbereich", this.applicationDepartment);
		this.applicationAdmin = this.applicationView.getApplicationModel().getApplicationAdmin();
		application.setAttribute("Admin", this.applicationAdmin);
		return application;
	}
	
	//Methode zum Hinzufügen einer Anwendung aus einem XML-Dokument, welches als Modell dargestellt wird
	public static ApplicationView importXMLElement(Element item, ModelView modelView) {
		String applicationId = item.getAttribute("ID");
		String applicationName = item.getAttribute("Anwendungsname");
		String applicationDescription = item.getAttribute("Beschreibung");
		String applicationCategory = item.getAttribute("Kategorie");
		String applicationProducer = item.getAttribute("Hersteller");
		String applicationManager = item.getAttribute("Anwendungsmanager");
		String applicationDepartment = item.getAttribute("Fachbereich");
		String applicationAdmin = item.getAttribute("Admin");
		ApplicationModel applicationModel = new ApplicationModel(Integer.parseInt(applicationId), applicationName, applicationDescription, applicationCategory, applicationProducer, applicationManager, applicationDepartment, applicationAdmin);
		ApplicationView applicationView = new ApplicationView(applicationModel, modelView);
		modelView.addElement(applicationView);
		NodeList nodeList = item.getElementsByTagName("XML-Element");
		Element element = (Element)nodeList.item(0);
		Node node = element.getElementsByTagName("Element").item(0);
		ElementControl.importXMLSettings((Element)node, applicationView);
		return applicationView;
	}
	
}
