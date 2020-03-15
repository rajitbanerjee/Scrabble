package ui;

import constants.Constants;
import game.Board;
import game.Square;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BoardView extends GridPane {
    private Board board;

    public BoardView(Board board) {
        super();
        this.board = board;
        init();
    }

    public static Node getSquareUI(Square square) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        switch (square.getMultiplier()) {
            case CENTRE:
            case DOUBLE_WS:
                rect.setFill(Color.LIGHTPINK);
                break;
            case DOUBLE_LS:
                rect.setFill(Color.LIGHTBLUE);
                break;
            case NORMAL:
                rect.setFill(Color.GHOSTWHITE);
                break;
            case TRIPLE_LS:
                rect.setFill(Color.CADETBLUE);
                break;
            case TRIPLE_WS:
                rect.setFill(Color.INDIANRED);
                break;
        }
        Text text;
        if (square.getTile() == null) {
            text = new Text(square.toString());
        } else {
            text = new Text(Character.toString(square.getTile().getType()));
        }
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    public static Node getColumnUI(char ch) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        rect.setFill(Color.GHOSTWHITE);
        Text text = new Text(Character.toString(ch));
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    public static Node getRowUI(int i) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        rect.setFill(Color.GHOSTWHITE);
        Text text = new Text(Integer.toString(i));
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    public static Node getEmptyUI() {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        rect.setFill(Color.GHOSTWHITE);
        pane.getChildren().add(rect);
        return pane;
    }

    // add(column, row)
    private void init() {
        Square[][] boardArray = board.getBoard();
        // Initialise top-left empty block
        add(getEmptyUI(), 0, 0);
        // Draws the first row (Column Names)
        for (int k = 0; k < Constants.BOARD_SIZE; k++) {
            add(getColumnUI((char) ('A' + k)), k + 1, 0);
        }
        // Draws the first column (Row Numbers)
        for (int k = 0; k < Constants.BOARD_SIZE; k++) {
            add(getRowUI(k + 1), 0, k + 1);
        }
        // Draws the rest of the board
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                add(getSquareUI(boardArray[i][j]), j + 1, i + 1);
            }
        }
        setHgap(5);
        setVgap(5);
    }
}
