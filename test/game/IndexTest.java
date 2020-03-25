package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Index class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
class IndexTest {
    @Test
    void testIndex() {
        Index index1 = new Index(7, 8);
        assertEquals(7, index1.getRow());
        assertEquals(8, index1.getColumn());

        Index index2 = new Index(7, 8);
        String index3 = "7, 8";
        assertEquals(index1, index2);
        assertNotEquals(index1, index3);
        assertNotEquals(index1, index3);
    }

}