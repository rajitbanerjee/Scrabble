package game;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Frame holds the 7 tiles each player has in their possession.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class Frame {
    public static final int LIMIT = 7;
    private final Pool pool;
    private final ArrayList<Tile> frame;

    /**
     * Creates a new player frame.
     *
     * @param pool the pool from which the frame draws tiles
     * @throws IllegalArgumentException if pool object is null
     */
    public Frame(Pool pool) throws IllegalArgumentException {
        if (pool == null) {
            throw new IllegalArgumentException("Pool cannot be null");
        }
        frame = new ArrayList<>();
        this.pool = pool;
        refillFrame();
    }

    /**
     * Accessor for pool.
     *
     * @return pool
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Accessor for frame.
     *
     * @return the frame, an ArrayList of tiles
     */
    public ArrayList<Tile> getFrame() {
        return frame;
    }

    /**
     * Mutator for frame.
     *
     * @param frame ArrayList of tiles
     */
    public void setFrame(ArrayList<Tile> frame) {
        this.frame.clear();
        this.frame.addAll(frame);
    }

    /**
     * Refills the frame if it has less than 7 tiles.
     *
     * @return the String of newly drawn tiles from the pool
     * @throws IllegalStateException if pool is empty, frame cannot be refilled
     */
    public String refillFrame() throws IllegalStateException {
        if (pool.isEmpty()) {
            throw new IllegalStateException("> Cannot refill frame, no tiles left in pool.");
        }
        int numTilesToDraw = Math.min(pool.size(), LIMIT - frame.size());
        StringBuilder drawnTiles = new StringBuilder();
        for (int i = 0; i < numTilesToDraw; i++) {
            Tile draw = pool.drawTile();
            drawnTiles.append(draw.getType());
            frame.add(draw);
        }
        return drawnTiles.toString();
    }

    /**
     * Exchanges the given letters in the frame from the pool.
     *
     * @param letters to be exchanged e.g. "BCDE", "PA-T"
     * @return the newly drawn tiles from the pool
     * @throws IllegalStateException if there are insufficient tiles in the pool
     */
    public String exchange(String letters) throws IllegalStateException {
        if (pool.size() < LIMIT) {
            throw new IllegalStateException("> Cannot exchange, pool contains 6 tiles or less!");
        }
        for (char letter : letters.toCharArray()) {
            remove(letter);
        }
        String newLetters = refillFrame();
        pool.addTiles(letters);
        return newLetters;
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
            throw new NoSuchElementException("> Letter can't be removed. Not in frame!");
        }
    }

    /**
     * Accessor for letters in the frame.
     *
     * @param letter chosen by user to specify the selected tile from the frame
     * @return the tile object for the given letter in frame
     * @throws NoSuchElementException if required letter is not in frame
     */
    public Tile getTile(char letter) throws NoSuchElementException {
        if (contains(letter)) {
            return getTile(getLetterIndex(letter));
        } else {
            throw new NoSuchElementException("> Letter can't be accessed. Not in frame!");
        }
    }

    /**
     * Returns the Tile at a given index in the frame.
     *
     * @param index from which Tile is to be queried
     * @return the Tile at given index
     * @throws IllegalArgumentException if index is out of bounds
     */
    public Tile getTile(int index) throws IllegalArgumentException {
        if (index < 0 || index >= frame.size()) {
            throw new IllegalArgumentException("Index out of bounds!");
        }
        return frame.get(index);
    }

    /**
     * Check if a letter is found in the frame.
     *
     * @param letter the letter that is being searched
     * @return {@code true} if letter is inside frame
     */
    public boolean contains(char letter) {
        return getLetterIndex(letter) != -1;
    }

    //  Find index of first tile containing given letter, -1 if not found
    private int getLetterIndex(char letter) {
        for (int i = 0; i < frame.size(); i++) {
            if (getTile(i).getType() == letter) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if frame is empty.
     *
     * @return {@code true} if frame is empty
     */
    public boolean isEmpty() {
        return frame.isEmpty();
    }

}