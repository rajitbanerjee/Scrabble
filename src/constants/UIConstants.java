package constants;

import javafx.scene.text.Font;

/**
 * Stores all the hard-coded constants used in JavaFX related classes.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class UIConstants {
    public static final double CMD_INPUT_WIDTH = getSceneWidth() * 0.484;
    public static final double SQUARE_SIZE = getSceneWidth() * 0.03;
    public static final double BOARD_HGAP = getSceneHeight() * 0.005;
    public static final double BOARD_VGAP = getSceneHeight() * 0.005;
    public static final int DASH_LENGTH = (int) (CMD_INPUT_WIDTH * 0.175);
    // TODO move these to CSS stylesheet later, with other style options
    public static final Font pointsFont = new Font("verdana", 8);

    public static double getSceneWidth() {
        return 1200;
    }

    public static double getSceneHeight() {
        return 621;
    }

    public enum STATUS_CODE {P1_NAME, P2_NAME, P1_TURN, P2_TURN, GAME_OVER}

}
