package map;

import mapElements.animals.Animal;
import mapElements.positionAndDirection.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Animal animal);
}
