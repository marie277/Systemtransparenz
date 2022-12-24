package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ApplicationModel {
	
	/*private IntegerProperty id;
	private int applicationID;*/
	private StringProperty name;
	private String applicationName;
	/*private StringProperty description;
	private String applicationDescription;
	private IntegerProperty category;
	private int categoryID;
	private IntegerProperty department;
	private int departmentID;
	private IntegerProperty producer;
	private int producerID;
	private IntegerProperty admin;
	private int adminID;
	private IntegerProperty applicationManager;
	private int applicationManagerID;*/

	/*public ApplicationModel(int applicationID, String applicationName, String applicationDescription, int categoryID, int departmentID, int producerID, int adminID, int applicationManagerID) {
		this.applicationID = applicationID;
		this.applicationName = applicationName;
		this.applicationDescription = applicationDescription;
		this.categoryID = categoryID;
		this.departmentID = departmentID;
		this.producerID = producerID;
		this.adminID = adminID;
		this.applicationManagerID = applicationManagerID;
	}*/
	
	public ApplicationModel(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public String getApplicationName() {
		return (String)((this.name == null) ? this.applicationName : this.name.get());
	}
	
	public void setApplicationName(String applicationName) {
		if (this.name != null) {
            this.name.set((String)applicationName);
        }
        else {
            this.applicationName = applicationName;
        }
	}

	public StringProperty nameProperty() {
		if(this.name == null) {
			this.name = (StringProperty)new SimpleStringProperty((Object)this, "applicationName", this.applicationName);
		}
		return this.name;
	}
	
	/*public int getID() {
		if(id != null) {
			return id.get();
		}
		return 0;
	}
	
	public String getDescription() {
		if(description != null) {
			return description.get();
		}
		return "";
	}
	
	public int getCategory() {
		if(category != null) {
			return category.get();
		}
		return 0;
	}
	
	public int getDepartment() {
		if(department != null) {
			return department.get();
		}
		return 0;
	}
	
	public int getProducer() {
		if(producer != null) {
			return producer.get();
		}
		return 0;
	}
	
	public int getAdmin() {
		if(admin != null) {
			return admin.get();
		}
		return 0;
	}
	
	public int getApplicationManager() {
		if(applicationManager != null) {
			return applicationManager.get();
		}
		return 0;
	}
	
	public void setID(int applicationID) {
		this.idProperty().set(applicationID);
	}
	
	public void setDescription(String applicationDescription) {
		this.descriptionProperty().set(applicationDescription);
	}
	
	public void setCategory(int categoryID) {
		this.categoryProperty().set(categoryID);
	}
	
	public void setDepartment(int departmentID) {
		this.departmentProperty().set(departmentID);
	}
	
	public void setProducer(int producerID) {
		this.producerProperty().set(producerID);
	}
	
	public void setAdmin(int adminID) {
		this.adminProperty().set(adminID);
	}
	
	public void setApplicationManager(int applicationManagerID) {
		this.applicationManagerProperty().set(applicationManagerID);
	}
	
	public IntegerProperty idProperty() {
		if(id == null) {
			id = new SimpleIntegerProperty(0);
		}
		return id;
	}
	
	public StringProperty descriptionProperty() {
		if(description == null) {
			description = new SimpleStringProperty("");
		}
		return description;
	}
	
	public IntegerProperty categoryProperty() {
		if(category == null) {
			category = new SimpleIntegerProperty(0);
		}
		return category;
	}

	public IntegerProperty departmentProperty() {
		if(department == null) {
			department = new SimpleIntegerProperty(0);
		}
		return department;
	}
	
	public IntegerProperty producerProperty() {
		if(producer == null) {
			producer = new SimpleIntegerProperty(0);
		}
		return producer;
	}
	
	public IntegerProperty adminProperty() {
		if(admin == null) {
			admin = new SimpleIntegerProperty(0);
		}
		return admin;
	}
	
	public IntegerProperty applicationManagerProperty() {
		if(applicationManager == null) {
			applicationManager = new SimpleIntegerProperty(0);
		}
		return applicationManager;
	}
	*/

}
