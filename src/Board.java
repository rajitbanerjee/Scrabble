/**
 * The Board is a 15x15 matrix of Squares.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class Board {
    Square[][] board = new Square[15][15];

    /**
     * The constructor loops through every Square in the Board
     * and sets the multiplier that a particular index may have.
     */
    public Board() {
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
     * Place a given word either vertically or horizontally at a specified
     * row and column on the board
     *
     * @param column character between A-O to specify the board column
     * @param row    integer between 1-15 to specify the board row
     * @param orientation whether the word goes across or down
     * @param word  the word to be placed on the board
     * @param frame the players frame
     */
    private void placeWord(char column, int row, char orientation, String word, Frame frame) {
        word = word.toUpperCase();
        orientation = Character.toUpperCase(orientation);
        if(frameContainsALetter(word, frame) && !doesBoardConflict(column, row, orientation, word)){
            //check if frame contains sufficient tiles
        }else{
            //the word placement is invalid
        }
    }

    private boolean doesBoardConflict(char column, int row, char orientation, String word) {
        char [] wordArray = word.toCharArray();
        int wordLength = word.length();
        if(orientation == 'A'){
            for(int i = 0 ; i < wordLength ; i++){
                if(board[column - 'A' + i][row].getTile() != null &&
                        board[column - 'A' + i][row].getTile().getType() != wordArray[i]){
                    return true;
                }
            }
        }else{
            for(int i = 0 ; i < wordLength ; i++){
                if(board[column - 'A'][row + i].getTile() != null &&
                        board[column - 'A'][row + i].getTile().getType() != wordArray[i]){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks that at least one letter from the frame is used
     * @param word  the word to be placed
     * @param frame the players frame
     * @return  true if at least one letter from the frame is used
     */
    private boolean frameContainsALetter (String word, Frame frame){
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
     * @throws IllegalArgumentException if column or row is invalid
     */
    public void placeTile(char column, int row, Tile tile)
            throws IllegalArgumentException {
        column = Character.toUpperCase(column);
        if (column < 'A' || column > 'O') {
            throw new IllegalArgumentException("Illegal column index.");
        }
        if (row <= 0 || row > 15) {
            throw new IllegalArgumentException("Illegal row index.");
        }
        board[row - 1][column - 'A'].setTile(tile);
    }

    /**
     * Retrieve a Tile at a specified row and column on the board.
     *
     * @param column the specified column on the board
     * @param row    the specified row on the board
     * @return the Tile at a specified position on the board
     * @throws IllegalArgumentException if specified row or column are out of bounds
     */
    public Tile getTile(char column, int row)
            throws IllegalArgumentException {
        column = Character.toUpperCase(column);
        if (column < 'A' || column > 'O') {
            throw new IllegalArgumentException("Illegal column index.");
        }
        if (row <= 0 || row > 15) {
            throw new IllegalArgumentException("Illegal row index.");
        }
        return board[row - 1][column - 'A'].getTile();
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

    // temporary tests
    public static void main(String[] args) {
        Board b = new Board();
        b.placeTile('A', 1, new Tile('Z', 10));
        b.display();
    }

}
