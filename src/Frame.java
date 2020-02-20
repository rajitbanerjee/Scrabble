import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Frame holds the 7 tiles each player has in their possession. When tiles are
 * removed from the frame, the frame is refilled with tiles from the pool.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */

public class Frame {
    // Pool instance variable to store the pool for future method accesses
    private final Pool pool;
    // frame holds the tiles each player has access to
    private ArrayList<Tile> frame;

    /**
     * Constructor method for frame. Fills the frame with tiles.
     *
     * @param pool the game's shared pool
     * @throws IllegalArgumentException if given pool object is null
     */
    public Frame(Pool pool) throws IllegalArgumentException {
        if (pool == null) {
            throw new IllegalArgumentException("Pool cannot be null");
        }
        frame = new ArrayList<>();
        this.pool = pool;
        fillFrame();
    }

    /**
     * Method to fill/refill the frame if it has less than 7 tiles.
     *
     * @throws RuntimeException if pool is empty, frame cannot be filled
     */
    public void fillFrame() throws RuntimeException {
        if (pool.isEmpty()) {
            throw new RuntimeException("Cannot fill frame, no tiles left in pool.");
        }
        int numTilesToDraw = Math.min(pool.size(), Constants.FRAME_LIMIT - frame.size());
        for (int i = 0; i < numTilesToDraw; i++) {
            frame.add(pool.drawTile());
        }
    }

    /**
     * Check if frame is empty.
     *
     * @return {@code true} if frame is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
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
    private int getLetterIndex(char letter) {
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
    public boolean contains(char letter) {
        return getLetterIndex(letter) != -1;
    }

    /**
     * Removes letter from the frame.
     *
     * @param letter to be removed from the frame
     * @throws NoSuchElementException if specified letter isn't in frame
     */
    public void remove(char letter) throws NoSuchElementException {
        if (contains(letter)) {
            frame.remove(getLetterIndex(letter));
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
    public Tile getTile(char letter) throws NoSuchElementException {
        if (contains(letter)) {
            return frame.get(getLetterIndex(letter));
        } else {
            throw new NoSuchElementException("Letter can't be accessed. Not in frame");
        }
    }

    /**
     * Prints the frame to the command line.
     */
    public void printFrame() {
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

    /**
     * Accessor method for pool.
     *
     * @return the pool which is used to refill the frame
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Force the player's frame to a particular state.
     * Required for testing purposes.
     *
     * @param frame the required frame
     */
    public void setFrame(ArrayList<Tile> frame) {
        this.frame = frame;
    }

}
