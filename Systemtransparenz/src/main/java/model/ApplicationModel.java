package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Klasse zur Speicherung der Daten einer Anwendung
public class ApplicationModel {

	private String applicationName;
	private StringProperty name;
	private int applicationId;
	private IntegerProperty id;
	private String applicationDescription;
	private StringProperty description;
	private String applicationCategory;
	private StringProperty category;
	private String applicationProducer;
	private StringProperty producer;
	private String applicationManager;
	private StringProperty manager;
	private String applicationDepartment;
	private StringProperty department;
	private String applicationAdmin;
	private StringProperty admin;
	
	//Konstruktor
	public ApplicationModel(int applicationId, String applicationName, String applicationDescription, String applicationCategory, String applicationProducer, String applicationManager, String applicationDepartment, String applicationAdmin) {
		this.applicationId = applicationId;
		this.applicationName = applicationName;
		this.applicationDescription = applicationDescription;
		this.applicationCategory = applicationCategory;
		this.applicationProducer = applicationProducer;
		this.applicationManager = applicationManager;
		this.applicationDepartment = applicationDepartment;
		this.applicationAdmin = applicationAdmin;
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
		if(this.name != null) {
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
	
	//Getter-Methode für die AnwendungsID
	public int getApplicationId() {
		if(this.id != null) {
			return this.id.get();
		}
		else {
			return this.applicationId;
		}
	}
	
	//Setter-Methode für die AnwendungsID
	public void setApplicationId(int applicationId) {
		if(this.id != null) {
            this.id.set(applicationId);
        }
        else {
            this.applicationId = applicationId;
        }
	}

	//Getter-Methode für das Property der AnwendungsID
	public IntegerProperty getIdProperty() {
		if(this.id == null) {
			this.id = new SimpleIntegerProperty(this, "applicationId", this.applicationId);
		}
		return this.id;
	}
	
	//Getter-Methode für die Anwendungsbeschreibung
	public String getApplicationDescription() {
		if(this.description != null) {
			return this.description.get();
		}
		else {
			return this.applicationDescription;
		}
	}
	
	//Setter-Methode für die Anwendungsbeschreibung
	public void setApplicationDescription(String applicationDescription) {
		if(this.description != null) {
            this.description.set(applicationDescription);
        }
        else {
            this.applicationDescription = applicationDescription;
        }
	}

	//Getter-Methode für das Property der Anwendungsbeschreibung
	public StringProperty getDescriptionProperty() {
		if(this.description == null) {
			this.description = new SimpleStringProperty(this, "applicationDescription", this.applicationDescription);
		}
		return this.description;
	}
	
	//Getter-Methode für die Anwendungskategorie
	public String getApplicationCategory() {
		if(this.category != null) {
			return this.category.get();
		}
		else {
			return this.applicationCategory;
		}
	}
	
	//Setter-Methode für die Anwendungskategorie
	public void setApplicationCategory(String applicationCategory) {
		if(this.category != null) {
            this.category.set(applicationCategory);
        }
        else {
            this.applicationCategory = applicationCategory;
        }
	}

	//Getter-Methode für das Property der Anwendungskategorie
	public StringProperty getCategoryProperty() {
		if(this.category == null) {
			this.category = new SimpleStringProperty(this, "applicationCategory", this.applicationCategory);
		}
		return this.category;
	}
	
	//Getter-Methode für den Anwendungshersteller
	public String getApplicationProducer() {
		if(this.producer != null) {
			return this.producer.get();
		}
		else {
			return this.applicationProducer;
		}
	}
	
	//Setter-Methode für den Anwendungshersteller
	public void setApplicationProducer(String applicationProducer) {
		if(this.producer != null) {
            this.producer.set(applicationProducer);
        }
        else {
            this.applicationProducer = applicationProducer;
        }
	}

	//Getter-Methode für das Property des Anwendungsherstellers
	public StringProperty getProducerProperty() {
		if(this.producer == null) {
			this.producer = new SimpleStringProperty(this, "applicationProducer", this.applicationProducer);
		}
		return this.producer;
	}
	
	//Getter-Methode für den Anwendungsmanager
	public String getApplicationManager() {
		if(this.manager != null) {
			return this.manager.get();
		}
		else {
			return this.applicationManager;
		}
	}
	
	//Setter-Methode für den Anwendungsmanager
	public void setApplicationManager(String applicationManager) {
		if(this.manager != null) {
            this.manager.set(applicationManager);
        }
        else {
            this.applicationManager = applicationManager;
        }
	}

	//Getter-Methode für das Property des Anwendungsmanagers
	public StringProperty getManagerProperty() {
		if(this.manager == null) {
			this.manager = new SimpleStringProperty(this, "applicationManager", this.applicationManager);
		}
		return this.manager;
	}
	
	//Getter-Methode für den Anwendungsbereich
	public String getApplicationDepartment() {
		if(this.department != null) {
			return this.department.get();
		}
		else {
			return this.applicationDepartment;
		}
	}
	
	//Setter-Methode für den Anwendungsbereich
	public void setApplicationDepartment(String applicationDepartment) {
		if(this.department != null) {
            this.department.set(applicationDepartment);
        }
        else {
            this.applicationDepartment = applicationDepartment;
        }
	}

	//Getter-Methode für das Property des Anwendungsbereich
	public StringProperty getDepartmentProperty() {
		if(this.department == null) {
			this.department = new SimpleStringProperty(this, "applicationDepartment", this.applicationDepartment);
		}
		return this.department;
	}
	
	//Getter-Methode für den Anwendungsadmin
	public String getApplicationAdmin() {
		if(this.admin != null) {
			return this.admin.get();
		}
		else {
			return this.applicationAdmin;
		}
	}
	
	//Setter-Methode für den Anwendungsadmin
	public void setApplicationAdmin(String applicationAdmin) {
		if(this.admin != null) {
            this.admin.set(applicationAdmin);
        }
        else {
            this.applicationAdmin = applicationAdmin;
        }
	}

	//Getter-Methode für das Property des Anwendungsadmins
	public StringProperty getAdminProperty() {
		if(this.admin == null) {
			this.admin = new SimpleStringProperty(this, "applicationAdmin", this.applicationAdmin);
		}
		return this.admin;
	}
}
