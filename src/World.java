import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.Window;
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

    @Override
    public void start(Stage primaryStage) {



        Pane root = new Pane();
        root.setPrefSize(1200, 800);

        Stage initialStage = new Stage();
        initialStage.setTitle("Welcone to evolution generator");

        VBox box = new VBox();

        Button button1 = new Button();
        button1.setPrefSize(1200,400);
        button1.setText("Open one generator");
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                initialStage.close();
                Stage oneGenerator = new Stage();
                Timeline timeline = initGenerator(oneGenerator);
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
                oneGenerator.show();


            }
        });

        Button button2 = new Button();
        button2.setPrefSize(1200,400);
        button2.setText("Open two generators");
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initialStage.close();
                Stage oneGenerator = new Stage();
                Timeline timeline1 = initGenerator(oneGenerator);

                Stage anotherGenerator = new Stage();
                Timeline timeline2 = initGenerator(anotherGenerator);

                timeline1.setCycleCount(Timeline.INDEFINITE);
                timeline1.play();

                timeline2.setCycleCount(Timeline.INDEFINITE);
                timeline2.play();

                oneGenerator.show();
                anotherGenerator.show();


            }
        });

        box.getChildren().addAll(button1,button2);
        root.getChildren().add(box);

        initialStage.setScene(new Scene(root));
        initialStage.show();


    }

    private Timeline initGenerator(Stage generator) {


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

        Animal.maxEnergy = mapConfiguration.startEnergy;

        WorldMap map1 = initMap(mapConfiguration, 20);
        MapStatistics statistics1 = new MapStatistics(map1);
        MapGUIVisualizer visualizer1 = new MapGUIVisualizer(map1, statistics1);

        AtomicInteger counter1 = new AtomicInteger();
        Timeline timeline1 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                generator.setScene(new Scene(visualizer1.getVisualization()));
                map1.run();
            }
        }));

        visualizer1.addButtons(timeline1);


        return timeline1;
    }

    private WorldMap initMap(MapConfiguration mapConfiguration, int startingAnimals) {




        WorldMap map = new WorldMap(mapConfiguration.width,mapConfiguration.height,
                (int) (mapConfiguration.width * mapConfiguration.jungleRatio), mapConfiguration.plantEnergy, mapConfiguration.moveEnergy);
        Random rand = new Random();
        for (int i = 0; i < startingAnimals; i++) {
            map.place(new Animal(new Vector2d(rand.nextInt(mapConfiguration.width),rand.nextInt(mapConfiguration.height)), mapConfiguration.startEnergy));
        }
        return map;

    }

}

