package control.dataExport;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import control.MainControl;
import control.ModelControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import view.ModelView;

public class FileExportControl {

	private ModelView modelView;
	private BooleanProperty saved;
	private File fileLocation;

	public FileExportControl(ModelView modelView) {
		this.modelView = modelView;
		this.saved = (BooleanProperty)new SimpleBooleanProperty((Object)this,"gespeichert", false);
	}

	public static void openModel(MainControl mainControl) throws SAXException, IOException, ParserConfigurationException {
		FileChooser fileChooser = new FileChooser();
		String[] fileExtension = {".xml"};
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML-Dateien", fileExtension);
		fileChooser.setTitle("Modell �ffnen");
		fileChooser.getExtensionFilters().add(extensionFilter);
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = fileChooser.showOpenDialog(mainControl.getMainWindow());
		if(file != null) {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			document.getDocumentElement().normalize();
			ModelView modelView = importXMLModel(document.getElementsByTagName("Modell"));
			modelView.getFileExportControl().setFileLocation(file);
			modelView.getFileExportControl().setSaved(true);
			mainControl.importModelView(modelView);
		}
	}

	public void setSaved(boolean saved) {
		this.saved.set(saved);
	}

	public void setFileLocation(File fileLocation) {
		this.fileLocation = fileLocation;
	}

	private static ModelView importXMLModel(NodeList elementsByTagName) {
		return ModelControl.importXMLElement((Element)elementsByTagName.item(0));
	}

	public boolean isSaved() {
		return this.saved.get();
	}

	public void saveFile() throws TransformerException, IOException, ParserConfigurationException {
		File file = this.fileLocation;
		if(file == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Datei speichern");
			String[] fileExtension = {".xml"};
			ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML-Dateien", fileExtension);
			fileChooser.getExtensionFilters().add(extensionFilter);
			file = fileChooser.showSaveDialog(this.modelView.getScene().getWindow());
		}
		if(file != null) {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStream outputStream = new BufferedOutputStream(fileOutputStream);
			StreamResult streamResult = new StreamResult(outputStream);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(this.createXMLFile(), streamResult);
            outputStream.flush();
            outputStream.close();
            this.fileLocation = file;
            this.setSaved(true);
            return;
		}
		throw new IOException("Achtung! Datei konnte nicht gespeichert werden.");
	}

	private DOMSource createXMLFile() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element element = this.modelView.getModelControl().createXMLElement(doc);
        doc.appendChild(element);
		DOMSource domSource = new DOMSource(doc);
		return domSource;
	}

	public void saveFileAs() throws TransformerException, IOException, ParserConfigurationException {
		File file = this.fileLocation;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datei speichern als");
		String[] fileExtension = {".xml"};
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML-Dateien", fileExtension);
		fileChooser.getExtensionFilters().add(extensionFilter);
		if(file != null) {
			File parentFile = new File(file.getParent());
			fileChooser.setInitialDirectory(parentFile);
		}
		file = fileChooser.showSaveDialog(this.modelView.getScene().getWindow());
		if(file != null) {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStream outputStream = new BufferedOutputStream(fileOutputStream);
			StreamResult streamResult = new StreamResult(outputStream);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(this.createXMLFile(), streamResult);
            outputStream.flush();
            outputStream.close();
            this.fileLocation = file;
            this.setSaved(true);
            return;
		}
		throw new IOException("Achtung! Datei konnte nicht gespeichert werden.");
	}

	public BooleanProperty savedProperty() {
		return this.saved;
	}

}
