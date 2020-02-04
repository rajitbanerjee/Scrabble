public class Tile {
    private char type;
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
