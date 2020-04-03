package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Player class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
class PlayerTest {
    @Test
    void testPlayer() {
        Player playerA = new Player(new Frame(new Pool()));

        // Empty name tests
        try {
            playerA.setName(null);
            playerA.setName(" ");
            fail("Player name cannot be empty.");
        } catch (Exception ignored) {
            // test passed
        }

        // Name empty setter tests
        try {
            playerA.setName("");
            playerA.setName(null);
            fail("Player name cannot be empty.");
        } catch (Exception ignored) {
            // test passed
        }

        // Name non-empty setter test
        try {
            playerA.setName("A");
        } catch (Exception e) {
            fail("Name setter works, incorrect exception.");
        }

        // Try to increase score by a negative value
        try {
            playerA.increaseScore(-1);
            fail("increaseScore() should not accept negative values.");
        } catch (Exception ignored) {
            // Test passed
        }
        // Try to increase score by a positive value
        playerA.increaseScore(100);
        if (playerA.getScore() != 100) {
            fail("increaseScore() does not work as expected.");
        }

        // Try to decrease score by a negative value
        try {
            playerA.decreaseScore(-1);
            fail("decreaseScore() should not accept negative values.");
        } catch (Exception ignored) {
            // Test passed
        }
        // Try to increase score by a positive value
        playerA.decreaseScore(100);
        if (playerA.getScore() != 0) {
            fail("decreaseScore() does not work as expected.");
        }


        // Try to set score to a negative value
        try {
            playerA.setScore(-1);
            fail("setScore() should not accept negative values.");
        } catch (Exception ignored) {
            // Test passed
        }
        // Try to set score to a positive value
        playerA.setScore(1000);
        if (playerA.getScore() != 1000) {
            fail("setScore() does not work as expected.");
        }
        // Tests getName()
        if (!playerA.getName().equals("A")) {
            fail("Name set incorrectly.");
        }
        if (!playerA.toString().equals("A")) {
            fail("toString() not working.");
        }
        // Tests resetScore()
        playerA.resetScore();
        if (playerA.getScore() != 0) {
            fail("resetScore() does not work as expected.");
        }
        Frame temp = playerA.getFrame();
        // Tests getFrame() && setFrame()
        playerA.setFrame(temp);
        if (playerA.getFrame() == null) {
            fail("setFrame() or getFrame() does not work as expected.");
        }
    }
    
    @Test
    void testValidateName() {
        try {
            Player.validateNames("John", "jOHn");
            fail("Unique names required");
        } catch (Exception ignored) {
            // Test passed
        }
        try {
            Player.validateNames("John", "Jean");
        } catch (Exception e) {
            fail("Names are valid!");
        }
    }

}