package control;

import java.util.Collection;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.text.Font;
import model.ApplicationInRelation;
import model.RelationModel;
import view.ApplicationView;
import view.ModelView;
import view.RelationLineView;
import view.RelationView;

public class RelationControl extends ElementControl {
	
	private RelationView relationView;
	
	public RelationControl(RelationView relationView) {
		super(relationView);
		this.relationView = relationView;
	}

	public boolean isSelected(boolean b) {
		return this.relationView.isSelected();
	}

	public void removeApplications() {
		for(RelationLineView rLV : this.relationView.getRelationNodes()) {
			ApplicationInRelation aIR = rLV.getApplicationInRelation();
			rLV.unbindRelation();
			this.relationView.getRelationModel().getApplications().remove(aIR);
			rLV.getRelationLine().endXProperty().unbind();
			rLV.getRelationLine().endYProperty().unbind();
			rLV.getRelationLine().startXProperty().unbind();
			rLV.getRelationLine().startYProperty().unbind();
			for(Node n : rLV.getNodes()) {
				n.setVisible(false);
			}
			if(this.relationView.getModelView() != null) {
				this.relationView.getModelView().getChildren().removeAll((Collection<?>)rLV.getNodes());
			}
		}
		this.relationView.getRelationNodes().removeAll(this.relationView.getRelationNodes());
		this.relationView.getRelationModel().getApplications().removeAll(this.relationView.getRelationModel().getApplications());
		this.relationView.getModelView().getFileExportControl().setSaved(false);
	}

	public Element createXMLElement(Document doc) {
		Element relation = doc.createElement("Beziehung");
		relation.setAttribute("Beziehungstyp", this.relationView.getRelationModel().getInterfaceText());
		Element applicationInRelation = doc.createElement("Beziehungsteilnehmer");
		for(RelationLineView rLV : this.relationView.getRelationNodes()) {
			applicationInRelation.appendChild(rLV.createXMLElement(doc));
		}
		relation.appendChild(applicationInRelation);
		/*Element element = super.createXMLElement(doc);
		relation.appendChild(element);*/
		return relation;
	}

	@Override
	public void refresh() {
		this.relationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public void setRelationText(String relationText) {
		this.relationView.getRelationModel().setInterfaceText(relationText);
		this.relationView.setWidthHeight(((Node) this.relationView.getInterfaceText()).getLayoutBounds().getWidth()*2.0, ((Node) this.relationView.getInterfaceText()).getLayoutBounds().getWidth());
		this.refresh();
	}
	
	public RelationLineView getRelationLineView(ApplicationView applicationView) throws IllegalAccessException {
		LinkedList<RelationLineView> relationLineViews = new LinkedList<RelationLineView>();
		for(RelationLineView rLV : this.relationView.getRelationNodes()) {
			if(rLV.getApplicationInRelation().getApplicationView().equals(applicationView)) {
				relationLineViews.add(rLV);
			}
		}
		if(relationLineViews.size() > 1) {
			throw new IllegalAccessException("");
		}
		if(relationLineViews.size() == 1) {
			return relationLineViews.getFirst();
		}
		return null;
	}
	
	@Override
	public void zoom(double factor) {
		super.zoom(factor);
		Font f = this.relationView.getInterfaceText().getFont();
		this.relationView.getInterfaceText().setFont(new Font(f.getName(), f.getSize() * factor));
		this.refresh();
	}
	
	public void setSelected(boolean selected) {
		this.relationView.setSelected(selected);
	}
	
	public void move(double x, double y) {
		this.setWidthHeight(x, y);
	}
	
	@Override
	public boolean equals(Object object) {
		return super.equals(object) && this.getRelationView().equals(((RelationControl)object).getRelationView());
	}
	
	public RelationView getRelationView() {
		return this.relationView;
	}
	
	public void addPartOfRelation(ApplicationView applicationView) {
		ApplicationInRelation applicationInRelation = new ApplicationInRelation(applicationView);
		this.relationView.getRelationModel().getApplications().add(applicationInRelation);
		RelationLineView relationLineView = new RelationLineView(applicationInRelation);
		this.relationView.getRelationNodes().add(relationLineView);
		relationLineView.getRelationLine().endXProperty().bind((ObservableValue<? extends Number>)this.relationView.getElementRegion().layoutXProperty().add((ObservableNumberValue)this.relationView.getElementRegion().prefWidthProperty().divide(2.0)));
		relationLineView.getRelationLine().endYProperty().bind((ObservableValue<? extends Number>)this.relationView.getElementRegion().layoutYProperty().add((ObservableNumberValue)this.relationView.getElementRegion().prefHeightProperty().divide(2.0)));
		applicationInRelation.getApplicationView().getElementRegion().boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
			for(RelationLineView rLV : this.relationView.getRelationNodes()) {
				if(rLV.getApplicationInRelation().equals(applicationInRelation)) {
					rLV.calculateCenterPoint();
				}
			}
		});
		if(this.relationView.getModelView() != null) {
			this.relationView.getModelView().getChildren().addAll(0, (Collection<? extends Node>)relationLineView.getNodes());
		}
		this.relationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public void removeApplication(ApplicationView applicationView) throws IllegalAccessException {
		ApplicationInRelation applicationInRelation = null;
		RelationLineView relationLineView = this.getRelationLineView(applicationView);
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
		relationLineView.getRelationLine().endXProperty().unbind();
		relationLineView.getRelationLine().endYProperty().unbind();
		relationLineView.getRelationLine().startXProperty().unbind();
		relationLineView.getRelationLine().startYProperty().unbind();
		for(Node n : relationLineView.getNodes()) {
			n.setVisible(false);
		}
		if(this.relationView.getModelView() != null) {
			this.relationView.getModelView().getChildren().removeAll((Collection<?>)relationLineView.getNodes());
		}
		relationLineView = null;
		applicationInRelation = null;
		this.relationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public void removePartOfRelation(ApplicationInRelation applicationInRelation) {
		ApplicationInRelation aIR = applicationInRelation;
		RelationLineView relationLineView = null;
		for(RelationLineView rLV : this.relationView.getRelationNodes()) {
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
		relationLineView.getRelationLine().endXProperty().unbind();
		relationLineView.getRelationLine().endYProperty().unbind();
		relationLineView.getRelationLine().startXProperty().unbind();
		relationLineView.getRelationLine().startYProperty().unbind();
		for(final Node n : relationLineView.getNodes()) {
			n.setVisible(false);
		}
		if(this.relationView.getModelView() != null) {
			this.relationView.getModelView().getChildren().removeAll((Collection<?>)relationLineView.getNodes());
		}
		relationLineView = null;
		aIR = null;
		this.relationView.getModelView().getFileExportControl().setSaved(false);
	}
	
	public boolean isPartOfRelation(ApplicationView applicationView) {
		for(ApplicationInRelation aIR : this.relationView.getRelationModel().getApplications()) {
			if(aIR.getApplicationView().equals(applicationView)) {
				return true;
			}
		}
		return false;
	}
	
	public static void importXMLElement(Element element, ModelView modelView) {
		NodeList relationNodes = element.getElementsByTagName("Beziehungslinie");
		LinkedList<RelationLineView> relationLineViews = new LinkedList<RelationLineView>();
		for(int i = 0; i < relationNodes.getLength(); ++i) {
			relationLineViews.add(RelationLineView.importRelationFromXML((Element) relationNodes.item(i), modelView));
		}
		RelationModel relationModel = new RelationModel(relationLineViews.getFirst().getApplicationInRelation(), relationLineViews.get(1).getApplicationInRelation());
		for(int j = 2; j < relationLineViews.size(); ++j) {
			relationModel.getApplications().add(relationLineViews.get(j).getApplicationInRelation());
		}
		RelationView relationView = new RelationView(relationModel, modelView);
		relationView.getRelationControl().setRelationText(element.getAttribute("Beziehungstyp"));
		//ElementControl.importXMLSettings((Element)element.getElementsByTagName("Element").item(0), relationView);
		modelView.addElement(relationView);
	}

}
