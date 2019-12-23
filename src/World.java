import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
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
import map.mapTypes.MapStatistics;
import map.mapTypes.WorldMap;
import mapElements.IMapElement;
import mapElements.animals.Genotype;
import mapElements.animals.Animal;
import mapElements.otherElements.Grass;
import mapElements.positionAndDirection.Vector2d;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private Button pauseButton;

    /*
    private Task<Void> createTask(Stage newStage) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                WorldMap map1 = new WorldMap(50,50,10, 250, 1);
                Random rand = new Random();
                for (int i = 0; i < 30; i++) {
                    map1.place(new Animal(new Vector2d(rand.nextInt(50),rand.nextInt(50)), 150));
                }
                MapGUIVisualizer visualizer1 = new MapGUIVisualizer(map1);

                AtomicInteger counter = new AtomicInteger();
                Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        newStage.setScene(new Scene(visualizer1.getVisualization()));
                        map1.run();
                    }
                }));

                visualizer1.addButtons(timeline);
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

                newStage.show();
                return null;
            }
        };
    }
    */

    @Override
    public void start(Stage primaryStage) {

        //ExecutorService exec = Executors.newFixedThreadPool(2);


        WorldMap map1 = new WorldMap(50,50,10, 250, 1);
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            map1.place(new Animal(new Vector2d(rand.nextInt(50),rand.nextInt(50)), 150));
        }
        MapStatistics statistics1 = new MapStatistics(map1);
        MapGUIVisualizer visualizer1 = new MapGUIVisualizer(map1, statistics1);

        //exec.execute(createTask(primaryStage));
        //exec.execute(createTask(new Stage()));


        WorldMap map2 = new WorldMap(50,50,10, 250, 1);
        for (int i = 0; i < 30; i++) {
            map2.place(new Animal(new Vector2d(rand.nextInt(50),rand.nextInt(50)), 150));
        }
        MapStatistics statistics2 = new MapStatistics(map2);
        MapGUIVisualizer visualizer2 = new MapGUIVisualizer(map2, statistics2);

        window = primaryStage;
        window.setTitle("Evolution generator");

        Stage anotherWindow = new Stage();
        anotherWindow.setTitle("Second window");




        AtomicInteger counter1 = new AtomicInteger();
        Timeline timeline1 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                window.setScene(new Scene(visualizer1.getVisualization()));
                map1.run();
            }
        }));

        AtomicInteger counter2 = new AtomicInteger();
        Timeline timeline2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anotherWindow.setScene(new Scene(visualizer2.getVisualization()));
                map2.run();
            }
        }));


        visualizer1.addButtons(timeline1);
        visualizer2.addButtons(timeline2);

        timeline1.setCycleCount(Timeline.INDEFINITE);
        timeline1.play();

        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.play();


        window.show();
        anotherWindow.show();


    }

}

