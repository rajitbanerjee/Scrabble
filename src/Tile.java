/**
 * Each Tile has an associated type (blank or A-Z) and an
 * assigned number of points for using it to construct a word.
 *
 * @author Rajit Banerjee, 18202817
 */
public class Tile {
    // each tile has a type (blank/letter A-Z)
    private char type;
    // points for each tile vary between 0 and 10.
    private int points;

    Tile() {
        type = 0;
        points = 0;
    }

    Tile(char type, int points) {
        setType(type);
        setPoints(points);
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setType(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return type + " (points: " + points + ")";
    }
}
