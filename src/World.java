import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import map.mapTypes.WorldMap;
import mapElements.animals.Genotype;
import mapElements.animals.Animal;
import mapElements.positionAndDirection.Vector2d;

import java.util.HashMap;
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
        WorldMap map = new WorldMap(15,15,10);
        map.place(new Animal(new Vector2d(10,10),0));
        map.place(new Animal(new Vector2d(5,5),0));
        map.place(new Animal(new Vector2d(5,5),0));
        map.place(new Animal(new Vector2d(5,5),0));
        map.place(new Animal(new Vector2d(5,5),0));
        while (true){
            try {
                Thread.sleep(250);
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

