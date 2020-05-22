package game;

import logic.Scoring;
import logic.Scrabble;

import java.util.HashMap;

/**
 * The Board is a 15x15 matrix of Squares.
 * TODO javadoc comments
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class Board {
    public static final int SIZE = 15;
    public static final String[] LAYOUT = {
            "3W,N,N,2L,N,N,N,3W,N,N,N,2L,N,N,3W",
            "N,2W,N,N,N,3L,N,N,N,3L,N,N,N,2W,N",
            "N,N,2W,N,N,N,2L,N,2L,N,N,N,2W,N,N",
            "2L,N,N,2W,N,N,N,2L,N,N,N,2W,N,N,2L",
            "N,N,N,N,2W,N,N,N,N,N,2W,N,N,N,N",
            "N,3L,N,N,N,3L,N,N,N,3L,N,N,N,3L,N",
            "N,N,2L,N,N,N,2L,N,2L,N,N,N,2L,N,N",
            "3W,N,N,2L,N,N,N,*,N,N,N,2L,N,N,3W",
    };
    private final Square[][] board;
    private final HashMap<Index, String> horizontalWords = new HashMap<>();
    private final HashMap<Index, String> verticalWords = new HashMap<>();
    private boolean isFirstMove;

    /**
     * Creates a new Scrabble board.
     */
    public Board() {
        board = new Square[SIZE][SIZE];
        setFirstMove(true);
        // Initialise multiplier squares on board
        String[] row;
        for (int i = 0; i < SIZE; i++) {
            row = (i <= 7) ? LAYOUT[i].split(",") : LAYOUT[SIZE - i - 1].split(",");
            for (int j = 0; j < SIZE; j++) {
                switch (row[j]) {
                    case "*":
                        board[i][j] = new Square(Square.MULTIPLIER.CENTRE);
                        break;
                    case "2L":
                        board[i][j] = new Square(Square.MULTIPLIER.DOUBLE_LS);
                        break;
                    case "3L":
                        board[i][j] = new Square(Square.MULTIPLIER.TRIPLE_LS);
                        break;
                    case "2W":
                        board[i][j] = new Square(Square.MULTIPLIER.DOUBLE_WS);
                        break;
                    case "3W":
                        board[i][j] = new Square(Square.MULTIPLIER.TRIPLE_WS);
                        break;
                    default:
                        board[i][j] = new Square(Square.MULTIPLIER.NORMAL);
                        break;
                }
            }
        }
    }

    public void storeHorizontalWords() {
        horizontalWords.clear();
        for (int i = 0; i < Board.SIZE; i++) {
            int j = 0;
            while (j < Board.SIZE) {
                StringBuilder word = new StringBuilder();
                Index start = new Index(i, j);
                while (Square.isValid(i, j) && !isEmpty(i, j)) {
                    word.append(getLetter(i, j));
                    j++;
                }
                if (!word.toString().equals("")) {
                    for (int k = 0; k < word.length(); k++) {
                        horizontalWords.put(start, word.toString());
                        start = new Index(i, start.getColumn() + 1);
                    }
                }
                j++;
            }
        }
    }

    public void storeVerticalWords() {
        verticalWords.clear();
        for (int j = 0; j < Board.SIZE; j++) {
            int i = 0;
            while (i < Board.SIZE) {
                StringBuilder word = new StringBuilder();
                Index start = new Index(i, j);
                while (Square.isValid(i, j) && !isEmpty(i, j)) {
                    word.append(getLetter(i, j));
                    i++;
                }
                if (!word.toString().equals("")) {
                    for (int k = 0; k < word.length(); k++) {
                        verticalWords.put(start, word.toString());
                        start = new Index(start.getRow() + 1, j);
                    }
                }
                i++;
            }
        }
    }

    /**
     * Gets the horizontal word if it exists, else "".
     *
     * @param row    board row
     * @param column board column
     * @return the horizontal word starting at given index
     */
    public String getHorizontalWord(int row, int column) {
        Index c = new Index(row, column);
        return horizontalWords.getOrDefault(c, "");
    }

    /**
     * Gets the vertical word if it exists, else "".
     *
     * @param row    board row
     * @param column board column
     * @return the vertical word starting at given index
     */
    public String getVerticalWord(int row, int column) {
        Index c = new Index(row, column);
        return verticalWords.getOrDefault(c, "");
    }

    /**
     * Accessor for board.
     *
     * @return the board
     */
    public Square[][] getBoard() {
        return board;
    }

    /**
     * Accessor for letter at specified row and column.
     *
     * @param row    board row
     * @param column board column
     * @return the letter on the Square at the given index
     */
    public char getLetter(int row, int column) {
        return board[row][column].getTile().getType();
    }

    /**
     * Accessor for isFirstMove.
     *
     * @return boolean value indicating whether the first move has been played
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }

    /**
     * Setter for isFirstMove.
     *
     * @param isFirstMove indicates whether the current move is the first move of the game
     */
    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    public boolean isEmpty(int row, int column) {
        return board[row][column].isEmpty();
    }

    /**
     * Check if board contains no tiles.
     *
     * @return {@code true} if board is empty
     */
    public boolean isEmpty() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Place a given Tile at a specified row and column on the Board.
     * Add the index of the Tile to the list of last placed indices.
     *
     * @param column character between 'A' - 'O' to specify the board column
     * @param row    integer between 1 - 15 to specify the board row
     * @param tile   the Tile to be placed at the specified index
     */
    public void placeTile(char column, int row, Tile tile) {
        board[row - 1][column - 'A'].setTile(tile);
        Scoring.LAST_COVERED_INDICES.add(new Index(row - 1, column - 'A'));
    }

    /**
     * Gets the word on the board in given row, starting and ending at given columns.
     *
     * @param row         the board row in which the word is placed
     * @param startColumn the starting board column of the word
     * @param endColumn   the ending board column of the word
     * @return the String representation of the word
     */
    public String getHorizontalWord(int row, int startColumn, int endColumn) {
        StringBuilder word = new StringBuilder();
        for (int i = startColumn; i <= endColumn; i++) {
            word.append(board[row][i].getTile().getType());
        }
        return word.toString();
    }

    /**
     * Gets the word on the board in given row, starting and ending at given columns.
     *
     * @param column   the board column in which the word is placed
     * @param startRow the starting board row of the word
     * @param endRow   the ending board row of the word
     * @return the String representation of the word
     */
    public String getVerticalWord(int column, int startRow, int endRow) {
        StringBuilder word = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            word.append(board[i][column].getTile().getType());
        }
        return word.toString();
    }

    /**
     * Place a given word either vertically or horizontally starting at a
     * specified row and column on the board.
     *
     * @param word  to be placed on the board
     * @param frame the players frame
     * @throws IllegalArgumentException if word placement is illegal
     */
    public void placeWord(Word word, Frame frame) throws IllegalArgumentException {
        if (!isWordLegal(word, frame)) {
            throw new IllegalArgumentException("> Invalid word placement");
        }
        Scoring.LAST_COVERED_INDICES.clear();
        setFirstMove(false);
        int row, column;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (word.isHorizontal()) {
                // Column index increases for word placed horizontally
                column = word.getColumn() + i;
                row = word.getRow();
            } else {
                // Row index increases for word placed vertically
                row = word.getRow() + i;
                column = word.getColumn();
            }
            // Ignore filled squares, place tiles on the remaining squares
            if (isEmpty(row, column)) {
                if (frame.contains(ch)) {
                    // Place tile on the board and remove it from the frame
                    placeTile((char) (column + 'A'), row + 1, frame.getTile(ch));
                    frame.remove(ch);
                } else {
                    // Convert blank tile to a given letter if letter is not in the frame
                    Tile blankTile = new Tile(ch, 0);
                    placeTile((char) (column + 'A'), row + 1, blankTile);
                    frame.remove('-');
                }
            }
        }
    }

    /**
     * Determines if a word placement is legal or not.
     *
     * @param word  to be placed on the board
     * @param frame the players frame
     * @return {@code true} if word placement is legal
     */
    public boolean isWordLegal(Word word, Frame frame) {
        // Checks for input validity
        if (!Square.isValid(word.getRow(), word.getColumn()) ||
                !(word.isHorizontal() || word.isVertical()) ||
                word.length() < 2 || !word.isAlphaString() ||
                frame == null) {
            return false;
        }
        // Checks if word length exceeds the size of the board
        if (isOverflowed(word)) {
            Scrabble.printToOutput("> Word goes out of the board!");
            return false;
        }
        // Checks for conflicts with existing letters on the board
        if (doesWordConflict(word)) {
            Scrabble.printToOutput("> Word conflicts with existing word on board!");
            return false;
        }
        // Checks if frame contains the required tiles
        if (!doesFrameContainTiles(word, frame)) {
            Scrabble.printToOutput("> Frame doesn't contain tiles");
            return false;
        }
        // Checks whether the placement uses at least one letter from frame
        if (!isFrameUsed(word, frame)) {
            Scrabble.printToOutput("> Frame not used!");
            return false;
        }
        // If first move, checks if it covers the centre square
        if (isFirstMove) {
            boolean isCentreCovered = doesWordCoverCentre(word);
            if (!isCentreCovered) {
                Scrabble.printToOutput("> First move must cover centre square!");
            }
            return isCentreCovered;
        } else {
            // If not first move, checks if word connects with an existing word on board
            return isWordJoined(word);
        }
    }

    // Checks if a word placement goes out of the board
    private boolean isOverflowed(Word word) {
        if (word.isHorizontal()) {
            return (word.getColumn() + word.length() - 1) >= SIZE;
        } else {
            return (word.getRow() + word.length() - 1) >= SIZE;
        }
    }

    // Checks if a word conflicts with any existing words on the board
    private boolean doesWordConflict(Word word) {
        char[] wordArray = word.getLetters().toCharArray();
        int row = word.getRow();
        int column = word.getColumn();
        if (word.isHorizontal()) {
            // Check if the squares before and after the word are empty
            if (Square.isValid(row, column - 1) &&
                    Square.isValid(row, column + word.length())) {
                if (!isEmpty(row, column - 1) ||
                        !isEmpty(row, column + word.length())) {
                    return true;
                }
            }
            for (int i = 0; i < word.length(); i++) {
                // Square is filled but tile does not match letters in the placed word
                if (!isEmpty(row, column + i) &&
                        getLetter(row, column + i) != wordArray[i]) {
                    return true;
                }
            }
        } else {
            // Check if the squares before and after the word are empty
            if (Square.isValid(row - 1, column) &&
                    Square.isValid(row + word.length(), column)) {
                if (!isEmpty(row - 1, column) ||
                        !isEmpty(row + word.length(), column)) {
                    return true;
                }
            }
            for (int i = 0; i < word.length(); i++) {
                // Square is filled but tile does not match letters in the placed word
                if (!isEmpty(row + i, column) &&
                        getLetter(row + i, column) != wordArray[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    // Checks if the frame contains the letters necessary for word placement
    private boolean doesFrameContainTiles(Word word, Frame frame) {
        StringBuilder sb = new StringBuilder();
        for (Tile t : frame.getFrame()) {
            sb.append(t.getType());
        }
        String tilesInFrame = sb.toString();
        int row, column;
        for (int i = 0; i < word.length(); i++) {
            if (word.isHorizontal()) {
                // Column index increases for word placed horizontally
                column = word.getColumn() + i;
                row = word.getRow();
            } else {
                // Row index increases for word placed vertically
                row = word.getRow() + i;
                column = word.getColumn();
            }
            String letter = Character.toString(word.charAt(i));
            if (isEmpty(row, column)) {
                if (tilesInFrame.contains(letter)) {
                    // Check for the specified letter from the frame
                    tilesInFrame = tilesInFrame.replaceFirst(letter, "");
                } else if (tilesInFrame.contains("-")) {
                    // Check for any blank characters from the frame
                    tilesInFrame = tilesInFrame.replaceFirst("-", "");
                } else {
                    // Frame does not contain letter needed
                    return false;
                }
            }
        }
        return true;
    }

    // Checks that at least one letter from the frame is used
    private boolean isFrameUsed(Word word, Frame frame) {
        int row, column;
        for (int i = 0; i < word.length(); i++) {
            if (word.isHorizontal()) {
                // Column index increases for word placed horizontally
                column = word.getColumn() + i;
                row = word.getRow();
            } else {
                // Row index increases for word placed vertically
                row = word.getRow() + i;
                column = word.getColumn();
            }
            char letter = word.charAt(i);
            // If square is empty, check if frame contains the required letter or a blank tile
            if (isEmpty(row, column)) {
                if (frame.contains(letter) || frame.contains('-')) {
                    return true;
                }
            }
        }
        return false;
    }

    // Checks if the word to be placed covers the centre square H8 (aka 7, 7)
    private boolean doesWordCoverCentre(Word word) {
        int row = word.getRow();
        int column = word.getColumn();
        if (word.isHorizontal()) {
            return row == SIZE / 2 && column <= SIZE / 2 &&
                    column + word.length() - 1 >= SIZE / 2;
        } else {
            return column == SIZE / 2 && row <= SIZE / 2 &&
                    row + word.length() - 1 >= SIZE / 2;
        }
    }

    // Checks if a word placement connects with another existing word on the board
    private boolean isWordJoined(Word word) {
        int row = word.getRow();
        int column = word.getColumn();
        if (word.isHorizontal()) {
            for (int i = 0; i < word.length(); i++) {
                // Check top
                if (Square.isValid(row - 1, column + i)) {
                    if (!isEmpty(row - 1, column + i)) {
                        return true;
                    }
                }
                // Check bottom
                if (Square.isValid(row + 1, column + i)) {
                    if (!isEmpty(row + 1, column + i)) {
                        return true;
                    }
                }
                // Check if the word contains tiles already on the board
                if (Square.isValid(row, column + i)) {
                    if (!isEmpty(row, column + i)) {
                        return true;
                    }
                }
            }
        } else {
            for (int i = 0; i < word.length(); i++) {
                // Check left
                if (Square.isValid(row + i, column - 1)) {
                    if (!isEmpty(row + i, column - 1)) {
                        return true;
                    }
                }
                // Check right
                if (Square.isValid(row + i, column + 1)) {
                    if (!isEmpty(row + i, column + 1)) {
                        return true;
                    }
                }
                // Check if the word contains letter already on the board
                if (Square.isValid(row + i, column)) {
                    if (!isEmpty(row + i, column)) {
                        return true;
                    }
                }
            }
        }
        Scrabble.printToOutput("> Word needs to connect with an existing word on board!");
        return false;
    }

}