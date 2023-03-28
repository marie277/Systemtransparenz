package control.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class HelpFXMLControl implements Initializable {

	@FXML
	private BorderPane borderPane;
	@FXML
	private TextArea textArea;
	@FXML
	private Button quit;
	
	//Methode zum Schlie�en des Hilfestellungs-Fensters
	@FXML
	private void quit(ActionEvent event) {
		this.borderPane.getScene().getWindow().hide();
	}
	
	//Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.textArea.setText("1. Modell anlegen oder Modell �ffnen\r\n"
				+ "Die Men�punkte zum Anlegen oder �ffnen eines Modells sind im Men� \"Datei\" zu finden. Beim Anlegen ist der Name des Modells auszuw�hlen. Um ein Modell �ffnen zu k�nnen, muss es zuvor angelegt worden sein und der Speicherpfad zum ausw�hlen bekannt sein. Nach dem �ffnen bzw. Anlegen k�nnen alle Modelle gleicherma�en bearbeitet werden. Sie k�nnen jederzeit umbenannt oder auch wieder geschlossen werden. Au�erdem k�nnen sie bearbeitet und als Bild- und/oder XML-Datei gespeichert werden."
				+ "\r\n"
				+ "\r\n"
				+ "2. Modell bearbeiten\r\n"
				+ "Die Men�punkte zum Bearbeiten eines Modells sind im Men� \"Bearbeiten\" zu finden. Zum Bearbeiten eines Modells geh�rt das Anlegen, �ndern und Entfernen von Anwendungen und Beziehungen."
				+ "\r\n"
				+ "Das Anlegen von Anwendungen geschieht �ber den entsprechenden Men�punkt oder Button. Es erscheint ein Fenster, in dem alle Eigenschaften einer Anwendung angegeben und best�tigt werden m�ssen. Die Anwendung wird daraufhin in das Modell eingef�gt. Die Eigenschaften k�nnen nach Belieben ein- und ausgeblendet werden."
				+ "\r\n"
				+ "Eine Beziehung kann erst angelegt werden, wenn mindestens zwei Anwendungen vorhanden sind. Zum Anlegen ist der entsprechende Men�punkt oder Button auszuw�hlen. Es erscheint ein Fenster, in dem genau zwei Anwendungen ausgew�hlt, der Beziehungstyp angegeben und die eingehende Anwendung (auf welche der gerichtete Pfeil zeigt) festgelegt und die Auswahl best�tigt werden."
				+ "\r\n"
				+ "Zum �ndern ist eine Anwendung oder Beziehung per Klick auszuw�hlen und die Eigenschaften im rechten Teil der Hauptansicht anzugeben und erneut zu best�tigen. Die �nderungen werden in das Modell �bernommen."
				+ "\r\n"
				+ "Das Entfernen von Anwendungen und Beziehungen geschieht ebenfalls den entsprechenden Men�punkt oder Button. Sie sind nicht l�nger in dem Modell enthalten."
				+ "\r\n"
				+ "\r\n"
				+ "3. Anwendungen importieren und exportieren\r\n"
				+ "Die Men�punkte f�r den Import und Export von Anwendungen sind ebenfalls im Men� \"Bearbeiten\" zu finden. Es kann ausgew�hlt werden, ob eine manuelle oder eine voreingestellte Datenbankverbindung genutzt wird. Voraussetzung f�r den Import oder Export ist, dass ein Modell ge�ffnet ist und f�r den Export sollte es bereits mindestens eine Anwendung enthalten."
				+ "\r\n"
				+ "Beim Import von Anwendungen unter Nutzung einer manuellen Datenbankverbindung sind die erforderlichen Parameter f�r den Verbindungsaufbau sowie die Bezeichnungen der einzelnen Tabellen und Attribute anzugeben. Es k�nnen nur Datenbanken genutzt werden, welche ein geeignetes Schema besitzen. Der Import geschieht durch das Anklicken von \"Laden\", die in der Datenbank vorhandenen Anwendungen werden dann in der Vorschau angezeigt. Durch das Anklicken von Best�tigen werden sie in das Modell �bernommen."
				+ "\r\n"
				+ "Beim Import von Anwendungen unter Nutzung einer voreingestellten Datenbankverbindung ist die gew�nschte Option auszuw�hlen. Das restliche Vorgehen entspricht dem des Imports unter Nutzung einer manuell anzulegenden Datenbankverbindung."
				+ "\r\n"
				+ "Beim Export von Anwendungen unter Nutzung einer manuellen Datenbankverbindung sind die erforderlichen Parameter f�r den Verbindungsaufbau sowie die Bezeichnungen der einzelnen Tabellen und Attribute anzugeben. Es ist auch m�glich, eine neue Datenbank zu erstellen. Durch das Anklicken von \"Laden\" werden die im Modell vorhandenen Anwendungen in der Vorschau angezeigt. Durch das Anklicken von \"Best�tigen\" werden sie in die angegebene Datenbank exportiert."
				+ "\r\n"
				+ "Beim Export von Anwendungen unter Nutzung einer voreingestellten Datenbankverbindung ist die gew�nschte Option auszuw�hlen. Das restliche Vorgehen entspricht dem des Exports unter Nutzung einer manuell anzulegenden Datenbankverbindung."
				+ "\r\n"
				+ "\r\n"
				+ "4. Modell als Datei oder Bild speichern\r\n"
				+ "Die Men�punkte f�r das Speichern eines Modells oder Bilds sind im Men� \"Datei\" zu finden."
				+ "\r\n"
				+ "Beim erstmaligen Speichern oder gew�nschter �nderung des Speicherpfads eines Modells ist der Men�punkt \"Speichern als...\" auszuw�hlen. Daraufhin erscheint ein Fenster zur Auswahl des Speicherpfads. Die Auswahl ist durch das Anklicken von \"Speichern\" zu best�tigen. Unter dem entsprechenden Pfad ist nun eine XML-Datei mit dem gew�nschten Namen zu finden."
				+ "\r\n"
				+ "Beim Speichern von �nderungen eines bereits gespeicherten Modells ist der Men�punkt \"Speichern\" auszuw�hlen. Daraufhin werden die �nderungen aus dem Modell in die XML-Datei �bernommen."
				+ "\r\n"
				+ "Au�erdem kann ein Modell �ber den Men�punkt \"Bild speichern\" als PNG-Datei gespeichert werden. Daraufhin erscheint ein Fenster zur Auswahl des Speicherpfads. Die Auswahl ist durch das Anklicken von \"Speichern\" zu best�tigen. Unter dem entsprechenden Pfad ist nun eine XML-Datei mit dem gew�nschten Namen zu finden."
				+ "\r\n"
		);
		this.textArea.setWrapText(true);
	}

}
