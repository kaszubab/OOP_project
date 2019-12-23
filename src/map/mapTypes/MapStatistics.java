package map.mapTypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapStatistics {

    private List<MapData> dataHistory = new ArrayList<>();
    private int currentEpoch = 1;
    private WorldMap map;
    private MapGUIVisualizer GUI = null;

    public MapStatistics(WorldMap map) {
        this.map = map;
        map.addStatistics(this);
    }

    public void createSnapshot(int animalCount, int plantCount, int [] domineeringGenes, double averageAge, double averageChildrenCount) {
        MapData mpDt = new MapData(currentEpoch, animalCount, plantCount, domineeringGenes,averageAge, averageChildrenCount);
        currentEpoch++;
        dataHistory.add(mpDt);

        if (this.GUI != null)this.GUI.updateCharts(mpDt);

    }

    public MapData getMapData() {
        return dataHistory.get(currentEpoch-2);
    }

    public void addGUI(MapGUIVisualizer GUI) {
        this.GUI = GUI;
    }





    class MapData {

        public MapData(int epoch, int animalCount, int grassCount, int [] domineeringGenes, double avAge, double avChildren) {
            this.epoch = epoch;
            this.animalCount = animalCount;
            this.grassCount = grassCount;
            this.domineeringGenes = domineeringGenes;
            this.averageAge = avAge;
            this.averageChildren = avChildren;
        }

        public int epoch;
        public int animalCount;
        public int grassCount;
        public int [] domineeringGenes;
        public double averageAge;
        public double averageChildren;

    }
}
