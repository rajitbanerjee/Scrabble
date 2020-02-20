import java.util.Scanner;

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
        // IsFirstMove is true when a Board is created; set to false after first word is placed
        isFirstMove = true;
        // initialise multiplier squares on board
        int centre = Constants.BOARD_SIZE / 2; // 15/2 = 7
        board[centre][centre] = new Square(Constants.MULTIPLIER.CENTRE);
        for (int[] index : Constants.NORMAL_SQ_ARRAY) {
            board[index[0]][index[1]] = new Square(Constants.MULTIPLIER.NORMAL);
        }
        for (int[] index : Constants.DOUBLE_LS_ARRAY) {
            board[index[0]][index[1]] = new Square(Constants.MULTIPLIER.DOUBLE_LS);
        }
        for (int[] index : Constants.TRIPLE_LS_ARRAY) {
            board[index[0]][index[1]] = new Square(Constants.MULTIPLIER.TRIPLE_LS);
        }
        for (int[] index : Constants.DOUBLE_WS_ARRAY) {
            board[index[0]][index[1]] = new Square(Constants.MULTIPLIER.DOUBLE_WS);
        }
        for (int[] index : Constants.TRIPLE_WS_ARRAY) {
            board[index[0]][index[1]] = new Square(Constants.MULTIPLIER.TRIPLE_WS);
        }
    }

    // temporary tests
    public static void main(String[] args) {
        Board b = new Board();
        Pool pool = new Pool();
        Player player = new Player("name", new Frame(pool));

        player.getFrame().printFrame();
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter a word: ");
        String s = in.nextLine();

        b.placeWord('H', 8, 'A', s, player.getFrame());
        b.display();
        player.getFrame().printFrame();
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
     * Mutator for isFirstMove.
     * Should be used after first move is completed to change isFirstMove to false.
     *
     * @param firstMove false after first move has been made
     */
    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    /**
     * Reset the board so that it contains 0 Tiles.
     */
    public void reset() {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
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
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
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
     * Place a given word either vertically or horizontally starting at a
     * specified row and column on the board.
     *
     * @param column      character between 'A' - 'O' to specify the board column
     * @param row         integer between 1 - 15 to specify the board row
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @throws IllegalArgumentException for illegal word placement
     */
    public void placeWord(char column, int row, char orientation, String word, Frame frame)
            throws IllegalArgumentException {
        if (!isWordPlacementValid(column, row, orientation, word, frame)) {
            throw new IllegalArgumentException("Invalid word placement");
        }
        // Input standardisation
        orientation = Character.toUpperCase(orientation);
        word = word.toUpperCase().trim();
        // convert column and row to real board indices (0 - 14)
        column -= 'A';
        row -= 1;
        //place word
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            // Checks the horizontal direction
            if (orientation == 'A') {
                if (board[column + i][row].getTile() == null) {
                    placeTile(column + i, row, frame.getTile(ch));
                    frame.remove(ch);
                }
            } else {
                // Checks the vertical direction
                if (board[column][row + i].getTile() == null) {
                    placeTile(column, row + i, frame.getTile(ch));
                    frame.remove(ch);
                }
            }
        }
    }

    /**
     * Allows a word placement to be checked to determine if it is legal or not.
     *
     * @param column      character between 'A' - 'O' to specify the real board column index
     * @param row         integer between 1 - 15 to specify the real board row index
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @return {@code true} if word placement is legal
     */
    public boolean isWordPlacementValid(char column, int row, char orientation, String word, Frame frame) {
        // Input standardisation
        orientation = Character.toUpperCase(orientation);
        word = word.toUpperCase().trim();
        // convert column and row to real board indices (0 - 14)
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
        if (!isFrameUsed(column, row, orientation, word, frame)) {
            return false;
        }
        // If first move, checks if it covers the centre square
        if (isFirstMove) {
            return doesWordCoverCentre(column, row, orientation, word.length());
        } else {
            // If not first move, checks if word connects with an existing word on board
            return isWordJoined(column, row, orientation, word.length());
        }
    }

    /**
     * Place a given Tile at a specified row and column on the Board.
     *
     * @param column integer between 0 - 14 to specify the board column
     * @param row    integer between 0 - 14 to specify the board row
     * @param tile   the Tile to be placed at the specified index
     */
    private void placeTile(int column, int row, Tile tile) {
        board[row][column].setTile(tile);
    }

    /**
     * Checks if a specified square index is within the board.
     *
     * @param column integer between 0 - 14 to specify the real board column index
     * @param row    integer between 0 - 14 to specify the real board row index
     * @return {@code true} if the specified square index is valid
     */
    private boolean isValidSquare(int column, int row) {
        return column >= 0 && column < Constants.BOARD_SIZE && row >= 0 && row < Constants.BOARD_SIZE;
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
            // Checks the horizontal direction
            return (column + wordLength - 1) >= Constants.BOARD_SIZE;
        } else {
            // Checks the vertical direction
            return (row + wordLength - 1) >= Constants.BOARD_SIZE;
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
    private boolean doesWordConflict(int column, int row, char orientation, String word) {
        char[] wordArray = word.toCharArray();
        // Checks the horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < word.length(); i++) {
                if (!isSquareEmpty(column + i, row) &&
                        board[row][column + i].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        } else {
            // Checks the vertical direction
            for (int i = 0; i < word.length(); i++) {
                if (!isSquareEmpty(column, row + i) &&
                        board[row + i][column].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the frame contains the letters necessary for word placement (ignores filled squares).
     *
     * @param column      integer between 0 - 14 to specify the board column
     * @param row         integer between 0 - 14 to specify the board row
     * @param orientation whether the word goes across or down
     * @param word        the word to be placed on the board
     * @param frame       the players frame
     * @return {@code true} if the frame contains all tiles needed
     */
    private boolean doesFrameContainTiles(int column, int row, char orientation, String word, Frame frame) {
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
     */
    private boolean isFrameUsed(int column, int row, char orientation, String word, Frame frame) {
        int i = 0;
        if (orientation == 'A') {
            for (char ch : word.toCharArray()) {
                if (isSquareEmpty(column + i, row)) {
                    if (frame.contains(ch) || frame.contains('-')) {
                        return true;
                    }
                }
                i++;
            }
        } else {
            for (char ch : word.toCharArray()) {
                if (isSquareEmpty(column, row + i)) {
                    if (frame.contains(ch) || frame.contains('-')) {
                        return true;
                    }
                }
                i++;
            }
        }
        return false;
    }

    /**
     * Checks if the word to be placed covers the centre square H8 (aka 7, 7).
     *
     * @param column      integer between 0 - 14 to specify the board column
     * @param row         integer between 0 - 14 to specify the board row
     * @param orientation whether the word goes across or down
     * @param wordLength  the length of the word
     * @return {@code true} if the word being placed covers the centre square
     */
    private boolean doesWordCoverCentre(int column, int row, char orientation, int wordLength) {
        if (orientation == 'A') {
            // Checks the horizontal direction
            return row == Constants.BOARD_SIZE / 2 &&
                    ((column + wordLength - 1) >= Constants.BOARD_SIZE / 2);
        } else {
            // Checks the vertical direction
            return orientation == 'D' && column == Constants.BOARD_SIZE / 2 &&
                    ((row + wordLength - 1) >= Constants.BOARD_SIZE / 2);
        }
    }

    /**
     * Checks if a word is joined with any existing tiles.
     *
     * @param column      integer between 0 - 14 to specify the board column
     * @param row         integer between 0 - 14 to specify the board row
     * @param orientation whether the word goes across or down
     * @param wordLength  the length of the word
     * @return {@code true} if the word is joined with existing board tiles
     */
    private boolean isWordJoined(int column, int row, char orientation, int wordLength) {
        // Checks the horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < wordLength; i++) {
                // Check top
                if (isValidSquare(column + i, row - 1)) {
                    if (!isSquareEmpty(column + i, row - 1)) {
                        return true;
                    }
                }
                // Check bottom
                if (isValidSquare(column + i, row + 1)) {
                    if (!isSquareEmpty(column + i, row + 1)) {
                        return true;
                    }
                }
                //check if the word contains tiles already on the board
                if (isValidSquare(column + i, row)) {
                    if (!isSquareEmpty(column + i, row)) {
                        return true;
                    }
                }
            }
        } else {
            // Check the vertical direction
            for (int i = 0; i < wordLength; i++) {
                // Check left
                if (isValidSquare(column - 1, row + i)) {
                    if (!isSquareEmpty(column - 1, row + i)) {
                        return true;
                    }
                }
                // Check right
                if (isValidSquare(column + 1, row + i)) {
                    if (!isSquareEmpty(column + 1, row + i)) {
                        return true;
                    }
                }
                //check if the word contains letter already on the board
                if (isValidSquare(column, row + i)) {
                    if (!isSquareEmpty(column, row + i)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if a given square doesn't contain a tile.
     *
     * @param column integer between 0 - 14 to specify the board column
     * @param row    integer between 0 - 14 to specify the board row
     * @return {@code true} if the given square is empty
     */
    private boolean isSquareEmpty(int column, int row) {
        return board[row][column].getTile() == null;
    }

    /**
     * Checks if a string only contains alphabetical characters.
     *
     * @param input give string
     * @return {@code true} if the given string only contains alphabetical characters
     */
    private boolean isAlphaString(String input) {
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
     * Display the column indices 'A' - 'O' on the board.
     */
    private void printColumnIndices() {
        System.out.print("\t|");
        for (char ch = 'A'; ch <= 'O'; ch++) {
            System.out.print("  " + ch + "  |");
        }
        printLine();
    }

}
