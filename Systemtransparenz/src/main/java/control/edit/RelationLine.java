package control.edit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.shape.Line;

public class RelationLine extends Line {
	private static final PseudoClass SELECTED;
	private BooleanProperty selected;
	
	static {
		SELECTED = PseudoClass.getPseudoClass("selected");
	}
	
	public RelationLine() {
		
	}
	
	public boolean isSelected() {
		return this.selected != null && this.selectedProperty().get();
	}
	
	public void setSelected(boolean selected) {
		this.selectedProperty().set(selected);
	}
	
	public BooleanProperty selectedProperty() {
		if(this.selected == null) {
			this.selected = (BooleanProperty)new BooleanPropertyBase() {
                protected void invalidated() {
                    RelationLine.this.pseudoClassStateChanged(RelationLine.SELECTED, this.get());
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
