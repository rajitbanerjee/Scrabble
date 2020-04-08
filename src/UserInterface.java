import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UserInterface implements UserInterfaceAPI {

    BorderPane mainPane;
    GridPane boardPane;
    TextArea infoArea;
    TextField commandField;
    Button[][] displaySquares = new Button[Board.BOARD_SIZE][Board.BOARD_SIZE];
    Scrabble scrabble;
    boolean gameOver;
    boolean opponentMadePlay;
    Bots bots;
    String latestInfo;
    StringBuilder allInfo;
    int gameOverDelayCount;

    UserInterface(Scrabble scrabble) throws FileNotFoundException {
        this.scrabble = scrabble;
        gameOver = false;
        opponentMadePlay = false;
        latestInfo = "";
        allInfo = new StringBuilder();
        gameOverDelayCount = 0;
    }

    public void setBots(Bots bots) {
        this.bots = bots;
    }

    // Stage display methods

    public void displayStage(Stage primaryStage) throws InterruptedException {
        primaryStage.setTitle("Scrabble");

        mainPane = new BorderPane();
        boardPane = new GridPane();
        infoArea = new TextArea();
        commandField = new TextField();

        infoArea.setPrefRowCount(10);
        infoArea.setPrefColumnCount(15);
        infoArea.setWrapText(true);

        commandField.setPromptText("Enter command...");
        commandField.setPrefColumnCount(15);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(Main.BOT_DELAY),
                        event -> {
                            if (!gameOver) {
                                String input = bots.getBot(scrabble.getCurrentPlayerId()).getCommand();
                                printLine("> " + input);
                                processInput(input);
                            }
                            if (!gameOver) {
                                printPrompt();
                            } else {
                                gameOverDelayCount++;
                            }
                            if (gameOverDelayCount >= 3) {
                                System.exit(0);
                            }
                        }
                        )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // rows are numbered from zero at the top
        // columns are numbers from zero at the left
        boardPane.setGridLinesVisible(true);
        int squareSize = 50;
        for (int r = 0; r < Board.BOARD_SIZE; r++) {
            boardPane.getColumnConstraints().add(new ColumnConstraints(squareSize));
            boardPane.getRowConstraints().add(new RowConstraints(squareSize));
            for (int c = 0; c < Board.BOARD_SIZE; c++) {
                Button button = new Button();
                boardPane.add(button, c, r);
                displaySquares[r][c] = button;
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setFillHeight(button, true);
                GridPane.setFillWidth(button, true);
            }
        }
        refreshBoard();

        mainPane.setCenter(boardPane);
        mainPane.setBottom(commandField);
        mainPane.setRight(infoArea);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();

        printGameStart();
        printPrompt();
    }

    private void pause() throws InterruptedException {
        try {
            Thread.sleep(Main.BOT_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void refreshBoard() {
        for (int r = 0; r < Board.BOARD_SIZE; r++) {
            for (int c = 0; c < Board.BOARD_SIZE; c++) {
                displaySquare(r, c);
            }
        }
    }

    void displaySquare(int r, int c) {
        Square square = scrabble.getBoard().getSquare(r, c);
        Button button = displaySquares[r][c];
        StringBuilder style = new StringBuilder();
        style.append("-fx-background-radius: 0;");
        String color;
        if (square.isDoubleLetter()) {
            color = "8080ff";
        } else if (square.isTripleLetter()) {
            color = "0000ff";
        } else if (square.isDoubleWord()) {
            color = "ff8080";
        } else if (square.isTripleWord()) {
            color = "ff0000";
        } else {
            color = "ffffff";
        }
        style.append("-fx-background-color: #").append(color).append(';');
        if (square.isOccupied()) {
            style.append("-fx-font-size: 14pt;");
            style.append("-fx-font-weight: bold;");
            button.setStyle(style.toString());
            button.setText(square.getTile() + "");  // placed letter
        } else {
            style.append("-fx-font-size: 8pt;");
            style.append("-fx-font-weight: lighter;");
            button.setStyle(style.toString());
            if (r==0) {
                button.setText(((char) (((int) 'A') + c)) + "");  // column letters
            } else if (c==0) {
                button.setText((r + 1) + "");  // row numbers
            } else {
                button.setText("");  // empty
            }
        }
    }

    // Input methods

    private void processInput(String input) {
        // this deals with user commands
        String command = input.trim().toUpperCase();
        Player currentPlayer = scrabble.getCurrentPlayer();
        if (!gameOver && (command.equals("PASS") || command.equals("P"))) {
            scrabble.zeroScorePlay();
            if (scrabble.isZeroScorePlaysOverLimit()) {
                printZeroScorePlaysOverLimit();
                gameOver = true;
            } else {
                opponentMadePlay = false;
                scrabble.turnOver();
            }
        } else if (!gameOver && (command.equals("HELP") || command.equals("H"))) {
            printHelp();
        } else if (!gameOver && (command.equals("SCORE") || command.equals("S"))) {
            printScores();
        } else if (!gameOver && (command.equals("POOL") || command.equals("O"))) {
            printPoolSize();
        } else if (!gameOver &&
                (command.matches("[A-O](\\d){1,2}( )+[A,D]( )+([A-Z]){1,15}") ||                // no blanks
                (command.matches("[A-O](\\d){1,2}( )+[A,D]( )+([A-Z_]){1,17}( )+([A-Z]){1,2}")  // with blanks
                        && isValidPlayWithBlanks(command)) ) ) {
            // no blanks
            Word word = parsePlay(command);
            if (!scrabble.getBoard().isLegalPlay(currentPlayer.getFrame(), word)) {
                printPlayError(scrabble.getBoard().getErrorCode());
            } else {
                scrabble.play(word);
                refreshBoard();
                printPoints(scrabble.getLatestPoints());
                if (scrabble.framesAreEmpty()) {
                    printAllTilesPlayed();
                    gameOver = true;
                } else {
                    opponentMadePlay = true;
                    scrabble.turnOver();
                }
            }
        } else if (!gameOver && (command.matches("EXCHANGE( )+([A-Z_]){1,7}") || command.matches("X( )+([A-Z_]){1,7}"))) {
            String[] parts = command.split("( )+");
            String letters = parts[1];
            if (!currentPlayer.getFrame().isLegalExchange(scrabble.getPool(),letters)) {
                printExchangeError(currentPlayer.getFrame().getErrorCode());
            } else {
                currentPlayer.getFrame().exchange(scrabble.getPool(),letters);
                printTiles();
                scrabble.zeroScorePlay();
                if (scrabble.isZeroScorePlaysOverLimit()) {
                    printZeroScorePlaysOverLimit();
                    gameOver = true;
                } else {
                    opponentMadePlay = false;
                    scrabble.turnOver();
                }
            }
        } else if (!gameOver && (command.matches("NAME( )+[A-Z][A-Z0-9]*") || command.matches("N( )+[A-Z][A-Z0-9]*"))) {
            String[] parts = input.split("( )+");
            currentPlayer.setName(parts[1]);
            printNewName();
        } else if (!gameOver && opponentMadePlay && (command.equals("CHALLENGE") || command.equals("C"))) {
            if (scrabble.getDictionary().areWords(scrabble.getLatestWords())) {
                printChallengeFail();
                scrabble.turnOver();
            } else {
                scrabble.undoPlay();
                refreshBoard();
                printChallengeSuccess();
            }
        } else {
            printLine("Error: command syntax incorrect. See help.");
        }
        if (gameOver) {
            scrabble.adjustScores();
            printScores();
            printWinner();
            printGameOver();
        }
    }

    private Word parsePlay(String command) {
        // this converts the play command into a Word
        String[] parts = command.split("( )+");
        String gridText = parts[0];
        int column = ((int) gridText.charAt(0)) - ((int) 'A');
        String rowText = parts[0].substring(1);
        int row = Integer.parseInt(rowText)-1;
        String directionText = parts[1];
        boolean isHorizontal = directionText.equals("A");
        String letters = parts[2];
        Word word;
        if (parts.length == 3) {
            word = new Word(row, column, isHorizontal, letters);
        } else {
            String designatedBlanks = parts[3];
            word = new Word(row, column, isHorizontal, letters, designatedBlanks);
        }
        return word;
    }

    private boolean isValidPlayWithBlanks(String command) {
        // this just checks that the number of blanks matches the number of designated blanks
        String[] parts = command.split("( )+");
        int blankCount = 0;
        for (int i=0; i<parts[2].length(); i++) {
            if (parts[2].charAt(i) == Tile.BLANK) {
                blankCount++;
            }
        }
        if (blankCount == 0 || parts[3].length() != blankCount) {
            return false;
        } else {
            return true;
        }
    }

    // Print methods

    private void print(String text) {
        infoArea.appendText(text);
        latestInfo = text;
        allInfo.append(text);
    }

    private void printLine(String text) {
        print(text + "\n");
    }

    private void printGameStart() {
        printLine("WELCOME TO SCRABBLE");
    }

    private void printNewName() {
        printLine("Player " + scrabble.getCurrentPlayer().getPrintableId() + " is now named " + scrabble.getCurrentPlayer().getName() + ".");
    }

    private void printTiles() {
        printLine(scrabble.getCurrentPlayer() + " has the following tiles:");
        for (Tile tile : scrabble.getCurrentPlayer().getFrame().getTiles()) {
            if (!Main.BOT_GAME) {
                print(tile + " ");
            } else {
                print("? ");
            }
        }
        printLine("");
    }

    private void printPrompt() {
        printLine(scrabble.getCurrentPlayer() + "'s turn:");
        printTiles();   // suppressed for the Bots
    }

    private void printPoints(int points) {
        printLine(scrabble.getCurrentPlayer() + " scored " + points + " points.");
    }

    private void printPoolSize() {
        printLine("Pool has " + scrabble.getPool().size() + " tiles");
    }

    private void printHelp() {
        printLine("Command options: Q (quit), N (name), P (pass), X (exchange), C (challenge), S (scores), O (pool) or play");
        printLine("Names must begin with a letter.");
        printLine("For an exchange, enter the letters that you wish to exchange. E.g. X ABC");
        printLine("For a play, enter the grid reference of the first letter, A (across) or D (down), " +
                        " and the word including any letters already on the board. E.g. A1 D HELLO");
        printLine("For a play with a blank, enter the grid reference of the first letter, A (across) or D (down)," +
                " the word with underscores for blanks including any letters already on the board," +
                " and the designated letter(s) for the blanks. E.g. A1 D H_LLO E");
    }

    private void printPlayError(int errCode) {
        String message = "";
        switch (errCode) {
            case Board.WORD_OUT_OF_BOUNDS:
                message = "Error: Word does not fit on the board.";
                break;
            case Board.WORD_LETTER_NOT_IN_FRAME:
                message = "Error: You do not have the necessary letters.";
                break;
            case Board.WORD_LETTER_CLASH:
                message = "Error: The word entered does not fit with the letters on the board.";
                break;
            case Board.WORD_NO_LETTER_PLACED:
                message = "Error: The word does not use any of your letters.";
                break;
            case Board.WORD_NO_CONNECTION:
                message = "Error: The word is not connected with the words on the board. ";
                break;
            case Board.WORD_INCORRECT_FIRST_PLAY:
                message = "Error: The first word must be in the centre of the board.";
                break;
            case Board.WORD_EXCLUDES_LETTERS:
                message = "Error: The word places excludes letters already on the board";
                break;
            case Board.WORD_ONLY_ONE_LETTER:
                message = "Error: Only one letter in the word.";
                break;
        }
        printLine(message);
    }

    public void printExchangeError (int errCode) {
        String message = "";
        switch (errCode) {
            case Frame.EXCHANGE_NOT_AVAILABLE:
                message = "Error: Letter not available in the frame.";
                break;
            case Frame.EXCHANGE_NOT_ENOUGH_IN_POOL:
                message = "Error: No enough tiles in the pool.";
                break;
        }
        printLine(message);
    }

    private void printZeroScorePlaysOverLimit() {
        printLine("The number of zero score plays is over the limit.");
    }

    private void printScores() {
        for (Player player : scrabble.getPlayers()) {
            printLine(player + " has " + player.getScore() + " points.");
        }
    }

    private void printChallengeFail() {
        printLine("Challenge fail - the words are in the dictionary.");
        printLine("Turn over.");
    }

    private void printChallengeSuccess() {
        printLine("Challenge success - the words are not in the dictionary.");
        printLine(scrabble.getOpposingPlayer() + " loses " + scrabble.getLatestPoints() + ".");
    }

    private void printAllTilesPlayed() {
        printLine("All tiles played.");
    }

    private void printWinner() {
        int maxScore = -1000;
        ArrayList<Player> winners = new ArrayList<>();
        boolean draw = false;
        for (Player player : scrabble.getPlayers()) {
            if (player.getScore() > maxScore) {
                draw = false;
                winners.clear();
                winners.add(player);
                maxScore = player.getScore();
            } else if (player.getScore() == maxScore) {
                draw = true;
                winners.add(player);
            }
        }
        if (!draw) {
            printLine(winners.get(0) + " wins!");
        } else {
            printLine("The following players draw!");
            for (Player winner : winners) {
                printLine(winner + "");
            }
        }
    }

    private void printGameOver() {
        printLine("GAME OVER");
    }

    // Methods for Bots

    public String getLatestInfo() {
        return latestInfo;
    }

    public String getAllInfo() {
        return allInfo + "";
    }

}


