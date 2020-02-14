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
        } else {
            this.points = points;
        }
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
}
