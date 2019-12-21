import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import map.mapTypes.WorldMap;
import mapElements.IMapElement;
import mapElements.animals.Genotype;
import mapElements.animals.Animal;
import mapElements.otherElements.Grass;
import mapElements.positionAndDirection.Vector2d;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class World {
    public static void main(String [] args) {
        Animal.maxEnergy = 30;
        /*
        try {
            MoveDirection[] directions = new OptionParser(args).getValidArgTable();
            IWorldMap map = new WorldMap(10, 5);
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
        */

        WorldMap map = new WorldMap(5,5,2, 10, 5);
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        map.place(new Animal(new Vector2d(2,2),10));
        while (true){
            try {
                Thread.sleep(10);
            }
            catch (Exception e) {
                System.out.println(e);
                break;
            }
            map.run();
            System.out.println(map);
        }
    }
}

