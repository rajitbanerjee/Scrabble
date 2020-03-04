package game_engine;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Button btn = new Button();
//        btn.setText("Play Game");
//        btn.setOnAction(event -> Scrabble.main(null));
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        primaryStage.setScene(new Scene(root, 300, 250));
//        primaryStage.show();


        Parent root = FXMLLoader.load(getClass().getResource("DarkMode.fxml"));
        primaryStage.setTitle("Scrabble by DarkMode.");
        primaryStage.setScene(new Scene(root, 850, 550));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
