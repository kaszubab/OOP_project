package map.visualization;

import map.IPositionChangeObserver;
import mapElements.positionAndDirection.Vector2d;

import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver {

    SortedSet<Vector2d> xList;
    SortedSet<Vector2d> yList;


    public MapBoundary() {


        this.xList = new TreeSet<Vector2d>((o1, o2) -> {
            if (o1.x < o2.x || (o1.x == o2.x && o1.y < o2.y)) {
                return  -1;
            }
            if (o1.x > o2.x || (o1.x == o2.x && o1.y > o2.y)) {
                return  1;
            }
            return 0;
        });


        this.yList = new TreeSet<Vector2d>((o1, o2) -> {
            if (o1.y < o2.y || (o1.y == o2.y && o1.x < o2.x)) {
                return  -1;
            }
            if (o1.y > o2.y || (o1.y == o2.y && o1.x > o2.x)) {
                return  1;
            }
            return 0;
        });

    }

    public void addObject (Vector2d object) {
        xList.add(object);
        yList.add(object);
    }
    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        if (oldPosition.x != newPosition.x) {
            xList.remove(oldPosition);
            xList.add(newPosition);
        }

        if (oldPosition.y != newPosition.y) {
            yList.remove(oldPosition);
            yList.add(newPosition);
        }

    }

    public Vector2d xMin() {
        return xList.first();
    }

    public Vector2d xMax() {
        return xList.last();
    }

    public Vector2d yMin() {
        return yList.first();
    }

    public Vector2d yMax() {
        return yList.last();
    }

}
