package control.edit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.shape.Circle;

public class Interface extends Circle {
	
	private static final PseudoClass SELECTED;
	private BooleanProperty selected;
	
	static {
		SELECTED = PseudoClass.getPseudoClass("selected");
	}
	
	public Interface() {
		//this.selectedProperty().bind((ObservableValue<? extends Boolean>)this.selectedProperty());
	}
	
	public boolean isSelected() {
		return this.selected != null && this.selectedProperty().get();
	}
	
	public void setSelected(final boolean selected) {
		this.selectedProperty().set(selected);
	}
	
	public BooleanProperty selectedProperty() {
		if(this.selected == null) {
			this.selected = (BooleanProperty)new BooleanPropertyBase() {
                protected void invalidated() {
                    Interface.this.pseudoClassStateChanged(Interface.SELECTED, this.get());
                }
                
                public String getName() {
                    return "selected";
                }
                
                public Object getBean() {
                    return this;
                }
            };
		}
		return this.selected;
	}

}
