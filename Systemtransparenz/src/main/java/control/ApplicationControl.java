package control;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.ApplicationModel;
import view.ApplicationView;
import view.ModelView;

//Klasse zur Steuerung einer Anwendungs-Ansicht, beerbt die abstrakte Klasse zur allgemeinen Elements-Steuerung
public class ApplicationControl extends ElementControl {

	private ApplicationView applicationView;
	private Text applicationText;
	private Bounds applicationBounds;
	private String applicationName;
	private int applicationId;
	private String applicationDescription;
	private String applicationCategory;
	private String applicationProducer;
	private String applicationManager;
	private String applicationDepartment;
	private String applicationAdmin;
	private static final double factor = 1.2;

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

	//Methode zur Aktualisierung der Elements-Grenzen
	@Override
	public void refresh() {
		this.applicationBounds = this.applicationView.getText().getLayoutBounds();
		this.applicationView.setLayout(this.applicationBounds.getWidth()*factor, this.applicationBounds.getHeight()*factor);
	}
	
	//Methode zur Aktualisierung der Schriftgröße des Elements
	@Override
	public void zoom(double factor) {
		super.zoom(factor);
		this.applicationText = this.applicationView.getText();
		String fontName = this.applicationText.getFont().getName();
		double fontSize = this.applicationText.getFont().getSize();
		this.applicationText.setFont(new Font(fontName, fontSize*factor));
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
		this.applicationDescription = this.applicationView.getApplicationModel().getApplicationDescription();
		application.setAttribute("Beschreibung", this.applicationDescription);
		this.applicationCategory = this.applicationView.getApplicationModel().getApplicationCategory();
		application.setAttribute("Kategorie", this.applicationCategory);
		this.applicationProducer = this.applicationView.getApplicationModel().getApplicationProducer();
		application.setAttribute("Hersteller", this.applicationProducer);
		this.applicationManager = this.applicationView.getApplicationModel().getApplicationManager();
		application.setAttribute("Anwendungsmanager", this.applicationManager);
		this.applicationDepartment = this.applicationView.getApplicationModel().getApplicationDepartment();
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
	
	//Methode zur Umbenennung einer (importierten) Anwendung
	public void renameApplication(String applicationName) {
		this.applicationView.getApplicationModel().setApplicationName(applicationName);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	//Methode zur Prüfung, ob eine Anwendungs-Ansicht mit der gesteuerten übereinstimmt
	@Override
	public boolean equals(Object object) {
		if(super.equals(object) && this.getApplicationView().equals(((ApplicationControl) object).getApplicationView())) {
			return true;
		}
		else {
			return false;
		}
	}

	public void changeApplicationId(String applicationId) {
		this.applicationView.getApplicationModel().setApplicationId(Integer.parseInt(applicationId));
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	public void changeApplicationDescription(String applicationDescription) {
		this.applicationView.getApplicationModel().setApplicationDescription(applicationDescription);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	public void changeApplicationCategory(String applicationCategory) {
		this.applicationView.getApplicationModel().setApplicationCategory(applicationCategory);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	public void changeApplicationProducer(String applicationProducer) {
		this.applicationView.getApplicationModel().setApplicationProducer(applicationProducer);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
	public void changeApplicationManager(String applicationManager) {
		this.applicationView.getApplicationModel().setApplicationDescription(applicationManager);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	public void changeApplicationDepartment(String applicationDepartment) {
		this.applicationView.getApplicationModel().setApplicationDepartment(applicationDepartment);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	public void changeApplicationAdmin(String applicationAdmin) {
		this.applicationView.getApplicationModel().setApplicationAdmin(applicationAdmin);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}
}
