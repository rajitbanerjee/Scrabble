import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Frame holds the 7 tiles each player has in their possession. When tiles are
 * removed from the frame, the frame is refilled with tiles from the pool.
 *
 * @author Katarina Cvetkovic, 18347921
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
     * Method to fill/refill the frame if it has less than 7 tiles
     *
     * @param pool the game's shared pool
     */
    public void fillFrame(Pool pool) {
        for (int i = frame.size(); i < 7; i++) {
            frame.add(pool.drawTile());
        }
    }

    /**
     * Check if frame is empty
     *
     * @return true if frame is empty
     */
    public boolean isFrameEmpty() {
        return frame.isEmpty();
    }

    /**
     * returns the index of the first tile containing a specified letter
     * returns -1 if the letter is not in the frame.
     *
     * @param letter chosen by user
     * @return the index of the first tile containing this letter or -1 if the
     * letter is not in the frame
     */
    private int findLetterIndex(char letter) {
        int i = 0;
        for (Tile tile : frame) {
            if (tile.getType() == Character.toUpperCase(letter)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Check if a letter is found inside the frame
     *
     * @param letter the letter that is being searched
     * @return true if letter is inside frame
     */
    public boolean isLetterInFrame(char letter) {
        return findLetterIndex(letter) != -1;
    }

    /**
     * Removes letter from the frame
     *
     * @param letter to be removed from the frame
     */
    public void removeLetter(char letter) {
        if(!isLetterInFrame(letter)){
            throw new NoSuchElementException("Letter can't be removed. Not in frame");
        }
        frame.remove(findLetterIndex(letter));
    }

    /**
     * accessor to letters in the frame
     *
     * @param letter chosen by user to specify which tile from the frame is chosen
     * @return the tile associated with the letter
     */
    public Tile accessLetter(char letter) {
        if(!isLetterInFrame(letter)){
            throw new NoSuchElementException("Letter can't be accessed. Not in frame");
        }
        return frame.get(findLetterIndex(letter));
    }

    /**
     * Prints the frame to the CLI
     */
    public void displayFrame() {
        System.out.println(frame);
    }

    /**
     * Accessor method for frame
     *
     * @return the frame
     */
    public ArrayList<Tile> getFrame() {
        return frame;
    }
}
