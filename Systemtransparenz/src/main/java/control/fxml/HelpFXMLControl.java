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
	
	//Methode zum Schließen des Hilfestellungs-Fensters
	@FXML
	private void quit(ActionEvent event) {
		this.borderPane.getScene().getWindow().hide();
	}
	
	//Methode zur Initialisierung der Steuerung
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.textArea.setText("1. Modell anlegen oder Modell öffnen\r\n"
				+ "Die Menüpunkte zum Anlegen oder Öffnen eines Modells sind im Menü \"Datei\" zu finden. Beim Anlegen ist der Name des Modells auszuwählen. Um ein Modell öffnen zu können, muss es zuvor angelegt worden sein und der Speicherpfad zum auswählen bekannt sein. Nach dem Öffnen bzw. Anlegen können alle Modelle gleichermaßen bearbeitet werden. Sie können jederzeit umbenannt oder auch wieder geschlossen werden. Außerdem können sie bearbeitet und als Bild- und/oder XML-Datei gespeichert werden."
				+ "\r\n"
				+ "\r\n"
				+ "2. Modell bearbeiten\r\n"
				+ "Die Menüpunkte zum Bearbeiten eines Modells sind im Menü \"Bearbeiten\" zu finden. Zum Bearbeiten eines Modells gehört das Anlegen, Ändern und Entfernen von Anwendungen und Beziehungen."
				+ "\r\n"
				+ "Das Anlegen von Anwendungen geschieht über den entsprechenden Menüpunkt oder Button. Es erscheint ein Fenster, in dem alle Eigenschaften einer Anwendung angegeben und bestätigt werden müssen. Die Anwendung wird daraufhin in das Modell eingefügt. Die Eigenschaften können nach Belieben ein- und ausgeblendet werden."
				+ "\r\n"
				+ "Eine Beziehung kann erst angelegt werden, wenn mindestens zwei Anwendungen vorhanden sind. Zum Anlegen ist der entsprechende Menüpunkt oder Button auszuwählen. Es erscheint ein Fenster, in dem genau zwei Anwendungen ausgewählt, der Beziehungstyp angegeben und die eingehende Anwendung (auf welche der gerichtete Pfeil zeigt) festgelegt und die Auswahl bestätigt werden."
				+ "\r\n"
				+ "Zum Ändern ist eine Anwendung oder Beziehung per Klick auszuwählen und die Eigenschaften im rechten Teil der Hauptansicht anzugeben und erneut zu bestätigen. Die Änderungen werden in das Modell übernommen."
				+ "\r\n"
				+ "Das Entfernen von Anwendungen und Beziehungen geschieht ebenfalls den entsprechenden Menüpunkt oder Button. Sie sind nicht länger in dem Modell enthalten."
				+ "\r\n"
				+ "\r\n"
				+ "3. Anwendungen importieren und exportieren\r\n"
				+ "Die Menüpunkte für den Import und Export von Anwendungen sind ebenfalls im Menü \"Bearbeiten\" zu finden. Es kann ausgewählt werden, ob eine manuelle oder eine voreingestellte Datenbankverbindung genutzt wird. Voraussetzung für den Import oder Export ist, dass ein Modell geöffnet ist und für den Export sollte es bereits mindestens eine Anwendung enthalten."
				+ "\r\n"
				+ "Beim Import von Anwendungen unter Nutzung einer manuellen Datenbankverbindung sind die erforderlichen Parameter für den Verbindungsaufbau sowie die Bezeichnungen der einzelnen Tabellen und Attribute anzugeben. Es können nur Datenbanken genutzt werden, welche ein geeignetes Schema besitzen. Der Import geschieht durch das Anklicken von \"Laden\", die in der Datenbank vorhandenen Anwendungen werden dann in der Vorschau angezeigt. Durch das Anklicken von Bestätigen werden sie in das Modell übernommen."
				+ "\r\n"
				+ "Beim Import von Anwendungen unter Nutzung einer voreingestellten Datenbankverbindung ist die gewünschte Option auszuwählen. Das restliche Vorgehen entspricht dem des Imports unter Nutzung einer manuell anzulegenden Datenbankverbindung."
				+ "\r\n"
				+ "Beim Export von Anwendungen unter Nutzung einer manuellen Datenbankverbindung sind die erforderlichen Parameter für den Verbindungsaufbau sowie die Bezeichnungen der einzelnen Tabellen und Attribute anzugeben. Es ist auch möglich, eine neue Datenbank zu erstellen. Durch das Anklicken von \"Laden\" werden die im Modell vorhandenen Anwendungen in der Vorschau angezeigt. Durch das Anklicken von \"Bestätigen\" werden sie in die angegebene Datenbank exportiert."
				+ "\r\n"
				+ "Beim Export von Anwendungen unter Nutzung einer voreingestellten Datenbankverbindung ist die gewünschte Option auszuwählen. Das restliche Vorgehen entspricht dem des Exports unter Nutzung einer manuell anzulegenden Datenbankverbindung."
				+ "\r\n"
				+ "\r\n"
				+ "4. Modell als Datei oder Bild speichern\r\n"
				+ "Die Menüpunkte für das Speichern eines Modells oder Bilds sind im Menü \"Datei\" zu finden."
				+ "\r\n"
				+ "Beim erstmaligen Speichern oder gewünschter Änderung des Speicherpfads eines Modells ist der Menüpunkt \"Speichern als...\" auszuwählen. Daraufhin erscheint ein Fenster zur Auswahl des Speicherpfads. Die Auswahl ist durch das Anklicken von \"Speichern\" zu bestätigen. Unter dem entsprechenden Pfad ist nun eine XML-Datei mit dem gewünschten Namen zu finden."
				+ "\r\n"
				+ "Beim Speichern von Änderungen eines bereits gespeicherten Modells ist der Menüpunkt \"Speichern\" auszuwählen. Daraufhin werden die Änderungen aus dem Modell in die XML-Datei übernommen."
				+ "\r\n"
				+ "Außerdem kann ein Modell über den Menüpunkt \"Bild speichern\" als PNG-Datei gespeichert werden. Daraufhin erscheint ein Fenster zur Auswahl des Speicherpfads. Die Auswahl ist durch das Anklicken von \"Speichern\" zu bestätigen. Unter dem entsprechenden Pfad ist nun eine XML-Datei mit dem gewünschten Namen zu finden."
				+ "\r\n"
		);
		this.textArea.setWrapText(true);
	}

}
