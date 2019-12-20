package mapElements.otherElements;

import mapElements.IMapElement;
import mapElements.positionAndDirection.MapDirection;
import mapElements.positionAndDirection.Vector2d;

public class Grass implements IMapElement {

    private Vector2d position;


    public Grass(Vector2d initialPosition) {
        this.position = initialPosition;
    }

    public Vector2d getPosition() {
        return new Vector2d(0,0).add(this.position);
    }


    public String toString() {
        return "G";
    }
}
