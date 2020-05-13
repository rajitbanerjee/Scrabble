package game;

import constants.GameConstants;
import game_engine.Scoring;
import game_engine.Scrabble;

/**
 * The Board is a 15x15 matrix of Squares.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class Board {
    private final Square[][] board;
    private boolean isFirstMove;

    /**
     * Creates a new Scrabble board.
     */
    public Board() {
        int size = GameConstants.BOARD_SIZE;
        int centre = size / 2;
        board = new Square[size][size];
        setFirstMove(true);
        // Initialise multiplier squares on board
        board[centre][centre] = new Square(GameConstants.MULTIPLIER.CENTRE);
        for (int[] index : GameConstants.NORMAL_SQ_ARRAY) {
            board[index[0]][index[1]] = new Square(GameConstants.MULTIPLIER.NORMAL);
        }
        for (int[] index : GameConstants.DOUBLE_LS_ARRAY) {
            board[index[0]][index[1]] = new Square(GameConstants.MULTIPLIER.DOUBLE_LS);
        }
        for (int[] index : GameConstants.TRIPLE_LS_ARRAY) {
            board[index[0]][index[1]] = new Square(GameConstants.MULTIPLIER.TRIPLE_LS);
        }
        for (int[] index : GameConstants.DOUBLE_WS_ARRAY) {
            board[index[0]][index[1]] = new Square(GameConstants.MULTIPLIER.DOUBLE_WS);
        }
        for (int[] index : GameConstants.TRIPLE_WS_ARRAY) {
            board[index[0]][index[1]] = new Square(GameConstants.MULTIPLIER.TRIPLE_WS);
        }
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
     * Setter for firstMove.
     *
     * @param firstMove boolean value indicating whether the first move has been played
     */
    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    /**
     * Check if board contains no tiles.
     *
     * @return {@code true} if board is empty
     */
    public boolean isEmpty() {
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
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
            if (board[row][column].isEmpty()) {
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
        if (!Square.isValid(word.getColumn(), word.getRow()) ||
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
            return (word.getColumn() + word.length() - 1) >= GameConstants.BOARD_SIZE;
        } else {
            return (word.getRow() + word.length() - 1) >= GameConstants.BOARD_SIZE;
        }
    }

    // Checks if a word conflicts with any existing words on the board
    private boolean doesWordConflict(Word word) {
        char[] wordArray = word.getLetters().toCharArray();
        int column = word.getColumn();
        int row = word.getRow();
        if (word.isHorizontal()) {
            // Check if the squares before and after the word are empty
            if (Square.isValid(column - 1, row) &&
                    Square.isValid(column + word.length(), row)) {
                if (!board[row][column - 1].isEmpty() ||
                        !board[row][column + word.length()].isEmpty()) {
                    return true;
                }
            }
            for (int i = 0; i < word.length(); i++) {
                // Square is filled but tile does not match letters in the placed word
                if (!board[row][column + i].isEmpty() &&
                        board[row][column + i].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        } else {
            // Check if the squares before and after the word are empty
            if (Square.isValid(column, row - 1) &&
                    Square.isValid(column, row + word.length())) {
                if (!board[row - 1][column].isEmpty() ||
                        !board[row + word.length()][column].isEmpty()) {
                    return true;
                }
            }
            for (int i = 0; i < word.length(); i++) {
                // Square is filled but tile does not match letters in the placed word
                if (!board[row + i][column].isEmpty() &&
                        board[row + i][column].getTile().getType() != wordArray[i]) {
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
            if (board[row][column].isEmpty()) {
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
            if (board[row][column].isEmpty()) {
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
            return row == GameConstants.BOARD_SIZE / 2 &&
                    column <= GameConstants.BOARD_SIZE / 2 &&
                    column + word.length() - 1 >= GameConstants.BOARD_SIZE / 2;
        } else {
            return column == GameConstants.BOARD_SIZE / 2 &&
                    row <= GameConstants.BOARD_SIZE / 2 &&
                    row + word.length() - 1 >= GameConstants.BOARD_SIZE / 2;
        }
    }

    // Checks if a word placement connects with another existing word on the board
    private boolean isWordJoined(Word word) {
        int row = word.getRow();
        int column = word.getColumn();
        if (word.isHorizontal()) {
            for (int i = 0; i < word.length(); i++) {
                // Check top
                if (Square.isValid(column + i, row - 1)) {
                    if (!board[row - 1][column + i].isEmpty()) {
                        return true;
                    }
                }
                // Check bottom
                if (Square.isValid(column + i, row + 1)) {
                    if (!board[row + 1][column + i].isEmpty()) {
                        return true;
                    }
                }
                // Check if the word contains tiles already on the board
                if (Square.isValid(column + i, row)) {
                    if (!board[row][column + i].isEmpty()) {
                        return true;
                    }
                }
            }
        } else {
            for (int i = 0; i < word.length(); i++) {
                // Check left
                if (Square.isValid(column - 1, row + i)) {
                    if (!board[row + i][column - 1].isEmpty()) {
                        return true;
                    }
                }
                // Check right
                if (Square.isValid(column + 1, row + i)) {
                    if (!board[row + i][column + 1].isEmpty()) {
                        return true;
                    }
                }
                // Check if the word contains letter already on the board
                if (Square.isValid(column, row + i)) {
                    if (!board[row + i][column].isEmpty()) {
                        return true;
                    }
                }
            }
        }
        Scrabble.printToOutput("> Word needs to connect with an existing word on board!");
        return false;
    }

}