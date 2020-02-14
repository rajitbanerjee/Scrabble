import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void testPlayer() {
        Player playerA = new Player("A", new Frame(new Pool()));
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