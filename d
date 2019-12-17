[1mdiff --git a/src/World.java b/src/World.java[m
[1mindex 3f0f67d..a102072 100644[m
[1m--- a/src/World.java[m
[1m+++ b/src/World.java[m
[36m@@ -1,21 +1,16 @@[m
[31m-import map.mapTypes.GrassField;[m
[31m-import map.mapTypes.IWorldMap;[m
[31m-import map.visualization.OptionParser;[m
[31m-import map.mapTypes.RectangularMap;[m
[32m+[m[32mimport map.mapTypes.WorldMap;[m
 import mapElements.animals.Genotype;[m
[31m-import mapElements.positionAndDirection.MoveDirection;[m
[31m-import mapElements.positionAndDirection.Vector2d;[m
 import mapElements.animals.Animal;[m
[31m-[m
[31m-import java.util.Arrays;[m
[32m+[m[32mimport mapElements.positionAndDirection.Vector2d;[m
 [m
 [m
 public class World {[m
     public static void main(String [] args) {[m
[32m+[m[32m        Animal.maxEnergy = 30;[m
         /*[m
         try {[m
             MoveDirection[] directions = new OptionParser(args).getValidArgTable();[m
[31m-            IWorldMap map = new RectangularMap(10, 5);[m
[32m+[m[32m            IWorldMap map = new WorldMap(10, 5);[m
             map.place(new Animal(map));[m
             map.place(new Animal(map, new Vector2d(3, 4)));[m
             System.out.println(map.objectAt(new Vector2d(3, 4)));[m
[36m@@ -33,12 +28,14 @@[m [mpublic class World {[m
             System.out.println( e );[m
         }[m
         */[m
[31m-        Genotype type1 = new Genotype();[m
[31m-        Genotype type2 = new Genotype();[m
[31m-        System.out.println(type1);[m
[31m-        System.out.println(type2);[m
[31m-        Genotype child = new Genotype(type1, type2);[m
[31m-        System.out.println(child);[m
[32m+[m[32m        WorldMap map = new WorldMap(30,30,10);[m
[32m+[m[32m        map.place(new Animal(new Vector2d(15,15),0));[m
[32m+[m[32m        map.place(new Animal(new Vector2d(20,20),0));[m
[32m+[m[32m        System.out.println(map);[m
[32m+[m[32m        for (int x = 0; x < 10; x++) {[m
[32m+[m[32m            map.run();[m
[32m+[m[32m            System.out.println(map);[m
[32m+[m[32m        }[m
     }[m
 }[m
 [m
[1mdiff --git a/src/map/IPositionChangeObserver.java b/src/map/IPositionChangeObserver.java[m
[1mindex 8449a0c..654bb58 100644[m
[1m--- a/src/map/IPositionChangeObserver.java[m
[1m+++ b/src/map/IPositionChangeObserver.java[m
[36m@@ -1,7 +1,8 @@[m
 package map;[m
 [m
[32m+[m[32mimport mapElements.animals.Animal;[m
 import mapElements.positionAndDirection.Vector2d;[m
 [m
 public interface IPositionChangeObserver {[m
[31m-    void positionChanged(Vector2d oldPosition, Vector2d newPosition);[m
[32m+[m[32m    void positionChanged(Vector2d oldPosition, Animal animal);[m
 }[m
[1mdiff --git a/src/map/mapTypes/AbstractWorldMap.java b/src/map/mapTypes/AbstractWorldMap.java[m
[1mindex ef5b815..ae492ef 100644[m
[1m--- a/src/map/mapTypes/AbstractWorldMap.java[m
[1m+++ b/src/map/mapTypes/AbstractWorldMap.java[m
[36m@@ -1,7 +1,6 @@[m
 package map.mapTypes;[m
 [m
 import map.IPositionChangeObserver;[m
[31m-import map.visualization.MapBoundary;[m
 import map.visualization.MapVisualizer;[m
 import mapElements.IMapElement;[m
 import mapElements.positionAndDirection.MoveDirection;[m
[36m@@ -10,12 +9,13 @@[m [mimport mapElements.animals.Animal;[m
 [m
 import java.util.ArrayList;[m
 import java.util.HashMap;[m
[32m+[m[32mimport java.util.LinkedList;[m
 import java.util.List;[m
 [m
 public abstract class  AbstractWorldMap implements IWorldMap, IPositionChangeObserver {[m
[31m-    protected List<IMapElement> animalList = new ArrayList<>();[m
[31m-    protected HashMap<Vector2d,IMapElement> elementMap = new HashMap<>();[m
[31m-    protected MapBoundary mBoundary = new MapBoundary();[m
[32m+[m
[32m+[m[32m    protected List<Animal> animalList = new ArrayList<>();[m
[32m+[m[32m    protected HashMap<Vector2d,List<IMapElement>> elementMap = new HashMap<>();[m
     protected MapVisualizer mapVis;[m
 [m
     @Override[m
[36m@@ -23,42 +23,33 @@[m [mpublic abstract class  AbstractWorldMap implements IWorldMap, IPositionChangeObs[m
         return objectAt(position) != null;[m
     }[m
 [m
[31m-    public boolean canMoveTo(Vector2d position) {[m
[31m-        return !isOccupied(position);[m
[31m-    }[m
 [m
     public boolean place(Animal animal) {[m
[31m-        if (!isOccupied(animal.getPosition())) {[m
[31m-            animalList.add(animal);[m
[31m-            elementMap.put(animal.getPosition(),animal);[m
[31m-            mBoundary.addObject(animal.getPosition());[m
[31m-            animal.addObserver(mBoundary);[m
[31m-            animal.addObserver(this);[m
[31m-            return true;[m
[31m-        }[m
[31m-        throw new IllegalArgumentException(" Position " + animal.getPosition() + " is already occupied");[m
[32m+[m[32m        animalList.add(animal);[m
[32m+[m[32m        elementMap.computeIfAbsent(animal.getPosition(), k -> new LinkedList<>());[m
[32m+[m[32m        elementMap.get(animal.getPosition()).add(animal);[m
[32m+[m[32m        animal.addObserver(this);[m
[32m+[m[32m        return true;[m
     }[m
 [m
[31m-    public void run(MoveDirection[] directions) {[m
[31m-        for (int i = 0; i < directions.length; i++) {[m
[31m-            Animal animal = (Animal) animalList.get(i % animalList.size());[m
[31m-[m
[31m-            animal.move(directions[i]);[m
[31m-[m
[32m+[m[32m    public void run() {[m
[32m+[m[32m        for (Animal x : animalList) {[m
[32m+[m[32m            x.move();[m
         }[m
     }[m
 [m
[31m-    @Override[m
[31m-    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {[m
[31m-        IMapElement element = elementMap.remove(oldPosition);[m
[31m-        elementMap.put(newPosition,element);[m
[32m+[m[32m    public void positionChanged(Vector2d oldPosition, Animal movingAnimal) {[m
[32m+[m[32m        elementMap.get(oldPosition).remove(movingAnimal);[m
[32m+[m[32m        elementMap.computeIfAbsent(movingAnimal.getPosition(), k -> new LinkedList<>());[m
[32m+[m[32m        elementMap.get(movingAnimal.getPosition()).add(movingAnimal);[m
     }[m
 [m
[31m-    protected Vector2d minPoint() {[m
[31m-        return new Vector2d(mBoundary.xMin().x,mBoundary.yMin().y);[m
[31m-    };[m
[31m-    protected Vector2d maxPoint() {[m
[31m-        return new Vector2d(mBoundary.xMax().x,mBoundary.yMax().y);[m
[32m+[m[32m    protected abstract Vector2d minPoint();[m
[32m+[m[32m    protected abstract Vector2d maxPoint();[m
[32m+[m
[32m+[m[32m    @Override[m
[32m+[m[32m    public boolean canPlace(Vector2d position) {[m
[32m+[m[32m        return elementMap.get(position).isEmpty();[m
     }[m
 [m
     public Object objectAt(Vector2d position) {[m
[1mdiff --git a/src/map/mapTypes/GrassField.java b/src/map/mapTypes/GrassField.java[m
[1mindex 170b672..e9aaf59 100644[m
[1m--- a/src/map/mapTypes/GrassField.java[m
[1m+++ b/src/map/mapTypes/GrassField.java[m
[36m@@ -1,5 +1,5 @@[m
 package map.mapTypes;[m
[31m-[m
[32m+[m[32m/*[m
 import map.visualization.MapVisualizer;[m
 import mapElements.otherElements.Grass;[m
 import mapElements.IMapElement;[m
[36m@@ -61,3 +61,4 @@[m [mpublic class GrassField  extends AbstractWorldMap {[m
 [m
 [m
 }[m
[32m+[m[32m*/[m
\ No newline at end of file[m
[1mdiff --git a/src/map/mapTypes/IWorldMap.java b/src/map/mapTypes/IWorldMap.java[m
[1mindex 154c481..8d49af6 100644[m
[1m--- a/src/map/mapTypes/IWorldMap.java[m
[1m+++ b/src/map/mapTypes/IWorldMap.java[m
[36m@@ -19,10 +19,10 @@[m [mpublic interface IWorldMap {[m
      *            The position checked for the movement possibility.[m
      * @return True if the object can move to that position.[m
      */[m
[31m-    boolean canMoveTo(Vector2d position);[m
[32m+[m[32m    boolean canPlace(Vector2d position);[m
 [m
     /**[m
[31m-     * Place a animal on the map.[m
[32m+[m[32m     * Place a aplant on the map.[m
      *[m
      * @param animal[m
      *            The animal to place on the map.[m
[36m@@ -37,7 +37,7 @@[m [mpublic interface IWorldMap {[m
      * @param directions[m
      *            Array of move directions.[m
      */[m
[31m-    void run(MoveDirection[] directions);[m
[32m+[m[32m    void run();[m
 [m
     /**[m
      * Return true if given position on the map is occupied. Should not be[m
[1mdiff --git a/src/map/mapTypes/WorldMap.java b/src/map/mapTypes/WorldMap.java[m
[1mindex 68719f7..2e4ca58 100644[m
[1m--- a/src/map/mapTypes/WorldMap.java[m
[1m+++ b/src/map/mapTypes/WorldMap.java[m
[36m@@ -6,7 +6,7 @@[m [mimport mapElements.animals.Animal;[m
 import mapElements.positionAndDirection.Vector2d;[m
 [m
 [m
[31m-public class RectangularMap extends AbstractWorldMap {[m
[32m+[m[32mpublic class WorldMap extends AbstractWorldMap {[m
 [m
     private int width;[m
     private int height;[m
[36m@@ -17,7 +17,7 @@[m [mpublic class RectangularMap extends AbstractWorldMap {[m
     private int jungleCapacity;[m
     private int savannahCapacity;[m
 [m
[31m-    public RectangularMap(int width, int height, int jungleSize) {[m
[32m+[m[32m    public WorldMap(int width, int height, int jungleSize) {[m
         this.width = width;[m
         this.height = height;[m
 [m
[36m@@ -31,14 +31,6 @@[m [mpublic class RectangularMap extends AbstractWorldMap {[m
                 position.follows(new Vector2d(0,0));[m
     }[m
 [m
[31m-    private boolean inJungle(Vector2d )[m
[31m-[m
[31m-    @Override[m
[31m-    public boolean canMoveTo(Vector2d position) {[m
[31m-        if (inMap(position)) return super.canMoveTo(position);[m
[31m-        return false;[m
[31m-    }[m
[31m-[m
     @Override[m
     public boolean place(Animal animal) {[m
         if ( inMap(animal.getPosition())) return super.place( animal);[m
[1mdiff --git a/src/mapElements/animals/Animal.java b/src/mapElements/animals/Animal.java[m
[1mindex b6c70cb..02401f4 100644[m
[1m--- a/src/mapElements/animals/Animal.java[m
[1m+++ b/src/mapElements/animals/Animal.java[m
[36m@@ -7,29 +7,36 @@[m [mimport mapElements.positionAndDirection.Vector2d;[m
 import mapElements.IMapElement;[m
 import map.IPositionChangeObserver;[m
 [m
[32m+[m[32mimport java.util.Collections;[m
 import java.util.LinkedList;[m
[32m+[m[32mimport java.util.Random;[m
 [m
 [m
 public class Animal implements IMapElement {[m
 [m
[31m-    private MapDirection direction = MapDirection.NORTH;[m
[31m-    private Vector2d position = new Vector2d(2,2);[m
[31m-    private IWorldMap map;[m
[31m-[m
[32m+[m[32m    private MapDirection direction;[m
[32m+[m[32m    private Vector2d position;[m
[32m+[m[32m    private Genotype DNA;[m
[32m+[m[32m    private int children = 0;[m
[32m+[m[32m    public static int maxEnergy;[m
     public int energy;[m
[31m-    public Genotype DNA;[m
 [m
     private LinkedList<IPositionChangeObserver> observerList;[m
 [m
[31m-    public Animal(IWorldMap map) {[m
[31m-        this.map = map;[m
[31m-        observerList = new LinkedList<>();[m
[32m+[m[32m    public Animal(Vector2d initialPosition, int startingEnergy) {[m
[32m+[m[32m        this.position = initialPosition;[m
[32m+[m[32m        this.observerList = new LinkedList<>();[m
[32m+[m[32m        this.energy = startingEnergy;[m
[32m+[m[32m        this.DNA = new Genotype();[m
[32m+[m[32m        this.pickDirection();[m
     }[m
 [m
[31m-    public Animal(IWorldMap map, Vector2d initialPosition) {[m
[32m+[m[32m    private Animal(Vector2d initialPosition, int startingEnergy, Genotype genes) {[m
         this.position = initialPosition;[m
[31m-        this.map = map;[m
[31m-        observerList = new LinkedList<>();[m
[32m+[m[32m        this.observerList = new LinkedList<>();[m
[32m+[m[32m        this.energy = startingEnergy;[m
[32m+[m[32m        this.DNA = new Genotype();[m
[32m+[m[32m        this.pickDirection();[m
     }[m
 [m
     public void addObserver(IPositionChangeObserver observer) {[m
[36m@@ -42,26 +49,55 @@[m [mpublic class Animal implements IMapElement {[m
 [m
     private void positionChanged(Vector2d oldPosition) {[m
         for (IPositionChangeObserver x : observerList) {[m
[31m-            x.positionChanged(oldPosition, this.position);[m
[32m+[m[32m            x.positionChanged(oldPosition, this);[m
         }[m
     }[m
 [m
[32m+[m[32m    private void pickDirection() {[m
[32m+[m[32m        Random r = new Random();[m
[32m+[m[32m        this.direction = MapDirection.intToMapDirection(r.nextInt(8));[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
     public Vector2d getPosition() {[m
         return new Vector2d(0,0).add(this.position);[m
     }[m
 [m
[31m-    public void move(MoveDirection direction) {[m
[32m+[m[32m    public void move() {[m
 [m
         Vector2d oldPosition = this.position;[m
         int turn = this.DNA.pickDirection();[m
[32m+[m
         for (int i = 9; i < turn; i++) {[m
             this.direction = this.direction.next();[m
         }[m
[32m+[m
         this.position = this.position.add(this.direction.toUnitVector());[m
         positionChanged(oldPosition);[m
[32m+[m[32m    }[m
 [m
[32m+[m[32m    public void changeEnergy(int energy) {[m
[32m+[m[32m        this.energy = Math.min(this.energy+energy , this.maxEnergy);[m
     }[m
 [m
[32m+[m[32m    public boolean dead() {[m
[32m+[m[32m        return this.energy <= 0;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public int getChildren() {[m
[32m+[m[32m        return children;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public boolean canCopulate() {[m
[32m+[m[32m        return this.energy >= this.maxEnergy;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public Animal copulate( Animal partner, Vector2d birthPlace) {[m
[32m+[m[32m        this.children++;[m
[32m+[m[32m        return new Animal(new Vector2d(birthPlace.x, birthPlace.y), this.energy / 4 + partner.energy / 4, new Genotype(this.DNA, partner.DNA));[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
     public String toString() {[m
         // lab 3 return "Position " + position +" direction " + direction;[m
         switch (this.direction) {[m
[1mdiff --git a/src/mapElements/otherElements/Grass.java b/src/mapElements/otherElements/Grass.java[m
[1mindex 4896cb0..a3f052d 100644[m
[1m--- a/src/mapElements/otherElements/Grass.java[m
[1m+++ b/src/mapElements/otherElements/Grass.java[m
[36m@@ -5,7 +5,7 @@[m [mimport mapElements.positionAndDirection.MapDirection;[m
 import mapElements.positionAndDirection.Vector2d;[m
 [m
 public class Grass implements IMapElement {[m
[31m-    private MapDirection direction = MapDirection.NORTH;[m
[32m+[m
     private Vector2d position;[m
 [m
 [m
[1mdiff --git a/src/mapElements/positionAndDirection/MapDirection.java b/src/mapElements/positionAndDirection/MapDirection.java[m
[1mindex 860f360..9634086 100644[m
[1m--- a/src/mapElements/positionAndDirection/MapDirection.java[m
[1m+++ b/src/mapElements/positionAndDirection/MapDirection.java[m
[36m@@ -10,6 +10,7 @@[m [mpublic enum MapDirection {[m
     SOUTHEAST,[m
     EAST;[m
 [m
[32m+[m
     public String toString()[m
     {[m
         switch (this) {[m
[36m@@ -25,6 +26,20 @@[m [mpublic enum MapDirection {[m
         return null;[m
     }[m
 [m
[32m+[m[32m    public static MapDirection intToMapDirection(int val) {[m
[32m+[m[32m        switch (val) {[m
[32m+[m[32m            case 0: return NORTH;[m
[32m+[m[32m            case 1: return NORTHEAST;[m
[32m+[m[32m            case 2: return EAST;[m
[32m+[m[32m            case 3: return SOUTHEAST;[m
[32m+[m[32m            case 4: return SOUTH;[m
[32m+[m[32m            case 5: return SOUTHWEST;[m
[32m+[m[32m            case 6: return WEST;[m
[32m+[m[32m            case 7: return NORTHWEST;[m
[32m+[m[32m        }[m
[32m+[m[32m        return null;[m
[32m+[m[32m    }[m
[32m+[m
     public MapDirection next() {[m
         switch (this) {[m
             case EAST: return SOUTH;[m
[1mdiff --git a/test/AnimalTest.java b/test/AnimalTest.java[m
[1mindex 1d24291..5f1c274 100644[m
[1m--- a/test/AnimalTest.java[m
[1m+++ b/test/AnimalTest.java[m
[36m@@ -1,16 +1,16 @@[m
[32m+[m[32mimport map.mapTypes.WorldMap;[m
 import mapElements.positionAndDirection.Vector2d;[m
 import map.visualization.OptionParser;[m
 import mapElements.animals.Animal;[m
 import map.mapTypes.IWorldMap;[m
[31m-import map.mapTypes.RectangularMap;[m
 import org.junit.Assert;[m
 import org.junit.Test;[m
[31m-[m
[32m+[m[32m/*[m
 public class AnimalTest {[m
 [m
     @Test[m
     public void testNext() {[m
[31m-        IWorldMap map = new RectangularMap(4,4);[m
[32m+[m[32m        IWorldMap map = new WorldMap(4,4);[m
         String [] moves = new String[20];[m
         for (int i = 0; i < 20; i+=4) {[m
             moves[i] =  "f";[m
[36m@@ -31,7 +31,7 @@[m [mpublic class AnimalTest {[m
             moves[i+2] =  "b";[m
             moves[i+3] =  "l";[m
         }[m
[31m-        map = new RectangularMap(4,4);[m
[32m+[m[32m        map = new WorldMap(4,4);[m
         myAnimal = new Animal(map);[m
         map.place(myAnimal);[m
         map.run(new OptionParser(moves).getValidArgTable());[m
[36m@@ -42,3 +42,4 @@[m [mpublic class AnimalTest {[m
 [m
 [m
 }[m
[32m+[m[32m*/[m
\ No newline at end of file[m
[1mdiff --git a/test/GrassFieldTest.java b/test/GrassFieldTest.java[m
[1mindex 360d0cb..7b0b9ef 100644[m
[1m--- a/test/GrassFieldTest.java[m
[1m+++ b/test/GrassFieldTest.java[m
[36m@@ -3,10 +3,9 @@[m [mimport mapElements.positionAndDirection.Vector2d;[m
 import map.visualization.OptionParser;[m
 import mapElements.animals.Animal;[m
 import map.mapTypes.IWorldMap;[m
[31m-import map.mapTypes.GrassField;[m
 import org.junit.Assert;[m
 import org.junit.Test;[m
[31m-[m
[32m+[m[32m/*[m
 public class GrassFieldTest {[m
     @Test[m
     public void testMap() {[m
[36m@@ -79,3 +78,4 @@[m [mpublic class GrassFieldTest {[m
 [m
     }[m
 }[m
[32m+[m[32m*/[m
