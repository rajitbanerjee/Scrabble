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
        // normal squares without score multipliers - hard-coding is the easiest way to obtain the indices
        int[][] normal = {
                {0, 1}, {0, 2}, {0, 4}, {0, 5}, {0, 6}, {0, 8}, {0, 9}, {0, 10}, {0, 12}, {0, 13},
                {1, 0}, {1, 2}, {1, 3}, {1, 4}, {1, 6}, {1, 7}, {1, 8}, {1, 10}, {1, 11}, {1, 12},
                {1, 14}, {2, 0}, {2, 1}, {2, 3}, {2, 4}, {2, 5}, {2, 7}, {2, 9}, {2, 10}, {2, 11},
                {2, 13}, {2, 14}, {3, 1}, {3, 2}, {3, 4}, {3, 5}, {3, 6}, {3, 8}, {3, 9}, {3, 10},
                {3, 12}, {3, 13}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 5}, {4, 6}, {4, 7}, {4, 8},
                {4, 9}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {5, 0}, {5, 2}, {5, 3}, {5, 4}, {5, 6},
                {5, 7}, {5, 8}, {5, 10}, {5, 11}, {5, 12}, {5, 14}, {6, 0}, {6, 1}, {6, 3}, {6, 4},
                {6, 5}, {6, 7}, {6, 9}, {6, 10}, {6, 11}, {6, 13}, {6, 14}, {7, 1}, {7, 2}, {7, 4},
                {7, 5}, {7, 6}, {7, 8}, {7, 9}, {7, 10}, {7, 12}, {7, 13}, {8, 0}, {8, 1}, {8, 3},
                {8, 4}, {8, 5}, {8, 7}, {8, 9}, {8, 10}, {8, 11}, {8, 13}, {8, 14}, {9, 0}, {9, 2},
                {9, 3}, {9, 4}, {9, 6}, {9, 7}, {9, 8}, {9, 10}, {9, 11}, {9, 12}, {9, 14}, {10, 0},
                {10, 1}, {10, 2}, {10, 3}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 9}, {10, 11},
                {10, 12}, {10, 13}, {10, 14}, {11, 1}, {11, 2}, {11, 4}, {11, 5}, {11, 6}, {11, 8},
                {11, 9}, {11, 10}, {11, 12}, {11, 13}, {12, 0}, {12, 1}, {12, 3}, {12, 4}, {12, 5},
                {12, 7}, {12, 9}, {12, 10}, {12, 11}, {12, 13}, {12, 14}, {13, 0}, {13, 2}, {13, 3},
                {13, 4}, {13, 6}, {13, 7}, {13, 8}, {13, 10}, {13, 11}, {13, 12}, {13, 14}, {14, 1},
                {14, 2}, {14, 4}, {14, 5}, {14, 6}, {14, 8}, {14, 9}, {14, 10}, {14, 12}, {14, 13}
        };


        // check if multipliers are set correctly
        for (int[] index : double_ls) {
            assertEquals(Square.Multiplier.DOUBLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : triple_ls) {
            assertEquals(Square.Multiplier.TRIPLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : double_ws) {
            assertEquals(Square.Multiplier.DOUBLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : triple_ws) {
            assertEquals(Square.Multiplier.TRIPLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : normal) {
            assertEquals(Square.Multiplier.NORMAL, b[index[0]][index[1]].getMultiplier());
        }
        assertEquals(Square.Multiplier.CENTRE, b[7][7].getMultiplier());
    }

    @Test
    void testReset() {
        Square[][] b = board.getBoard();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                b[i][j].setTile(new Tile('Z', 10));
            }
        }
        board.reset();
        b = board.getBoard();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
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
            for (int row = 1; row <= 15; row++) {
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
    void testPlaceTile() {
        Square[][] b = board.getBoard();
        Tile tile = Tile.makeTile('Z');
        for (char column = 'A'; column <= 'O'; column++) {
            for (int row = 1; row <= 15; row++) {
                tile.setType((char) ((int) (Math.random() * 26) + 'A'));
                board.placeTile(column, row, tile);
                assertEquals(tile, b[row - 1][column - 'A'].getTile());
            }
        }

        // test if exception is thrown for square out of bounds
        try {
            // try to place a Tile outside the board's bounds
            board.placeTile('A', 16, tile);
            board.placeTile('P', 14, tile);
            board.placeTile('B', 0, tile);
            fail("Queried tile out of bounds.");
        } catch (Exception ignored) {
            // test passed
        }
        try {
            // try to place a null Tile object
            board.placeTile('A', 1, null);
            fail("Cannot place a null tile.");
        } catch (Exception ignored) {
            // test passed
        }

        try {
            // try to place a Tile on the same Square twice
            board.placeTile('A', 1, tile);
            board.placeTile('A', 1, tile);
            fail("Cannot place a tile on an occupied square.");
        } catch (Exception ignored) {
            // test passed
        }
    }

    @Test
    void testWordPlacementValidity() {
        Pool pool = new Pool();
        Frame frame = new Frame(pool);
        ArrayList<Tile> f = new ArrayList<>();
        for (char ch: "HEL".toCharArray()) {
            f.add(Tile.makeTile(ch));
        }
        frame.setFrame(f);
        assertFalse(board.isWordPlacementValid(7, 7, 'A', "HELLO", frame));
    }

}