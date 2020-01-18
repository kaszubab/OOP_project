import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import map.mapTypes.MapGUIVisualizer;
import map.mapTypes.MapStatistics;
import map.mapTypes.WorldMap;
import map.visualization.MapConfiguration;
import mapElements.animals.Animal;
import mapElements.positionAndDirection.Vector2d;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.awt.*;
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
        Timeline timeline1 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.05), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                map1.run();
            }
        }));

        generator.setScene(new Scene(visualizer1.getVisualization()));
        generator.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getSceneX() < 800) {
                    int tilesX = (int) Math.floor(event.getSceneX() / (800 / map1.getWidth()));
                    int tilesY = (int) Math.floor(event.getSceneY() / (800 / map1.getHeight()));
                    System.out.println(tilesX + " " +tilesY);
                    Animal clickedAnimal = map1.getAnimalInfo(tilesX, tilesY);

                    if (clickedAnimal != null) {
                        timeline1.stop();

                        Stage animalStats = new Stage();
                        animalStats.setTitle("Animal Stats");

                        StackPane root = new StackPane();

                        root.setBackground(Background.EMPTY);

                        VBox textBox = new VBox();
                        textBox.setPadding(new Insets(30, 30, 30, 30));

                        Text text1 = new Text();
                        text1.setFont(Font.font("Comic Sans MS",40));
                        text1.setFill(Color.DARKGREEN);
                        text1.setTextAlignment(TextAlignment.CENTER);
                        text1.setText("Days lived : " + clickedAnimal.getAge()
                        + "\n" + "Children born : " + clickedAnimal.getChildren() + "\n" + "Current energy : " + clickedAnimal.energy);

                        textBox.getChildren().add(text1);
                        root.getChildren().add(textBox);




                        animalStats.setScene(new Scene(root));
                        animalStats.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                timeline1.play();
                            }
                        });

                        animalStats.show();

                    }



                }
            }
        });

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

