package control;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.scene.text.Font;
import model.ApplicationModel;
import view.ApplicationView;
import view.ModelView;

public class ApplicationControl extends ElementControl {

	private ApplicationView applicationView;

	public ApplicationControl(ApplicationView applicationView) {
		super(applicationView);
		this.applicationView = applicationView;
	}

	public void isSelected(boolean selected) {
		this.applicationView.isSelected(selected);
	}

	public static ApplicationView importXMLElement(Element item, ModelView modelView) {
		//ApplicationView applicationView = new ApplicationView(new ApplicationModel(0, "", null, 0, 0, 0, 0, 0), modelView);
		ApplicationView applicationView = new ApplicationView(new ApplicationModel(""), modelView);
		applicationView.getApplicationControl().renameApplication(item.getAttribute("Anwendungsname"));
		modelView.addElement(applicationView);
		NodeList nodeList = item.getElementsByTagName("Element");
		Element element = (Element)nodeList.item(0);
		ElementControl.importXMLSettings((Element)element.getElementsByTagName("XML-Element").item(0), applicationView);
		return applicationView;
	}

	private void renameApplication(String applicationName) {
		this.applicationView.getApplicationModel().setApplicationName(applicationName);
		this.applicationView.getModelView().getFileExportControl().setSaved(false);
	}

	@Override
	public Element createXMLElement(Document doc) {
		Element application = doc.createElement("Anwendung");
		application.setAttribute("Anwendungsname", this.applicationView.getApplicationModel().getApplicationName());
		Element element = super.createXMLElement(doc);
		application.appendChild(element);
		return application;
	}

	@Override
	public void refresh() {
		this.applicationView.setWidthHeight(this.applicationView.getText().getLayoutBounds().getWidth()*1.2, this.applicationView.getText().getLayoutBounds().getHeight()*1.2);
	}
	
	public ApplicationView getApplicationView() {
		return this.applicationView;
	}
	
	@Override
	public void zoom(double factor) {
		super.zoom(factor);
		Font f = this.applicationView.getText().getFont();
		this.applicationView.getText().setFont(new Font(f.getName(), f.getSize()*factor));
	}
	
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		ApplicationControl applicationControl = (ApplicationControl)object;
		return this.getApplicationView().equals(applicationControl.getApplicationView());
	}

}
