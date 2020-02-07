import java.util.HashMap;

/**
 * Tests functionality of Player, Pool and Frame classes.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class PlayerTest {
    public static void main(String[] args) {
        Pool pool = new Pool();
        // Display pool count
        pool.displayTileCount();
        // Run tests on Pool
        testPool(pool);
        Player playerA = new Player("A", new Frame(pool));
        // Run tests on Player
        testPlayer(playerA);
        // TODO: Add tests for Frame
    }

    // Test the correctness of Tiles in Pool and the Pool size
    private static void testPool(Pool pool) {
        System.out.println("Testing Tiles in Pool...");
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
                System.out.println(String.format("Error: Character %c is invalid", t.getType()));
            } else if (map.get(t.getType()) != pool.getTileValue(t)) {
                System.out.println("Error: getTileValue() returns incorrect values");
            } else {
                correctTiles++;
            }
        }
        // Checks if Pool is empty
        if (!pool.isPoolEmpty()) {
            System.out.println("Error: Pool size error");
        }
        pool.resetPool();
        // Tests resetPool() method
        if (pool.countTiles() != 100) {
            System.out.println("Error: resetPool() does not work as expected.");
        }
        // Prints final test result
        System.out.println(String.format("Pool Test completed. (%d/100 tiles correct)", correctTiles));
    }

    public static void testPlayer(Player playerA) {
        System.out.println(String.format("Player name: %s\tScore: %d", playerA.getName(), playerA.getScore()));
        System.out.println("Testing the Player class...");
        // Try to increase score by a negative value
        try {
            playerA.increaseScore(-1);
            System.out.println("Error: increaseScore() should not accept negative values ");
        } catch (Exception e) {
            // Test passed
        }
        // Try to increase score by a positive value
        playerA.increaseScore(100);
        if (playerA.getScore() != 100) {
            System.out.println("Error: increaseScore() does not work as expected");
        }
        // Try to set score to a negative value
        try {
            playerA.setScore(-1);
            System.out.println("Error: setScore() should not accept negative values ");
        } catch (Exception e) {
            // Test passed
        }
        // Try to set score to a positive value
        playerA.setScore(1000);
        if (playerA.getScore() != 1000) {
            System.out.println("Error: setScore() does not work as expected");
        }
        // Tests getName()
        if (!playerA.getName().equals("A")) {
            System.out.println("Error: Name set incorrectly");
        }
        // Tests resetScore()
        playerA.resetScore();
        if (playerA.getScore() != 0) {
            System.out.println("Error: resetScore() does not work as expected");
        }
        // Tests reset()
        playerA.setScore(100);
        Frame temp = playerA.getFrame();
        playerA.reset();
        if (playerA.getScore() != 0 || !playerA.getName().equals("") || playerA.getFrame() != null) {
            System.out.println("Error: reset() does not work as expected");
        }
        // Tests getFrame() && setFrame()
        playerA.setFrame(temp);
        if (playerA.getFrame() == null) {
            System.out.println("Error: setFrame() or getFrame() does not work as expected");
        }
        System.out.println("Player Test completed.");
    }
}
