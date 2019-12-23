import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import map.mapTypes.MapGUIVisualizer;
import map.mapTypes.MapStatistics;
import map.mapTypes.WorldMap;
import map.visualization.MapConfiguration;
import mapElements.animals.Animal;
import mapElements.positionAndDirection.Vector2d;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class World extends Application {
    public static void main(String [] args) {

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

        MapConfiguration mapConfiguration = null;

        try {
            mapConfiguration = new MapConfiguration("src/resources/parameters.json");
        }
        catch (FileNotFoundException e) {
            System.out.println("File parameters.json missing");
            System.exit(-1);
        }
        catch (NullPointerException e) {
            System.out.println("Json object field missing or incorrect");
            System.exit(-1);
        }
        catch (ClassCastException e) {
            System.out.println("Incorrect data type");
            System.exit(-1);
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }

        //ExecutorService exec = Executors.newFixedThreadPool(2);

        Animal.maxEnergy = mapConfiguration.startEnergy;
        WorldMap map1 = new WorldMap(mapConfiguration.width,mapConfiguration.height,
                (int) (mapConfiguration.width * mapConfiguration.jungleRatio), mapConfiguration.plantEnergy, mapConfiguration.moveEnergy);
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            map1.place(new Animal(new Vector2d(rand.nextInt(mapConfiguration.width),rand.nextInt(mapConfiguration.height)), mapConfiguration.startEnergy));
        }
        MapStatistics statistics1 = new MapStatistics(map1);
        MapGUIVisualizer visualizer1 = new MapGUIVisualizer(map1, statistics1);

        //exec.execute(createTask(primaryStage));
        //exec.execute(createTask(new Stage()));


        WorldMap map2 = new WorldMap(mapConfiguration.width,mapConfiguration.height,
                (int) (mapConfiguration.width * mapConfiguration.jungleRatio), mapConfiguration.plantEnergy, mapConfiguration.moveEnergy);
        for (int i = 0; i < 30; i++) {
            map2.place(new Animal(new Vector2d(rand.nextInt(mapConfiguration.width),rand.nextInt(mapConfiguration.height)), mapConfiguration.startEnergy));
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

