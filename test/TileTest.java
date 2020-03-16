import constants.GameConstants;
import game.Square;
import game.Tile;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

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
        Tile t = Tile.makeTile('Z');

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
        if (!t.toString().equals(" Z ")) {
            fail("Error: toString() doesn't work as expected.");
        }

        // Test equals()
        Tile t1 = Tile.makeTile('Z');
        Tile t2 = Tile.makeTile('Z');
        Square s1 = new Square(GameConstants.MULTIPLIER.NORMAL);
        s1.setTile(t1);
        assertEquals(t1, t2);
        assertNotEquals(t1, s1);

        // Test hashCode()
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), s1.hashCode());


        // Test makeTile()
        // Create a HashMap to test the score of each letter
        HashMap<Character, Integer> map = new HashMap<>();
        String onePointLetters = "AEILNORSTU";
        String twoPointLetters = "DG";
        String threePointLetters = "BCMP";
        String fourPointLetters = "FHVWY";

        for (char ch : onePointLetters.toCharArray()) {
            map.put(ch, 1);
        }
        for (char ch : twoPointLetters.toCharArray()) {
            map.put(ch, 2);
        }
        for (char ch : threePointLetters.toCharArray()) {
            map.put(ch, 3);
        }
        for (char ch : fourPointLetters.toCharArray()) {
            map.put(ch, 4);
        }
        // 5 point
        map.put('K', 5);
        // 8 point
        map.put('J', 8);
        map.put('X', 8);
        // 10 point
        map.put('Q', 10);
        map.put('Z', 10);
        // 0 point
        map.put('-', 0);

        // check if all the tiles have the correct number of points
        for (HashMap.Entry<Character, Integer> entry : map.entrySet()) {
            assertEquals(entry.getValue(), Tile.makeTile(entry.getKey()).getPoints());
        }

        try {
            Tile.makeTile('*');
            fail("Invalid tile type");
        } catch (Exception ignored) {
            // test passed
        }
    }

}