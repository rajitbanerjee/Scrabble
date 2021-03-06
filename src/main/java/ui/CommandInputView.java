package ui;

import javafx.scene.control.TextField;

/**
 * Command input text field GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class CommandInputView extends TextField {
    /**
     * Creates a new command input view.
     */
    public CommandInputView() {
        super();
        setId("input-view");
        setPromptText("Enter your move here...");
    }

}