package mapElements.animals;

import map.mapTypes.IWorldMap;
import mapElements.positionAndDirection.MapDirection;
import mapElements.positionAndDirection.MoveDirection;
import mapElements.positionAndDirection.Vector2d;
import mapElements.IMapElement;
import map.IPositionChangeObserver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;


public class Animal implements IMapElement {

    private MapDirection direction;
    private Vector2d position;
    private Genotype DNA;
    private int children = 0;
    public static int maxEnergy;
    public int energy;
    private int age = 0;
    private boolean copulated;

    private LinkedList<IPositionChangeObserver> observerList;

    public Animal(Vector2d initialPosition, int startingEnergy) {
        this.position = initialPosition;
        this.observerList = new LinkedList<>();
        this.energy = startingEnergy;
        this.DNA = new Genotype();
        this.pickDirection();
    }

    private Animal(Vector2d initialPosition, int startingEnergy, Genotype genes) {
        this.position = initialPosition;
        this.observerList = new LinkedList<>();
        this.energy = startingEnergy;
        this.DNA = genes;
        this.pickDirection();
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observerList.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observerList.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition) {
        for (IPositionChangeObserver x : observerList) {
            x.positionChanged(oldPosition, this);
        }
    }

    private void pickDirection() {
        Random r = new Random();
        this.direction = MapDirection.intToMapDirection(r.nextInt(8));
    }


    public Vector2d getPosition() {
        return new Vector2d(0,0).add(this.position);
    }

    public void move(Vector2d minimalBoundary, Vector2d maxBoundary) {

        Vector2d oldPosition = this.position;
        int turn = this.DNA.pickDirection();

        for (int i = 0; i < turn; i++) {
            this.direction = this.direction.next();
        }

        int futureX, futureY;
        this.position = this.position.add(this.direction.toUnitVector());

        if (this.position.x > maxBoundary.x ) futureX = minimalBoundary.x;
        else if (this.position.x < minimalBoundary.x) futureX = maxBoundary.x;
        else futureX = this.position.x;

        if (this.position.y > maxBoundary.y ) futureY = minimalBoundary.y;
        else if (this.position.y < minimalBoundary.y) futureY = maxBoundary.y;
        else futureY = this.position.y;

        this.position = new Vector2d(futureX, futureY);
        this.copulated = false;
        this.age++;

        positionChanged(oldPosition);
    }

    public void changeEnergy(int energy) {
        this.energy = Math.min(this.energy+energy , this.maxEnergy);
    }

    public boolean dead() {
        return this.energy <= 0;
    }

    public int getChildren() {
        return children;
    }

    public int getDomineeringGene() {
        return this.DNA.getDomineeringGene();
    }


    public boolean canCopulate() {
        return this.energy >= this.maxEnergy/2 && !this.copulated;
    }

    public Animal copulate( Animal partner, Vector2d birthPlace) {
        this.children++;
        this.copulated = true;
        partner.copulated = true;
        Animal newAnimal =  new Animal(new Vector2d(birthPlace.x, birthPlace.y),
                this.energy / 4 + partner.energy / 4, new Genotype(this.DNA, partner.DNA));
        this.changeEnergy(-this.energy/4);
        partner.changeEnergy(-partner.energy/4);
        return newAnimal;
    }

    public int getAge() {
        return age;
    }


    public String toString() {
        // lab 3 return "Position " + position +" direction " + direction;
        /*switch (this.direction) {
            case SOUTH:
                return "4";
            case NORTH:
                return "0";
            case WEST:
                return "6";
            case EAST:
                return "2";
            case NORTHEAST:
                return "1";
            case SOUTHEAST:
                return "3";
            case SOUTHWEST:
                return "5";
            case NORTHWEST:
                return "7";
        }
        return null;*/
        return Integer.toString(this.energy);
    }
}
