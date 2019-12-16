package map.visualization;

import mapElements.positionAndDirection.MoveDirection;

import java.util.LinkedList;

public class OptionParser {
    private MoveDirection [] validArgTable;

    public OptionParser(String [] name) {
        LinkedList<MoveDirection> list = new LinkedList<>();
        for (String x : name) {
            switch (x) {
                case "forward":
                case "f":
                    list.add(MoveDirection.FORWARD);
                    break;
                case "backward":
                case "b":
                    list.add(MoveDirection.BACKWARD);
                    break;
                case "right":
                case "r":
                    list.add(MoveDirection.RIGHT);
                    break;
                case "left":
                case "l":
                    list.add(MoveDirection.LEFT);
                    break;
                default:
                    throw new IllegalArgumentException("argument " + x + " is not legal move specification");

            }
        }
        validArgTable = list.toArray(new MoveDirection[0]);
    }

    public MoveDirection [] getValidArgTable() {
        return validArgTable;
    }
}
