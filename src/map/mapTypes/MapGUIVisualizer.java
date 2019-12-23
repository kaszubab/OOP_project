package map.mapTypes;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.SceneBuilder;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import map.mapTypes.WorldMap;
import mapElements.positionAndDirection.Vector2d;

import java.util.Observable;

public class MapGUIVisualizer {

    private WorldMap map;
    private MapStatistics statistics;
    private Pane root;

    public MapGUIVisualizer(WorldMap map, MapStatistics statistics) {
        this.map = map;
        this.statistics = statistics;
        this.map.addVisualizer(this);
        visualize();
    }

    private void visualize() {

        Pane root = new Pane();
        root.setPrefSize(800, 600);



        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                Tile tile;
                if ( i >= this.map.jungleLeftLower.x && i <= this.map.jungleRightUpper.x &&
                        j <= this.map.jungleRightUpper.y && j >= this.map.jungleLeftLower.y) {
                    tile = new Tile(600 / this.map.getWidth(), 600 / this.map.getHeight(), Color.LIGHTGREEN);
                }
                else {
                    tile = new Tile(600 / this.map.getWidth(), 600 / this.map.getHeight(), null);
                }

                tile.setTranslateX(i * 600 / this.map.getWidth());
                tile.setTranslateY(j * 600 / this.map.getHeight());
                root.getChildren().add(tile);
            }
        }


        this.root =  root;
    }

    public void addButtons (Timeline timeline) {

        VBox box = new VBox();
        box.setTranslateX(600);

        Button button1 = new Button();
        button1.setText("Pause");
        button1.setPrefWidth(200);
        button1.setPrefHeight(50);

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.pause();
            }
        });

        Button button2 = new Button();
        button2.setText("Start");
        button2.setPrefWidth(200);
        button2.setPrefHeight(50);

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.play();
            }
        });

        box.getChildren().add(button1);
        box.getChildren().add(button2);
        box.getChildren().add(addChart());

        this.root.getChildren().add(box);
    }


    public Parent getVisualization() {
        return new Pane(root);
    }

    private Chart addChart() {
        MapStatistics.MapData data = statistics.getMapData();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("0", data.domineeringGenes[0]),
                        new PieChart.Data("1", data.domineeringGenes[1]),
                        new PieChart.Data("2", data.domineeringGenes[2]),
                        new PieChart.Data("3", data.domineeringGenes[3]),
                        new PieChart.Data("4", data.domineeringGenes[4]),
                        new PieChart.Data("5", data.domineeringGenes[5]),
                        new PieChart.Data("6", data.domineeringGenes[6]),
                        new PieChart.Data("7", data.domineeringGenes[7])
                );
        final PieChart chart = new PieChart(pieChartData);
        chart.setMaxWidth(200);
        chart.setMaxHeight(300);

        chart.setTitle("Domineering Ganes");
        return chart;
    }

    public void updateChart() {
        ((VBox)this.root.getChildren().get(root.getChildren().size()-1)).getChildren().remove(2);
        ((VBox)this.root.getChildren().get(root.getChildren().size()-1)).getChildren().add(addChart());
    }

    public void recolorTile (Vector2d obj, Color color) {
        Tile tile = new Tile(600 / this.map.getWidth(), 600 / this.map.getHeight(), color);
        tile.setTranslateX(obj.x * 600 / this.map.getWidth());
        tile.setTranslateY(obj.y * 600 / this.map.getHeight());

        this.root.getChildren().set(obj.x * map.getWidth() + obj.y , tile);
    }


    private class Tile extends StackPane {
        public Tile(int x, int y, Color color) {
            Rectangle border = new Rectangle(x, y);
            border.setFill(color);

            border.setStroke(Color.BLACK);
            setAlignment(Pos.CENTER);
            getChildren().addAll(border);
        }
    }

}
