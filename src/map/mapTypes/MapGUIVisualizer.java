package map.mapTypes;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import mapElements.positionAndDirection.Vector2d;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

public class MapGUIVisualizer {

    private WorldMap map;
    private MapStatistics statistics;
    private Pane root;
    private final int windowSize = 20;

    public MapGUIVisualizer(WorldMap map, MapStatistics statistics) {
        this.map = map;
        this.statistics = statistics;
        this.map.addVisualizer(this);
        statistics.addGUI(this);
        visualize();
    }

    private void visualize() {

        Pane root = new Pane();
        root.setPrefSize(1200, 800);



        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                Tile tile;
                if ( i >= this.map.jungleLeftLower.x && i <= this.map.jungleRightUpper.x &&
                        j <= this.map.jungleRightUpper.y && j >= this.map.jungleLeftLower.y) {
                    tile = new Tile(800 / this.map.getWidth(), 800 / this.map.getHeight(), Color.LIGHTGREEN);
                }
                else {
                    tile = new Tile(800 / this.map.getWidth(), 800 / this.map.getHeight(), null);
                }

                tile.setTranslateX(i * 800 / this.map.getWidth());
                tile.setTranslateY(j * 800 / this.map.getHeight());

                root.getChildren().add(tile);
            }
        }


        this.root =  root;
    }

    public void addButtons (Timeline timeline) {

        VBox box = new VBox();
        box.setTranslateX(800);

        Button button1 = new Button();
        button1.setText("Pause");
        button1.setPrefWidth(400);
        button1.setPrefHeight(30);

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.pause();

                try (FileWriter file = new FileWriter("historyData.json")) {

                    file.write(statistics.statisticsToFile().toJSONString());
                    file.flush();

                } catch (IOException e) {

                }


            }
        });

        Button button2 = new Button();
        button2.setText("Start");
        button2.setPrefWidth(400);
        button2.setPrefHeight(30);

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.play();
            }
        });



        box.getChildren().add(button1);
        box.getChildren().add(button2);

        MapStatistics.MapData data = statistics.getMapData();

        Text area1 = new Text("Average life length "  + data.averageAge);
        Text area2 = new Text("Average children count "  + data.averageChildren);

        box.getChildren().add(area1);
        box.getChildren().add(area2);

        box.getChildren().add(addLineChart());
        box.getChildren().add(addPieChart(data));


        this.root.getChildren().add(box);
    }


    public Parent getVisualization() {
        return  new Pane(root);
    }

    private Chart addPieChart(MapStatistics.MapData data) {

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
        chart.setMaxWidth(400);
        chart.setMaxHeight(350);

        chart.setTitle("Domineering Genes");
        return chart;
    }

    private Chart addLineChart() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Time/s");
        xAxis.setAnimated(false);
        yAxis.setLabel("Quantity");
        yAxis.setAnimated(false);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setMaxWidth(400);
        lineChart.setMaxHeight(350);
        lineChart.setTitle("Quantity Chart");
        lineChart.setAnimated(false);

        XYChart.Series<String, Number> animalSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> plantSeries = new XYChart.Series<>();


        animalSeries.setName("Animals");
        lineChart.getData().add(animalSeries);

        plantSeries.setName("Plants");
        lineChart.getData().add(plantSeries);

        return lineChart;
    }

    private void  updateLineChart(Node chart, MapStatistics.MapData data) {

        if (data.epoch % 10 != 0) return;
        LineChart<String, Number> lineChart = (LineChart<String, Number>) chart;
        if ( lineChart.getData().get(0).getData().size() >= windowSize) lineChart.getData().get(0).getData().remove(0);
        if ( lineChart.getData().get(1).getData().size() >= windowSize) lineChart.getData().get(1).getData().remove(0);

        lineChart.getData().get(0).getData().add(new XYChart.Data<>(Integer.toString(data.epoch), data.animalCount));
        lineChart.getData().get(1).getData().add(new XYChart.Data<>(Integer.toString(data.epoch), data.grassCount));
    }



    public void updateCharts(MapStatistics.MapData mpDt) {


        updateLineChart(((VBox)this.root.getChildren().get(root.getChildren().size()-1)).getChildren().get(4), mpDt);
        ((VBox)this.root.getChildren().get(root.getChildren().size()-1)).getChildren().remove(5);
        ((VBox)this.root.getChildren().get(root.getChildren().size()-1)).getChildren().add(addPieChart(mpDt));

        Text area1 = new Text("Average life length "  + mpDt.averageAge);
        Text area2 = new Text("Average children count "  + mpDt.averageChildren);

        ((VBox)this.root.getChildren().get(root.getChildren().size()-1)).getChildren().set(2,area1);
        ((VBox)this.root.getChildren().get(root.getChildren().size()-1)).getChildren().set(3,area2);
    }

    public void recolorTile (Vector2d obj, Color color) {
        Tile tile = new Tile(800 / this.map.getWidth(), 800 / this.map.getHeight(), color);
        tile.setTranslateX(obj.x * 800 / this.map.getWidth());
        tile.setTranslateY(obj.y * 800 / this.map.getHeight());

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
