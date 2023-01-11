package model;

import java.util.LinkedList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Klasse zur Speicherung der Daten einer Anwendung
public class RelationModel {

	private String relationText;
	private StringProperty relationName;
	private static int number = 0;
	private IntegerProperty id;
	private LinkedList<ApplicationInRelation> applicationsInRelation;
	
	//Konstruktor
	public RelationModel(ApplicationInRelation firstApplication, ApplicationInRelation secondApplication) {
		this.applicationsInRelation = new LinkedList<ApplicationInRelation>();
        this.applicationsInRelation.add(firstApplication);
        this.applicationsInRelation.add(secondApplication);
		this.relationText = "Verknüpft mit";
		int relationId = RelationModel.number++;
        this.id = new SimpleIntegerProperty(this, "id", relationId); 
	}

	//Getter-Methode für die in einer Beziehung enthaltenen Anwendungen
	public LinkedList<ApplicationInRelation> getApplications() {
		return this.applicationsInRelation;
	}
	
	//Getter-Methode für den Beziehungstext, welcher dem Beziehungstypen entspricht
	public String getRelationText() {
		if(this.relationName != null) {
			return this.relationName.get();
		}
		else {
			return this.relationText;
		}
	}
	
	//Setter-Methode für den Beziehungstext
	public void setRelationText(String relationText) {
		if(this.relationName != null) {
			this.relationName.set(relationText);
		}
		else {
			this.relationText = relationText;
		}
	}
	
	//Getter-Methode für das Property des Beziehungstexts
	public StringProperty getRelationNameProperty() {
		if(this.relationName == null) {
			this.relationName = new SimpleStringProperty(this, "Relation", this.relationText);
		}
		return this.relationName;
	}
	
	//Getter-Methode für die ID der beziehung
	public int getId() {
		return this.id.get();
	}
	
	//Getter-Methode für das Property der ID
	public IntegerProperty getIdProperty() {
		return this.id;
	}
	
	//Methode zum Vergleich, ob eine Beziehung mit der hier gespeicherten übereinstimmt
	@Override
	public boolean equals(Object object) {
		RelationModel relationModel = (RelationModel)object;
		if(super.equals(object) && this.getId() == relationModel.getId() && this.getRelationText().equals(relationModel.getRelationText()) && this.getApplications().equals(relationModel.getApplications())) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
