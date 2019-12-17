package map.mapTypes;


import map.visualization.MapVisualizer;
import mapElements.animals.Animal;
import mapElements.positionAndDirection.Vector2d;


public class WorldMap extends AbstractWorldMap {

    private int width;
    private int height;
    private Vector2d jungleRightUpper;
    private Vector2d JungleLeftLower;
    private int grassInSavannah;
    private int grassInJungle;
    private int jungleCapacity;
    private int savannahCapacity;

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

    @Override
    public boolean place(Animal animal) {
        if ( inMap(animal.getPosition())) return super.place( animal);
        return false;
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
