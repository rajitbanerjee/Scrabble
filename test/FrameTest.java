import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Frame class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */

class FrameTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    static void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    static void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testFrame() {
        Pool pool = new Pool();
        // try filling from an empty pool
        try {
            for (int i = 0; i < 100; i++) {
                pool.drawTile();
            }
            new Frame(pool);
        } catch (Exception ignored) {
            // test passed
        }
        pool.reset();

        Player playerA = new Player("A", new Frame(pool));
        Frame frame = playerA.getFrame();

        // test empty pool exception
        try {
            new Frame(null);
            fail("Error: Pool cannot be null.");
        } catch (Exception ignore) {
            // test passed
        }

        // test isEmpty()
        if (frame.isEmpty()) {
            fail("Error: Frame should not be empty");
        }
        // empty frame
        frame.getFrame().clear();
        if (!frame.isEmpty()) {
            fail("Error: Frame should be empty");
        }
        // testing fillFrame()
        // draw tiles until pool is emptied
        while (frame.getPool().size() > 0) {
            // num of tiles in pool before drawing
            int tilesInPoolBefore = frame.getPool().size();
            frame.fillFrame();
            // check that no more than 7 tiles are removed
            if (tilesInPoolBefore - frame.getPool().size() > 7) {
                fail("Error: No more than 7 tiles should be drawn from the pool");
            }
            // check if tiles are still available in the pool
            if (frame.isEmpty() && frame.getPool().size() > 0) {
                fail("Error: frame should not be empty");
            }
            frame.getFrame().clear();
        }
        // restore pool to 100
        frame.getPool().reset();
        frame.fillFrame();

        // test isLetterInFrame()
        for (int i = 0; i < frame.getFrame().size(); i++) {
            // check if each drawn tile is in the frame
            if (!frame.contains(frame.getFrame().get(i).getType())) {
                fail("Error: letter is not found in frame");
            }
        }
        // test removeLetter() with letters from the frame
        for (int i = frame.getFrame().size() - 1; i >= 0; i--) {
            // removes all letters currently in the frame
            frame.remove(frame.getFrame().get(i).getType());
        }
        if (!frame.isEmpty()) {
            // check that all elements have been removed correctly
            fail("Error: The frame should be empty");
        }
        // test removeLetter() with inputs not inside the frame
        try {
            String alphabet = "abcdefghi-";
            // at least one letter will be outside the frame
            char[] letters = alphabet.toCharArray();
            for (char letter : letters) {
                frame.remove(letter);
            }
            fail("Error: frame cannot remove letters it does not contain");
        } catch (Exception ignored) {
            // test passes
        }

        // test getTile()
        frame.fillFrame();
        for (int i = 0; i < frame.getFrame().size(); i++) {
            // letter being accessed
            char letter = frame.getFrame().get(i).getType();
            // the tile of the letter
            Tile accessedTile = frame.getTile(letter);
            if (accessedTile.getType() != letter) {
                fail("The correct letter was not accessed");
            }
        }

        ArrayList<Tile> newFrame = new ArrayList<>();
        newFrame.add(Tile.makeTile('Q'));
        frame.setFrame(newFrame);
        try {
            frame.getTile('Z');
            fail("Letter not in frame.");
        } catch (Exception ignored) {
            // test passed
        }

        // test frame printing to command line
        frame.printFrame();
        assertEquals("[  Q  ]", outContent.toString().strip());
        newFrame.add(Tile.makeTile('Z'));
        frame.setFrame(newFrame);
        outContent.reset();
        frame.printFrame();
        assertEquals("[  Q  ,   Z  ]", outContent.toString().strip());
    }

}