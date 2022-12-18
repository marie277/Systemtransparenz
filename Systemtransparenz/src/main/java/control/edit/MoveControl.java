package control.edit;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class MoveControl {

	private static Region baseRegion;
	private static Region eventRegion;
	private static Move movedRegion;
	private boolean isStraightMove;
	private int minX;
	private int minY;

	public MoveControl(Region eventRegion, Region baseRegion, Move movedRegion) {
		MoveControl.eventRegion = eventRegion;
		MoveControl.baseRegion = baseRegion;
		MoveControl.movedRegion = movedRegion;
	}
	
	public static void makeRegionMoveable(Region applicationBorderPane, Region modelView, Move move) {
		MoveControl moveControl = new MoveControl(eventRegion, baseRegion, movedRegion);
		eventRegion.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> moveControl.pressMouse(e));
		eventRegion.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> moveControl.dragMouse(e));
		eventRegion.addEventFilter(MouseEvent.MOUSE_MOVED, e -> moveControl.moveMouse(e));
		eventRegion.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> moveControl.releaseMouse(e));
	}

	private void releaseMouse(MouseEvent event) {
		this.isStraightMove = false;
		MoveControl.eventRegion.setCursor(Cursor.DEFAULT);
	}

	private void moveMouse(MouseEvent event) {
		if(this.isMoveableEvent(event) && this.isStraightMove) {
			MoveControl.eventRegion.setCursor(Cursor.MOVE);
		}
		else {
			MoveControl.eventRegion.setCursor(Cursor.DEFAULT);
		}
	}

	private void dragMouse(MouseEvent event) {
		if (this.isStraightMove && MoveControl.movedRegion.isMoveable()) {
            double collectedX = 0.0;
            double collectedY = 0.0;
            Parent parent = MoveControl.baseRegion.getParent();
            do {
                collectedX += parent.getLayoutX();
                collectedY += parent.getLayoutY();
                parent = parent.getParent();
            } while (parent.getParent() != null);
            double mouseY = event.getSceneY() - collectedY;
            double mouseX = event.getSceneX() - collectedX;
            int newX = (int)(mouseX - MoveControl.eventRegion.getWidth() / 2.0);
            int newY = (int)(mouseY - MoveControl.eventRegion.getHeight() / 2.0);
            if (newX > this.minX && newY > this.minY) {
                MoveControl.movedRegion.move(newX, newY);
            }
            else if (newX > this.minX) {
                MoveControl.movedRegion.move(newX, this.minY);
            }
            else if (newY > this.minY) {
            	MoveControl.movedRegion.move(this.minX, newY);
            }
            else {
            	MoveControl.movedRegion.move(this.minX, this.minY);
            }
            MoveControl.eventRegion.setCursor(Cursor.MOVE);
            event.consume();
		}
	}

	private void pressMouse(MouseEvent event) {
		if(this.isMoveableEvent(event)) {
			this.isStraightMove = true;
		}
	}

	private boolean isMoveableEvent(MouseEvent event) {
		return event.getY() > 5.0 && event.getY() < MoveControl.eventRegion.getHeight() - 5.0 && event.getX() > 5.0 && event.getX() < MoveControl.eventRegion.getWidth() - 5.0;	
	}

}
