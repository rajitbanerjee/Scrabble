package constants;

import javafx.stage.Screen;

public class UIConstants {
    public enum STATUS_CODE {P1_NAME, P2_NAME, P1_TURN, P2_TURN, GAME_OVER}

    public static double getSceneWidth() {
        return Screen.getPrimary().getBounds().getWidth() * 0.75;
    }

    public static double getSceneHeight() {
        return Screen.getPrimary().getBounds().getHeight() * 0.70;
    }

    public static final double SQUARE_SIZE = getSceneWidth() * 0.03;

    public static final double CMD_INPUT_WIDTH = getSceneWidth() * 0.435;
}
