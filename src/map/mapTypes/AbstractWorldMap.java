package map.mapTypes;

import map.IPositionChangeObserver;
import map.visualization.MapBoundary;
import map.visualization.MapVisualizer;
import mapElements.IMapElement;
import mapElements.positionAndDirection.MoveDirection;
import mapElements.positionAndDirection.Vector2d;
import mapElements.animals.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class  AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected List<IMapElement> animalList = new ArrayList<>();
    protected HashMap<Vector2d,IMapElement> elementMap = new HashMap<>();
    protected MapBoundary mBoundary = new MapBoundary();
    protected MapVisualizer mapVis;

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position);
    }

    public boolean place(Animal animal) {
        if (!isOccupied(animal.getPosition())) {
            animalList.add(animal);
            elementMap.put(animal.getPosition(),animal);
            mBoundary.addObject(animal.getPosition());
            animal.addObserver(mBoundary);
            animal.addObserver(this);
            return true;
        }
        throw new IllegalArgumentException(" Position " + animal.getPosition() + " is already occupied");
    }

    public void run(MoveDirection[] directions) {
        for (int i = 0; i < directions.length; i++) {
            Animal animal = (Animal) animalList.get(i % animalList.size());

            animal.move(directions[i]);

        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        IMapElement element = elementMap.remove(oldPosition);
        elementMap.put(newPosition,element);
    }

    protected Vector2d minPoint() {
        return new Vector2d(mBoundary.xMin().x,mBoundary.yMin().y);
    };
    protected Vector2d maxPoint() {
        return new Vector2d(mBoundary.xMax().x,mBoundary.yMax().y);
    }

    public Object objectAt(Vector2d position) {
        return elementMap.get(position);
    }

    public String toString() {
        return mapVis.draw(minPoint(), maxPoint());
    }


}
