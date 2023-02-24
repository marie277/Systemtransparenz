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
import javafx.beans.value.ObservableNumberValue;
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
	private static final double factor = 1.2;
	
	//Konstruktor
	public RelationControl(RelationView relationView) {
		super(relationView);
		this.relationView = relationView;
	}
	
	//Methode zur Festlegung, ob eine Beziehung gespeichert wurde
	public void setSaved(boolean isSaved) {
		this.relationView.getModelView().getFileExportControl().setSaved(isSaved);
	}

	//Methode zur Entfernung der an einer Beziehung beteiligten Anwendungen
	public void removeApplications() {
		for(RelationNode relationLineView : this.relationView.getRelationNodes()) {
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
		LinkedList<RelationNode> relationNodes = this.relationView.getRelationNodes();
		relationNodes.removeAll(relationNodes);
		LinkedList<ApplicationInRelation> applications = this.relationView.getRelationModel().getApplications();
		applications.removeAll(applications);
		this.setSaved(false);
	}

	//Methode zur Aktualisierung der Elements-Grenzen
	@Override
	public void refresh() {
		this.relationBounds = this.relationView.getRelationText().getLayoutBounds();
		double x = this.relationBounds.getWidth()*factor;
		double y = this.relationBounds.getHeight()*factor;
		this.relationView.setLayout(x, y);
		this.setSaved(false);
	}
	
	//Getter-Methode für die zu einer Anwendung gehörige Beziehungs-Linie
	public RelationNode getRelationLineView(ApplicationView applicationView) throws IllegalAccessException {
		LinkedList<RelationNode> relationNodes = new LinkedList<RelationNode>();
		for(RelationNode relationLineView : this.relationView.getRelationNodes()) {
			if(relationLineView.getApplicationInRelation().getApplicationView().getApplicationModel().getApplicationName().equals(applicationView.getApplicationModel().getApplicationName())) {
				relationNodes.add(relationLineView);
			}
		}
		return relationNodes.get(0);
	}
	
	//Methode zur Aktualisierung der Schriftgröße des Elements
	@Override
	public void zoom(double factor) {
		super.zoom(factor);
		this.relationText = this.relationView.getRelationText();
		String fontName = this.relationText.getFont().getName();
		double fontSize = this.relationText.getFont().getSize();
		this.relationText.setFont(new Font(fontName, fontSize*factor));
		for(RelationNode rN : this.relationView.getRelationNodes()) {
			double point2 = rN.getRelationArrow().getPoints().get(2);
			double point3 = rN.getRelationArrow().getPoints().get(3);
			double point4 = rN.getRelationArrow().getPoints().get(4);
			double point5 = rN.getRelationArrow().getPoints().get(5);
			rN.getRelationArrow().getPoints().setAll(new Double[]{
					0d, 0d,
					point2*factor, point3*factor,
			        point4*factor, point5*factor,
			});
		}
		this.refresh();
	}
	
	//Methode zur Festlegung, ob eine Beziehungs-Ansicht ausgewählt wurde
	public void setSelected(boolean selected) {
		this.relationView.setSelected(selected);
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
		RelationNode relationLineView = new RelationNode(applicationInRelation, relationType, relationDirection);
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
            for (RelationNode rLV : this.relationView.getRelationNodes()) {
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
		RelationNode relationLineView = this.getRelationLineView(applicationView);
		if(relationLineView == null) {
			return;
		}
		relationLineView.unbindRelation();
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
		ApplicationInRelation aIR = applicationInRelation;
		RelationNode relationLineView = null;
		for(RelationNode rLV : this.relationView.getRelationNodes()) {
			if(rLV.getApplicationInRelation().equals(aIR)) {
				relationLineView = rLV;
				break;
			}
		}
		if(relationLineView == null) {
			return;
		}
		relationLineView.unbindRelation();
		if(aIR == null) {
			return;
		}
		this.relationView.getRelationModel().getApplications().remove(aIR);
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
		for(RelationNode relationLineView : this.relationView.getRelationNodes()) {
			applicationInRelation.appendChild(relationLineView.createXMLElement(doc));
		}
		relation.appendChild(applicationInRelation);
		return relation;
	}
	
	//Methode zum Hinzufügen einer Beziehung aus einem XML-Dokument, welches als Modell dargestellt wird
	public static void importXMLElement(Element element, ModelView modelView) {
		NodeList relationNodes = element.getElementsByTagName("Beziehungslinie");
		LinkedList<RelationNode> relationLineViews = new LinkedList<RelationNode>();
		for(int i = 0; i < relationNodes.getLength(); i++) {
			relationLineViews.add(RelationNode.importFromXML((Element) relationNodes.item(i), modelView));
		}
		boolean relationDirection = false;
		if(element.getAttribute("Beziehungsrichtung").equals(relationLineViews.getFirst().getApplicationInRelation().getApplicationView().getApplicationModel().getApplicationName())){
			relationDirection = true;
		}
		RelationModel relationModel = new RelationModel(relationLineViews.getFirst().getApplicationInRelation(), relationLineViews.get(1).getApplicationInRelation(), element.getAttribute("Beziehungstyp"), relationDirection);
		for(int i = 2; i < relationLineViews.size(); i++) {
			relationModel.getApplications().add(relationLineViews.get(i).getApplicationInRelation());
		}
		RelationView relationView = new RelationView(relationModel, modelView);
		relationView.getRelationModel().setRelationType(element.getAttribute("Beziehungstyp"));
		relationView.getRelationModel().setRelationDirection(relationDirection);
		modelView.addElement(relationView);
	}
	
	//Methode zur Bewegung einer Beziehungs-Ansicht innerhalb des Modells
	@Override
	public void move(double x, double y) {
        this.setMoved(x, y);
        for (RelationNode rLV : this.relationView.getRelationNodes()) {
            rLV.getRelationHub();
        }
	}

}
