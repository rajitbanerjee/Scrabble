import game.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Word class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class WordTest {
    private Word word;

    @BeforeEach
    void setUp() {
        word = new Word("HELLO", 'H', 8, 'A');
    }

    @Test
    void testWordLength() {
        assertEquals("HELLO".length(), word.length());
    }

    @Test
    void testCharAt() {
        for (int i = 0; i < 5; i++) {
            assertEquals("HELLO".charAt(i), word.charAt(i));
        }
    }

    @Test
    void testOrientation() {
        assertTrue(word.isHorizontal());
        assertFalse(word.isVertical());
    }

    @Test
    void testGetLetters() {
        assertEquals("HELLO", word.getLetters());
    }

    @Test
    void testIsAlphaString() {
        assertTrue(word.isAlphaString());
    }

    @Test void testGetPosition() {
        assertEquals(7, word.getRow());
        assertEquals(7, word.getColumn());
    }
}
