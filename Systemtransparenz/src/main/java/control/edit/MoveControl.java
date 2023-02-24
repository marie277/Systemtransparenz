package control.edit;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import view.ElementView;
import view.ModelView;

//Klasse zur Steuerung der Bewegung von Elementen
public class MoveControl {

	private ModelView modelView;
	private Region region;
	private ElementView elementView;
	private boolean isMovable;
	private int x;
	private int y;

	//Konstruktor
	public MoveControl() {

	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public void setModelView(ModelView modelView) {
		this.modelView = modelView;
	}
	
	public void setElementView(ElementView elementView) {
		this.elementView = elementView;
	}
	
	//Methode zur Steuerung der Bewegung von Elementen
	public static void makeRegionMoveable(Region region, ModelView modelView, ElementView elementView) {
		MoveControl moveControl = new MoveControl();
		moveControl.setRegion(region);
		moveControl.setModelView(modelView);
		moveControl.setElementView(elementView);
		region.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			boolean isInside = e.getY() > 5.0 && e.getY() < moveControl.region.getHeight() - 5.0 && e.getX() > 5.0 && e.getX() < moveControl.region.getWidth() - 5.0;
			if(isInside) {
				moveControl.isMovable = true;
			}
			else {
				moveControl.isMovable = false;
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
			if (moveControl.isMovable && moveControl.elementView.isMoveable()) {
	            Parent parent = moveControl.modelView.getParent();
	            double layoutX = 0.0;
	            double layoutY = 0.0;
	            do {
	                layoutX += parent.getLayoutX();
	                layoutY += parent.getLayoutY();
	                parent = parent.getParent();
	            } while (parent.getParent() != null);
	            int newLayoutX = (int)(e.getSceneX() - layoutX - moveControl.region.getWidth() / 2.0);
	            int newLayoutY = (int)(e.getSceneY() - layoutY - moveControl.region.getHeight() / 2.0);
	            if (newLayoutX > moveControl.x && newLayoutY > moveControl.y) {
	            	moveControl.elementView.move(newLayoutX, newLayoutY);
	            }
	            else if (newLayoutX > moveControl.x) {
	            	moveControl.elementView.move(newLayoutX, moveControl.y);
	            }
	            else if (newLayoutY > moveControl.y) {
	            	moveControl.elementView.move(moveControl.x, newLayoutY);
	            }
	            else {
	            	moveControl.elementView.move(moveControl.x, moveControl.y);
	            }
	            moveControl.region.setCursor(Cursor.MOVE);
	            e.consume();
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
			boolean isInside = e.getY() > 5.0 && e.getY() < moveControl.region.getHeight() - 5.0 && e.getX() > 5.0 && e.getX() < moveControl.region.getWidth() - 5.0;
			if(isInside && moveControl.isMovable) {
				moveControl.region.setCursor(Cursor.MOVE);
			}
			else {
				moveControl.region.setCursor(Cursor.DEFAULT);
			}
		});
		region.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
			moveControl.isMovable = false;
			moveControl.region.setCursor(Cursor.DEFAULT);
		});
	}

}
