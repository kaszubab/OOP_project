import map.mapTypes.GrassField;
import map.mapTypes.IWorldMap;
import map.visualization.OptionParser;
import map.mapTypes.RectangularMap;
import mapElements.positionAndDirection.MoveDirection;
import mapElements.positionAndDirection.Vector2d;
import mapElements.animals.Animal;


public class World {
    public static void main(String [] args) {
        try {
            MoveDirection[] directions = new OptionParser(args).getValidArgTable();
            IWorldMap map = new RectangularMap(10, 5);
            map.place(new Animal(map));
            map.place(new Animal(map, new Vector2d(3, 4)));
            System.out.println(map.objectAt(new Vector2d(3, 4)));
            System.out.println(map);
            map.run(directions);
            System.out.println(map);

            IWorldMap myMap = new GrassField(3);
            System.out.println(myMap);
            myMap.place(new Animal(map, new Vector2d(0, 0)));
            myMap.place(new Animal(map, new Vector2d(1, 1)));
            System.out.println(myMap);
        }
        catch (Exception e) {
            System.out.println( e );
        }

    }
}
