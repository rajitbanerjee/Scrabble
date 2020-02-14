import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void testGetTile() {
        Board b = new Board();
        Tile tile = new Tile('Z', 10);
        b.placeTile('A', 1, tile);
        assertEquals(tile, b.getTile('A', 1));
    }
}