package map.visualization;
import org.json.simple.JSONObject;
import java.io.FileReader;


public class MapConfiguration {

    public int width;
    public int height;
    public int startEnergy;
    public int moveEnergy;
    public int plantEnergy;
    public double jungleRatio;


    public MapConfiguration (String filename)  throws Exception {


        Object obj = new org.json.simple.parser.JSONParser().parse(new FileReader( filename));
        JSONObject jobj = (JSONObject) obj;

        this.width = Math.toIntExact((long) jobj.get("width"));
        this.height = Math.toIntExact((long) jobj.get("height"));
        this.startEnergy = Math.toIntExact((long) jobj.get("startEnergy"));
        this.moveEnergy = Math.toIntExact((long) jobj.get("moveEnergy"));
        this.plantEnergy = Math.toIntExact((long) jobj.get("plantEnergy"));
        this.jungleRatio = (double) jobj.get("jungleRatio");

    }
}
