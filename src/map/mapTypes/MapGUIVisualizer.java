package map.mapTypes;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import map.mapTypes.WorldMap;
import mapElements.positionAndDirection.Vector2d;

public class MapGUIVisualizer {

    private WorldMap map;
    private Pane root;

    public MapGUIVisualizer(WorldMap map) {
        this.map = map;
        this.map.addVisualizer(this);
        visualize();
    }

    private void visualize() {
        Pane root = new Pane();
        root.setPrefSize(800, 800);

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

    public Parent getVisualization() {
        return new Pane(root);
    }

    public void recolorTile (Vector2d obj, Color color) {
        Tile tile = new Tile(800 / this.map.getWidth(), 800 / this.map.getHeight(), color);
        tile.setTranslateX(obj.x * 800 / this.map.getWidth());
        tile.setTranslateY(obj.y * 800 / this.map.getHeight());
        this.root.getChildren().set(obj.x * map.getWidth() + obj.y, tile);

        // ((Rectangle) tile.getChildren().get(0)).setStroke(color);
        System.out.println("Changed");
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
