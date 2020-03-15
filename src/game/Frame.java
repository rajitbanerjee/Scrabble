package game;

import constants.Constants;

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
    private final Pool pool;
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
        refillFrame();
    }

    public Pool getPool() {
        return pool;
    }

    public ArrayList<Tile> getFrame() {
        return frame;
    }

    public void setFrame(ArrayList<Tile> frame) {
        this.frame = frame;
    }

    /**
     * Method to refill the frame if it has less than 7 tiles.
     *
     * @throws IllegalStateException if pool is empty, frame cannot be refilled
     */
    public void refillFrame() throws IllegalStateException {
        if (pool.isEmpty()) {
            throw new IllegalStateException("Cannot refill frame, no tiles left in pool.");
        }
        int numTilesToDraw = Math.min(pool.size(), Constants.FRAME_LIMIT - frame.size());
        for (int i = 0; i < numTilesToDraw; i++) {
            frame.add(pool.drawTile());
        }
    }

    /**
     * Exchanges the given letters in the frame from the pool.
     *
     * @param letters to be exchanged e.g. "BCDE", "PA-T"
     * @throws IllegalStateException if there are insufficient tiles in the pool
     */
    public void exchange(String letters) throws IllegalStateException {
        if (pool.size() < Constants.FRAME_LIMIT) {
            throw new IllegalStateException("Cannot exchange, pool contains 6 tiles or less!");
        }
        for (char letter : letters.toCharArray()) {
            remove(letter);
        }
        refillFrame();
        pool.addTiles(letters);
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
            throw new NoSuchElementException("Letter can't be removed. Not in frame!");
        }
    }

    /**
     * Accessor to letters in the frame.
     *
     * @param letter chosen by user to specify the selected tile from the frame
     * @return the tile object for the given letter in frame
     * @throws NoSuchElementException if required letter is not in frame
     */
    public Tile getTile(char letter) throws NoSuchElementException {
        if (contains(letter)) {
            return frame.get(getLetterIndex(letter));
        } else {
            throw new NoSuchElementException("Letter can't be accessed. Not in frame!");
        }
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

    //  Find index of first tile containing some letter, -1 if not found
    private int getLetterIndex(char letter) {
        for (int i = 0; i < frame.size(); i++) {
            if (frame.get(i).getType() == Character.toUpperCase(letter)) {
                return i;
            }
        }
        return -1;
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
     * Prints the frame to the command line.
     */
    public void printFrame() {
        System.out.println(frame);
    }


    /**
     * Override toString method
     *
     * @return Sting representation of Frame
     */
    @Override
    public String toString() {
        return frame.toString();
    }
}
