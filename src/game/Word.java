package game;

/**
 * A word represents a String, placed on the board at a starting
 * row and column, and with an orientation (across/down).
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class Word {
    private String letters;
    private int row;
    private int column;
    private char orientation;

    /**
     * Construct a word with given letters, starting at a specified
     * position (row, column), and having a certain orientation.
     *
     * @param letters     the letters forming the word
     * @param column      character between 'A' - 'O' specifying the board column
     * @param row         integer between 1 - 15 specifying the board row
     * @param orientation character 'A' - word going across / 'D' - down
     */
    public Word(String letters, char column, int row, char orientation) {
        this.letters = letters.toUpperCase().strip();
        // Convert column and row to real board indices (0 - 14)
        this.row = row - 1;
        this.column = column - 'A';
        this.orientation = Character.toUpperCase(orientation);
    }

    /**
     * Compute the number of letters in the word.
     *
     * @return number of letters in word
     */
    public int length() {
        return letters.length();
    }

    /**
     * Gets the character at a specified word index.
     *
     * @param index required character's index in the word
     * @return the character at the given index
     */
    public char charAt(int index) {
        return letters.charAt(index);
    }

    /**
     * Checks if a word goes across the board.
     *
     * @return {@code true}, if word is placed horizontally
     */
    public boolean isHorizontal() {
        return orientation == 'A';
    }

    /**
     * Checks if a word goes down the board.
     *
     * @return {@code true}, if word is placed vertically
     */
    public boolean isVertical() {
        return orientation == 'D';
    }

    /**
     * Accessor for the letters that form the word.
     *
     * @return the String of letters in the word
     */
    public String getLetters() {
        return letters;
    }

    /**
     * Checks if the word contains alphabets only.
     *
     * @return {@code true}, if the word is strictly alphabetic.
     */
    public boolean isAlphaString() {
        return letters.matches("[A-Za-z]+");
    }

    /**
     * Returns the word's starting row.
     *
     * @return integer between 0 - 14 (real board row index)
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the word's starting column.
     *
     * @return integer between 0 - 14 (real board column index)
     */
    public int getColumn() {
        return column;
    }

}
