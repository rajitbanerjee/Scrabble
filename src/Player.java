/**
 * The Player class stores information about the player.
 *
 * @author Tee Chee Guan, 18202044
 */
public class Player {
    private String name;
    private int score;
    private Frame frame;

    /**
     * First Constructor for the player class, frame remains null if unspecified.
     * Throws an exception if the given name is null.
     */
    public Player(String name) {
        if (name == null) {
            throw new NullPointerException("Player name cannot be null");
        }
        this.name = name;
        score = 0;
        frame = null;
    }

    /**
     * Second Constructor for the player class.
     * Throws an exception if the given name is null.
     */
    public Player(String name, Frame frame) {
        this(name);
        this.frame = frame;
    }

    /**
     * Increases the player score by dx.
     *
     * @param dx increment value
     */
    public void increaseScore(int dx) {
        score += dx;
    }

    /**
     * Resets the player information to default, empty name and zero score.
     */
    public void reset() {
        name = "";
        score = 0;
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
     * Mutator method for the score variable. (Might be useful for debugging purposes)
     * Throws an IllegalArgumentException if x < 0
     *
     * @param x player score
     */
    public void setScore(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Player score cannot be less than 0");
        }
        score = x;
    }

    /**
     * Accessor method for the frame variable.
     *
     * @return player frame
     */
    public Frame getFrame() {
        return frame;
    }

    /**
     * Mutator method for the frame variable.
     *
     * @param frame player frame
     */
    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    /**
     * Accessor method for the name variable.
     *
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutator method for the frame variable. Throws an exception if the given string is null.
     *
     * @param name player name
     */
    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException("Player name cannot be null");
        }
        this.name = name;
    }
}
