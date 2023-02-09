package control;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import control.edit.RelationLine;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.ApplicationInRelation;
import model.RelationModel;
import view.ApplicationView;
import view.ModelView;
import view.RelationLineView;
import view.RelationView;

//Klasse zur Steuerung einer Beziehungs-Ansicht, beerbt die abstrakte Klasse zur allgemeinen Elements-Steuerung
public class RelationControl extends ElementControl {
	
	private RelationView relationView;
	private Bounds relationBounds;
	private Text relationText;
	
	//Konstruktor
	public RelationControl(RelationView relationView) {
		super(relationView);
		this.relationView = relationView;
	}

	//Methode zur Festlegung, ob eine Beziehung ausgewählt wurde
	public boolean isSelected() {
		return this.relationView.isSelected();
	}
	
	public void setSaved(boolean isSaved) {
		this.relationView.getModelView().getFileExportControl().setSaved(isSaved);
	}

	//Methode zur Entfernung der an einer Beziehung beteiligten Anwendungen
	public void removeApplications() {
		for(RelationLineView relationLineView : this.relationView.getRelationNodes()) {
			ApplicationInRelation applicationInRelation = relationLineView.getApplicationInRelation();
			relationLineView.unbindRelation();
			this.relationView.getRelationModel().getApplications().remove(applicationInRelation);
			RelationLine relationLine = relationLineView.getRelationLine();
			relationLine.endXProperty().unbind();
			relationLine.endYProperty().unbind();
			relationLine.startXProperty().unbind();
			relationLine.startYProperty().unbind();
			for(Node node : relationLineView.getRelationNodes()) {
				node.setVisible(false);
			}
			ModelView modelView = this.relationView.getModelView();
			if(modelView != null) {
				modelView.getChildren().removeAll(relationLineView.getRelationNodes());
			}
		}
		LinkedList<RelationLineView> relationNodes = this.relationView.getRelationNodes();
		relationNodes.removeAll(relationNodes);
		LinkedList<ApplicationInRelation> applications = this.relationView.getRelationModel().getApplications();
		applications.removeAll(applications);
		this.setSaved(false);
	}

	//Methode zur Aktualisierung der Elements-Grenzen
	@Override
	public void refresh() {
		this.relationBounds = this.relationView.getRelationText().getLayoutBounds();
		this.relationView.setLayout(this.relationBounds.getWidth()*1.2, this.relationBounds.getHeight()*1.2);
		this.setSaved(false);
	}
	
	//Setter-Methode für den Beziehungs-Text, welcher den Beziehungstyp angibt
	public void setRelationType(String relationType) {
		this.relationView.getRelationModel().setRelationType(relationType);
		this.refresh();
	}
	
	//Getter-Methode für die zu einer Anwendung gehörige Beziehungs-Linie
	public RelationLineView getRelationLineView(ApplicationView applicationView) throws IllegalAccessException {
		LinkedList<RelationLineView> relationNodes = new LinkedList<RelationLineView>();
		for(RelationLineView relationLineView : this.relationView.getRelationNodes()) {
			if(relationLineView.getApplicationInRelation().getApplicationView().equals(applicationView)) {
				relationNodes.add(relationLineView);
			}
		}
		return relationNodes.getFirst();
	}
	
	//Methode zur Aktualisierung der Schriftgröße des Elements
	@Override
	public void zoom(double factor) {
		super.zoom(factor);
		this.relationText = this.relationView.getRelationText();
		String fontName = this.relationText.getFont().getName();
		double fontSize = this.relationText.getFont().getSize();
		this.relationText.setFont(new Font(fontName, fontSize*factor));
		this.refresh();
	}
	
	//Methode zur Festlegung, ob eine Beziehungs-Ansicht ausgewählt wurde
	public void setSelected(boolean selected) {
		this.relationView.setSelected(selected);
	}

	//Methode zur Prüfung, ob eine Beziehungs-Ansicht mit der gesteuerten übereinstimmt
	@Override
	public boolean equals(Object object) {
		if(super.equals(object) && this.getRelationView().equals(((RelationControl)object).getRelationView())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Getter-Methode für die Beziehungs-Ansicht
	public RelationView getRelationView() {
		return this.relationView;
	}
	
	//Methode zum Hinzufügen einer Anwendung als Beziehungs-Teilnehmer
	public void addPartOfRelation(ApplicationView applicationView) {
		ApplicationInRelation applicationInRelation = new ApplicationInRelation(applicationView);
		this.relationView.getRelationModel().getApplications().add(applicationInRelation);
		String relationType = this.relationView.getRelationModel().getRelationType();
		boolean relationDirection = this.relationView.getRelationModel().getRelationDirection();
		RelationLineView relationLineView = new RelationLineView(applicationInRelation, relationType, relationDirection);
		this.relationView.getRelationNodes().add(relationLineView);
		Region elementRegion = this.relationView.getElementRegion();
		DoubleProperty endX = relationLineView.getRelationLine().endXProperty();
		DoubleProperty layoutX = elementRegion.layoutXProperty();
		DoubleBinding widthX = elementRegion.prefWidthProperty().divide(2.0);
		endX.bind((ObservableValue<? extends Number>)layoutX.add((ObservableNumberValue)widthX));
		DoubleProperty endY = relationLineView.getRelationLine().endYProperty();
		DoubleProperty layoutY = elementRegion.layoutYProperty();
		DoubleBinding widthY = elementRegion.prefHeightProperty().divide(2.0);
		endY.bind((ObservableValue<? extends Number>)layoutY.add((ObservableNumberValue)widthY));
		applicationInRelation.getApplicationView().getElementRegion().boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            for (RelationLineView rLV : this.relationView.getRelationNodes()) {
                if (rLV.getApplicationInRelation().equals(applicationInRelation)) {
                	rLV.getRelationHub();
                }
            }
        });
		if(this.relationView.getModelView() != null) {
			this.relationView.getModelView().getChildren().addAll(0, relationLineView.getRelationNodes());
		}
		this.setSaved(false);
	}
	
	//Methode zum Entfernen einer Anwendung aus einer Beziehung
	public void removeApplication(ApplicationView applicationView) throws IllegalAccessException {
		ApplicationInRelation applicationInRelation = null;
		RelationLineView relationLineView = this.getRelationLineView(applicationView);
		if(relationLineView == null) {
			return;
		}
		relationLineView.unbindRelation();
		for(ApplicationInRelation applicationInRelationRel : this.relationView.getRelationModel().getApplications()) {
			if(applicationInRelationRel.getApplicationView().equals(applicationView)) {
				applicationInRelation = applicationInRelationRel;
				break;
			}
		}
		if(applicationInRelation == null) {
			return;
		}
		this.relationView.getRelationModel().getApplications().remove(applicationInRelation);
		this.relationView.getRelationNodes().remove(relationLineView);
		RelationLine relationLine = relationLineView.getRelationLine();
		relationLine.endXProperty().unbind();
		relationLine.endYProperty().unbind();
		relationLine.startXProperty().unbind();
		relationLine.startYProperty().unbind();
		for(Node node : relationLineView.getRelationNodes()) {
			node.setVisible(false);
		}
		ModelView modelView = this.relationView.getModelView();
		if(modelView != null) {
			modelView.getChildren().removeAll(relationLineView.getRelationNodes());
		}
		relationLineView = null;
		applicationInRelation = null;
		modelView.getFileExportControl().setSaved(false);
	}
	
	//Methode zum Entfernen eines Beziehungs-Teilnehmers aus einer Beziehung
	public void removePartOfRelation(ApplicationInRelation applicationInRelation) {
		ApplicationInRelation applicationInRelationRel = applicationInRelation;
		RelationLineView relationLineView = null;
		for(RelationLineView relationLineViewApp : this.relationView.getRelationNodes()) {
			if(relationLineViewApp.getApplicationInRelation().equals(applicationInRelationRel)) {
				relationLineView = relationLineViewApp;
				break;
			}
		}
		if(relationLineView == null) {
			return;
		}
		relationLineView.unbindRelation();
		if(applicationInRelationRel == null) {
			return;
		}
		this.relationView.getRelationModel().getApplications().remove(applicationInRelationRel);
		this.relationView.getRelationNodes().remove(relationLineView);
		RelationLine relationLine = relationLineView.getRelationLine();
		relationLine.endXProperty().unbind();
		relationLine.endYProperty().unbind();
		relationLine.startXProperty().unbind();
		relationLine.startYProperty().unbind();
		for(Node node : relationLineView.getRelationNodes()) {
			node.setVisible(false);
		}
		ModelView modelView = this.relationView.getModelView();
		if(modelView != null) {
			modelView.getChildren().removeAll(relationLineView.getRelationNodes());
		}
		relationLineView = null;
		applicationInRelationRel = null;
		modelView.getFileExportControl().setSaved(false);
	}
	
	//Methode zur Prüfung, ob eine Anwendungs-Ansicht Teil einer Beziehungs-Ansicht ist
	public boolean isPartOfRelation(ApplicationView applicationView) {
		LinkedList<ApplicationInRelation> applications = this.relationView.getRelationModel().getApplications();
		for(ApplicationInRelation applicationInRelation : applications) {
			if(applicationInRelation.getApplicationView().equals(applicationView)) {
				return true;
			}
		}
		return false;
	}
	

	//Methode zur Erstellung einer Beziehung als XML-Element, welches in einer Datei exportiert werden kann
	public Element createXMLElement(Document doc) {
		Element relation = doc.createElement("Beziehung");
		String relationType = this.relationView.getRelationModel().getRelationType();
		relation.setAttribute("Beziehungstyp", relationType);
		String relationDirection = "";
		if(this.relationView.getRelationModel().getRelationDirection() == true) {
			relationDirection = this.relationView.getRelationModel().getApplications().get(0).getApplicationView().getApplicationModel().getApplicationName();
		}
		relation.setAttribute("Beziehungsrichtung", relationDirection);
		Element applicationInRelation = doc.createElement("Beziehungsteilnehmer");
		for(RelationLineView relationLineView : this.relationView.getRelationNodes()) {
			applicationInRelation.appendChild(relationLineView.createXMLElement(doc));
		}
		relation.appendChild(applicationInRelation);
		return relation;
	}
	
	//Methode zum Hinzufügen einer Beziehung aus einem XML-Dokument, welches als Modell dargestellt wird
	public static void importXMLElement(Element element, ModelView modelView) {
		NodeList relationNodes = element.getElementsByTagName("Beziehungslinie");
		LinkedList<RelationLineView> relationLineViews = new LinkedList<RelationLineView>();
		for(int i = 0; i < relationNodes.getLength(); i++) {
			relationLineViews.add(RelationLineView.importFromXML((Element) relationNodes.item(i), modelView));
		
		}
		boolean relationDirection = false;
		if(element.getAttribute("Beziehungsrichtung").equals(relationLineViews.getFirst().getApplicationInRelation().getApplicationView().getApplicationModel().getApplicationName())){
			relationDirection = false;
		}
		RelationModel relationModel = new RelationModel(relationLineViews.getFirst().getApplicationInRelation(), relationLineViews.get(1).getApplicationInRelation(), element.getAttribute("Beziehungstyp"), relationDirection);
		for(int i = 2; i < relationLineViews.size(); i++) {
			relationModel.getApplications().add(relationLineViews.get(i).getApplicationInRelation());
		}
		RelationView relationView = new RelationView(relationModel, modelView);
		relationView.getRelationControl().setRelationType(element.getAttribute("Beziehungstyp"));
		relationView.getRelationControl().setRelationDirection(relationDirection);
		modelView.addElement(relationView);
	}

	private void setRelationDirection(boolean relationDirection) {
		this.relationView.getRelationModel().setRelationDirection(relationDirection);
		//this.refresh();
	}

}
