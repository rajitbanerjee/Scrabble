package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Square class.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
class SquareTest {
    @Test
    void testMultiplier() {
        try {
            Square s1 = new Square(Square.MULTIPLIER.DOUBLE_LS);
            assertEquals(Square.MULTIPLIER.DOUBLE_LS, s1.getMultiplier());
            Square s2 = new Square(Square.MULTIPLIER.TRIPLE_LS);
            assertEquals(Square.MULTIPLIER.TRIPLE_LS, s2.getMultiplier());
            Square s3 = new Square(Square.MULTIPLIER.DOUBLE_WS);
            assertEquals(Square.MULTIPLIER.DOUBLE_WS, s3.getMultiplier());
            Square s4 = new Square(Square.MULTIPLIER.TRIPLE_WS);
            assertEquals(Square.MULTIPLIER.TRIPLE_WS, s4.getMultiplier());
            Square s5 = new Square(Square.MULTIPLIER.NORMAL);
            assertEquals(Square.MULTIPLIER.NORMAL, s5.getMultiplier());
            Square s6 = new Square(Square.MULTIPLIER.CENTRE);
            assertEquals(Square.MULTIPLIER.CENTRE, s6.getMultiplier());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }

    @Test
    void testIsEmpty() {
        Square square = new Square(Square.MULTIPLIER.NORMAL);
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
        Square square = new Square(Square.MULTIPLIER.NORMAL);
        square.setTile(Tile.makeTile('Z'));
        assertEquals(Tile.makeTile('Z'), square.getTile());
    }

    @Test
    void testGetLetterMultiplier() {
        Square square = new Square(Square.MULTIPLIER.NORMAL);
        assertEquals(1, square.getLetterMultiplier());
        square = new Square(Square.MULTIPLIER.CENTRE);
        assertEquals(1, square.getLetterMultiplier());
        square = new Square(Square.MULTIPLIER.DOUBLE_WS);
        assertEquals(1, square.getLetterMultiplier());
        square = new Square(Square.MULTIPLIER.TRIPLE_WS);
        assertEquals(1, square.getLetterMultiplier());
        square = new Square(Square.MULTIPLIER.DOUBLE_LS);
        assertEquals(2, square.getLetterMultiplier());
        square = new Square(Square.MULTIPLIER.TRIPLE_LS);
        assertEquals(3, square.getLetterMultiplier());
    }

    @Test
    void testGetWordMultiplier() {
        Square square = new Square(Square.MULTIPLIER.NORMAL);
        assertEquals(1, square.getWordMultiplier());
        square = new Square(Square.MULTIPLIER.DOUBLE_LS);
        assertEquals(1, square.getWordMultiplier());
        square = new Square(Square.MULTIPLIER.TRIPLE_LS);
        assertEquals(1, square.getWordMultiplier());
        square = new Square(Square.MULTIPLIER.CENTRE);
        assertEquals(2, square.getWordMultiplier());
        square = new Square(Square.MULTIPLIER.DOUBLE_WS);
        assertEquals(2, square.getWordMultiplier());
        square = new Square(Square.MULTIPLIER.TRIPLE_WS);
        assertEquals(3, square.getWordMultiplier());
    }

    @Test
    void testToString() {
        try {
            Square s1 = new Square(Square.MULTIPLIER.DOUBLE_LS);
            assertEquals("2L", s1.toString());
            Square s2 = new Square(Square.MULTIPLIER.TRIPLE_LS);
            assertEquals("3L", s2.toString());
            Square s3 = new Square(Square.MULTIPLIER.DOUBLE_WS);
            assertEquals("2W", s3.toString());
            Square s4 = new Square(Square.MULTIPLIER.TRIPLE_WS);
            assertEquals("3W", s4.toString());
            Square s5 = new Square(Square.MULTIPLIER.NORMAL);
            assertEquals(" ", s5.toString());
            Square s6 = new Square(Square.MULTIPLIER.CENTRE);
            assertEquals("*", s6.toString());
            Square s7 = new Square(Square.MULTIPLIER.CENTRE);
            s7.setTile(Tile.makeTile('Z'));
            assertEquals("Z", s7.toString());
        } catch (Exception e) {
            fail("Square multipliers cannot be set properly.");
        }
    }

}