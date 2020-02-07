/**
 * The Player class stores information about the player.
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

    /**
     * First Constructor for the player class, frame remains null if unspecified.
     *
     * @throws NullPointerException if the given name is null
     */
    public Player(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("Player name cannot be null");
        } else {
            this.name = name;
            score = 0;
            frame = null;
        }
    }

    /**
     * Second Constructor for the player class.
     * Call to first Constructor throws an exception if the given name is null.
     */
    public Player(String name, Frame frame) {
        this(name);
        this.frame = frame;
    }

    /**
     * Increases the player score by dx.
     *
     * @param value increment value
     * @throws IllegalArgumentException if increment value is negative
     */
    public void increaseScore(int value) throws IllegalArgumentException {
        if (value < 0) {
            throw new IllegalArgumentException("Increment value must be positive!");
        } else {
            score += value;
        }
    }

    /**
     * Resets the player information to default, empty name and zero score.
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
     * Mutator method for the score variable. (Might be useful for debugging purposes)
     *
     * @param score player score
     * @throws IllegalArgumentException if given score is negative
     */
    public void setScore(int score) throws IllegalArgumentException {
        if (score < 0) {
            throw new IllegalArgumentException("Player score cannot be less than 0");
        } else {
            this.score = score;
        }
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
     * Mutator method for the frame variable.
     *
     * @param name player name
     * @throws NullPointerException if given name is null
     */
    public void setName(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("Player name cannot be null");
        } else {
            this.name = name;
        }
    }

    /**
     * Prints the player name to the CLI.
     */
    public void printName() {
        System.out.println(name);
    }

    /**
     * Override toString method to return player name.
     *
     * @return the player name (can be changed to include player score)
     */
    @Override
    public String toString() {
        return name;
    }
}