import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Board class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testGetBoard() {
        assertNotEquals(null, board.getBoard());
    }

    @Test
    void testBoardDimensions() {
        Square[][] b = board.getBoard();
        assertEquals(15, b.length);
        assertEquals(15, b[0].length);
    }

    @Test
    void testConstructor() {
        Square[][] b = board.getBoard();
        
        // check if multipliers are set correctly
        for (int[] index : Constants.DOUBLE_LS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.DOUBLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.TRIPLE_LS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.TRIPLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.DOUBLE_WS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.DOUBLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.TRIPLE_WS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.TRIPLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.NORMAL_SQ_ARRAY) {
            assertEquals(Constants.MULTIPLIER.NORMAL, b[index[0]][index[1]].getMultiplier());
        }
        assertEquals(Constants.MULTIPLIER.CENTRE, b[7][7].getMultiplier());
    }

    @Test
    void testReset() {
        Square[][] b = board.getBoard();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                b[i][j].setTile(Tile.makeTile('Z'));
            }
        }
        board.reset();
        b = board.getBoard();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (b[i][j].getTile() != null) {
                    fail("reset() doesn't work as expected");
                }
            }
        }
    }

    @Test
    void testGetTile() {
        Square[][] b = board.getBoard();
        Tile tile = Tile.makeTile('Z');
        for (char column = 'A'; column <= 'O'; column++) {
            for (int row = 1; row <= Constants.BOARD_SIZE; row++) {
                tile.setType((char) ((int) (Math.random() * 26) + 'A'));
                b[row - 1][column - 'A'].setTile(tile);
                assertEquals(tile, board.getTile(column, row));
            }
        }

        // test if exception is thrown for square out of bounds
        try {
            board.getTile('A', 16);
            board.getTile('P', 14);
            board.getTile('B', 0);
            fail("Queried tile out of bounds.");
        } catch (Exception ignored) {
            // test passed
        }
    }

    @Test
    void testWordPlacementValidity() {
        Pool pool = new Pool();
        Frame frame = new Frame(pool);
        ArrayList<Tile> f = new ArrayList<>();
        for (char ch : "HELLO".toCharArray()) {
            f.add(Tile.makeTile(ch));
        }
        frame.setFrame(f);

        //test for isValid()
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('R', 7, 'A', "HELLO", frame));
        assertFalse(board.isWordPlacementValid((char) ('A' - 1), 7, 'A', "HELLO", frame));
        assertFalse(board.isWordPlacementValid('H', -1, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 16, 'A', "Hello", frame));

        //test for correct orientation input
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'D', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'a', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'd', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'r', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'q', "Hello", frame));

        //testing for too short a word
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "H", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "He", frame));

        //test for alphabetic word
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "123", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "AB?", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));

        //test for null frame object
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "123", null));

        //test to check if first move covers the centre square
        board.reset();
        //horizontal placement
        //TODO Fix error in doesWordCoverCentre()
//        assertTrue(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));
//        assertFalse(board.isWordPlacementValid('I', 8, 'A', "Hello", frame));
//        assertFalse(board.isWordPlacementValid('A', 8, 'A', "Hello", frame));
//        //vertical placement
//        assertTrue(board.isWordPlacementValid('H', 8, 'D', "Hello", frame));
//        assertTrue(board.isWordPlacementValid('H', 6, 'D', "Hello", frame));
//        assertFalse(board.isWordPlacementValid('H', 9, 'D', "Hello", frame));


        //test for isWordJoined()
        ArrayList<Tile> f1 = new ArrayList<>();
        for (char ch : "ABCD".toCharArray()) {
            f1.add(Tile.makeTile(ch));
        }
        frame.setFrame(f1);
        board.setFirstMove(false);
        board.getBoard()[7][('H' - 'A')].setTile(new Tile('X', 8));
        //valid horizontal placements
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "XCD", frame));
        assertTrue(board.isWordPlacementValid('G', 8, 'A', "AXCD", frame));
        assertTrue(board.isWordPlacementValid('F', 7, 'A', "ABCD", frame));
        assertTrue(board.isWordPlacementValid('G', 9, 'A', "ABCD", frame));
        //valid vertical placements
        assertTrue(board.isWordPlacementValid('H', 8, 'D', "XCD", frame));
        assertTrue(board.isWordPlacementValid('H', 7, 'D', "AXCD", frame));
        assertTrue(board.isWordPlacementValid('G', 6, 'D', "ABCD", frame));
        assertTrue(board.isWordPlacementValid('I', 8, 'D', "ABCD", frame));
        //invalid placements
        assertFalse(board.isWordPlacementValid('H', 12, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('H', 5, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('I', 7, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('I', 9, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('A', 0, 'D', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('A', 7, 'D', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('G', 9, 'D', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('I', 9, 'D', "ABCD", frame));

        //test for isOverflowed()
        board.getBoard()[7][('O' - 'A')].setTile(new Tile('X', 8));    //place tile at edge of board
        board.getBoard()[14][('H' - 'A')].setTile(new Tile('X', 8));    //place tile at edge of board
        assertTrue(board.isWordPlacementValid('K', 8, 'A', "ABCDX", frame));
        assertTrue(board.isWordPlacementValid('H', 11, 'D', "ABCDX", frame));
        assertFalse(board.isWordPlacementValid('O', 9, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('N', 6, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('I', 15, 'D', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('G', 13, 'D', "ABCD", frame));

        //test for doesWordConflict()
        board.reset();
        board.getBoard()[7][('H' - 'A')].setTile(new Tile('X', 8));
        board.setFirstMove(false);
        //word does not conflict
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "XBCD", frame));
        assertTrue(board.isWordPlacementValid('G', 8, 'A', "AXCD", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'D', "XBCD", frame));
        assertTrue(board.isWordPlacementValid('H', 7, 'D', "AXCD", frame));
        //word does conflict
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('F', 8, 'A', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'D', "ABCD", frame));
        assertFalse(board.isWordPlacementValid('H', 6, 'D', "ABCD", frame));

        //test for frameContainTiles()
        ArrayList<Tile> f2 = new ArrayList<>();
        for (char ch : "H-LL".toCharArray()) {
            f2.add(Tile.makeTile(ch));
        }
        frame.setFrame(f2);
        assertTrue(board.isWordPlacementValid('H', 9, 'A', "He", frame));
        assertTrue(board.isWordPlacementValid('H', 9, 'A', "lleH", frame));
        assertFalse(board.isWordPlacementValid('H', 9, 'A', "Hello", frame));

        //test for isFrameUsed()
        board.reset();
        board.getBoard()[7][('H' - 'A')].setTile(new Tile('A', 1));
        board.getBoard()[7][('I' - 'A')].setTile(new Tile('B', 3));
        board.setFirstMove(false);
        ArrayList<Tile> f3 = new ArrayList<>();
        for (char ch : "-CDE-".toCharArray()) {
            f3.add(Tile.makeTile(ch));
        }
        frame.setFrame(f3);
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "ABC", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "ABX", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'D', "AB", frame));
        assertTrue(board.isWordPlacementValid('H', 7, 'D', "XAX", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "AB", frame));
        ArrayList<Tile> f4 = new ArrayList<>();
        for (char ch : "CDE".toCharArray()) {
            f4.add(Tile.makeTile(ch));
        }
        frame.setFrame(f4);
        //assert false for when frame contains no '-' tiles
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "ABX", frame));
    }

}