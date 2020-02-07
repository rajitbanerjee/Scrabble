import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Frame holds the 7 tiles each player has in their possession. When tiles are
 * removed from the frame, the frame is refilled with tiles from the pool.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 */

public class Frame {
    // frame holds the tiles each player has access to
    private ArrayList<Tile> frame;

    /**
     * Constructor method for frame. Fills the frame with tiles.
     *
     * @param pool the game's shared pool
     */
    public Frame(Pool pool) {
        frame = new ArrayList<>();
        fillFrame(pool);
    }

    /**
     * Method to fill/refill the frame if it has less than 7 tiles.
     *
     * @param pool the game's shared pool
     * @throws RuntimeException if pool is empty, frame cannot be filled
     */
    public void fillFrame(Pool pool) throws RuntimeException {
        if (pool.isPoolEmpty()) {
            throw new RuntimeException("Cannot fill frame, no tiles left in pool.");
        } else {
            int numTilesToDraw = Math.min(pool.countTiles(), 7 - frame.size());
            for (int i = 0; i < numTilesToDraw; i++) {
                frame.add(pool.drawTile());
            }
        }
    }

    /**
     * Check if frame is empty.
     *
     * @return {@code true} if frame is empty, {@code false} otherwise
     */
    public boolean isFrameEmpty() {
        return frame.isEmpty();
    }

    /**
     * Returns the index of the first tile containing a specified letter.
     * Returns -1 if the letter is not in the frame.
     *
     * @param letter chosen by user
     * @return the index of the first tile containing this letter or -1 if the
     * letter is not in the frame
     */
    private int findLetterIndex(char letter) {
        for (int i = 0; i < frame.size(); i++) {
            if (frame.get(i).getType() == Character.toUpperCase(letter)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if a letter is found inside the frame.
     *
     * @param letter the letter that is being searched
     * @return {@code true} if letter is inside frame, {@code false} otherwise
     */
    public boolean isLetterInFrame(char letter) {
        return findLetterIndex(letter) != -1;
    }

    /**
     * Removes letter from the frame.
     *
     * @param letter to be removed from the frame
     * @throws NoSuchElementException if specified letter isn't in frame
     */
    public void removeLetter(char letter) throws NoSuchElementException {
        if (isLetterInFrame(letter)) {
            frame.remove(findLetterIndex(letter));
        } else {
            throw new NoSuchElementException("Letter can't be removed. Not in frame");
        }
    }

    /**
     * Accessor to letters in the frame.
     *
     * @param letter chosen by user to specify which tile from the frame is chosen
     * @return the tile associated with the letter
     * @throws NoSuchElementException if required letter is not in frame
     */
    public Tile accessLetter(char letter) throws NoSuchElementException {
        if (isLetterInFrame(letter)) {
            return frame.get(findLetterIndex(letter));
        } else {
            throw new NoSuchElementException("Letter can't be accessed. Not in frame");
        }
    }

    /**
     * Prints the frame to the CLI.
     */
    public void displayFrame() {
        System.out.println(frame);
    }

    /**
     * Accessor method for frame.
     *
     * @return the frame
     */
    public ArrayList<Tile> getFrame() {
        return frame;
    }
}