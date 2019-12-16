package map.mapTypes;

import map.visualization.MapVisualizer;
import mapElements.otherElements.Grass;
import mapElements.IMapElement;
import mapElements.positionAndDirection.Vector2d;
import mapElements.animals.Animal;

import java.util.Random;

public class GrassField  extends AbstractWorldMap {

    public GrassField(int grassFieldQuantity) {
        Random generator = new Random();
        generator.setSeed(2137); // uncomment to test
        int range = (int)Math.sqrt(grassFieldQuantity*10);
        for (int i = 0; i < grassFieldQuantity; ) {
            Grass newGrass = new Grass(new Vector2d(generator.nextInt(range),generator.nextInt(range)));
            if (!(objectAt(newGrass.getPosition()) instanceof Grass)) {
                elementMap.put(newGrass.getPosition(), newGrass);
                mBoundary.addObject(newGrass.getPosition());
                i++;
            }
        }
        mapVis = new MapVisualizer(this);

    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal);
    }

    @Override
    public Object objectAt(Vector2d position) {
        for (IMapElement x : animalList) {
            if (x.getPosition().equals(position) && (x instanceof Animal)) return x;
        }
        return super.objectAt(position);
    }

    @Override
    public boolean place(Animal animal) {
        try {
            return super.place(animal);
        }
        catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                if (!(objectAt(animal.getPosition()) instanceof Animal)) {
                    animal.addObserver(this);
                    animal.addObserver(mBoundary);
                    animalList.add(animal);
                    elementMap.put(animal.getPosition(), animal);
                    mBoundary.addObject(animal.getPosition());
                }
            }
            throw new IllegalArgumentException("The position "+ animal.getPosition() + " is already occupied");
        }
    }


}
