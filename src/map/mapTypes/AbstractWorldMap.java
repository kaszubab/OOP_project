package map.mapTypes;

import map.IPositionChangeObserver;
import map.visualization.MapVisualizer;
import mapElements.IMapElement;
import mapElements.positionAndDirection.MoveDirection;
import mapElements.positionAndDirection.Vector2d;
import mapElements.animals.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class  AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    protected List<Animal> animalList = new ArrayList<>();
    protected HashMap<Vector2d,List<IMapElement>> elementMap = new HashMap<>();
    protected MapVisualizer mapVis;

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }


    public boolean place(Animal animal) {
        animalList.add(animal);
        elementMap.computeIfAbsent(animal.getPosition(), k -> new LinkedList<>());
        elementMap.get(animal.getPosition()).add(animal);
        animal.addObserver(this);
        return true;
    }

    public void run() {
        for (Animal x : animalList) {
            x.move();
        }
    }

    public void positionChanged(Vector2d oldPosition, Animal movingAnimal) {
        elementMap.get(oldPosition).remove(movingAnimal);
        elementMap.computeIfAbsent(movingAnimal.getPosition(), k -> new LinkedList<>());
        elementMap.get(movingAnimal.getPosition()).add(movingAnimal);
    }

    protected abstract Vector2d minPoint();
    protected abstract Vector2d maxPoint();

    @Override
    public boolean canPlace(Vector2d position) {
        return elementMap.get(position).isEmpty();
    }

    public Object objectAt(Vector2d position) {
        return elementMap.get(position);
    }

    public String toString() {
        return mapVis.draw(minPoint(), maxPoint());
    }


}
