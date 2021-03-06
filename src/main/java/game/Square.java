package game;

/**
 * Each Square has an assigned multiplier and can have
 * a Tile placed on it.
 * The Board is comprised of 15x15 Squares.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class Square {
    private final MULTIPLIER multiplier;
    private Tile tile = null;

    /**
     * Creates a new Square.
     *
     * @param multiplier square multiplier type
     */
    public Square(MULTIPLIER multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Checks if a specified square index is within the board.
     *
     * @param column integer between 0 - 14 to specify the real board column index
     * @param row    integer between 0 - 14 to specify the real board row index
     * @return {@code true} if the specified square index is valid
     */
    public static boolean isValid(int column, int row) {
        return column >= 0 && column < Board.SIZE &&
                row >= 0 && row < Board.SIZE;
    }

    /**
     * Accessor for multiplier.
     *
     * @return GameConstants.MULTIPLIER multiplier
     */
    public MULTIPLIER getMultiplier() {
        return multiplier;
    }

    /**
     * Accessor for tile in a square
     *
     * @return tile
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Places a tile in a square
     *
     * @param tile to be placed in square
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Checks if a given square doesn't contain a tile.
     *
     * @return {@code true} if the given square is empty
     */
    public boolean isEmpty() {
        return getTile() == null;
    }

    /**
     * Returns the integer value of a letter multiplier.
     *
     * @return integer representing the letter multiplier
     */
    public int getLetterMultiplier() {
        switch (multiplier) {
            case DOUBLE_LS:
                return 2;
            case TRIPLE_LS:
                return 3;
            default:
                return 1;
        }
    }

    /**
     * Returns the integer value of a word multiplier.
     *
     * @return integer representing the word multiplier
     */
    public int getWordMultiplier() {
        switch (multiplier) {
            case CENTRE:
            case DOUBLE_WS:
                return 2;
            case TRIPLE_WS:
                return 3;
            default:
                return 1;
        }
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
                    return "2L";
                case TRIPLE_LS:
                    return "3L";
                case DOUBLE_WS:
                    return "2W";
                case TRIPLE_WS:
                    return "3W";
                case CENTRE:
                    return "*";
                default:
                    return " ";
            }
        }
        return tile.toString();
    }

    /**
     * Represents the type of square on the board.
     */
    public enum MULTIPLIER {DOUBLE_LS, TRIPLE_LS, DOUBLE_WS, TRIPLE_WS, NORMAL, CENTRE}

}