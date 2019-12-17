package mapElements.positionAndDirection;

public enum MapDirection {
    NORTH,
    NORTHWEST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHEAST,
    SOUTHEAST,
    EAST;


    public String toString()
    {
        switch (this) {
            case EAST: return "Wschód";
            case WEST: return "Zachód";
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
            case NORTHEAST: return "Północny wschód";
            case SOUTHEAST: return "Południowy wcchód";
            case SOUTHWEST: return "Południowy zachód";
            case NORTHWEST: return "Północny zachód";
        }
        return null;
    }

    public static MapDirection intToMapDirection(int val) {
        switch (val) {
            case 0: return NORTH;
            case 1: return NORTHEAST;
            case 2: return EAST;
            case 3: return SOUTHEAST;
            case 4: return SOUTH;
            case 5: return SOUTHWEST;
            case 6: return WEST;
            case 7: return NORTHWEST;
        }
        return null;
    }

    public MapDirection next() {
        switch (this) {
            case EAST: return SOUTH;
            case WEST: return NORTH;
            case NORTH: return EAST;
            case SOUTH: return WEST;
        }
        return null;
    }

    public MapDirection previous() {
        switch (this) {
            case EAST: return NORTH;
            case WEST: return SOUTH;
            case NORTH: return WEST;
            case SOUTH: return EAST;
        }
        return null;
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case EAST: return new Vector2d(1, 0);
            case WEST: return new Vector2d(-1, 0);
            case NORTH: return new Vector2d(0, 1);
            case SOUTH: return new Vector2d(0, -1);
            case NORTHWEST: return new Vector2d(-1 ,1);
            case NORTHEAST: return new Vector2d(1,1);
            case SOUTHEAST: return new Vector2d(1,-1);
            case SOUTHWEST: return  new Vector2d(-1, -1);
        }
        return null;
    }
}
