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
    private Constants.MULTIPLIER multiplier;
    private Tile tile = null;

    public Square(Constants.MULTIPLIER multiplier) {
        this.multiplier = multiplier;
    }

    // Required setters and getters
    public Constants.MULTIPLIER getMultiplier() {
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
                    return "2xL";
                case TRIPLE_LS:
                    return "3xL";
                case DOUBLE_WS:
                    return "2xW";
                case TRIPLE_WS:
                    return "3xW";
                case CENTRE:
                    return " * ";
                default:
                    return "   ";
            }
        }
        return tile.toString();
    }
    
}
