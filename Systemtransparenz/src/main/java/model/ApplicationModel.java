package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Klasse zur Speicherung der Daten einer Anwendung
public class ApplicationModel {

	private String applicationName;
	private StringProperty name;
	
	//Konstruktor
	public ApplicationModel(String applicationName) {
		this.applicationName = applicationName;
	}
	
	//Getter-Methode für den Anwendungsnamen
	public String getApplicationName() {
		if(this.name != null) {
			return this.name.get();
		}
		else {
			return this.applicationName;
		}
	}
	
	//Setter-Methode für den Anwendungsnamen
	public void setApplicationName(String applicationName) {
		if (this.name != null) {
            this.name.set(applicationName);
        }
        else {
            this.applicationName = applicationName;
        }
	}

	//Getter-Methode für das Property des Anwendungsnamens
	public StringProperty getNameProperty() {
		if(this.name == null) {
			this.name = new SimpleStringProperty(this, "applicationName", this.applicationName);
		}
		return this.name;
	}

}
