package game;

/**
 * The Player is a Scrabble player with a name, frame and score.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class Player {
    private String name;
    private Frame frame;
    private int score;

    /**
     * Creates a new Player.
     *
     * @param frame player's frame
     */
    public Player(Frame frame) {
        setFrame(frame);
        score = 0;
    }

    /**
     * Check if two players' names are identical (not permitted).
     *
     * @param name1 player 1's name
     * @param name2 player 2's name
     * @throws IllegalArgumentException if the two names are identical
     */
    public static void validateNames(String name1, String name2) throws IllegalArgumentException {
        if (name1.equalsIgnoreCase(name2)) {
            throw new IllegalArgumentException("> Name taken! Please choose another name.");
        }
    }

    /**
     * Accessor for the name.
     *
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutator for the name.
     *
     * @param name player name
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("> Player name cannot be empty!");
        }
        this.name = name.trim();
    }

    /**
     * Accessor for the frame.
     *
     * @return player frame
     */
    public Frame getFrame() {
        return frame;
    }

    /**
     * Mutator for the frame.
     *
     * @param frame player frame
     */
    public void setFrame(Frame frame) {
        this.frame = frame;
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
     * Accessor for score.
     *
     * @return player score
     */
    public int getScore() {
        return score;
    }

}