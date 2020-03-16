import constants.GameConstants;
import game.Square;
import game.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Square class.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */

class SquareTest {
    @Test
    void testMultiplier() {
        try {
            Square s1 = new Square(GameConstants.MULTIPLIER.DOUBLE_LS);
            assertEquals(GameConstants.MULTIPLIER.DOUBLE_LS, s1.getMultiplier());
            Square s2 = new Square(GameConstants.MULTIPLIER.TRIPLE_LS);
            assertEquals(GameConstants.MULTIPLIER.TRIPLE_LS, s2.getMultiplier());
            Square s3 = new Square(GameConstants.MULTIPLIER.DOUBLE_WS);
            assertEquals(GameConstants.MULTIPLIER.DOUBLE_WS, s3.getMultiplier());
            Square s4 = new Square(GameConstants.MULTIPLIER.TRIPLE_WS);
            assertEquals(GameConstants.MULTIPLIER.TRIPLE_WS, s4.getMultiplier());
            Square s5 = new Square(GameConstants.MULTIPLIER.NORMAL);
            assertEquals(GameConstants.MULTIPLIER.NORMAL, s5.getMultiplier());
            Square s6 = new Square(GameConstants.MULTIPLIER.CENTRE);
            assertEquals(GameConstants.MULTIPLIER.CENTRE, s6.getMultiplier());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }

    @Test
    void testIsEmpty() {
        Square square = new Square(GameConstants.MULTIPLIER.NORMAL);
        assertTrue(square.isEmpty());
        square.setTile(Tile.makeTile('Z'));
        assertFalse(square.isEmpty());
    }

    @Test
    void testIsValid() {
        assertTrue(Square.isValid('H' - 'A', 8));
        assertFalse(Square.isValid('R' - 'A', 7));
        assertFalse(Square.isValid((char) ('A' - 1), 7));
        assertFalse(Square.isValid('H', -1));
        assertFalse(Square.isValid('H', 16));
    }

    @Test
    void testTile() {
        Square square = new Square(GameConstants.MULTIPLIER.NORMAL);
        square.setTile(Tile.makeTile('Z'));
        assertEquals(Tile.makeTile('Z'), square.getTile());
    }

    @Test
    void testToString() {
        try {
            Square s1 = new Square(GameConstants.MULTIPLIER.DOUBLE_LS);
            assertEquals("2xL", s1.toString());
            Square s2 = new Square(GameConstants.MULTIPLIER.TRIPLE_LS);
            assertEquals("3xL", s2.toString());
            Square s3 = new Square(GameConstants.MULTIPLIER.DOUBLE_WS);
            assertEquals("2xW", s3.toString());
            Square s4 = new Square(GameConstants.MULTIPLIER.TRIPLE_WS);
            assertEquals("3xW", s4.toString());
            Square s5 = new Square(GameConstants.MULTIPLIER.NORMAL);
            assertEquals("   ", s5.toString());
            Square s6 = new Square(GameConstants.MULTIPLIER.CENTRE);
            assertEquals(" * ", s6.toString());
            Square s7 = new Square(GameConstants.MULTIPLIER.CENTRE);
            s7.setTile(Tile.makeTile('Z'));
            assertEquals(" Z ", s7.toString());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }
}