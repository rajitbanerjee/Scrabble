package constants;

import javafx.scene.text.Font;
import javafx.stage.Screen;

/**
 * Stores all the hard-coded constants used in JavaFX related classes.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class UIConstants {
    public enum STATUS_CODE {P1_NAME, P2_NAME, P1_TURN, P2_TURN}

    public static double getSceneWidth() {
        return Screen.getPrimary().getBounds().getWidth() * 0.75;
    }

    public static double getSceneHeight() {
        return Screen.getPrimary().getBounds().getHeight() * 0.70;
    }

    public static final double SQUARE_SIZE = getSceneWidth() * 0.03;

    public static final double CMD_INPUT_WIDTH = getSceneWidth() * 0.435;

    public static final double BOARD_HGAP = getSceneWidth() * 0.005;

    public static final double BOARD_VGAP = getSceneHeight() * 0.005;

    public static final int DASH_LENGTH = (int) (UIConstants.CMD_INPUT_WIDTH * 0.175);

    // TODO move these to CSS stylesheet later, with other style options
    public static final Font pointsFont = new Font("verdana", 6);

    public static final Font cliFont = new Font("verdana", 12);

}
