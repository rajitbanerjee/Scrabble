/**
 * Each Tile has an associated type (blank or A-Z) and an
 * assigned number of points for using it to construct a word.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class Tile {
    // each tile has a type (blank/letter A-Z)
    private char type;
    // points for each tile vary between 0 and 10.
    private int points;

    // create a new Tile with given type (blank or A-Z) and assigned points
    public Tile(char type, int points) {
        setType(type);
        setPoints(points);
    }

    public char getType() {
        return type;
    }

    /**
     * Setter for tile type.
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

    // getter and setter methods for points
    public int getPoints() {
        return points;
    }

    /**
     * Setter for tile's associated points
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
     * @param o Object to be tested for equality against another Tile
     * @return {@code true} if Tiles are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Tile) {
            Tile tile = (Tile) o;
            return tile.getType() == getType() && tile.getPoints() == getPoints();
        } else {
            return false;
        }
    }

    // each Tile is displayed as the type of Tile and the associated points
    @Override
    public String toString() {
        return String.format("  %c  ", type);
    }

    /**
     * Creates a Tile object of the specified type.
     *
     * @param type the type of Tile to be created A-Z or blank -
     * @return Tile object for specified type
     * @throws IllegalArgumentException if specified type is neither A-Z nor -
     */
    public static Tile makeTile(char type) throws IllegalArgumentException {
        type = Character.toUpperCase(type);
        // different types of tiles ("-" represents the blank tile) grouped...
        // according to equality of associated points
        String[] tileTypes = {"-", "E", "AI", "O", "NRT", "LSU",
                "D", "G", "BCMP", "FHVWY", "K", "JX", "QZ"};
        // the points associated for each tile in each group
        int[] pointTypes = {0, 1, 1, 1, 1, 1, 2, 2, 3, 4, 5, 8, 10};

        int points = -1;
        for (int i = 0; i < tileTypes.length; i++) {
            if (tileTypes[i].contains(type + "")) {
                points = pointTypes[i];
                break;
            }
        }
        // every tile must have associated points
        // if points remain -1 (initial), that means there's an error with type
        if (points == -1) {
            throw new IllegalArgumentException("Invalid type of tile given");
        }
        return new Tile(type, points);
    }
}
