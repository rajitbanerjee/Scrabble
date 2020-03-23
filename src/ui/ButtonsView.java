package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonsView extends HBox {
    private Button pass;
    private Button challenge;
    private Button quit;


    public ButtonsView() {
        pass = new Button("PASS");
        challenge = new Button("CHALLENGE");
        quit = new Button("QUIT");
        getChildren().addAll(pass, challenge, quit);
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setPadding(new Insets(10));
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

}
