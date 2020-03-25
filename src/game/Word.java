package game;

/**
 * A Word is represented by a String of letters, placed on the board
 * at a starting row and column, and with an orientation (across/down).
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
        this.letters = letters.toUpperCase().trim();
        // Convert column and row to real board indices (0 - 14)
        this.row = row - 1;
        this.column = column - 'A';
        this.orientation = Character.toUpperCase(orientation);
    }

    /**
     * Parses the move from the expected String format to a word, a factory method
     *
     * @param move move to be converted into a word
     * @return a new Word instance
     */
    public static Word parseMove(String move) {
        String[] inputArguments = move.split("\\s+");
        char column = inputArguments[0].charAt(0);
        int row = Integer.parseInt(inputArguments[0].substring(1));
        char orientation = inputArguments[1].charAt(0);
        String letters = inputArguments[2];
        return new Word(letters, column, row, orientation);
    }

    /**
     * Accessor for letters.
     *
     * @return string of letters
     */
    public String getLetters() {
        return letters;
    }

    /**
     * Accessor for column of word.
     *
     * @return column - Integer 1-15
     */
    public int getColumn() {
        return column;
    }

    /**
     * Accessor for row of word.
     *
     * @return row - Integer 1 - 15
     */
    public int getRow() {
        return row;
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
     * Checks if the word contains alphabets only.
     *
     * @return {@code true}, if the word is strictly alphabetic.
     */
    public boolean isAlphaString() {
        return letters.matches("[A-Za-z]+");
    }

    /**
     * Word equality checker.
     *
     * @param obj Object to be tested for equality against another Word
     * @return {@code true} if Tiles are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Word) {
            Word word = (Word) obj;
            return word.getLetters().equals(letters) && word.getColumn() == getColumn() &&
                    word.getRow() == getRow() && word.orientation == orientation;
        } else {
            return false;
        }
    }

}