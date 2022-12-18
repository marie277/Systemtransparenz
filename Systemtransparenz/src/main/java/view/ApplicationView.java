package view;

import control.ApplicationControl;
import control.ElementControl;
import control.edit.ApplicationBorderPane;
import control.edit.Move;
import control.edit.MoveControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import model.ApplicationModel;

public class ApplicationView extends ElementView {
	
	private ModelView modelView;
	private ApplicationModel applicationModel;
	private ApplicationControl applicationControl;
	private ApplicationBorderPane applicationBorderPane;
	private BooleanProperty selectedProperty;
	private boolean isSelected;
	private Text text;

	public ApplicationView(ApplicationModel applicationModel, ModelView modelView) {
		this.applicationModel = applicationModel;
		this.modelView = modelView;
		this.applicationControl = new ApplicationControl(this);
		this.applicationBorderPane = new ApplicationBorderPane();
		this.applicationBorderPane.setId("Anwendung");
		this.applicationBorderPane.getStyleClass().add("Anwendung");
		this.applicationBorderPane.setPrefSize(10.0, 10.0);
		(this.text = new Text("")).setOnMouseClicked(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.applicationControl.isSelected(true);
			e.consume();
		});
		this.text.setOnMousePressed(e -> {
			if(modelView != null) {
				modelView.deselectElements();
				modelView.setElementView(this);
			}
			this.applicationControl.isSelected(true);
			e.consume();
		});
		this.text.textProperty().bind((ObservableValue<? extends String>)this.applicationModel.nameProperty());
		this.text.textProperty().addListener((observable, oldValue, newValue) -> {
			this.applicationBorderPane.setPrefSize(this.text.getLayoutBounds().getWidth()*1.2, this.text.getLayoutBounds().getHeight()*1.2);
		});
		this.applicationBorderPane.setPrefSize(this.text.getLayoutBounds().getWidth()*1.2, this.text.getLayoutBounds().getHeight()*1.2);
		MoveControl.makeRegionMoveable((Region)this.applicationBorderPane, (Region)this.getModelView(), (Move)this);
		this.applicationBorderPane.getStylesheets().add(this.getClass().getResource("application/application.css").toExternalForm());
		this.applicationBorderPane.selectedProperty().bind((ObservableValue<? extends Boolean>)this.selectedProperty());
		this.applicationBorderPane.setPadding(new Insets(3.0));
		this.isSelected = false;
	}

	private BooleanProperty selectedProperty() {
		if(this.selectedProperty == null) {
			this.selectedProperty = (BooleanProperty)new SimpleBooleanProperty((Object)this, "selected", this.isSelected);
		}
		return this.selectedProperty;
	}

	public ApplicationControl getApplicationControl() {
		return this.applicationControl;
	}

	@Override
	public Region getElementRegion() {
		return (Region)this.applicationBorderPane;
	}

	public void isSelected(boolean selected) {
		if(this.selectedProperty == null) {
			this.isSelected = selected;
		}
		else {
			this.selectedProperty.set(selected);
		}
	}

	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

	public ModelView getModelView() {
		return this.modelView;
	}

	public Text getText() {
		return this.text;
	}
	
	public BorderPane getApplicationBorderPane() {
		return this.applicationBorderPane;
	}
	
	public void setApplicationBorderPane(final ApplicationBorderPane applicationBorderPane) {
		this.applicationBorderPane = applicationBorderPane;
	}

	@Override
	public ElementControl getElementControl() {
		return this.getApplicationControl();
	}

	@Override
	public void zoom(double factor) {
		this.applicationControl.zoom(factor);
	}

	@Override
	public boolean isMoveable() {
		return true;
	}

	@Override
	public void isMoved(double x, double y) {
		this.applicationControl.isMoved(x, y);
	}

	@Override
	public void move(double x, double y) {
		this.applicationControl.move(x, y);
	}

	@Override
	public void addElement(ElementView element) {
		if(element.getClass().equals(ApplicationView.class)) {
			this.getElements().add(element);
			return;
		}
	}

	public boolean selected() {
		if(this.selectedProperty == null) {
			return this.isSelected;
		}
		else {
			return this.selectedProperty.get();
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if(!super.equals(object)) {
			return false;
		}
		ApplicationView applicationView = (ApplicationView)object;
		return this.getApplicationModel().equals(applicationView.getApplicationModel())&&this.getModelView().equals(applicationView.getModelView())&&this.selected()==applicationView.selected();
	}
	
}
