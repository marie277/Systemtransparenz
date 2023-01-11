package control.edit;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

//Klasse zur Steuerung der Bewegung von Elementen
public class MoveControl {

	private Region baseRegion;
	private Region eventRegion;
	private Move movedRegion;
	private boolean isMove;
	private int minX;
	private int minY;

	//Konstruktor
	public MoveControl(Region eventRegion, Region baseRegion, Move movedRegion) {
		this.eventRegion = eventRegion;
		this.baseRegion = baseRegion;
		this.movedRegion = movedRegion;
	}
	
	//Methode zur Steuerung der Bewegung von Elementen
	public static void makeRegionMoveable(Region eventRegion, Region baseRegion, Move moveRegion) {
		MoveControl moveControl = new MoveControl(eventRegion, baseRegion, moveRegion);
		eventRegion.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			boolean isMoveable = e.getY() > 5.0 && e.getY() < moveControl.eventRegion.getHeight() - 5.0 && e.getX() > 5.0 && e.getX() < moveControl.eventRegion.getWidth() - 5.0;
			if(isMoveable) {
				moveControl.isMove = true;
			}
			
		});
		eventRegion.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
			if (moveControl.isMove && moveControl.movedRegion.isMoveable()) {
	            Parent parent = moveControl.baseRegion.getParent();
	            double sumX = 0.0;
	            double sumY = 0.0;
	            do {
	                sumX += parent.getLayoutX();
	                sumY += parent.getLayoutY();
	                parent = parent.getParent();
	            } while (parent.getParent() != null);
	            int newX = (int)(e.getSceneX() - sumX - moveControl.eventRegion.getWidth() / 2.0);
	            int newY = (int)(e.getSceneY() - sumY - moveControl.eventRegion.getHeight() / 2.0);
	            if (newX > moveControl.minX && newY > moveControl.minY) {
	            	moveControl.movedRegion.move(newX, newY);
	            }
	            else if (newX > moveControl.minX) {
	            	moveControl.movedRegion.move(newX, moveControl.minY);
	            }
	            else if (newY > moveControl.minY) {
	            	moveControl.movedRegion.move(moveControl.minX, newY);
	            }
	            else {
	            	moveControl.movedRegion.move(moveControl.minX, moveControl.minY);
	            }
	            moveControl.eventRegion.setCursor(Cursor.MOVE);
	            e.consume();
			}
		});
		eventRegion.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
			boolean isMoveable = e.getY() > 5.0 && e.getY() < moveControl.eventRegion.getHeight() - 5.0 && e.getX() > 5.0 && e.getX() < moveControl.eventRegion.getWidth() - 5.0;
			if(isMoveable && moveControl.isMove) {
				moveControl.eventRegion.setCursor(Cursor.MOVE);
			}
			else {
				moveControl.eventRegion.setCursor(Cursor.DEFAULT);
			}
		});
		eventRegion.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
			moveControl.isMove = false;
			moveControl.eventRegion.setCursor(Cursor.DEFAULT);
		});
	}

}
