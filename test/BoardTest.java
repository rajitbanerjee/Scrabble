import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    Board board;

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
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                    // centre square index
                    if (i == 7 && j == 7) {
                        assertEquals(Square.Multiplier.CENTRE, b[i][j].getMultiplier());
                    } else {
                        assertEquals(Square.Multiplier.NORMAL, b[i][j].getMultiplier());
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
    }

    @Test
    void testGetTile() {
        Tile tile = new Tile('Z', 10);
        board.placeTile('A', 1, tile);
        assertEquals(tile, board.getTile('A', 1));
    }
}