package control;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import control.edit.ApplicationInRelation;
import control.edit.RelationLine;
import control.edit.RelationNode;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.RelationModel;
import view.ApplicationView;
import view.ModelView;
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
	
	//Getter-Methode für die Beziehungs-Ansicht
	public RelationView getRelationView() {
		return this.relationView;
	}
	
	//Getter-Methode für die zu einer Anwendung gehörige Beziehungs-Linie
	public RelationNode getRelationNode(ApplicationView applicationView) throws IllegalAccessException {
		LinkedList<RelationNode> relationNodes = new LinkedList<RelationNode>();
		for(RelationNode relationNode : this.relationView.getRelationNodes()) {
			if(relationNode.getApplicationInRelation().getApplicationView().getApplicationModel().getApplicationName().equals(applicationView.getApplicationModel().getApplicationName())) {
				relationNodes.add(relationNode);
			}
		}
		return relationNodes.get(0);
	}
	
	//Methode zur Festlegung, ob eine Beziehung gespeichert wurde
	public void setSaved(boolean isSaved) {
		this.relationView.getModelView().getFileExportControl().setSaved(isSaved);
	}
	
	//Methode zur Festlegung, ob eine Beziehungs-Ansicht ausgewählt wurde
	public void setSelected(boolean selected) {
		this.relationView.setSelected(selected);
	}

	//Methode zur Aktualisierung der Schriftgröße des Elements
	@Override
	public void zoom(double factor) {
		super.zoom(factor);
		this.relationText = this.relationView.getRelationText();
		String fontName = this.relationText.getFont().getName();
		double fontSize = this.relationText.getFont().getSize();
		this.relationText.setFont(new Font(fontName, fontSize*factor));
		for(RelationNode relationNode : this.relationView.getRelationNodes()) {
			double point2 = relationNode.getRelationArrow().getPoints().get(2);
			double point3 = relationNode.getRelationArrow().getPoints().get(3);
			double point4 = relationNode.getRelationArrow().getPoints().get(4);
			double point5 = relationNode.getRelationArrow().getPoints().get(5);
			relationNode.getRelationArrow().getPoints().setAll(new Double[]{
					0d, 0d,
					point2*factor, point3*factor,
			        point4*factor, point5*factor,
			});
			relationNode.getRelationLine().setStrokeWidth(relationNode.getRelationLine().getStrokeWidth()*factor);
		}
		this.relationBounds = this.relationView.getRelationText().getLayoutBounds();
		double x = this.relationBounds.getWidth()*factor;
		double y = this.relationBounds.getHeight()*factor;
		this.relationView.setLayout(x, y);
		this.setSaved(false);
	}
	
	//Methode zur Entfernung der an einer Beziehung beteiligten Anwendungen
	public void removeApplications() {
		for(RelationNode relationNode : this.relationView.getRelationNodes()) {
			ApplicationInRelation applicationInRelation = relationNode.getApplicationInRelation();
			relationNode.unbindRelation();
			this.relationView.getRelationModel().getApplications().remove(applicationInRelation);
			RelationLine relationLine = relationNode.getRelationLine();
			relationLine.endXProperty().unbind();
			relationLine.endYProperty().unbind();
			relationLine.startXProperty().unbind();
			relationLine.startYProperty().unbind();
			for(Node node : relationNode.getRelationNodes()) {
				node.setVisible(false);
			}
			ModelView modelView = this.relationView.getModelView();
			if(modelView != null) {
				modelView.getChildren().removeAll(relationNode.getRelationNodes());
			}
		}
		LinkedList<RelationNode> relationNodes = this.relationView.getRelationNodes();
		relationNodes.removeAll(relationNodes);
		LinkedList<ApplicationInRelation> applications = this.relationView.getRelationModel().getApplications();
		applications.removeAll(applications);
		this.setSaved(false);
	}	
	
	//Methode zum Hinzufügen einer Anwendung als Beziehungs-Teilnehmer
	public void addPartOfRelation(ApplicationView applicationView) {
		ApplicationInRelation applicationInRelation = new ApplicationInRelation(applicationView);
		this.relationView.getRelationModel().getApplications().add(applicationInRelation);
		String relationType = this.relationView.getRelationModel().getRelationType();
		boolean relationDirection = this.relationView.getRelationModel().getRelationDirection();
		RelationNode relationNode = new RelationNode(applicationInRelation, relationType, relationDirection);
		this.relationView.getRelationNodes().add(relationNode);
		Region elementRegion = this.relationView.getElementRegion();
		DoubleProperty endX = relationNode.getRelationLine().endXProperty();
		DoubleProperty layoutX = elementRegion.layoutXProperty();
		DoubleBinding widthX = elementRegion.prefWidthProperty().divide(2.0);
		endX.bind(layoutX.add(widthX));
		DoubleProperty endY = relationNode.getRelationLine().endYProperty();
		DoubleProperty layoutY = elementRegion.layoutYProperty();
		DoubleBinding widthY = elementRegion.prefHeightProperty().divide(2.0);
		endY.bind(layoutY.add(widthY));
		applicationInRelation.getApplicationView().getElementRegion().boundsInParentProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				for(RelationNode rN : relationView.getRelationNodes()) {
	                if(rN.getApplicationInRelation().equals(applicationInRelation)) {
	                	rN.getRelationHub();
	                }
	            }	
			}
        });
		if(this.relationView.getModelView() != null) {
			this.relationView.getModelView().getChildren().addAll(0, relationNode.getRelationNodes());
		}
		this.setSaved(false);
	}
	
	//Methode zum Entfernen einer Anwendung aus einer Beziehung
	public void removeApplication(ApplicationView applicationView) throws IllegalAccessException {
		ApplicationInRelation applicationInRelation = null;
		RelationNode relationNode = this.getRelationNode(applicationView);
		if(relationNode == null) {
			return;
		}
		relationNode.unbindRelation();
		for(ApplicationInRelation aIR : this.relationView.getRelationModel().getApplications()) {
			if(aIR.getApplicationView().equals(applicationView)) {
				applicationInRelation = aIR;
				break;
			}
		}
		if(applicationInRelation == null) {
			return;
		}
		this.relationView.getRelationModel().getApplications().remove(applicationInRelation);
		this.relationView.getRelationNodes().remove(relationNode);
		RelationLine relationLine = relationNode.getRelationLine();
		relationLine.endXProperty().unbind();
		relationLine.endYProperty().unbind();
		relationLine.startXProperty().unbind();
		relationLine.startYProperty().unbind();
		for(Node node : relationNode.getRelationNodes()) {
			node.setVisible(false);
		}
		ModelView modelView = this.relationView.getModelView();
		if(modelView != null) {
			modelView.getChildren().removeAll(relationNode.getRelationNodes());
		}
		relationNode = null;
		applicationInRelation = null;
		modelView.getFileExportControl().setSaved(false);
	}
	
	//Methode zum Entfernen eines Beziehungs-Teilnehmers aus einer Beziehung
	public void removePartOfRelation(ApplicationInRelation applicationInRelation) {
		ApplicationInRelation aIR = applicationInRelation;
		RelationNode relationNode = null;
		for(RelationNode rN : this.relationView.getRelationNodes()) {
			if(rN.getApplicationInRelation().equals(aIR)) {
				relationNode = rN;
				break;
			}
		}
		if(relationNode == null) {
			return;
		}
		relationNode.unbindRelation();
		if(aIR == null) {
			return;
		}
		this.relationView.getRelationModel().getApplications().remove(aIR);
		this.relationView.getRelationNodes().remove(relationNode);
		RelationLine relationLine = relationNode.getRelationLine();
		relationLine.endXProperty().unbind();
		relationLine.endYProperty().unbind();
		relationLine.startXProperty().unbind();
		relationLine.startYProperty().unbind();
		for(Node node : relationNode.getRelationNodes()) {
			node.setVisible(false);
		}
		ModelView modelView = this.relationView.getModelView();
		if(modelView != null) {
			modelView.getChildren().removeAll(relationNode.getRelationNodes());
		}
		relationNode = null;
		aIR = null;
		modelView.getFileExportControl().setSaved(false);
	}
	
	//Methode zur Prüfung, ob eine Anwendungs-Ansicht Teil einer Beziehungs-Ansicht ist
	public boolean isPartOfRelation(ApplicationView applicationView) {
		LinkedList<ApplicationInRelation> applications = this.relationView.getRelationModel().getApplications();
		for(ApplicationInRelation applicationInRelation : applications) {
			if(applicationInRelation.getApplicationView().equals(applicationView)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	//Methode zur Bewegung einer Beziehungs-Ansicht innerhalb des Modells
	@Override
	public void move(double x, double y) {
        this.setMoved(x, y);
        for (RelationNode relationNode : this.relationView.getRelationNodes()) {
            relationNode.getRelationHub();
        }
	}
	
	//Methode zur Erstellung einer Beziehung als XML-Element, welches in einer Datei exportiert werden kann
	public Element createXMLElement(Document doc) {
		Element element = super.createXMLElement(doc);
		Element relation = doc.createElement("Beziehung");
		relation.appendChild(element);
		String relationType = this.relationView.getRelationModel().getRelationType();
		relation.setAttribute("Beziehungstyp", relationType);
		String relationDirection = "";
		if(this.relationView.getRelationModel().getRelationDirection() == true) {
			relationDirection = this.relationView.getRelationModel().getApplications().get(0).getApplicationView().getApplicationModel().getApplicationName();
		}
		relation.setAttribute("EingehendeAnwendung", relationDirection);
		Element applicationInRelation = doc.createElement("Beziehungsteilnehmer");
		for(RelationNode relationNode : this.relationView.getRelationNodes()) {
			applicationInRelation.appendChild(relationNode.createXMLElement(doc));
		}
		relation.appendChild(applicationInRelation);
		return relation;
	}
	
	//Methode zum Hinzufügen einer Beziehung aus einem XML-Dokument, welches als Modell dargestellt wird
	public static void importXMLElement(Element element, ModelView modelView) {
		NodeList nodes = element.getElementsByTagName("Beziehungslinie");
		LinkedList<RelationNode> relationNodes = new LinkedList<RelationNode>();
		for(int i = 0; i < nodes.getLength(); i++) {
			relationNodes.add(RelationNode.importFromXML((Element) nodes.item(i), modelView));
		}
		boolean relationDirection = false;
		if(element.getAttribute("EingehendeAnwendung").equals(relationNodes.getFirst()
				.getApplicationInRelation().getApplicationView().getApplicationModel()
				.getApplicationName())){
			relationDirection = true;
		}
		RelationModel relationModel = new RelationModel(relationNodes.getFirst()
				.getApplicationInRelation(), 
				relationNodes.get(1).getApplicationInRelation(), 
				element.getAttribute("Beziehungstyp"), relationDirection);
		RelationView relationView = new RelationView(relationModel, modelView);
		modelView.addElement(relationView);
		NodeList nodeList = element.getElementsByTagName("XML-Element");
		Element item = (Element)nodeList.item(0);
		org.w3c.dom.Node node = item.getElementsByTagName("Element").item(0);
		ElementControl.importXMLSettings((Element)node, relationView);
	}
	
}
