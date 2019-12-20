package map.mapTypes;


import map.visualization.MapVisualizer;
import mapElements.IMapElement;
import mapElements.animals.Animal;
import mapElements.otherElements.Grass;
import mapElements.positionAndDirection.Vector2d;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;


public class WorldMap extends AbstractWorldMap {

    private int width;
    private int height;
    private Vector2d jungleRightUpper;
    private Vector2d JungleLeftLower;
    private LinkedList<IMapElement> grassList = new LinkedList<>();

    public WorldMap(int width, int height, int jungleSize) {
        this.width = width;
        this.height = height;

        this.jungleRightUpper = new Vector2d(width/2 + jungleSize/2, height/2 + jungleSize/2);
        this.JungleLeftLower = new Vector2d(width/2 - jungleSize/2, height/2 - jungleSize/2);
        this.mapVis = new MapVisualizer(this);
    }

    private boolean inMap(Vector2d position) {
        return position.precedes(new Vector2d(width,height)) &&
                position.follows(new Vector2d(0,0));
    }

    private boolean inJungle(Vector2d position) {
        return position.precedes(this.jungleRightUpper ) && position.follows(this.JungleLeftLower);
    }

    private void growGrass() {
        Random rand = new Random();
        int tries = 0;
        while (true) {
            Vector2d newPosition = new Vector2d(rand.nextInt(this.width), rand.nextInt(this.height));
            while (this.inJungle(newPosition)) newPosition = new Vector2d(rand.nextInt(this.width), rand.nextInt(this.height));
            if (tries++ > this.width * this.height || placeGrass(newPosition)) break;
        }
    }

    @Override
    public boolean place(Animal animal) {
        if ( inMap(animal.getPosition())) return super.place(animal);
        return false;
    }

    private boolean placeGrass(Vector2d position) {
        elementMap.putIfAbsent(position, new LinkedList<>());
        if ( elementMap.get(position).isEmpty() ) {
            elementMap.get(position).add(new Grass(position));
            return true;
        }
        return false;
    }

    private void animalEats(Vector2d position, LinkedList<IMapElement> positionList) {
        LinkedList<Animal> strongesAnimals = new LinkedList<>();


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
