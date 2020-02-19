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
        for (int[] index : Constants.DOUBLE_LS) {
            assertEquals(Square.Multiplier.DOUBLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.TRIPLE_LS) {
            assertEquals(Square.Multiplier.TRIPLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.DOUBLE_WS) {
            assertEquals(Square.Multiplier.DOUBLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.TRIPLE_WS) {
            assertEquals(Square.Multiplier.TRIPLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.NORMAL_SQUARES) {
            assertEquals(Square.Multiplier.NORMAL, b[index[0]][index[1]].getMultiplier());
        }
        assertEquals(Square.Multiplier.CENTRE, b[7][7].getMultiplier());
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
        for (char ch : "HEL".toCharArray()) {
            f.add(Tile.makeTile(ch));
        }
        frame.setFrame(f);
        assertFalse(board.isWordPlacementValid('H', 8, 'A', "HELLO", frame));
        /*
         * TODO
         *  1. Test for square out of bounds
         *  2. Test for invalid orientation
         *  3. Test for too short a word
         *  4. Test for alphabetic word
         *  5. Test for null frame object
         *  6. Test for board overflow detection
         *  7. Test for conflict in word placement with existing words on board
         *  8. Test to check that frame contains required tiles for the word
         *  9. Test to check if the word uses at least one letter from frame
         *  10. Test to check if the first move covers the centre square
         *  11. Test to check that the placed word connects with another word on board
         */
    }

}