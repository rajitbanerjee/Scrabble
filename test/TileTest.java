import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tile class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */

class TileTest {
    @Test
    void testTile() {
        Tile t = new Tile('Z', 10);

        // Try to access tile type
        if (t.getType() != 'Z') {
            fail("Error: type accessor doesn't work");
        }

        // Try to set an invalid tile type
        try {
            t.setType('*');
            fail("Error: type setter should check for invalid tile types");
        } catch (Exception ignored) {
            // Test passed
        }

        // Try to access tile points
        if (t.getPoints() != 10) {
            fail("Error: points accessor doesn't work");
        }

        // Try to set invalid tile points
        try {
            t.setPoints(11);
            fail("Error: point setter should allow maximum 10 points");
        } catch (Exception e) {
            // Test passed
        }

        // Try to set invalid tile points
        try {
            t.setPoints(-1);
            System.out.println("Error: point setter shouldn't allow negative points");
        } catch (Exception ignored) {
            // Test passed
        }

        // Test toString()
        if (!t.toString().equals("  Z  ")) {
            fail("Error: toString() doesn't work as expected.");
        }
    }

}