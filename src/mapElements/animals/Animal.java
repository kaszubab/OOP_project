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
        this.DNA = new Genotype();
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

    public void move() {

        Vector2d oldPosition = this.position;
        int turn = this.DNA.pickDirection();

        for (int i = 9; i < turn; i++) {
            this.direction = this.direction.next();
        }

        this.position = this.position.add(this.direction.toUnitVector());
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

    public boolean canCopulate() {
        return this.energy >= this.maxEnergy;
    }

    public Animal copulate( Animal partner, Vector2d birthPlace) {
        this.children++;
        return new Animal(new Vector2d(birthPlace.x, birthPlace.y), this.energy / 4 + partner.energy / 4, new Genotype(this.DNA, partner.DNA));
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
