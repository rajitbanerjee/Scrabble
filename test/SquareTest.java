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
            Square s1 = new Square(Constants.MULTIPLIER.DOUBLE_LS);
            assertEquals(Constants.MULTIPLIER.DOUBLE_LS, s1.getMultiplier());
            Square s2 = new Square(Constants.MULTIPLIER.TRIPLE_LS);
            assertEquals(Constants.MULTIPLIER.TRIPLE_LS, s2.getMultiplier());
            Square s3 = new Square(Constants.MULTIPLIER.DOUBLE_WS);
            assertEquals(Constants.MULTIPLIER.DOUBLE_WS, s3.getMultiplier());
            Square s4 = new Square(Constants.MULTIPLIER.TRIPLE_WS);
            assertEquals(Constants.MULTIPLIER.TRIPLE_WS, s4.getMultiplier());
            Square s5 = new Square(Constants.MULTIPLIER.NORMAL);
            assertEquals(Constants.MULTIPLIER.NORMAL, s5.getMultiplier());
            Square s6 = new Square(Constants.MULTIPLIER.CENTRE);
            assertEquals(Constants.MULTIPLIER.CENTRE, s6.getMultiplier());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }

    @Test
    void testTile() {
        Square square = new Square(Constants.MULTIPLIER.NORMAL);
        square.setTile(new Tile('Z', 10));
        assertEquals(new Tile('Z', 10), square.getTile());
    }

    @Test
    void testToString() {
        try {
            Square s1 = new Square(Constants.MULTIPLIER.DOUBLE_LS);
            assertEquals("2x_LS", s1.toString());
            Square s2 = new Square(Constants.MULTIPLIER.TRIPLE_LS);
            assertEquals("3x_LS", s2.toString());
            Square s3 = new Square(Constants.MULTIPLIER.DOUBLE_WS);
            assertEquals("2x_WS", s3.toString());
            Square s4 = new Square(Constants.MULTIPLIER.TRIPLE_WS);
            assertEquals("3x_WS", s4.toString());
            Square s5 = new Square(Constants.MULTIPLIER.NORMAL);
            assertEquals("     ", s5.toString());
            Square s6 = new Square(Constants.MULTIPLIER.CENTRE);
            assertEquals("  *  ", s6.toString());
            Square s7 = new Square(Constants.MULTIPLIER.CENTRE);
            s7.setTile(new Tile('Z', 10));
            assertEquals("  Z  ", s7.toString());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }
}