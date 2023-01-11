package control.edit;

//Interface, welches von Klassen implementiert werden soll, welche die Bewegung von Elementen steuern
public interface Move {
	
	//Abstrakte Methode zur Prüfung, ob ein Element bewegbar ist
	boolean isMoveable();
	//Abstrakte Methode zur Bewegung eines Elements an einen bestimmten Punkt
	void move(double x, double y);
	
}
