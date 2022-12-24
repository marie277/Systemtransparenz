package control.edit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class ApplicationBorderPane extends BorderPane {

	protected static final PseudoClass SELECTED;
	private BooleanProperty selected;

	static {
		SELECTED = PseudoClass.getPseudoClass("selected");
	}
	
	public ApplicationBorderPane() {
		
	}
	
	public ApplicationBorderPane(Node node) {
		super(node);
	}
	
	public boolean isSelected() {
		return this.selected != null && this.selected.get();
	}
	
	public void setSelected(boolean selected) {
		this.selectedProperty().set(selected);
	}
	
	public BooleanProperty selectedProperty() {
		if(this.selected == null) {
			this.selected = (BooleanProperty)new BooleanPropertyBase() {
                protected void invalidated() {
                    ApplicationBorderPane.this.pseudoClassStateChanged(ApplicationBorderPane.SELECTED, this.get());
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
	
	public boolean equals(Object object) {
		if (!super.equals(object)) {
            return false;
        }
        ApplicationBorderPane applicationBorderPane = (ApplicationBorderPane)object;
        return this.selected == null || this.selected.equals(applicationBorderPane.selectedProperty());
	}

}
