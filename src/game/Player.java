package game;

/**
 * The Player is a Scrabble player with a name, score and frame.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class Player {
    private String name;
    private int score;
    private Frame frame;

    public Player(String name, Frame frame) throws IllegalArgumentException {
        this(frame);
        setName(name);
    }

    public Player(Frame frame) {
        setFrame(frame);
        score = 0;
    }

    /**
     * Increases the player score by given value.
     *
     * @param value increment value
     * @throws IllegalArgumentException if increment value is negative
     */
    public void increaseScore(int value) throws IllegalArgumentException {
        if (value < 0) {
            throw new IllegalArgumentException("Increment value must be positive!");
        }
        score += value;
    }

    /**
     * Decreases the player score by given value.
     *
     * @param value decrement value
     * @throws IllegalArgumentException if decrement value is negative
     */
    public void decreaseScore(int value) throws IllegalArgumentException {
        if (value < 0) {
            throw new IllegalArgumentException("Decrement value must be positive!");
        }
        score -= value;
    }

    /**
     * Resets the player information: empty name, zero score and remove frame.
     */
    public void reset() {
        name = "";
        score = 0;
        frame = null;
    }

    /**
     * Resets the player's score to zero.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Mutator method for the score variable.
     *
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Mutator method for the score variable.
     * (Might be useful for debugging purposes).
     *
     * @param score player score
     * @throws IllegalArgumentException if given score is negative
     */
    public void setScore(int score) throws IllegalArgumentException {
        if (score < 0) {
            throw new IllegalArgumentException("Player score cannot be less than 0");
        }
        this.score = score;
    }

    /**
     * Accessor method for the frame.
     *
     * @return player frame
     */
    public Frame getFrame() {
        return frame;
    }

    /**
     * Mutator method for the frame.
     *
     * @param frame player frame
     */
    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    /**
     * Accessor method for the name.
     *
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutator method for the name.
     *
     * @param name player name
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Player name cannot be null");
        }
        this.name = name.trim();
    }

    /**
     * Get the String representation of a player.
     *
     * @return the player name (can be changed to include player score)
     */
    @Override
    public String toString() {
        return name;
    }

}