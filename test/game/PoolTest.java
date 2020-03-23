package game;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Pool class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
class PoolTest {
    @Test
    void testPool() {
        Pool pool = new Pool();
        // Tests the pool size
        assertEquals(100, pool.size());

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

        // Number of correct tiles
        int correctTiles = 0;
        // Draw 100 tiles from the pool and verify the characters and their associated scores
        for (int i = 0; i < 100; i++) {
            Tile t = pool.drawTile();
            if (!map.containsKey(t.getType()) || map.get(t.getType()) != t.getPoints()) {
                fail("Error: Character " + t.getType() + " is invalid");
            } else {
                correctTiles++;
            }
        }
        // Checks if Pool is empty
        if (!pool.isEmpty()) {
            fail("Error: Pool size error.");
        }
        try {
            pool.drawTile();
            fail("Error: Pool is empty, cannot draw tile");
        } catch (Exception ignored) {
            // test passed
        }
        pool.reset();
        // Tests resetPool() method
        if (pool.size() != 100) {
            fail("Error: resetPool() does not work as expected.");
        }
        // Checks if all 100 tiles are correct or not
        if (correctTiles != 100) {
            fail("Error: Not all 100 tiles are correct.");
        }
    }

}