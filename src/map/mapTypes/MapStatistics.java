package map.mapTypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapStatistics {

    private List<MapData> dataHistory = new ArrayList<>();
    private int currentEpoch = 1;
    private WorldMap map;

    public MapStatistics(WorldMap map) {
        this.map = map;
        map.addStatistics(this);
    }

    public void createSnapshot(int animalCount, int plantCount, int [] domineeringGenes) {
        MapData mpDt = new MapData(currentEpoch, animalCount, plantCount, domineeringGenes);
        currentEpoch++;
        dataHistory.add(mpDt);
    }

    public MapData getMapData() {
        return dataHistory.get(currentEpoch-2);
    }





    class MapData {

        public MapData(int epoch, int animalCount, int grassCount, int [] domineeringGenes) {
            this.epoch = epoch;
            this.animalCount = animalCount;
            this.grassCount = grassCount;
            this.domineeringGenes = domineeringGenes;
        }

        public int epoch;
        public int animalCount;
        public int grassCount;
        public int [] domineeringGenes;

    }
}
