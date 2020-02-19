import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The Board is a 15x15 matrix of Squares.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class Board {
    private Square[][] board;
    private boolean isFirstMove;

    /**
     * The constructor loops through every Square in the Board
     * and sets the multiplier that a particular index may have.
     */
    public Board() {
        board = new Square[15][15];
        isFirstMove = true;
        // set Centre and Normal squares
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                // centre square index
                if (i == 7 && j == 7) {
                    board[i][j] = new Square(Square.Multiplier.CENTRE);
                } else {
                    board[i][j] = new Square(Square.Multiplier.NORMAL);
                }
            }
        }
        // double letter score multiplier indices
        int[][] double_ls = {{0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7},
                {3, 14}, {6, 2}, {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11},
                {8, 2}, {8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7}, {11, 14},
                {12, 6}, {12, 8}, {14, 3}, {14, 11}};
        // triple letter score multiplier indices
        int[][] triple_ls = {{1, 5}, {1, 9}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, {9, 1},
                {9, 5}, {9, 9}, {9, 13}, {13, 5}, {13, 9}};
        // double word score multiplier indices
        int[][] double_ws = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {10, 10}, {11, 11},
                {12, 12}, {13, 13}, {1, 13}, {2, 12}, {3, 11}, {4, 10}, {10, 4},
                {11, 3}, {12, 2}, {13, 1}};
        // triple word score multiplier indices
        int[][] triple_ws = {{0, 0}, {0, 7}, {0, 14}, {7, 0},
                {7, 14}, {14, 0}, {14, 7}, {14, 14}};

        // initialise multiplier squares on board
        for (int[] index : double_ls) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.DOUBLE_LS);
        }
        for (int[] index : triple_ls) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.TRIPLE_LS);
        }
        for (int[] index : double_ws) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.DOUBLE_WS);
        }
        for (int[] index : triple_ws) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.TRIPLE_WS);
        }
    }

    // temporary tests
    public static void main(String[] args) {
        Board b = new Board();
        b.placeTile('H', 8, Tile.makeTile('Z'));
        b.display();
    }

    /**
     * Accessor for the board.
     * Required for testing purposes.
     *
     * @return the 15x15 matrix of Squares representing the board
     */
    public Square[][] getBoard() {
        return board;
    }

    /**
     * Reset the board so that it contains 0 Tiles.
     */
    public void reset() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j].setTile(null);
            }
        }
    }

    /**
     * Displays the board and tiles (if any) on the command line.
     */
    public void display() {
        printLine();
        printColumnIndices();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print((i + 1) + "\t|");
                }
                System.out.print(board[i][j] + "|");
                if (j == 14) {
                    System.out.print("\t  " + (i + 1));
                }
            }
            printLine();
        }
        printColumnIndices();
    }

    /**
     * Retrieve a tile at a specified row and column on the board.
     *
     * @param column the specified column ('A' - 'O') on the board
     * @param row    the specified row (1 - 15) on the board
     * @return the Tile at a specified position on the board, null if empty
     * @throws IllegalArgumentException if specified row or column are out of bounds
     */
    public Tile getTile(char column, int row) throws IllegalArgumentException {
        column = Character.toUpperCase(column);
        // convert column and row to real indices(0 - 14)
        column -= 'A';
        row -= 1;
        if (!isValidSquare(column, row)) {
            throw new IllegalArgumentException("Square out of bounds.");
        }
        return board[row][column].getTile();
    }

    /**
     * Place a given Tile at a specified row and column on the Board.
     *
     * @param column character between 'A' - 'O' to specify the board column
     * @param row    integer between 1 - 15 to specify the board row
     * @param tile   the Tile to be placed at the specified index
     * @throws IllegalArgumentException if column or row is invalid, or when tile is occupied
     */
    public void placeTile(char column, int row, Tile tile) throws IllegalArgumentException {
        column = Character.toUpperCase(column);
        // Convert row and column to real indices (0 - 14)
        column -= 'A';
        row -= 1;
        if (!isValidSquare(column, row)) {
            throw new IllegalArgumentException("Square out of bounds.");
        }
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null.");
        }
        if (!isSquareEmpty(column, row)) {
            throw new IllegalArgumentException("Square is currently occupied.");
        }
        board[row][column].setTile(tile);
    }

    /**
     * Place a given word either vertically or horizontally starting at a
     * specified row and column on the board.
     *
     * @param column      character between 'A'-'O' to specify the board column
     * @param row         integer between 1-15 to specify the board row
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @throws IllegalArgumentException for illegal word placement
     */
    public void placeWord(char column, int row, char orientation, String word, Frame frame)
            throws IllegalArgumentException {
        // Input standardisation
        orientation = Character.toUpperCase(orientation);
        word = word.toUpperCase().trim();
        // convert column and row to real board indices (0 - 14)
        column -= 'A';
        row -= 1;
        if (!isWordPlacementValid(column, row, orientation, word, frame)) {
            throw new IllegalArgumentException("Invalid word placement");
        }
        // TODO: Place word
    }

    /**
     * Allows a word placement to be checked to determine if it is legal or not.
     *
     * @param column      integer between 0-14 to specify the real board column index
     * @param row         integer between 0-14 to specify the real board row index
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @return {@code true} if word placement is legal
     */
    public boolean isWordPlacementValid(int column, int row, char orientation, String word, Frame frame) {
        // Input standardisation
        orientation = Character.toUpperCase(orientation);
        word = word.toUpperCase().trim();
        // Convert column and row to real indices (0 - 14)
        column -= 'A';
        row -= 1;
        // Checks for input validity
        if (!isValidSquare(column, row) || (orientation != 'A' && orientation != 'D') ||
                word.length() < 2 || !isAlphaString(word) || frame == null) {
            return false;
        }
        // Checks for overflow
        if (isOverflowed(column, row, orientation, word.length())) {
            return false;
        }
        // Checks for conflicts with existing letters on the board
        if (doesWordConflict(column, row, orientation, word)) {
            return false;
        }
        // Checks if frame contains the required tiles
        if (!doesFrameContainTiles(column, row, orientation, word, frame)) {
            return false;
        }
        // Checks whether the placement uses at least one letter from frame
        if (!isFrameUsed(word, frame)) {
            return false;
        }
        // If first move checks if it covers the center square
        if (isFirstMove) {
            return doesWordCoverSquare(column, row, orientation, word.length(), 7, 7);
        } else {
            // If not first move checks if it connects with at least one existing character on the board
            return isWordJoined(column, row, orientation, word.length());
        }
    }


    /**
     * Checks if a specified square index is within the board.
     *
     * @param column integer between 0-14 to specify the real board column index
     * @param row    integer between 0-14 to specify the real board row index
     * @return {@code true} if the specified square index is valid
     */
    private boolean isValidSquare(int column, int row) {
        return column >= 0 && column < 15 && row >= 0 && row < 15;
    }

    /**
     * Checks if a word placement goes out of the board.
     *
     * @param column      the starting column index (0 - 14) of the word
     * @param row         the starting row index (0 - 14) of the word
     * @param orientation whether the word goes across or down
     * @param wordLength  the length of the word
     * @return {@code true} if the word placement goes out of the board
     */
    private boolean isOverflowed(int column, int row, char orientation, int wordLength) {
        if (orientation == 'A') {
            return (column + wordLength - 1) > 14;
        } else {
            return (row + wordLength - 1) > 14;
        }
    }

    /**
     * Checks if a word conflicts with any existing words on the board.
     *
     * @param column      the starting column index (0 - 14) of the word
     * @param row         the starting row index (0 - 14) of the word
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed
     * @return {@code true} if the word placement conflicts with existing words on board
     */
    private boolean doesWordConflict(int column, int row, char orientation, @NotNull String word) {
        char[] wordArray = word.toCharArray();
        // Checks the horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < word.length(); i++) {
                if (!isSquareEmpty(column, row) && board[row][column + i].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        } else {
            // Checks the vertical direction
            for (int i = 0; i < word.length(); i++) {
                if (!isSquareEmpty(column, row) && board[row + i][column].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the frame contains the letters necessary for word placement (ignores filled squares).
     *
     * @param column      integer between 0-14 to specify the board column
     * @param row         integer between 0-14 to specify the board row
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @return {@code true} if the frame contains all tiles needed
     */
    private boolean doesFrameContainTiles(int column, int row, char orientation, String word, @NotNull Frame frame) {
        StringBuilder sb = new StringBuilder();
        for (Tile t : frame.getFrame()) {
            sb.append(t.getType());
        }
        String tilesInFrame = sb.toString();
        // checks the horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < word.length(); i++) {
                String character = Character.toString(word.charAt(i));
                // return false if frame does not contain letter needed
                if (isSquareEmpty(column + i, row)) {
                    if (tilesInFrame.contains(character)) {
                        tilesInFrame = tilesInFrame.replaceFirst(character, "");
                    } else if (tilesInFrame.contains("-")) {
                        tilesInFrame = tilesInFrame.replaceFirst("-", "");
                    } else {
                        return false;
                    }
                }
            }
        } else {
            // checks the vertical direction
            for (int i = 0; i < word.length(); i++) {
                String character = Character.toString(word.charAt(i));
                // return false if frame does not contain letter needed
                if (isSquareEmpty(column, row + i)) {
                    if (tilesInFrame.contains(character)) {
                        tilesInFrame = tilesInFrame.replaceFirst(character, "");
                    } else if (tilesInFrame.contains("-")) {
                        tilesInFrame = tilesInFrame.replaceFirst("-", "");
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks that at least one letter from the frame is used.
     *
     * @param word  the word to be placed
     * @param frame the players frame
     * @return {@code true} if at least one letter from the frame is used
     * @throws IllegalArgumentException if word is empty or frame object is null
     */
    private boolean isFrameUsed(@NotNull String word, Frame frame) throws IllegalArgumentException {
        if (word.trim().equals("") || frame == null) {
            throw new IllegalArgumentException("Either word or frame is empty.");
        }
        for (char ch : word.toCharArray()) {
            if (frame.contains(ch) || frame.contains('-')) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the word to be placed covers a given square.
     *
     * @param column       integer between 0-14 to specify the board column
     * @param row          integer between 0-14 to specify the board row
     * @param orientation  whether the word goes across or down
     * @param wordLength   the length of the word
     * @param targetColumn the required column index to be covered
     * @param targetRow    the required row index to be covered
     * @return {@code true} if the word being placed covers the required square
     */
    private boolean doesWordCoverSquare(int column, int row, char orientation, int wordLength,
                                        int targetColumn, int targetRow) {
        if (orientation == 'A') {
            return row == targetRow &&
                    ((column + wordLength - 1) >= targetColumn);
        } else {
            return orientation == 'D' && column == targetColumn &&
                    ((row + wordLength - 1) >= targetRow);
        }
    }

    /**
     * Checks if a word is joined with any existing tiles.
     *
     * @param column      integer between 0-14 to specify the board column
     * @param row         integer between 0-14 to specify the board row
     * @param orientation whether the word goes across or down
     * @param wordLength  the length of the word
     * @return {@code true} if the word is joined with existing board tiles
     */
    private boolean isWordJoined(int column, int row, char orientation, int wordLength) {
        boolean isJoined = false;
        if (orientation == 'A') {
            for (int i = 0; i < wordLength; i++) {
                // if first letter check left
                if (i == 0 && isValidSquare(column - 1, row)) {
                    isJoined = !isSquareEmpty(column - 1, row);
                }
                // if last letter check right
                if (i == wordLength - 1 && isValidSquare(column + 1, row)) {
                    isJoined = !isSquareEmpty(column + 1, row);
                }
                // Check top
                if (isValidSquare(column, row - 1)) {
                    isJoined = !isSquareEmpty(column, row - 1);
                }
                // Check bottom
                if (isValidSquare(column, row + 1)) {
                    isJoined = !isSquareEmpty(column, row + 1);
                }
            }
        } else {
            // Check the vertical direction
            for (int i = 0; i < wordLength; i++) {
                // if first letter check top
                if (i == 0 && isValidSquare(column, row - 1)) {
                    isJoined = !isSquareEmpty(column, row - 1);
                }
                // if last letter check bottom
                if (i == wordLength - 1 && isValidSquare(column, row + 1)) {
                    isJoined = !isSquareEmpty(column + 1, row);
                }
                // Check left
                if (isValidSquare(column - 1, row)) {
                    isJoined = !isSquareEmpty(column - 1, row);
                }
                // Check right
                if (isValidSquare(column + 1, row)) {
                    isJoined = !isSquareEmpty(column + 1, row);
                }
            }
        }
        return isJoined;
    }


    /**
     * Checks if a given square doesn't contain a tile.
     *
     * @param column integer between 0-14 to specify the board column
     * @param row    integer between 0-14 to specify the board row
     * @return {@code true} if the given square is empty
     */
    private boolean isSquareEmpty(int column, int row) {
        return board[row][column].getTile() == null;
    }

    /**
     * Checks if a string only contains alphabetical characters
     *
     * @param input give string
     * @return {@code true} if the given string only contains alphabetical characters
     */
    @Contract(pure = true)
    private boolean isAlphaString(@NotNull String input) {
        return input.matches("[A-Za-z]+");
    }

    /**
     * Display a line of dashes, used to display the Board.
     */
    private void printLine() {
        System.out.println();
        for (int i = 0; i < 100; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Display the column indices 'A' - 'O' on the board
     */
    private void printColumnIndices() {
        System.out.print("\t|");
        for (char ch = 'A'; ch <= 'O'; ch++) {
            System.out.print("  " + ch + "  |");
        }
        printLine();
    }

}
