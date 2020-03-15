package game;

import constants.Constants;

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

    /**
     * Checks if a specified square index is within the board.
     *
     * @param column integer between 0 - 14 to specify the real board column index
     * @param row    integer between 0 - 14 to specify the real board row index
     * @return {@code true} if the specified square index is valid
     */
    public static boolean isValid(int column, int row) {
        return column >= 0 && column < Constants.BOARD_SIZE &&
                row >= 0 && row < Constants.BOARD_SIZE;
    }

    /**
     * Checks if a given square doesn't contain a tile.
     *
     * @return {@code true} if the given square is empty
     */
    public boolean isEmpty() {
        return getTile() == null;
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

    // Returns the integer value of a letter multiplier
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

    // Returns the integer value of a word multiplier
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
