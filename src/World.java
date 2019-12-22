import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import map.mapTypes.MapGUIVisualizer;
import map.mapTypes.WorldMap;
import mapElements.IMapElement;
import mapElements.animals.Genotype;
import mapElements.animals.Animal;
import mapElements.otherElements.Grass;
import mapElements.positionAndDirection.Vector2d;

import java.awt.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class World extends Application {
    public static void main(String [] args) {
        Animal.maxEnergy = 500;
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

    private Stage window;
    @Override
    public void start(Stage primaryStage) {
        WorldMap map = new WorldMap(50,50,10, 250, 1);
        for (int i = 0; i < 50; i += 2) {
            map.place(new Animal(new Vector2d(0+i,48 - i), 150));
        }
        for (int i = 0; i < 50; i += 2) {
            map.place(new Animal(new Vector2d(0+i,0 + i), 150));
        }
        map.place(new Animal(new Vector2d(2,2), 150));
        map.place(new Animal(new Vector2d(1,1), 150));
        map.place(new Animal(new Vector2d(2,2), 150));
        map.place(new Animal(new Vector2d(1,1), 150));
        map.place(new Animal(new Vector2d(2,2), 150));
        map.place(new Animal(new Vector2d(1,1), 150));




        MapGUIVisualizer visualizer = new MapGUIVisualizer(map);

        window = primaryStage;

        window.setTitle("Evolution generator");

        AtomicInteger counter = new AtomicInteger();
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.setScene(new Scene(visualizer.getVisualization()));
                map.run();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        window.show();


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

