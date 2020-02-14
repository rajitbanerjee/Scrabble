/**
 * Each Square has an assigned multiplier and can have
 * a Tile placed on it.
 * The Board is comprised of 15x15 Squares.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class Square {
    private Multiplier multiplier;
    private Tile tile = null;

    public Square(Multiplier multiplier) {
        this.multiplier = multiplier;
    }

    // Required setters and getters
    public Multiplier getMultiplier() {
        return multiplier;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Gets the String representation of a Square.
     * If no Tile is placed on a Square, the Square
     * multiplier is displayed, else the Tile letter
     * is displayed.
     *
     * @return String representation of a Square
     */
    @Override
    public String toString() {
        if (tile == null) {
            switch (multiplier) {
                case DOUBLE_LS:
                    return "2x_LS";
                case TRIPLE_LS:
                    return "3x_LS";
                case DOUBLE_WS:
                    return "2x_WS";
                case TRIPLE_WS:
                    return "3x_WS";
                case CENTRE:
                    return "  *  ";
                default:
                    return "     ";
            }
        }
        return tile.toString();
    }

    // stores the different types of multipliers on a board
    enum Multiplier {DOUBLE_LS, TRIPLE_LS, DOUBLE_WS, TRIPLE_WS, NORMAL, CENTRE}
}
