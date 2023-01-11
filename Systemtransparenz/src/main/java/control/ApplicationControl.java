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
		this.applicationName = this.applicationView.getApplicationModel().getApplicationName();
		application.setAttribute("Anwendungsname", this.applicationName);
		return application;
	}
	
	//Methode zum Hinzufügen einer Anwendung aus einem XML-Dokument, welches als Modell dargestellt wird
	public static ApplicationView importXMLElement(Element item, ModelView modelView) {
		ApplicationModel applicationModel = new ApplicationModel("");
		ApplicationView applicationView = new ApplicationView(applicationModel, modelView);
		String applicationName = item.getAttribute("Anwendungsname");
		applicationView.getApplicationControl().renameApplication(applicationName);
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

}
