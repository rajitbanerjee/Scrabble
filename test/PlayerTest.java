import game.Frame;
import game.Pool;
import game_engine.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Player class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */

class PlayerTest {
    @Test
    void testPlayer() {
        // Empty name tests
        try {
            new Player(null);
            new Player(" ");
            fail("Error: Player name cannot be empty.");
        } catch (Exception ignored) {
            // test passed
        }

        Player playerA = new Player("A", new Frame(new Pool()));

        // Name empty setter tests
        try {
            playerA.setName("");
            playerA.setName(null);
            fail("Error: Player name cannot be empty.");
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
            fail("Error: increaseScore() should not accept negative values.");
        } catch (Exception ignored) {
            // Test passed
        }
        // Try to increase score by a positive value
        playerA.increaseScore(100);
        if (playerA.getScore() != 100) {
            fail("Error: increaseScore() does not work as expected.");
        }
        // Try to set score to a negative value
        try {
            playerA.setScore(-1);
            fail("Error: setScore() should not accept negative values.");
        } catch (Exception ignored) {
            // Test passed
        }
        // Try to set score to a positive value
        playerA.setScore(1000);
        if (playerA.getScore() != 1000) {
            fail("Error: setScore() does not work as expected.");
        }
        // Tests getName()
        if (!playerA.getName().equals("A")) {
            fail("Error: Name set incorrectly.");
        }
        if (!playerA.toString().equals("A")) {
            fail("Error: toString() not working.");
        }
        // Tests resetScore()
        playerA.resetScore();
        if (playerA.getScore() != 0) {
            fail("Error: resetScore() does not work as expected.");
        }
        // Tests reset()
        playerA.setScore(100);
        Frame temp = playerA.getFrame();
        playerA.reset();
        if (playerA.getScore() != 0 || !playerA.getName().equals("")
                || playerA.getFrame() != null) {
            fail("Error: reset() does not work as expected.");
        }
        // Tests getFrame() && setFrame()
        playerA.setFrame(temp);
        if (playerA.getFrame() == null) {
            fail("Error: setFrame() or getFrame() does not work as expected.");
        }
    }

}