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
        System.out.println("Running tests for Tile, Pool, Player and Frame classes:\n");
        // Run tests on Tile
        testTile();
        // Run tests on Pool
        Pool pool = new Pool();
        pool.printSize();
        testPool(pool);
        // Run tests on Player
        Player playerA = new Player("A", new Frame(pool));
        testPlayer(playerA);
        //demonstrate displayFrame()
        System.out.print("Player's Frame: ");
        playerA.getFrame().printFrame();
        // Run tests on Frame
        testFrame(playerA.getFrame());
    }

    // basic tests for properties of Frame and Pool's constituent tiles
    private static void testTile() {
        System.out.println("Testing the Tile class...");
        Tile t = new Tile('Z', 10);

        // Try to access tile type
        if (t.getType() != 'Z') {
            System.out.println("Error: type accessor doesn't work");
        }

        // Try to set an invalid tile type
        try {
            t.setType('*');
            System.out.println("Error: type setter should check for invalid tile types");
        } catch (Exception e) {
            // Test passed
        }

        // Try to access tile points
        if (t.getPoints() != 10) {
            System.out.println("Error: points accessor doesn't work");
        }

        // Try to set invalid tile points
        try {
            t.setPoints(11);
            System.out.println("Error: point setter should allow maximum 10 points");
        } catch (Exception e) {
            // Test passed
        }

        // Try to set invalid tile points
        try {
            t.setPoints(-1);
            System.out.println("Error: point setter shouldn't allow negative points");
        } catch (Exception e) {
            // Test passed
        }

        // Test toString()
        if (!t.toString().equals("  Z  ")) {
            System.out.println("Error: toString() doesn't work as expected.");
        }

        System.out.println("Tile test completed.\n");
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
                System.out.printf("\nError: Character %c is invalid", t.getType());
            } else if (map.get(t.getType()) != pool.getTileValue(t)) {
                System.out.println("Error: getTileValue() returns incorrect values");
            } else {
                correctTiles++;
            }
        }
        // Checks if Pool is empty
        if (!pool.isEmpty()) {
            System.out.println("Error: Pool size error.");
        }
        pool.reset();
        // Tests resetPool() method
        if (pool.size() != 100) {
            System.out.println("Error: resetPool() does not work as expected.");
        }
        // Prints final test result
        System.out.printf("\nPool tests completed. (%d/100 tiles correct)\n",
                correctTiles);
    }

    // Player class tests
    public static void testPlayer(Player playerA) {
        System.out.printf("\nPlayer name: %s\tScore: %d",
                playerA.getName(), playerA.getScore());
        System.out.println("Testing the Player class...");
        // Try to increase score by a negative value
        try {
            playerA.increaseScore(-1);
            System.out.println("Error: increaseScore() should not accept negative values.");
        } catch (Exception e) {
            // Test passed
        }
        // Try to increase score by a positive value
        playerA.increaseScore(100);
        if (playerA.getScore() != 100) {
            System.out.println("Error: increaseScore() does not work as expected.");
        }
        // Try to set score to a negative value
        try {
            playerA.setScore(-1);
            System.out.println("Error: setScore() should not accept negative values.");
        } catch (Exception e) {
            // Test passed
        }
        // Try to set score to a positive value
        playerA.setScore(1000);
        if (playerA.getScore() != 1000) {
            System.out.println("Error: setScore() does not work as expected.");
        }
        // Tests getName()
        if (!playerA.getName().equals("A")) {
            System.out.println("Error: Name set incorrectly.");
        }
        // Tests resetScore()
        playerA.resetScore();
        if (playerA.getScore() != 0) {
            System.out.println("Error: resetScore() does not work as expected.");
        }
        // Tests reset()
        playerA.setScore(100);
        Frame temp = playerA.getFrame();
        playerA.reset();
        if (playerA.getScore() != 0 || !playerA.getName().equals("")
                || playerA.getFrame() != null) {
            System.out.println("Error: reset() does not work as expected.");
        }
        // Tests getFrame() && setFrame()
        playerA.setFrame(temp);
        if (playerA.getFrame() == null) {
            System.out.println("Error: setFrame() or getFrame() does not work as expected.");
        }
        System.out.println("Player tests completed.\n");
    }

    // test the operations on each player's frame
    private static void testFrame(Frame frame) {
        System.out.println("Testing the Frame class...");

        // test isFrameEmpty()
        if (frame.isEmpty()) {
            System.out.println("Error: Frame should not be empty");
        }
        // empty frame
        frame.getFrame().clear();
        if (!frame.isEmpty()) {
            System.out.println("Error: Frame should be empty");
        }
        // testing fillFrame()
        // draw tiles until pool is emptied
        while (frame.getPool().size() > 0) {
            // num of tiles in pool before drawing
            int tilesInPoolBefore = frame.getPool().size();
            frame.fillFrame();
            // check that no more than 7 tiles are removed
            if (tilesInPoolBefore - frame.getPool().size() > 7) {
                System.out.println("Error: No more than 7 tiles should be drawn from the pool");
            }
            // check if tiles are still available in the pool
            if (frame.isEmpty() && frame.getPool().size() > 0) {
                System.out.println("Error: frame should not be empty");
            }
            frame.getFrame().clear();
        }
        // restore pool to 100
        frame.getPool().reset();
        frame.fillFrame();

        // test isLetterInFrame()
        for (int i = 0; i < frame.getFrame().size(); i++) {
            // check if each drawn tile is in the frame
            if (!frame.isLetterInFrame(frame.getFrame().get(i).getType())) {
                System.out.println("Error: letter is not found in frame");
            }
        }
        // test removeLetter() with letters from the frame
        for (int i = frame.getFrame().size() - 1; i >= 0; i--) {
            // removes all letters currently in the frame
            frame.remove(frame.getFrame().get(i).getType());
        }
        if (!frame.isEmpty()) {
            // check that all elements have been removed correctly
            System.out.println("Error: The frame should be empty");
        }
        // test removeLetter() with inputs not inside the frame
        try {
            String alphabet = "abcdefghi-";
            // at least one letter will be outside the frame
            char[] letters = alphabet.toCharArray();
            for (char letter : letters) {
                frame.remove(letter);
            }
            System.out.println("Error: frame cannot remove letters it does not contain");
        } catch (Exception e) {
            // test passes
        }

        // test accessLetter()
        frame.fillFrame();
        for (int i = 0; i < frame.getFrame().size(); i++) {
            // letter being accessed
            char letter = frame.getFrame().get(i).getType();
            // the tile of the letter
            Tile accessedTile = frame.getTile(letter);
            if (accessedTile.getType() != letter) {
                System.out.println("The correct letter was not accessed");
            }
        }

        System.out.println("Frame tests completed.\n");
    }
}
