package view;

import java.util.LinkedList;

import control.ElementControl;
import control.RelationControl;
import control.edit.ApplicationBorderPane;
import control.edit.Interface;
import control.edit.Move;
import control.edit.MoveControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import model.ApplicationInRelation;
import model.RelationModel;

public class RelationView extends ElementView{
	
	private RelationControl relationControl;
	private LinkedList<RelationLineView> relationNodes;
	private ApplicationBorderPane applicationBorderPane;
	private RelationModel relationModel;
	private Interface interfaceCircle;
	private Text interfaceText;
	private BooleanProperty selectedProperty;
	private boolean selected;
	private StackPane stackPane;

	public RelationView(RelationModel relationModel, ModelView modelView) {
		this.relationModel = relationModel;
		this.modelView = modelView;
		this.relationControl = new RelationControl(this);
		this.interfaceCircle = new Interface();
		this.interfaceText = new Text("");
		this.interfaceText.textProperty().bind((ObservableValue<? extends String>)this.relationModel.interfaceNameProperty());
		this.stackPane = new StackPane();
		this.relationNodes = new LinkedList<RelationLineView>();
		this.applicationBorderPane = new ApplicationBorderPane();
		this.interfaceCircle.setOnMousePressed(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.relationControl.setSelected(true);
			e.consume();
		});
		this.interfaceCircle.setOnMouseClicked(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.relationControl.setSelected(true);
			e.consume();
		});
		this.interfaceText.setOnMousePressed(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.relationControl.setSelected(true);
			e.consume();
		});
		this.interfaceText.setOnMouseClicked(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.relationControl.setSelected(true);
			e.consume();
		});
		this.interfaceCircle.selectedProperty().bind((ObservableValue<? extends Boolean>)this.selectedProperty());
		this.stackPane.getChildren().addAll(new Node[] {(Node)this.interfaceCircle, (Node)this.interfaceText});
		this.stackPane.setAlignment(Pos.CENTER);
		this.applicationBorderPane.setCenter((Node)this.stackPane);
		this.applicationBorderPane.setPrefSize(40.0, 40.0);
		for(ApplicationInRelation aIR : this.relationModel.getApplications()) {
			RelationLineView rLV = new RelationLineView(aIR);
			this.relationNodes.add(rLV);
			rLV.getRelationLine().endXProperty().bind((ObservableValue<? extends Number>)this.getElementRegion().layoutXProperty().add((ObservableNumberValue)this.getElementRegion().prefWidthProperty().divide(2.0)));
			rLV.getRelationLine().endYProperty().bind((ObservableValue<? extends Number>)this.getElementRegion().layoutYProperty().add((ObservableNumberValue)this.getElementRegion().prefHeightProperty().divide(2.0)));
		}
		for(ApplicationInRelation aIR : this.relationModel.getApplications()) {
			aIR.getApplicationView().getElementRegion().boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
				for(RelationLineView rLV : this.relationNodes) {
					if(rLV.getApplicationInRelation().equals(aIR)) {
						rLV.calculateCenterPoint();
					}
				}
			});
		}
		MoveControl.makeRegionMoveable(this.getElementRegion(), (Region)this.getModelView(), (Move)this);
		//this.applicationBorderPane.getStylesheets().add(this.getClass().getResource("application/relation.css").toExternalForm());
		this.selected = false;
	}

	public RelationControl getRelationControl() {
		return this.relationControl;
	}

	public LinkedList<RelationLineView> getRelationNodes() {
		return this.relationNodes;
	}

	@Override
	public Region getElementRegion() {
		return (Region)this.applicationBorderPane;
	}

	@Override
	public ElementControl getElementControl() {
		return this.getRelationControl();
	}

	@Override
	public void zoom(double factor) {
		this.relationControl.zoom(factor);
	}

	@Override
	public boolean isMoveable() {
		return true;
	}

	@Override
	public void isMoved(double x, double y) {
		this.relationControl.isMoved(x, y);
	}

	@Override
	public void move(double x, double y) {
		this.relationControl.move(x, y);
	}

	@Override
	public void addElement(ElementView element) {
		if(element.getClass().equals(RelationView.class)) {
			this.getElements().add(element);
			return;
		}
	}

	public RelationModel getRelationModel() {
		return this.relationModel;
	}

	public boolean isSelected() {
		if(this.selectedProperty == null) {
			return this.selected;
		}
		else {
			return this.selectedProperty.get();
		}
	}

	public void setSelected(boolean selected) {
		if(this.selectedProperty == null) {
			this.selected = selected;
		}
		else {
			this.selectedProperty.set(selected);
		}
		for(RelationLineView rLV : this.relationNodes) {
			rLV.setSelected(selected);
		}
	}
	
	public BooleanProperty selectedProperty() {
		if(this.selectedProperty != null) {
			return this.selectedProperty;
		}
		else {
			this.selectedProperty = new SimpleBooleanProperty((Object)this, "selected", this.selected);
			return this.selectedProperty;
		}
	}
	
	public void addPartOfRelation(ApplicationView applicationView) {
		this.relationControl.addPartOfRelation(applicationView);
	}
	
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		final RelationView rV = (RelationView)object;
		return this.getRelationModel().equals(rV.getRelationModel()) && this.getRelationNodes().equals(rV.getRelationNodes()) && this.getElementRegion().equals(rV.getElementRegion()) && this.isSelected() == rV.isSelected();
	}

	public Interface getInterfaceCircle() {
		return this.interfaceCircle;
	}
	
	public Text getInterfaceText() {
		return this.interfaceText;
	}
}
