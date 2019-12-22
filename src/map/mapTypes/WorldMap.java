package map.mapTypes;


import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import map.visualization.MapVisualizer;
import mapElements.IMapElement;
import mapElements.animals.Animal;
import mapElements.otherElements.Grass;
import mapElements.positionAndDirection.Vector2d;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Stream;


public class WorldMap extends AbstractWorldMap {

    public int getWidth() {
        return width;
    }

    private int width;

    public int getHeight() {
        return height;
    }

    private int height;
    private int plantEnergy;
    private int moveCost;

    protected Vector2d jungleRightUpper;
    protected Vector2d jungleLeftLower;
    private LinkedList<IMapElement> grassList = new LinkedList<>();
    private List<MapGUIVisualizer> visualizer = new LinkedList<>();

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
        return position.precedes(new Vector2d(width-1,height-1)) &&
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

        animalList.forEach(x -> {
            List<IMapElement> list = elementMap.get(x.getPosition());
            list.sort((y1,y2) -> {
                if (y1 instanceof Grass) return -1;
                if (y2 instanceof Grass) return 1;
                if (((Animal) y1).energy < ((Animal) y2).energy)
                    return -1;
                else if (((Animal) y1).energy == ((Animal) y2).energy)
                    return 0;
                else
                    return -1;
            });
        });

        feedAnimals();
        bearChildren();

        elementMap.forEach( (x,y) -> recolorTile(x, y) );
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

    private void bearChildren() {
        List<Animal> animalsToAdd = new LinkedList<>();
        animalList.forEach( animal -> {
            List<IMapElement> list = elementMap.get(animal.getPosition());
            if (list.size() <= 1) return;
            if ( ((Animal)list.get(0) ).canCopulate() && ( (Animal) list.get(1)).canCopulate() ) {
                System.out.println("Children Born");
                Vector2d placeForChildren = animal.getPosition();
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        Vector2d newPlaceForChildren = new Vector2d(animal.getPosition().x + i, animal.getPosition().y + j);
                        if (this.inMap(newPlaceForChildren) &&
                                (!this.elementMap.containsKey(newPlaceForChildren) ||
                                 this.elementMap.get(newPlaceForChildren).size()  == 0)) {
                            placeForChildren = newPlaceForChildren;

                            Animal child =  ((Animal)list.get(0) ).copulate(( (Animal) list.get(1)), placeForChildren);
                            animalsToAdd.add(child);
                            System.out.println("Children Born without parents");
                            return;
                        }
                    }
                }
                Animal child =  ((Animal)list.get(0) ).copulate(( (Animal) list.get(1)), placeForChildren);
                animalsToAdd.add(child);
                System.out.println("Children Born");
            }
        });
        animalsToAdd.forEach(x -> this.place(x));
    }

    public void addVisualizer (MapGUIVisualizer vis) {
        this.visualizer.add(vis);
    }

    private void recolorTile(Vector2d x, List<IMapElement> list) {
        if (list.isEmpty()) {
            if (inJungle(x)) visualizer.forEach( y -> y.recolorTile(x, Color.LIGHTGREEN));
            else visualizer.forEach( y -> y.recolorTile(x, null));
        }
        else if (this.elementMap.get(x).get(0) instanceof Grass) {
            visualizer.forEach( y -> y.recolorTile(x, Color.DARKGREEN));
        }
        else if ( ((Animal)this.elementMap.get(x).get(0)).energy > Animal.maxEnergy / 2) {
            visualizer.forEach( y -> y.recolorTile(x, Color.FUCHSIA));
        }
        else if ( ((Animal)this.elementMap.get(x).get(0)).energy > Animal.maxEnergy / 4) {
            visualizer.forEach( y -> y.recolorTile(x, Color.BLUE));
        }
        else {
            visualizer.forEach( y -> y.recolorTile(x, Color.RED));
        }
    }

    @Override
    protected Vector2d minPoint() {
        return new Vector2d(0,0);
    }

    @Override
    protected Vector2d maxPoint() {
        return new Vector2d(this.width-1, this.height-1);
    }

}
