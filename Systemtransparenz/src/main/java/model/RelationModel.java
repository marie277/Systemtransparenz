package model;

import java.util.LinkedList;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RelationModel {

	private String interfaceText;
	private StringProperty interfaceName;
	private static int idGenerator;
	private ReadOnlyIntegerWrapper id;
	private LinkedList<ApplicationInRelation> applicationsInRelation;
	
	static {
		RelationModel.idGenerator = 0;
	}
	
	public RelationModel(final ApplicationInRelation firstApplication, final ApplicationInRelation secondApplication) {
		int newID = RelationModel.idGenerator++;
        this.id = new ReadOnlyIntegerWrapper((Object)this, "id", newID);
        this.interfaceText = "Schnittstelle";
        (this.applicationsInRelation = new LinkedList<ApplicationInRelation>()).add(firstApplication);
        this.applicationsInRelation.add(secondApplication);
	}

	public LinkedList<ApplicationInRelation> getApplications() {
		return this.applicationsInRelation;
	}
	

	public String getInterfaceText() {
		if(this.interfaceName == null) {
			return this.interfaceText;
		}
		else {
			return this.interfaceName.get();
		}
	}
	
	public void setInterfaceText(final String interfaceText) {
		if(this.interfaceName != null) {
			this.interfaceName.set(interfaceText);
		}
		else {
			this.interfaceText = interfaceText;
		}
	}
	
	public StringProperty interfaceNameProperty() {
		if(this.interfaceName == null) {
			this.interfaceName = (StringProperty)new SimpleStringProperty((Object)this, "Interface", this.interfaceText);
		}
		return this.interfaceName;
	}
	
	public int getId() {
		return this.id.get();
	}
	
	public ReadOnlyIntegerProperty idProperty() {
		return this.id.getReadOnlyProperty();
	}
	
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		RelationModel rM = (RelationModel)object;
		return this.getId() == rM.getId() && this.getInterfaceText().equals(rM.getInterfaceText()) && this.getApplications().equals(rM.getApplications());
	}

}
