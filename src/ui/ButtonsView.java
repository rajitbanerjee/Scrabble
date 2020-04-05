package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Buttons GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class ButtonsView extends HBox {
    private final Button help = new Button("Help");
    private final Button pass = new Button("Pass");
    private final Button challenge = new Button("Challenge");
    private final Button quit = new Button("Quit");
    private final Button restart = new Button("Restart");
    private final Button theme = new Button("Switch Theme");

    /**
     * Creates a new buttons view.
     */
    public ButtonsView() {
        setId("buttons-view");
        getChildren().addAll(help, pass, challenge, quit, restart, theme);
        for (Node button : getChildren()) {
            button.setId("command-button");
        }
    }

    /**
     * Accessor for pass button.
     *
     * @return pass button
     */
    public Button getPassButton() {
        return pass;
    }

    /**
     * Accessor for challenge button.
     *
     * @return challenge button
     */
    public Button getChallengeButton() {
        return challenge;
    }

    /**
     * Accessor for quit button.
     *
     * @return quit button
     */
    public Button getQuitButton() {
        return quit;
    }

    /**
     * Accessor for help button.
     *
     * @return help button
     */
    public Button getHelpButton() {
        return help;
    }

    /**
     * Accessor for restart button.
     *
     * @return restart button
     */
    public Button getRestartButton() {
        return restart;
    }

    /**
     * Accessor for theme switch button.
     *
     * @return theme switch button
     */
    public Button getThemeButton() {
        return theme;
    }

}