package ui;

import constants.GameConstants;
import constants.UIConstants;
import game.Board;
import game.Square;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Board GUI design.
 * TODO javadoc comments
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class BoardView extends GridPane {
    private double squareSize = UIConstants.SQUARE_SIZE;
    private Board board;

    public BoardView(Board board) {
        super();
        setId("board-view");
        this.board = board;
        update();
    }

    public void update() {
        Square[][] boardArray = board.getBoard();
        // Initialise top-left empty block
        add(getEmptyUI(), 0, 0);
        // Draws the first row (Column Names)
        for (int k = 0; k < GameConstants.BOARD_SIZE; k++) {
            add(getColumnUI((char) ('A' + k)), k + 1, 0);
        }
        // Draws the first column (Row Numbers)
        for (int k = 0; k < GameConstants.BOARD_SIZE; k++) {
            add(getRowUI(k + 1), 0, k + 1);
        }
        // Draws the rest of the board
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                add(getSquareUI(boardArray[i][j]), j + 1, i + 1);
            }
        }
    }

    public Node getSquareUI(Square square) {
        String squareType = "";
        switch (square.getMultiplier()) {
            case CENTRE:
            case DOUBLE_WS:
                squareType = "square-double-ws";
                break;
            case DOUBLE_LS:
                squareType = "square-double-ls";
                break;
            case NORMAL:
                squareType = "square-normal";
                break;
            case TRIPLE_LS:
                squareType = "square-triple-ls";
                break;
            case TRIPLE_WS:
                squareType = "square-triple-ws";
                break;
        }
        Text text = new Text(square.toString());
        if (square.isEmpty()) {
            StackPane pane = new StackPane();
            pane.setId(squareType);
            pane.getChildren().add(text);
            return pane;
        } else {
            Text points = new Text(Integer.toString((square.getTile().getPoints())));
            points.setId("tile-points");
            return new TileView(text, points);
        }
    }

    public Node getColumnUI(char ch) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(squareSize, squareSize);
        rect.setId("square-blank");
        Label text = new Label(Character.toString(ch));
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    public Node getRowUI(int i) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(squareSize, squareSize);
        rect.setId("square-blank");
        Label text = new Label(Integer.toString(i));
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    public Node getEmptyUI() {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(squareSize, squareSize);
        rect.setId("square-blank");
        pane.getChildren().add(rect);
        return pane;
    }

}