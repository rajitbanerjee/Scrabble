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
}
