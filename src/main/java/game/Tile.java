package game;

/**
 * Each Tile has an associated type (blank or A-Z) and an
 * assigned number of points for using it to construct a word.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class Tile {
    /* Different types of tiles ("-" represents the blank tile) grouped
    according to equality of associated points */
    public static final String[] TYPES_ARRAY = {"-", "E", "AI", "O", "NRT", "LSU",
            "D", "G", "BCMP", "FHVWY", "K", "JX", "QZ"};
    // Number of tiles for each tile type in every group of tile types
    public static final int[] NUM_ARRAY = {2, 12, 9, 8, 6, 4, 4, 3, 2, 2, 1, 1, 1};
    // Points associated for each tile in each group
    public static final int[] POINTS_ARRAY = {0, 1, 1, 1, 1, 1, 2, 2, 3, 4, 5, 8, 10};
    private char type;
    private int points;

    /**
     * Creates a new Tile with given type and points.
     *
     * @param type   tile type/letter
     * @param points tile points
     */
    public Tile(char type, int points) {
        setType(type);
        setPoints(points);
    }

    /**
     * Creates a Tile object of the specified type.
     *
     * @param type the type of Tile to be created A-Z or blank -
     * @return Tile object for specified type
     * @throws IllegalArgumentException if specified type is neither A-Z nor -
     */
    public static Tile makeTile(char type) throws IllegalArgumentException {
        int points = -1;
        for (int i = 0; i < TYPES_ARRAY.length; i++) {
            if (TYPES_ARRAY[i].contains(type + "")) {
                points = POINTS_ARRAY[i];
                break;
            }
        }
        // If points remain -1 (initial), that means there's an error with type
        if (points == -1) {
            throw new IllegalArgumentException("Invalid type of tile given");
        }
        return new Tile(type, points);
    }

    /**
     * Accessor for tile type.
     *
     * @return type letter A-Z or blank type of tile
     */
    public char getType() {
        return type;
    }

    /**
     * Mutator for tile type.
     *
     * @param type letter A-Z or blank type of tile
     * @throws IllegalArgumentException if tile type is invalid
     */
    public void setType(char type) throws IllegalArgumentException {
        if (!Character.isLetter(Character.toUpperCase(type)) && type != '-') {
            throw new IllegalArgumentException("Invalid tile type.");
        } else {
            this.type = type;
        }
    }

    /**
     * Accessor for the points associated with a tile.
     *
     * @return points associated with a tile
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setter for tile's associated points.
     *
     * @param points assigned to each type of tile
     * @throws IllegalArgumentException if points are negative or ore than 10
     */
    public void setPoints(int points) throws IllegalArgumentException {
        if (points < 0 || points > 10) {
            throw new IllegalArgumentException("Invalid tile points.");
        }
        this.points = points;
    }

    /**
     * Tile equality checker.
     *
     * @param obj Object to be tested for equality against another Tile
     * @return {@code true} if Tiles are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return tile.getType() == getType() && tile.getPoints() == getPoints();
        } else {
            return false;
        }
    }

    /**
     * Each Tile is displayed as the type of Tile.
     */
    @Override
    public String toString() {
        return Character.toString(type);
    }

}