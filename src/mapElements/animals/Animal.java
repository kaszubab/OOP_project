package mapElements.animals;

import map.mapTypes.IWorldMap;
import mapElements.positionAndDirection.MapDirection;
import mapElements.positionAndDirection.MoveDirection;
import mapElements.positionAndDirection.Vector2d;
import mapElements.IMapElement;
import map.IPositionChangeObserver;

import java.util.LinkedList;


public class Animal implements IMapElement {

    private MapDirection direction = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2,2);
    private IWorldMap map;
    private LinkedList<IPositionChangeObserver> observerList;

    public Animal(IWorldMap map) {
        this.map = map;
        observerList = new LinkedList<>();
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
        this.position = initialPosition;
        this.map = map;
        observerList = new LinkedList<>();
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observerList.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observerList.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition) {
        for (IPositionChangeObserver x : observerList) {
            x.positionChanged(oldPosition, this.position);
        }
    }

    public Vector2d getPosition() {
        return new Vector2d(0,0).add(this.position);
    }

    public void move(MoveDirection direction) {

        Vector2d oldPosition = this.position;
        switch (direction) {
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case RIGHT:
                this.direction = this.direction.next();
                break;
            case FORWARD:
                if (this.map.canMoveTo(this.position.add(this.direction.toUnitVector()))) {
                    this.position = this.position.add(this.direction.toUnitVector());
                }
                break;
            case BACKWARD:
                if (this.map.canMoveTo(this.position.subtract(this.direction.toUnitVector()))) {
                    this.position = this.position.subtract(this.direction.toUnitVector());
                }
                break;
        }

        positionChanged(oldPosition);

    }

    public String toString() {
        // lab 3 return "Position " + position +" direction " + direction;
        switch (this.direction) {
            case SOUTH:
                return "S";
            case NORTH:
                return "N";
            case WEST:
                return "W";
            case EAST:
                return "E";
        }
        return null;
    }
}
