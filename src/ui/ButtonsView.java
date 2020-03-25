package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Buttons GUI design.
 * TODO javadoc comments
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

    public Button getPassButton() {
        return pass;
    }

    public Button getChallengeButton() {
        return challenge;
    }

    public Button getQuitButton() {
        return quit;
    }

    public Button getHelpButton() {
        return help;
    }

}