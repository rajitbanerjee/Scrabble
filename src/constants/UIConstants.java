package constants;

import javafx.scene.text.Font;

/**
 * Stores all the hard-coded constants used in JavaFX related classes.
 * TODO most of these to CSS stylesheet, with other style options
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class UIConstants {
    public static final double BOARD_DIMENSION = getSceneHeight();  //width and height of board
    public static final double OPTIONS_HEIGHT = getSceneHeight() * 0.25;
    public static final double CMD_INPUT_HEIGHT = getSceneHeight() - OPTIONS_HEIGHT;
    public static final double CMD_INPUT_WIDTH = getSceneWidth() - BOARD_DIMENSION - 2;
    public static final double SQUARE_SIZE = getSceneHeight() * 0.0578125;
    public static final double BOARD_HGAP = getSceneHeight() * 0.005;
    public static final double BOARD_VGAP = getSceneHeight() * 0.005;
    public static final int DASH_LENGTH = (int) (CMD_INPUT_WIDTH * 0.175);

    public static final Font pointsFont = new Font("verdana", 8);

    public static double getSceneWidth() {
        return 1200;
    }

    public static double getSceneHeight() {
        return 610;
    }

    public enum STATUS_CODE {P1_NAME, P2_NAME, P1_TURN, P2_TURN, GAME_OVER}

}