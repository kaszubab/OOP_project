package map.mapTypes;


import map.visualization.MapVisualizer;
import mapElements.IMapElement;
import mapElements.animals.Animal;
import mapElements.otherElements.Grass;
import mapElements.positionAndDirection.Vector2d;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


public class WorldMap extends AbstractWorldMap {

    private int width;
    private int height;
    private int plantEnergy;
    private int moveCost;

    private Vector2d jungleRightUpper;
    private Vector2d jungleLeftLower;
    private LinkedList<IMapElement> grassList = new LinkedList<>();

    public WorldMap(int width, int height, int jungleSize, int plantEnergy, int moveCost) {
        this.width = width;
        this.height = height;
        this.plantEnergy = plantEnergy;
        this.moveCost = moveCost;

        this.jungleRightUpper = new Vector2d(width/2 + jungleSize/2, height/2 + jungleSize/2);
        this.jungleLeftLower = new Vector2d(width/2 - jungleSize/2, height/2 - jungleSize/2);
        this.mapVis = new MapVisualizer(this);
    }

    private boolean inMap(Vector2d position) {
        return position.precedes(new Vector2d(width,height)) &&
                position.follows(new Vector2d(0,0));
    }

    private boolean inJungle(Vector2d position) {
        return position.precedes(this.jungleRightUpper ) && position.follows(this.jungleLeftLower);
    }

    private void growGrass() {
        Random rand = new Random();
        int tries = 0;
        while (true) {
            Vector2d newPosition = new Vector2d(rand.nextInt(this.width), rand.nextInt(this.height));
            while (this.inJungle(newPosition)) newPosition = new Vector2d(rand.nextInt(this.width), rand.nextInt(this.height));
            if (tries++ > this.width * this.height || placeGrass(newPosition)) break;
        }
        tries = 0;
        while (true) {
            Vector2d newPosition = new Vector2d(rand.nextInt(this.jungleRightUpper.x - this.jungleLeftLower.x +1) + this.jungleLeftLower.x
                    , rand.nextInt(this.jungleRightUpper.y - jungleLeftLower.y+1) + this.jungleLeftLower.y);
            if (tries++ > (jungleRightUpper.x - this.jungleLeftLower.x) * (this.jungleRightUpper.y - this.jungleLeftLower.y) || placeGrass(newPosition)) break;
        }
    }

    public void run() {
        killAnimals();
        growGrass();
        for (Animal x : animalList) {
            x.move(this.minPoint(), this.maxPoint());
            x.changeEnergy(-moveCost);
        }
        feedAnimals();

    }

    @Override
    public boolean place(Animal animal) {
        if ( inMap(animal.getPosition())) return super.place(animal);
        return false;
    }

    private boolean placeGrass(Vector2d position) {
        elementMap.putIfAbsent(position, new LinkedList<>());
        if ( elementMap.get(position).isEmpty() ) {
            Grass newGrass = new Grass(position);
            elementMap.get(position).add(newGrass);
            grassList.add(newGrass);
            return true;
        }
        return false;
    }

    private void feedAnimals() {
        List<Grass> grassToBeRemoved = new LinkedList<>();
        for (IMapElement x : this.grassList) {
            if (elementMap.get(x.getPosition()).size() > 1) {
                giveAnimalsFood(elementMap.get(x.getPosition()), x);
                grassToBeRemoved.add((Grass)x);
            }
        }
        grassList.removeAll(grassToBeRemoved);
    }

    public void giveAnimalsFood(List<IMapElement> list,  IMapElement grass) {
        list.remove(((Grass)grass));
        if (list.size() == 1) {
            ((Animal) list.get(0)).changeEnergy(this.plantEnergy);
            return;
        }
        list.sort((x,y) -> {
            if (((Animal) x).energy < ((Animal) y).energy)
                return -1;
            else if (((Animal) x).energy == ((Animal) y).energy)
                return 0;
            else
                return -1;
        });
        List<Animal> strongestAnimals = new LinkedList<>();
        strongestAnimals.add((Animal) list.get(0));

        list.forEach( x -> {
            if (((Animal) x).energy == ((LinkedList<Animal>) strongestAnimals).getFirst().energy) strongestAnimals.add(((Animal) x));
        });
        ((LinkedList<Animal>) strongestAnimals).removeFirst();
        strongestAnimals.forEach(x -> x.changeEnergy( this.plantEnergy / strongestAnimals.size()));

    }

    private void killAnimals() {
        List<Animal> animalsToBeKilled = new LinkedList<>();
        this.animalList.forEach(x -> {
            if (x.dead()) {
                animalsToBeKilled.add(x);
                this.elementMap.get(x.getPosition()).remove(x);
            }
        });
        this.animalList.removeAll(animalsToBeKilled);
    }



    @Override
    protected Vector2d minPoint() {
        return new Vector2d(0,0);
    }

    @Override
    protected Vector2d maxPoint() {
        return new Vector2d(this.width, this.height);
    }

}
