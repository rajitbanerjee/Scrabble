package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Buttons GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class ButtonsView extends HBox {
    private Button help = new Button("Help");
    private Button pass = new Button("Pass");
    private Button challenge = new Button("Challenge");
    private Button quit = new Button("Quit");

    public ButtonsView() {
        setId("buttons-view");
        getChildren().addAll(help, pass, challenge, quit);
    }

    /**
     * Accessor for pass.
     *
     * @return pass button
     */
    public Button getPassButton() {
        return pass;
    }

    /**
     * Accessor for challenge.
     *
     * @return challenge button
     */
    public Button getChallengeButton() {
        return challenge;
    }

    /**
     * Accessor for quit.
     *
     * @return quit button
     */
    public Button getQuitButton() {
        return quit;
    }

    /**
     * Accessor for help.
     *
     * @return help button
     */
    public Button getHelpButton() {
        return help;
    }

}