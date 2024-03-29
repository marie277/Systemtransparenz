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
import org.xml.sax.SAXException;

import control.ModelControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import model.MainModel;
import view.ModelView;

//Klasse zur Steuerung des XML-Datei-Exports und -Imports
public class FileExportControl {

	private ModelView modelView;
	private BooleanProperty savedProperty;
	private File fileLocation;

	//Konstruktor
	public FileExportControl(ModelView modelView) {
		this.modelView = modelView;
		this.savedProperty = new SimpleBooleanProperty(this, "saved", false);
	}
	
	//Methode zur Festlegung, ob eine Modell-Ansicht gespeichert wurde
	public void setSaved(boolean saved) {
		this.savedProperty.set(saved);
	}

	//Setter-Methode f�r den Speicherort der XML-Datei
	public void setFileLocation(File fileLocation) {
		this.fileLocation = fileLocation;
	}

	//Methode zur Pr�fung, ob eine Modellansicht gespeichert wurde
	public boolean isSaved() {
		return this.savedProperty.get();
	}

	//Methode zum �ffnen eines als XML-Datei gespeicherten Modells
	public static void openModel() throws SAXException, IOException, ParserConfigurationException {
		FileChooser fileChooser = new FileChooser();
		String fileExtension = "*.xml";
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML-Dateien", fileExtension);
		fileChooser.setTitle("Modell �ffnen");
		fileChooser.getExtensionFilters().add(extensionFilter);
		String systemProperty = System.getProperty("user.home");
		File path = new File(systemProperty);
		fileChooser.setInitialDirectory(path);
		Window mainWindow = MainModel.modelFXMLControl.getTabPane().getScene().getWindow();
		File file = fileChooser.showOpenDialog(mainWindow);
		if(file != null) {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			document.getDocumentElement().normalize();
			ModelView modelView = ModelControl.importXMLElement((Element)document.getElementsByTagName("Modell").item(0));
			modelView.getFileExportControl().setFileLocation(file);
			modelView.getFileExportControl().setSaved(true);
			ScrollPane scrollPane = new ScrollPane(modelView);
			String modelName = modelView.getModelName();
			Tab modelTab = new Tab(modelName, scrollPane);
			modelTab.textProperty().bind((ObservableValue<String>)modelView.getModelNameProperty());
			TabPane modelTabPane = MainModel.modelFXMLControl.getTabPane();
			modelTabPane.getTabs().add(modelTab);
			modelTabPane.getSelectionModel().select(modelTab);
		}
	}

	//Methode zum Speichern einer Modell-Ansicht als XML-Datei
	public void saveFile() throws TransformerException, IOException, ParserConfigurationException {
		File file = this.fileLocation;
		if(file == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Datei speichern");
			String fileExtension = ".xml";
			ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML-Dateien", fileExtension);
			fileChooser.getExtensionFilters().add(extensionFilter);
			file = fileChooser.showSaveDialog(this.modelView.getScene().getWindow());
		}
		save(file);
	}

	//Methode zum Speichern einer Modell-Ansicht als eine bestimmte Datei bzw. unter einem bestimmten Pfad
	public void saveFileAs() throws TransformerException, IOException, ParserConfigurationException {
		File file = this.fileLocation;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datei speichern als");
		String fileExtension = ".xml";
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML-Dateien", fileExtension);
		fileChooser.getExtensionFilters().add(extensionFilter);
		if(file != null) {
			File parentFile = new File(file.getParent());
			fileChooser.setInitialDirectory(parentFile);
		}
		file = fileChooser.showSaveDialog(this.modelView.getScene().getWindow());
		save(file);
	}
	
	//Methode zum Speichern eines Modells in einer XML-Datei
	private void save(File file) throws IOException, TransformerException, ParserConfigurationException {
		if(file != null) {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStream outputStream = new BufferedOutputStream(fileOutputStream);
			StreamResult streamResult = new StreamResult(outputStream);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        Document document = documentBuilder.newDocument();
	        Element element = this.modelView.getModelControl().createXMLElement(document);
	        document.appendChild(element);
			DOMSource domSource = new DOMSource(document);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(domSource, streamResult);
            outputStream.flush();
            outputStream.close();
            this.fileLocation = file;
            this.setSaved(true);
            return;
		}
		else {
			throw new IOException("Achtung! Datei konnte nicht gespeichert werden.");
		}
	}

}
