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
            Square s1 = new Square(Square.Multiplier.DOUBLE_LS);
            assertEquals(Square.Multiplier.DOUBLE_LS, s1.getMultiplier());
            Square s2 = new Square(Square.Multiplier.TRIPLE_LS);
            assertEquals(Square.Multiplier.TRIPLE_LS, s2.getMultiplier());
            Square s3 = new Square(Square.Multiplier.DOUBLE_WS);
            assertEquals(Square.Multiplier.DOUBLE_WS, s3.getMultiplier());
            Square s4 = new Square(Square.Multiplier.TRIPLE_WS);
            assertEquals(Square.Multiplier.TRIPLE_WS, s4.getMultiplier());
            Square s5 = new Square(Square.Multiplier.NORMAL);
            assertEquals(Square.Multiplier.NORMAL, s5.getMultiplier());
            Square s6 = new Square(Square.Multiplier.CENTRE);
            assertEquals(Square.Multiplier.CENTRE, s6.getMultiplier());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }

    @Test
    void testTile() {
        Square square = new Square(Square.Multiplier.NORMAL);
        square.setTile(new Tile('Z', 10));
        assertEquals(new Tile('Z', 10), square.getTile());
    }

    @Test
    void testToString() {
        try {
            Square s1 = new Square(Square.Multiplier.DOUBLE_LS);
            assertEquals("2x_LS", s1.toString());
            Square s2 = new Square(Square.Multiplier.TRIPLE_LS);
            assertEquals("3x_LS", s2.toString());
            Square s3 = new Square(Square.Multiplier.DOUBLE_WS);
            assertEquals("2x_WS", s3.toString());
            Square s4 = new Square(Square.Multiplier.TRIPLE_WS);
            assertEquals("3x_WS", s4.toString());
            Square s5 = new Square(Square.Multiplier.NORMAL);
            assertEquals("     ", s5.toString());
            Square s6 = new Square(Square.Multiplier.CENTRE);
            assertEquals("  *  ", s6.toString());
            Square s7 = new Square(Square.Multiplier.CENTRE);
            s7.setTile(new Tile('Z', 10));
            assertEquals("  Z  ", s7.toString());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }
}