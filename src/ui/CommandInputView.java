package ui;

import constants.UIConstants;
import javafx.scene.control.TextField;

/**
 * Command input text field GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class CommandInputView extends TextField {
    public CommandInputView() {
        super();
        setPromptText("Enter your move here...");
        setMinWidth(UIConstants.CMD_INPUT_WIDTH);
    }

}
