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

    public Tile() {
        type = 0;
        points = 0;
    }

    // create a new Tile with given type (blank or A-Z) and assigned points
    public Tile(char type, int points) {
        setType(type);
        setPoints(points);
    }

    // setter methods for instance variables
    public void setPoints(int points) {
        this.points = points;
    }

    public void setType(char type) {
        this.type = type;
    }

    // getter methods for instance variables
    public char getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    // each Tile is displayed as the type of Tile and the associated points
    @Override
    public String toString() {
        return type + " (points: " + points + ")";
    }
}