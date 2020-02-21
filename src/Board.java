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
        setFirstMove(true);
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
        Frame frame = new Frame(pool);

        Scanner sc = new Scanner(System.in);
        System.out.println("\nWelcome to Scrabble by DarkMode.");
        System.out.print("Please enter you name: ");
        String name = sc.nextLine();
        System.out.printf("\nWelcome %s! This is Scrabble in single player mode.", name);
        new Player(name, frame);

        String move;
        while (true) {
            b.display();
            System.out.print("\nFrame: ");
            frame.printFrame();
            System.out.println("Enter your move (E.g. \"H8 A HELLO\" or \"H8 D HI\"), (q/Q to exit): ");
            move = sc.nextLine().trim().toUpperCase();

            if (move.trim().equalsIgnoreCase("q")) {
                break;
            }

            char column = move.charAt(0);
            int row = (move.charAt(1)) - '0';
            char ori = (move.substring(move.indexOf(' ') + 1, move.lastIndexOf(' '))).charAt(0);
            String word = move.substring(move.lastIndexOf(' ') + 1);
            while (!b.isWordPlacementValid(column, row, ori, word, frame)) {
                System.out.println("Invalid word placement! Try again.");
                System.out.println("Enter your move (E.g. \"H8 A HELLO\" or \"H8 D HI\"), (q/Q to exit): ");
                move = sc.nextLine().trim().toUpperCase();

                if (move.trim().equalsIgnoreCase("q")) {
                    break;
                }

                column = move.charAt(0);
                row = (move.charAt(1)) - '0';
                ori = (move.substring(move.indexOf(' ') + 1, move.lastIndexOf(' '))).charAt(0);
                word = move.substring(move.lastIndexOf(' ') + 1);
            }
            b.placeWord(column, row, ori, word, frame);
            System.out.println("-------------------------");
            System.out.println("Word placed: " + word);
            System.out.println("-------------------------");
            try {
                System.out.print("Frame: ");
                frame.printFrame();
                System.out.print("Refilled frame: ");
                frame.fillFrame();
                frame.printFrame();
                pool.printSize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nThanks for playing!");
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
        setFirstMove(true);
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
                    System.out.printf("| %d\t|", (i + 1));
                }
                System.out.print(board[i][j] + "|");
                if (j == 14) {
                    if (i > 8) {
                        System.out.printf("\t %d|", (i + 1));
                    } else {
                        System.out.printf("\t %d |", (i + 1));
                    }
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
        // Convert column and row to real indices(0 - 14)
        column -= 'A';
        row -= 1;
        if (!Square.isValid(column, row)) {
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
        // If isFirstMove is true, set it to false
        setFirstMove(false);
        // Input standardisation
        orientation = Character.toUpperCase(orientation);
        word = word.toUpperCase().trim();
        // Convert column and row to real board indices (0 - 14)
        column -= 'A';
        row -= 1;
        // Place word
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            // Checks the horizontal direction
            if (orientation == 'A') {
                // Ignore filled squares
                if (board[row][column + i].isEmpty()) {
                    // Convert blank tile to a given letter if letter is not in the frame
                    if (!frame.contains(ch)) {
                        placeTile(column + i, row, Tile.makeTile(ch));
                        frame.remove('-');
                    } else {
                        // Place tile on the board and remove it from the frame
                        placeTile(column + i, row, frame.getTile(ch));
                        frame.remove(ch);
                    }
                }
            } else {
                // Checks the vertical direction
                // Ignore filled squares
                if (board[row + i][column].isEmpty()) {
                    // Convert blank tile to a given letter if letter is not in the frame
                    if (!frame.contains(ch)) {
                        placeTile(column, row + i, Tile.makeTile(ch));
                        frame.remove('-');
                    } else {
                        // Place tile on the board and remove it from the frame
                        placeTile(column, row + i, frame.getTile(ch));
                        frame.remove(ch);
                    }
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
        // Convert column and row to real board indices (0 - 14)
        column -= 'A';
        row -= 1;
        // Checks for input validity
        if (!Square.isValid(column, row) || (orientation != 'A' && orientation != 'D') ||
                word.length() < 2 || !isAlphaString(word) || frame == null) {
            return false;
        }
        // Checks for overflow
        if (isOverflowed(column, row, orientation, word.length())) {
            System.out.println("\nOverflowed");
            return false;
        }
        // Checks for conflicts with existing letters on the board
        if (doesWordConflict(column, row, orientation, word)) {
            System.out.println("\nWord conflicts");
            return false;
        }
        // Checks if frame contains the required tiles
        if (!doesFrameContainTiles(column, row, orientation, word, frame)) {
            System.out.println("\nFrame doesn't contain tiles");
            return false;
        }
        // Checks whether the placement uses at least one letter from frame
        if (!isFrameUsed(column, row, orientation, word, frame)) {
            System.out.println("\nFrame not used");
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
     * FIXME
     *         Existing word GETS
     *         Extension input should be GETSET, not just SET
     *         Currently SET is allowed
     *
     */
    private boolean doesWordConflict(int column, int row, char orientation, String word) {
        char[] wordArray = word.toCharArray();
        // Checks the horizontal direction
        if (orientation == 'A') {
            //check if the squares before and after the word are empty
            if(!board[row][column - 1].isEmpty() || !board[row][column + word.length()].isEmpty()){
                return true;
            }
            for (int i = 0; i < word.length(); i++) {
                // Conflict occurs if a square is filled but tile does not match letters in the placed word
                if (!board[row][column + i].isEmpty() &&
                        board[row][column + i].getTile().getType() != wordArray[i]) {
                    return true;
                }
            }
        } else {
            //check if the squares before and after the word are empty
            if(!board[row - 1][column].isEmpty() || !board[row + word.length()][column].isEmpty()){
                return true;
            }
            // Checks the vertical direction
            for (int i = 0; i < word.length(); i++) {
                // Conflict occurs if a square is filled but tile does not match letters in the placed word
                if (!board[row + i][column].isEmpty() &&
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
        // Create StringBuilder instance for String concatenation
        StringBuilder sb = new StringBuilder();
        // Append every character in the frame to a String
        for (Tile t : frame.getFrame()) {
            sb.append(t.getType());
        }
        String tilesInFrame = sb.toString();
        // Checks the horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < word.length(); i++) {
                String character = Character.toString(word.charAt(i));
                // Return false if frame does not contain letter needed
                if (board[row][column + i].isEmpty()) {
                    // Check for the specified character from the frame
                    if (tilesInFrame.contains(character)) {
                        tilesInFrame = tilesInFrame.replaceFirst(character, "");
                    } else if (tilesInFrame.contains("-")) {
                        // Else check for any blank characters from the frame
                        tilesInFrame = tilesInFrame.replaceFirst("-", "");
                    } else {
                        return false;
                    }
                }
            }
        } else {
            // Checks the vertical direction
            for (int i = 0; i < word.length(); i++) {
                String character = Character.toString(word.charAt(i));
                // Return false if frame does not contain letter needed
                if (board[row + i][column].isEmpty()) {
                    // Check for the specified character from the frame
                    if (tilesInFrame.contains(character)) {
                        tilesInFrame = tilesInFrame.replaceFirst(character, "");
                    } else if (tilesInFrame.contains("-")) {
                        // Else check for any blank characters from the frame
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
        // Checks for horizontal direction
        if (orientation == 'A') {
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                // If square is empty, check if frame contains the required Tile or a Blank Tile
                if (board[row][column + i].isEmpty()) {
                    if (frame.contains(ch) || frame.contains('-')) {
                        return true;
                    }
                }
            }
        } else {
            // Checks for vertical direction
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                // If square is empty, check if frame contains the required Tile or a Blank Tile
                if (board[row + i][column].isEmpty()) {
                    if (frame.contains(ch) || frame.contains('-')) {
                        return true;
                    }
                }
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
            return (row == Constants.BOARD_SIZE / 2) &&
                    ((column + wordLength - 1) >= Constants.BOARD_SIZE / 2) &&
                    (column <= Constants.BOARD_SIZE / 2);
        } else {
            // Checks the vertical direction
            return (column == Constants.BOARD_SIZE / 2) &&
                    ((row + wordLength - 1) >= Constants.BOARD_SIZE / 2) &&
                    (row <= Constants.BOARD_SIZE / 2);
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
            // Check the vertical direction
            for (int i = 0; i < wordLength; i++) {
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
        return false;
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
        for (int i = 0; i < 76; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Display the column indices 'A' - 'O' on the board.
     */
    private void printColumnIndices() {
        System.out.print("|\t|");
        for (char ch = 'A'; ch <= 'O'; ch++) {
            System.out.printf(" %c |", ch);
        }
        System.out.print("\t   |");
        printLine();
    }

}
