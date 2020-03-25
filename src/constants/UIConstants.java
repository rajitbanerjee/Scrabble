package constants;

/**
 * Stores all the hard-coded constants used in JavaFX related classes.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class UIConstants {
    public static final int SCENE_WIDTH = 1200;
    public static final int SCENE_HEIGHT = 610;
    // Scene height is the board's size as well
    public static final int CMD_INPUT_WIDTH = SCENE_WIDTH - SCENE_HEIGHT - 2;
    public static final double SQUARE_SIZE = SCENE_HEIGHT * 0.0578125;
    public static final double BOARD_HGAP = SCENE_HEIGHT * 0.005;
    public static final double BOARD_VGAP = SCENE_HEIGHT * 0.005;
    public static final int DASH_LENGTH = (int) (CMD_INPUT_WIDTH * 0.175);

    public enum STATUS_CODE {P1_NAME, P2_NAME, P1_TURN, P2_TURN, GAME_OVER}

}