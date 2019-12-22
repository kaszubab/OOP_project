import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import map.mapTypes.WorldMap;
import mapElements.IMapElement;
import mapElements.animals.Genotype;
import mapElements.animals.Animal;
import mapElements.otherElements.Grass;
import mapElements.positionAndDirection.Vector2d;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class World extends Application {
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


        /*
        while (true){
            try {
                Thread.sleep(200);
            }
            catch (Exception e) {
                System.out.println(e);
                break;
            }
            map.run();
            System.out.println(map);
        }*/
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        WorldMap map = new WorldMap(25,25,4, 10, 0);
        map.place(new Animal(new Vector2d(1,1),10));
        map.place(new Animal(new Vector2d(1,1),10));

        primaryStage.setTitle("Evolution generator");
        Scene sc = new Scene(map.visualize());
        primaryStage.setScene(sc);

        primaryStage.show();
    }

    private Parent createContent(WorldMap map) {
        Pane root = new Pane();
        root.setPrefSize(800, 800);

        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                Tile tile = new Tile(800 / map.getWidth(), 800 / map.getHeight());
                tile.setTranslateX(i * 800 / map.getWidth());
                tile.setTranslateY(j * 800 / map.getHeight());
                root.getChildren().add(tile);
            }
        }

        return root;
    }

    private class Tile extends StackPane {
        public Tile(int x, int y) {
            Rectangle border = new Rectangle(x, y);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(border);
        }
    }
}

