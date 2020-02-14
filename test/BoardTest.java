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
    @Test
    void testGetTile() {
        Board b = new Board();
        Tile tile = new Tile('Z', 10);
        b.placeTile('A', 1, tile);
        assertEquals(tile, b.getTile('A', 1));
    }
}