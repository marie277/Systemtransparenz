package control.edit;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class MoveControl {

	private Region baseRegion;
	private Region eventRegion;
	private Move movedRegion;
	private boolean isStraightMove;
	private int minX;
	private int minY;

	public MoveControl(Region eventRegion, Region baseRegion, Move movedRegion) {
		this.eventRegion = eventRegion;
		this.baseRegion = baseRegion;
		this.movedRegion = movedRegion;
	}
	
	public static void makeRegionMoveable(Region applicationBorderPane, Region modelView, Move move) {
		MoveControl moveControl = new MoveControl(applicationBorderPane, modelView, move);
		applicationBorderPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> moveControl.pressMouse(e));
		applicationBorderPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> moveControl.dragMouse(e));
		applicationBorderPane.addEventFilter(MouseEvent.MOUSE_MOVED, e -> moveControl.moveMouse(e));
		applicationBorderPane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> moveControl.releaseMouse(e));
	}

	private void releaseMouse(MouseEvent event) {
		this.isStraightMove = false;
		this.eventRegion.setCursor(Cursor.DEFAULT);
	}

	private void moveMouse(MouseEvent event) {
		if(this.isMoveableEvent(event) && this.isStraightMove) {
			this.eventRegion.setCursor(Cursor.MOVE);
		}
		else {
			this.eventRegion.setCursor(Cursor.DEFAULT);
		}
	}

	private void dragMouse(MouseEvent event) {
		if (this.isStraightMove && this.movedRegion.isMoveable()) {
            double collectedX = 0.0;
            double collectedY = 0.0;
            Parent parent = this.baseRegion.getParent();
            do {
                collectedX += parent.getLayoutX();
                collectedY += parent.getLayoutY();
                parent = parent.getParent();
            } while (parent.getParent() != null);
            double mouseY = event.getSceneY() - collectedY;
            double mouseX = event.getSceneX() - collectedX;
            int newX = (int)(mouseX - this.eventRegion.getWidth() / 2.0);
            int newY = (int)(mouseY - this.eventRegion.getHeight() / 2.0);
            if (newX > this.minX && newY > this.minY) {
                this.movedRegion.move(newX, newY);
            }
            else if (newX > this.minX) {
                this.movedRegion.move(newX, this.minY);
            }
            else if (newY > this.minY) {
            	this.movedRegion.move(this.minX, newY);
            }
            else {
            	this.movedRegion.move(this.minX, this.minY);
            }
            this.eventRegion.setCursor(Cursor.MOVE);
            event.consume();
		}
	}

	private void pressMouse(MouseEvent event) {
		if(this.isMoveableEvent(event)) {
			this.isStraightMove = true;
		}
	}

	private boolean isMoveableEvent(MouseEvent event) {
		return event.getY() > 5.0 && event.getY() < this.eventRegion.getHeight() - 5.0 && event.getX() > 5.0 && event.getX() < this.eventRegion.getWidth() - 5.0;	
	}

}
