package model;

import java.util.LinkedList;

import control.edit.ApplicationInRelation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Klasse zur Speicherung der Daten einer Anwendung
public class RelationModel {

	private String relationType;
	private StringProperty relationTypeProperty;
	private boolean relationDirection;
	private BooleanProperty relationDirectionProperty;
	private static int number = 0;
	private IntegerProperty idProperty;
	private LinkedList<ApplicationInRelation> applications;
	
	//Konstruktor
	public RelationModel(ApplicationInRelation firstApplication, ApplicationInRelation secondApplication, String relationType, boolean relationDirection) {
		this.applications = new LinkedList<ApplicationInRelation>();
        this.applications.add(firstApplication);
        this.applications.add(secondApplication);
        this.relationType = relationType;
        this.relationDirection = relationDirection;
		int relationId = RelationModel.number++;
        this.idProperty = new SimpleIntegerProperty(this, "id", relationId); 
	}

	//Getter-Methode für die in einer Beziehung enthaltenen Anwendungen
	public LinkedList<ApplicationInRelation> getApplications() {
		return this.applications;
	}
	
	//Getter-Methode für den Beziehungstext, welcher dem Beziehungstypen entspricht
	public String getRelationType() {
		if(this.relationTypeProperty != null) {
			return this.relationTypeProperty.get();
		}
		else {
			return this.relationType;
		}
	}
	
	//Setter-Methode für den Beziehungstext
	public void setRelationType(String relationType) {
		if(this.relationTypeProperty != null) {
			this.relationTypeProperty.set(relationType);
		}
		else {
			this.relationType = relationType;
		}
	}
	
	//Getter-Methode für das Property des Beziehungstexts
	public StringProperty getRelationTypeProperty() {
		if(this.relationTypeProperty == null) {
			this.relationTypeProperty = new SimpleStringProperty(this, "relationType", this.relationType);
		}
		return this.relationTypeProperty;
	}
	
	//Setter-Methode für die Beziehungsrichtung
	public void setRelationDirection(boolean relationDirection) {
		this.relationDirection = relationDirection;
	}
	
	//Getter-Methode für die Beziehungsrichtung
	public boolean getRelationDirection() {
		if(this.relationDirectionProperty != null) {
			return this.relationDirectionProperty.get();
		}
		else {
			return this.relationDirection;
		}
	}
	
	//Getter-Methode für das Property der Beziehungsrichtung
	public BooleanProperty getRelationDirectionProperty() {
		if(this.relationDirectionProperty == null) {
			this.relationDirectionProperty = new SimpleBooleanProperty(this, "relationDirection", this.relationDirection);
		}
		return this.relationDirectionProperty;
	}
	
	//Getter-Methode für die ID der beziehung
	public int getId() {
		return this.idProperty.get();
	}
	
	//Getter-Methode für das Property der ID
	public IntegerProperty getIdProperty() {
		return this.idProperty;
	}

}
