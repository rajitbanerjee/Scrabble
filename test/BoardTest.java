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
        int centre = Constants.BOARD_SIZE / 2;
        assertEquals(Constants.MULTIPLIER.CENTRE, b[centre][centre].getMultiplier());
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

        // Check if word is placed on a valid square
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('R', 7, 'A', "HELLO", frame));
        assertFalse(board.isWordPlacementValid((char) ('A' - 1), 7, 'A', "HELLO", frame));
        assertFalse(board.isWordPlacementValid('H', -1, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 16, 'A', "Hello", frame));

        // Check if appropriate orientation is provided A (Across)/D (Down)
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'D', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'a', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'd', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'r', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'q', "Hello", frame));

        // Check if word to be placed is too short
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "H", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "He", frame));

        // Check if provided word is alphabetic
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "123", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "AB?", frame));
        assertTrue(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));

        // Check that appropriate measures are taken for a null frame object
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "123", null));

        // Check that first move covers the centre square
        board.reset();
        assertTrue(board.isWordPlacementValid('E', 8, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('A', 8, 'A', "Hello", frame));

        // Check if placed word connects with an existing word on the board
        board.setFirstMove(false);
        board.getBoard()[7][('H' - 'A')].setTile(Tile.makeTile('Z'));
        // Valid horizontal placements
        assertTrue(board.isWordPlacementValid('H', 9, 'A', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 7, 'A', "Hello", frame));
        assertTrue(board.isWordPlacementValid('F', 7, 'A', "Hello", frame));
        assertTrue(board.isWordPlacementValid('C', 8, 'A', "Hello", frame));
        assertTrue(board.isWordPlacementValid('I', 8, 'A', "Hello", frame));
        assertTrue(board.isWordPlacementValid('D', 9, 'A', "Hello", frame));
        // Valid vertical placements
        assertTrue(board.isWordPlacementValid('H', 9, 'D', "Hello", frame));
        assertTrue(board.isWordPlacementValid('H', 3, 'D', "Hello", frame));
        assertTrue(board.isWordPlacementValid('G', 7, 'D', "Hello", frame));
        assertTrue(board.isWordPlacementValid('G', 4, 'D', "Hello", frame));
        assertTrue(board.isWordPlacementValid('I', 7, 'D', "Hello", frame));
        assertTrue(board.isWordPlacementValid('I', 4, 'D', "Hello", frame));
        // Invalid placements
        assertFalse(board.isWordPlacementValid('H', 12, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 5, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('I', 7, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('I', 9, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('A', 0, 'D', "Hello", frame));
        assertFalse(board.isWordPlacementValid('A', 7, 'D', "Hello", frame));

        // Check if word placement overflows out of the board
        // Place tile at edge of board
        board.getBoard()[Constants.BOARD_SIZE / 2][('O' - 'A')].setTile(Tile.makeTile('Z'));
        // Place tile at edge of board
        board.getBoard()[Constants.BOARD_SIZE - 1][('H' - 'A')].setTile(Tile.makeTile('Z'));
        assertFalse(board.isWordPlacementValid('O', 9, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('N', 6, 'A', "HELLO", frame));
        assertFalse(board.isWordPlacementValid('I', 15, 'D', "HELLO", frame));
        assertFalse(board.isWordPlacementValid('G', 12, 'D', "Hello", frame));

        // Check if appropriate measures are taken for a word placement conflict with existing words
        board.reset();
        board.getBoard()[Constants.BOARD_SIZE / 2][('H' - 'A')].setTile(Tile.makeTile('Z'));
        board.setFirstMove(false);
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('F', 8, 'A', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 8, 'D', "Hello", frame));
        assertFalse(board.isWordPlacementValid('H', 6, 'D', "Hello", frame));

        // Check if frame contains necessary tiles for word placement (ignores filled squares)
        ArrayList<Tile> f2 = new ArrayList<>();
        for (char ch : "H-LL".toCharArray()) {
            f2.add(Tile.makeTile(ch));
        }
        frame.setFrame(f2);
        assertTrue(board.isWordPlacementValid('H', 9, 'A', "He", frame));
        assertTrue(board.isWordPlacementValid('H', 9, 'A', "lleH", frame));
        assertFalse(board.isWordPlacementValid('H', 9, 'A', "Hello", frame));
        /*
         * TODO Test to check if the word uses at least one letter from frame
         */
    }

}