package game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Frame class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
class FrameTest {
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

        Player playerA = new Player(new Frame(pool));
        Frame frame = playerA.getFrame();

        // test empty pool exception
        try {
            new Frame(null);
            fail("Pool cannot be null.");
        } catch (Exception ignore) {
            // test passed
        }

        // test isEmpty()
        if (frame.isEmpty()) {
            fail("Frame should not be empty");
        }
        // empty frame
        frame.getFrame().clear();
        if (!frame.isEmpty()) {
            fail("Frame should be empty");
        }
        // testing refillFrame()
        // draw tiles until pool is emptied
        while (frame.getPool().size() > 0) {
            // num of tiles in pool before drawing
            int tilesInPoolBefore = frame.getPool().size();
            frame.refillFrame();
            // check that no more than 7 tiles are removed
            if (tilesInPoolBefore - frame.getPool().size() > 7) {
                fail("No more than 7 tiles should be drawn from the pool");
            }
            // check if tiles are still available in the pool
            if (frame.isEmpty() && frame.getPool().size() > 0) {
                fail("frame should not be empty");
            }
            frame.getFrame().clear();
        }
        // restore pool to 100
        frame.getPool().reset();
        frame.refillFrame();

        // test contains()
        for (int i = 0; i < frame.getFrame().size(); i++) {
            // check if each drawn tile is in the frame
            if (!frame.contains(frame.getFrame().get(i).getType())) {
                fail("letter is not found in frame");
            }
        }
        // test removeLetter() with letters from the frame
        for (int i = frame.getFrame().size() - 1; i >= 0; i--) {
            // removes all letters currently in the frame
            frame.remove(frame.getFrame().get(i).getType());
        }
        if (!frame.isEmpty()) {
            // check that all elements have been removed correctly
            fail("The frame should be empty");
        }
        // test removeLetter() with inputs not inside the frame
        try {
            String alphabet = "abcdefghi-";
            // at least one letter will be outside the frame
            char[] letters = alphabet.toCharArray();
            for (char letter : letters) {
                frame.remove(letter);
            }
            fail("frame cannot remove letters it does not contain");
        } catch (Exception ignored) {
            // test passes
        }

        // test getTile()
        try {
            frame.getTile(7);
            fail("Index out of bounds");
        } catch (Exception ignored) {
            // test passes
        }
        frame.refillFrame();
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
    }

    @Test
    void testExchange() {
        Pool pool = new Pool();
        Frame frame = new Frame(pool);
        frame.getFrame().clear();
        for (char ch : "HELLOZQ".toCharArray()) {
            frame.getFrame().add(Tile.makeTile(ch));
        }
        assertEquals(7, frame.getFrame().size());
        assertEquals(93, pool.size());
        frame.exchange("ZQ");
        assertEquals(93, pool.size());
        assertEquals(7, frame.getFrame().size());

        // Try to exchange tiles from pool when it's insufficient
        pool.getPool().clear();
        pool.addTiles("A");
        try {
            frame.exchange("ABC");
        } catch (Exception ignored) {
            // test passed
        }
    }

    @Test
    void testToString() {
        ArrayList<Tile> f = new ArrayList<>(); // Stores a copy of the frame for resetting purposes
        Frame frame = new Frame(new Pool());
        for (char ch : "HELLO".toCharArray()) {
            f.add(Tile.makeTile(ch));
        }
        frame.setFrame(f);
        assertEquals("[H, E, L, L, O]", frame.toString());
    }

}