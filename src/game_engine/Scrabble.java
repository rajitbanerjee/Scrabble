package game_engine;

import constants.GameConstants;
import constants.UIConstants;
import game.*;
import ui.CommandHistoryView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

import static constants.UIConstants.STATUS_CODE.*;

/**
 * Implement the scoring logic for a Scrabble game, supporting GUI.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class Scrabble {
    private Pool pool;
    private Board board;
    private Player player1, player2;
    private int opponentScore;
    private UIConstants.STATUS_CODE gameState;
    private boolean isChallengeSuccessful;
    private static CommandHistoryView historyView;
    private HashSet<String> dictionary;

    public Scrabble(CommandHistoryView historyView) {
        pool = new Pool();
        board = new Board();
        player1 = new Player(new Frame(pool));
        player2 = new Player(new Frame(pool));
        opponentScore = 0;
        gameState = P1_NAME; // initial game state is to ask player 1 for name
        isChallengeSuccessful = false;
        Scrabble.historyView = historyView;
        fillDictionary();
        printWelcome();
    }

    public Board getBoard() {
        return board;
    }

    public Frame getPlayer1Frame() {
        if (player1.getName() != null) {
            return player1.getFrame();
        } else {
            return null;
        }
    }

    public Frame getPlayer2Frame() {
        if (player2.getName() != null) {
            return player2.getFrame();
        } else {
            return null;
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public UIConstants.STATUS_CODE getGameState() {
        return gameState;
    }

    // Scan the SOWPODS dictionary file and store the words
    private void fillDictionary() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream in = classLoader.getResourceAsStream("resources/sowpods.txt");
        Scanner sc = new Scanner(Objects.requireNonNull(in));
        dictionary = new HashSet<>();
        while (sc.hasNext()) {
            dictionary.add(sc.next().toUpperCase());
        }
        sc.close();
    }

    // Display the welcome message
    private void printWelcome() {
        printToOutput("[Console] Welcome to Scrabble by DarkMode.");
        printToOutput("[Console] Player #1, please enter your name: ");
    }

    /**
     * Display the given to the command panel.
     *
     * @param text to be displayed
     */
    public static void printToOutput(String text) {
        if (historyView != null) {
            historyView.printText(text);
        }
    }

    /**
     * Respond to the text input from the user.
     *
     * @param command user's command in the text box
     * @return {@code true} if the board needs to be updated after the command processing
     * @throws InterruptedException if thread is interrupted while waiting, sleeping or occupied
     */
    public boolean processCommand(String command) throws InterruptedException {
        switch (gameState) {
            case P1_NAME:
                try {
                    player1.setName(command);
                    gameState = P2_NAME;
                    printToOutput("[Console] Player #2, please enter your name: ");
                } catch (IllegalArgumentException e) {
                    printToOutput("[Console] Player #1, please enter your name: ");
                }
                return false;
            case P2_NAME:
                try {
                    player2.setName(command);
                    gameState = P1_TURN;
                    startGame();
                } catch (IllegalArgumentException e) {
                    printToOutput("[Console] Player #2, please enter your name: ");
                }
                return false;
            default:
                Player player = (gameState == P1_TURN ? player1 : player2);
                Player opponent = (player.equals(player1) ? player2 : player1);
                command = command.trim().toUpperCase();
                if (isValidMove(command, player.getFrame())) {
                    makeMove(command, player, player.getFrame(), opponent);
                    if (isChallengeSuccessful) {
                        // Set variable back to false
                        isChallengeSuccessful = false;
                        askForMove(player);
                    } else {
                        gameState = (gameState == P1_TURN ? P2_TURN : P1_TURN);
                        askForMove(opponent);
                    }
                    return true;
                } else {
                    printDashes();
                    printToOutput("Invalid move! Try again.");
                    return false;
                }
        }
    }

    // Another welcome message and prompt user to enter move
    private void startGame() {
        printToOutput(String.format("Welcome %s and %s!", player1.getName(), player2.getName()));
        askForMove(player1);
    }

    // Prompt user to enter move
    private void askForMove(Player player) {
        Frame frame = player.getFrame();
        printToOutput(String.format("%s, it's your turn!", player.getName()));
        displayFrameScore(player, frame);
        promptUser();
    }

    // Display frame and score
    private void displayFrameScore(Player player, Frame frame) {
        printToOutput(String.format("%s's frame: ", player.getName()));
        printToOutput(frame.toString());
        printToOutput(String.format("%s's score: %d", player.getName(), player.getScore()));
        printDashes();
    }

    // Helper message
    private void promptUser() {
        printToOutput("Enter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\")");
        printToOutput("or QUIT/PASS/EXCHANGE <letters (no spaces)>/CHALLENGE: ");
    }

    // Check if a move is valid
    private boolean isValidMove(String move, Frame frame) {
        return move.equalsIgnoreCase("QUIT") ||
                move.equalsIgnoreCase("PASS") ||
                ((move.startsWith("EXCHANGE") && isExchangeLegal(move, frame))) ||
                move.equalsIgnoreCase("CHALLENGE") ||
                isPlacementLegal(move, frame);
    }

    // Check if an exchange is Valid
    // TODO: needs to be changed, bad programming practice
    private boolean isExchangeLegal(String move, Frame frame) {
        if (move.matches("EXCHANGE [A-Z-]+")) {
            try {
                Frame tempFrame = new Frame(pool);
                tempFrame.setFrame(new ArrayList<>(frame.getFrame()));
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

    // Check if a word placement move is legal
    private boolean isPlacementLegal(String move, Frame frame) {
        if (move == null || !move.matches("^[A-Z]\\d+\\s+[A-Z]\\s+[A-Z]+$")) {
            return false;
        }
        Word word = Word.parseMove(move);
        return board.isWordLegal(word, frame);
    }

    // Makes a valid move
    private void makeMove(String move, Player player, Frame frame, Player opponent)
            throws InterruptedException {
        printDashes();
        if (move.equalsIgnoreCase("QUIT")) {
            quit();
        } else if (move.equalsIgnoreCase("PASS")) {
            pass(player, false);
        } else if (move.startsWith("EXCHANGE")) {
            exchangeTiles(move, frame, false);
        } else if (move.equalsIgnoreCase("CHALLENGE")) {
            isChallengeSuccessful = challenge(opponent);
            if (isChallengeSuccessful) {
                // If challenge is successful, pass opponent's turn
                pass(opponent, true);
                return;
            } else {
                // Pass player's turn
                pass(player, false);
            }
        } else {
            scoreMove(move, player, frame);
        }
        if (isGameOver()) {
            quit();
        }
    }

    // Checks if the game is over
    private boolean isGameOver() {
        return pool.isEmpty() || (player1.getFrame().isEmpty() || player2.getFrame().isEmpty());
    }

    // Quit game
    // TODO Maybe set up a pop up box to display final scores
    private void quit() throws InterruptedException {
        printDashes();
        printToOutput("Final Scores:");
        printToOutput(String.format("%s's score: %d", player1.getName(), player1.getScore()));
        printToOutput(String.format("%s's score: %d", player2.getName(), player2.getScore()));
        int difference = player1.getScore() - player2.getScore();
        if (difference == 0) {
            printToOutput("Game is a tie!");
        } else {
            Player winner = (difference > 0) ? player1 : player2;
            printToOutput(String.format("%s wins the game! Well done.", winner.getName()));
        }
        printDashes();
        printToOutput("Thanks for playing!");
        // TODO wait() not best way to do this, idk how else to display final scores
        gameState = GAME_OVER;
        wait();
    }

    // Pass move
    private void pass(Player player, boolean removeLastScore) throws InterruptedException {
        printToOutput(String.format("Turn passed for %s!", player.getName()));
        Scoring.passMove(removeLastScore);
        displayFrameScore(player, player.getFrame());
        checkLastSixScores();
    }

    // Exchange tiles between frame and pool
    private void exchangeTiles(String move, Frame frame, boolean isTest) throws InterruptedException {
        String to_exchange = move.substring(move.indexOf(' ')).trim();
        frame.exchange(to_exchange);
        if (!isTest) {
            Scoring.addScoreToList(0);
            pool.printSize();
            printToOutput(String.format("Letters (%s) have been exchanged!", to_exchange));
            checkLastSixScores();
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
                printToOutput(String.format("Challenge successful! %s's tiles removed!",
                        opponent.getName()));
                if (board.isEmpty()) {
                    board.setFirstMove(true);
                }
                success = true;
            }
        }
        Scoring.challengeIndices.clear();
        return success;
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

    // Remove tile from board and put them back into frame
    private void removeTiles(Frame frame) {
        StringBuilder addToPool = new StringBuilder();
        int i = GameConstants.FRAME_LIMIT - Scoring.challengeIndices.size();
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

    // Award the score for a player's move
    private void scoreMove(String move, Player player, Frame frame) throws InterruptedException {
        Word word = Word.parseMove(move);
        board.placeWord(word, frame);
        Scoring.wordsFormed.clear();
        int score = Scoring.calculateScore(word, board);
        player.increaseScore(score);
        opponentScore = score; // current player is opponent for next player's move
        printToOutput("WORD(S) PLACED: " + Scoring.wordsFormed.toString());
        printToOutput("POINTS AWARDED: " + score);
        printDashes();
        try {
            frame.refillFrame();
        } catch (Exception e) {
            printToOutput(e.getMessage());
        }
        displayFrameScore(player, frame);
        pool.printSize();
        checkLastSixScores();
    }

    // End game if six consecutive scoreless moves occur
    private void checkLastSixScores() throws InterruptedException {
        // TODO remove printing scores later, added only for testing
        Scoring.printLastSixScores();
        if (Scoring.isLastSixZero()) {
            printToOutput("Six consecutive scoreless turns have occurred! Game over.");
            quit();
        }
    }

    // Print a line of dashes for design
    private void printDashes() {
        StringBuilder dash = new StringBuilder();
        for (int i = 0; i < UIConstants.DASH_LENGTH; i++) {
            dash.append("-");
        }
        printToOutput(dash.toString());
    }
}
