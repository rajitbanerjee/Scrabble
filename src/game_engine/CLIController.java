package game_engine;

import constants.Constants;
import game.*;
import javafx.scene.input.KeyCode;
import resources.Resource;
import ui.CommandHistoryView;
import ui.CommandInputView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class CLIController {
    private CommandInputView inputView;
    private CommandHistoryView historyView;
    private Pool pool;
    private Board board;
    private Player player1, player2;
    private Constants.STATUS_CODE gameState;
    private HashSet<String> dictionary;
    private BoardController boardController;
    private int opponentScore;

    public CLIController(CommandInputView inputView, CommandHistoryView historyView, BoardController boardController) {
        this.inputView = inputView;
        this.historyView = historyView;
        this.boardController = boardController;
        pool = new Pool();
        board = new Board();
        player1 = new Player("", new Frame(pool));
        player2 = new Player("", new Frame(pool));
        gameState = Constants.STATUS_CODE.P1_NAME;
        opponentScore = 0;
        setListeners();
        fillDictionary();
        printWelcome();
    }

    // End game if six consecutive scoreless moves occur
    public void checkLastSixScores() {
        // TODO remove printing scores later, added only for testing
        Scoring.printLastSixScores();
        if (Scoring.isLastSixZero()) {
            printToOutput("Six consecutive scoreless turns have occurred! Game over.");
            quit();
        }
    }

    // Exchange tiles between frame and pool
    private void exchangeTiles(String move, Frame frame, boolean isTest) {
        String to_exchange = move.substring(move.indexOf(' ')).trim();
        frame.exchange(to_exchange);
        if (!isTest) {
            Scoring.addScoreToList(0);
            pool.printSize();
            printToOutput(String.format("\nLetters (%s) have been exchanged!\n", to_exchange));
            checkLastSixScores();
        }
    }

    // Quit game
    private void quit() {
        printToOutput("---------------------------------------------------------");
        printToOutput("Final Scores:");
        printToOutput(String.format("%s's score: %d", player1.getName(), player1.getScore()));
        printToOutput(String.format("%s's score: %d\n", player2.getName(), player2.getScore()));
        int difference = player1.getScore() - player2.getScore();
        if (difference == 0) {
            printToOutput("Game is a tie!");
        } else {
            Player winner = (difference > 0) ? player1 : player2;
            printToOutput(String.format("%s wins the game! Well done.", winner.getName()));
            printToOutput(String.format("%s wins the game! Well done.", winner.getName()));
        }
        printToOutput("---------------------------------------------------------");
        printToOutput("Thanks for playing!");
        System.exit(0);
    }

    private void askForMove(Player player) {
        Frame frame = player.getFrame();
        printToOutput(String.format("%s, it's your turn!", player.getName()));
        displayFrameScore(player, frame);
        promptUser();
    }

    public void startGame() {
        printToOutput(String.format("Welcome %s and %s!", player1.getName(), player2.getName()));
        askForMove(player1);
    }

    public void printToOutput(String text) {
        historyView.printText(text);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setListeners() {
        inputView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                printToOutput(inputView.getText());
                processCommand(inputView.getText(), gameState);
                inputView.clear();
            }
        });
    }

    private void processCommand(String command, Constants.STATUS_CODE statusCode) {
        switch (statusCode) {
            case P1_NAME:
                player1.setName(command);
                if (player1.isValidName()) {
                    gameState = Constants.STATUS_CODE.P2_NAME;
                    printToOutput("[Console] Player #2, please enter your name: ");
                } else {
                    printToOutput("[Console] Player #1, please enter your name: ");
                }
                break;
            case P2_NAME:
                player2.setName(command);
                if (player2.isValidName()) {
                    gameState = Constants.STATUS_CODE.P1_TURN;
                    startGame();
                } else {
                    printToOutput("[Console] Player #2, please enter your name: ");
                }
                break;
            case P1_TURN:
                if (isValidMove(command, player1.getFrame())) {
                    makeMove(command, player1, player1.getFrame(), player2);
                    gameState = Constants.STATUS_CODE.P2_TURN;
                    askForMove(player2);
                    boardController.update();
                } else {
                    printToOutput("Invalid move! Try again.");
                    askForMove(player1);
                }
                break;
            case P2_TURN:
                if (isValidMove(command, player2.getFrame())) {
                    makeMove(command, player2, player2.getFrame(), player2);
                    gameState = Constants.STATUS_CODE.P1_TURN;
                    askForMove(player1);
                    boardController.update();
                } else {
                    printToOutput("Invalid move! Try again.");
                    askForMove(player2);
                }
        }
    }

    private void printWelcome() {
        printToOutput("[Console] Welcome to Scrabble by DarkMode.");
        printToOutput("[Console] Player #1, please enter your name: ");
    }

    private boolean isGameOver() {
        return pool.isEmpty() && (player1.getFrame().isEmpty() || player2.getFrame().isEmpty());
    }

    // Makes a valid move
    private void makeMove(String move, Player player, Frame frame, Player opponent) {
        if (move.equalsIgnoreCase("QUIT")) {
            quit();
        } else if (move.equalsIgnoreCase("PASS")) {
            pass(player, false);
        } else if (move.startsWith("EXCHANGE")) {
            exchangeTiles(move, frame, false);
        } else if (move.equalsIgnoreCase("CHALLENGE")) {
            boolean isChallengeSuccessful = challenge(opponent);
            if (isChallengeSuccessful) {
                pass(opponent, true);
                // TODO: Broken needs to be fixed
                //makeMove(player, frame, opponent);
            } else {
                pass(player, false);
            }
        } else {
            scoreMove(move, player, frame);
        }
        if (isGameOver()) {
            quit();
        }
    }

    // Display frame and score
    private void displayFrameScore(Player player, Frame frame) {
        printToOutput(String.format("%s's frame: ", player.getName()));
        printToOutput(frame.toString());
        printToOutput(String.format("%s's score: %d\n", player.getName(), player.getScore()));
    }

    // Helper message
    private void promptUser() {
        printToOutput("Enter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\")");
        printToOutput("or QUIT/PASS/EXCHANGE <letters (no spaces)>/CHALLENGE: ");
    }

    // Check if a move is valid
    private boolean isValidMove(String move, Frame frame) {
        return move.equalsIgnoreCase("QUIT") || move.equalsIgnoreCase("PASS")
                || ((move.startsWith("EXCHANGE") && isExchangeLegal(move, frame))) ||
                move.equalsIgnoreCase("CHALLENGE") || isPlacementLegal(move, frame);
    }

    // Check if an exchange is Valid
    // TODO: needs to be changed, bad programming practice
    private boolean isExchangeLegal(String move, Frame frame) {
        Frame tempFrame = new Frame(pool);
        ArrayList<Tile> tempList = new ArrayList<>(frame.getFrame());
        tempFrame.setFrame(tempList);
        if (move.matches("EXCHANGE [A-Z-]+")) {
            try {
                exchangeTiles(move, tempFrame, true);
                return true;
            } catch (Exception e) {
                printToOutput(e.getMessage());
                return false;
            }
        } else {
            printToOutput("Exchange must of the format: EXCHANGE <letters (no spaces)>");
            return false;
        }
    }

    // Challenge opponent's previous move and change scores accordingly
    private boolean challenge(Player opponent) {
        boolean success = false;
        if (Scoring.challengeIndices.isEmpty()) {
            printToOutput("Cannot challenge! No word placed by opponent.");
        } else {
            if (wordsInDictionary()) {
                printToOutput("Challenge unsuccessful!");
            } else {
                removeTiles(opponent.getFrame());
                opponent.decreaseScore(opponentScore);
                printToOutput(String.format("\n\nChallenge successful! %s's tiles removed!", opponent.getName()));
                if (board.isEmpty()) {
                    board.setFirstMove(true);
                }
                success = true;
            }
        }
        Scoring.challengeIndices.clear();
        return success;
    }

    // Remove tile from board and put them back into frame
    private void removeTiles(Frame frame) {
        StringBuilder addToPool = new StringBuilder();
        int i = Constants.FRAME_LIMIT - Scoring.challengeIndices.size();
        for (Index index : Scoring.challengeIndices) {
            int row = index.getRow();
            int column = index.getColumn();
            Tile tile = board.getBoard()[row][column].getTile();
            addToPool.append(tile.getType());
            frame.getFrame().remove(i);
            frame.getFrame().add(i, tile);
            board.getBoard()[row][column].setTile(null);
            i++;
        }
        pool.addTiles(addToPool.toString());
    }

    // Check if a word placement move is legal
    private boolean isPlacementLegal(String move, Frame frame) {
        if (move == null || !move.matches("^[A-Z]\\d+\\s+[A-Z]\\s+[A-Z]+$")) {
            return false;
        }
        Word word = Word.parseMove(move);
        return board.isWordLegal(word, frame);
    }

    // Look up all last formed words in the dictionary
    private boolean wordsInDictionary() {
        for (String word : Scoring.wordsFormed) {
            if (!dictionary.contains(word)) {
                return false;
            }
        }
        return true;
    }

    private void fillDictionary() {
        InputStream in = Resource.class.getResourceAsStream("sowpods.txt");
        Scanner sc = new Scanner(in);
        dictionary = new HashSet<>();
        while (sc.hasNext()) {
            dictionary.add(sc.next().toUpperCase());
        }
        sc.close();
    }

    // Pass move
    private void pass(Player player, boolean removeLastScore) {
        printToOutput(String.format("Turn passed for %s!", player.getName()));
        Scoring.passMove(removeLastScore);
        displayFrameScore(player, player.getFrame());
        checkLastSixScores();
    }

    // Award the score for a player's move
    private void scoreMove(String move, Player player, Frame frame) {
        Word word = Word.parseMove(move);
        board.placeWord(word, frame);
        Scoring.wordsFormed.clear();
        int score = Scoring.calculateScore(word, board);
        player.increaseScore(score);
        opponentScore = score; // current player is opponent for next player's move
        printToOutput("----------------------------");
        printToOutput("Word(s) placed: " + Scoring.wordsFormed.toString());
        printToOutput("Points awarded: " + score);
        printToOutput("----------------------------");
        try {
            frame.refillFrame();
        } catch (Exception e) {
            printToOutput(e.getMessage());
        }
        displayFrameScore(player, frame);
        printToOutput("Number of tiles in pool:" + pool.size());
        checkLastSixScores();
    }
}
