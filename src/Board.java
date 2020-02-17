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
        isFirstMove = false;
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
        b.placeTile('H', 8, new Tile('Z', 10));
        b.display();
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
     * Allows a word placement to be checked to determine if it is legal or not
     */
    public boolean isWordValid(char column, int row, char orientation, String word, Frame frame) {
        // Input conversion
        orientation = Character.toUpperCase(orientation);
        word = word.toUpperCase();
        word = word.trim();
        row -= 1;
        // Checks for input validity
        if (!isValidSquare(column, row) || (orientation != 'A' && orientation != 'D') || word.length() < 2
                || frame == null) {
            return false;
        }
        // Checks for overflow
        if (isOverflowed(column, row, orientation, word.length())) {
            return false;
        }
        // Checks for conflicts with existing letters on the board
        if (doesBoardConflict(column, row, orientation, word)) {
            return false;
        }
        // Checks if frame contains the required tiles
        if (!doesFrameContainTiles(column, row, orientation, word, frame)) {
            return false;
        }
        // Checks whether the placement uses at least one letter from frame
        if (!frameContainsALetter(word, frame)) {
            return false;
        }
        // If first move checks if it covers the center square
        if (isFirstMove) {
            return doesWordCoverSquare(column, row, orientation, word.length(), 'H', 7);
        } else {
            // If not first move checks if it connects with at least one existing character on the board
            return isWordJoined(column, row, orientation, word.length());
        }
    }

    /**
     * Place a given word either vertically or horizontally at a specified
     * row and column on the board
     *
     * @param column      character between A-O to specify the board column
     * @param row         integer between 1-15 to specify the board row
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @throws IllegalArgumentException for invalid column, row, orientation or word
     */
    public void placeWord(char column, int row, char orientation, String word, Frame frame)
            throws IllegalArgumentException {
        // Input conversion
        orientation = Character.toUpperCase(orientation);
        word = word.toUpperCase();
        word = word.trim();
        row -= 1;
        if (!isWordValid(column, row, orientation, word, frame)) {
            throw new IllegalArgumentException("Invalid word placement");
        }
        // TODO: Place word
    }

    /**
     * checks if the frame contains the correct letters necessary for this word placement (ignores filled squares)
     *
     * @param column      character between A-O to specify the board column
     * @param row         integer between 0-14 to specify the board row
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @return true if the frame contains all tiles needed
     */
    private boolean doesFrameContainTiles(char column, int row, char orientation, String word, Frame frame) {
        int wordLength = word.length();
        // Checks the horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < wordLength; i++) {
                // return false if frame does not contain letter needed
                if (isEmpty((char) (column + (char) i), row) && !frame.isLetterInFrame(word.charAt(i))) {
                    return false;
                }
            }
        } else {
            // checks the vertical direction
            for (int i = 0; i < wordLength; i++) {
                // return false if frame does not contain letter needed
                if (isEmpty(column, row + i) && !frame.isLetterInFrame(word.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * If a word is joined with any existing characters, return true, false otherwise
     */
    private boolean isWordJoined(char column, int row, char orientation, int wordLength) {
        boolean isJoined = false;
        if (orientation == 'A') {
            for (int i = 0; i < wordLength; i++) {
                // if first letter check left
                if (i == 0 && isValidSquare((char) (column - 1), row)) {
                    isJoined = !isEmpty((char) (column - 1), row);
                }
                // if last letter check right
                if (i == wordLength - 1 && isValidSquare((char) (column + 1), row)) {
                    isJoined = !isEmpty((char) (column + 1), row);
                }
                // Check top
                if (isValidSquare(column, row - 1)) {
                    isJoined = !isEmpty(column, row - 1);
                }
                // Check bottom
                if (isValidSquare(column, row + 1)) {
                    isJoined = !isEmpty(column, row + 1);
                }
            }
        } else {
            for (int i = 0; i < wordLength; i++) {
                // if first letter check top
                if (i == 0 && isValidSquare(column, row - 1)) {
                    isJoined = !isEmpty(column, row - 1);
                }
                // if last letter check bottom
                if (i == wordLength - 1 && isValidSquare(column, row + 1)) {
                    isJoined = !isEmpty((char) (column + 1), row);
                }
                // Check left
                if (isValidSquare((char) (column - 1), row)) {
                    isJoined = !isEmpty((char) (column - 1), row);
                }
                // Check right
                if (isValidSquare((char) (column + 1), row)) {
                    isJoined = !isEmpty((char) (column + 1), row);
                }
            }
        }
        return isJoined;
    }


    /**
     * Checks if a word conflicts with any existing words on the board
     */
    private boolean doesBoardConflict(char column, int row, char orientation, String word)
            throws IllegalArgumentException {
        // Checks if the supplied orientation is valid
        column = Character.toUpperCase(column);
        orientation = Character.toUpperCase(orientation);
        // change row from board position to actual index starting at 0
        row -= 1;
        if (!isValidSquare(column, row)) {
            throw new IllegalArgumentException("Square out of bounds.");
        }

        if ((orientation != 'A' && orientation != 'D') || word == null || word.trim().equals("")) {
            throw new IllegalArgumentException("Word cannot be placed.");
        }

        char[] wordArray = word.toCharArray();
        int wordLength = word.length();
        // Checks the horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < wordLength; i++) {
                if (!isEmpty(column, row) && board[row][column - 'A' + i].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        } else {
            // checks the vertical direction
            for (int i = 0; i < wordLength; i++) {
                if (!isEmpty(column, row) && board[row + i][column - 'A'].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Checks that at least one letter from the frame is used.
     *
     * @param word  the word to be placed
     * @param frame the players frame
     * @return true if at least one letter from the frame is used
     * @throws IllegalArgumentException if word is empty or frame object is null
     */
    private boolean frameContainsALetter(String word, Frame frame) throws IllegalArgumentException {
        if (word.trim().equals("") || frame == null) {
            throw new IllegalArgumentException("Either word or frame is empty.");
        }
        for (char ch : word.toCharArray()) {
            if (frame.isLetterInFrame(ch)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Place a given Tile at a specified row and column on the Board.
     *
     * @param column character between A-O to specify the board column
     * @param row    integer between 1-15 to specify the board row
     * @param tile   the Tile to be placed at the specified index
     * @throws IllegalArgumentException if column or row is invalid, or when tile is occupied
     */
    public void placeTile(char column, int row, Tile tile) throws IllegalArgumentException {
        column = Character.toUpperCase(column);
        row -= 1;
        if (!isValidSquare(column, row)) {
            throw new IllegalArgumentException("Square out of bounds.");
        }
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null.");
        }
        if (!isEmpty(column, row)) {
            throw new IllegalArgumentException("Square is currently occupied.");
        }
        board[row][column - 'A'].setTile(tile);
    }

    /**
     * Retrieve a Tile at a specified row and column on the board, return null if square is empty.
     *
     * @param column the specified column on the board
     * @param row    the specified row on the board
     * @return the Tile at a specified position on the board
     * @throws IllegalArgumentException if specified row or column are out of bounds
     */
    public Tile getTile(char column, int row) throws IllegalArgumentException {
        column = Character.toUpperCase(column);
        row -= 1;
        if (!isValidSquare(column, row)) {
            throw new IllegalArgumentException("Square out of bounds.");
        }
        return board[row][column - 'A'].getTile();
    }

    /**
     * Displays the board and tiles (if any) on the command line.
     */
    public void display() {
        printLine();
        System.out.print("\t|");
        for (char ch = 'A'; ch <= 'O'; ch++) {
            System.out.print("  " + ch + "  |");
        }
        printLine();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print((i + 1) + "\t|");
                }
                System.out.print(board[i][j] + "|");
            }
            printLine();
        }
    }

    /**
     * Display a line of dashes, used to display the Board.
     */
    private void printLine() {
        System.out.println();
        for (int i = 0; i < 95; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    // Accepts real index (0 - 14)
    private boolean isValidSquare(char column, int row) {
        column = Character.toUpperCase(column);
        return column >= 'A' && column <= 'O' && row >= 0 && row < 15;
    }

    // Accepts real index (0 - 14)
    private boolean isEmpty(char column, int row) {
        column = Character.toUpperCase(column);
        return board[row][column - 'A'].getTile() == null;
    }

    // Accepts real index (0 - 14)
    private boolean isOverflowed(char columnStart, int rowStart, char orientation, int wordLength) {
        if (orientation == 'A') {
            return (columnStart + wordLength - 1) > 'O';
        }
        return (rowStart + wordLength - 1) > 14;
    }

    // Accepts real index (0 - 14)
    private boolean doesWordCoverSquare(char columnStart, int rowStart, char orientation, int wordLength,
                                        char targetColumn, int targetRow) {

        int columnStartIndex = columnStart - 'A';
        int targetColumnIndex = targetColumn - 'A';
        if (orientation == 'A') {
            return rowStart == targetRow &&
                    ((columnStartIndex + wordLength - 1) >= targetColumnIndex);
        } else {
            return orientation == 'D' && columnStart == targetColumn &&
                    ((rowStart + wordLength - 1) >= targetRow);
        }
    }
}
