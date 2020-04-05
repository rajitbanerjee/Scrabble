package game_engine;

import constants.UIConstants;
import game.*;
import ui.CLIView;
import ui.PopupView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

import static constants.UIConstants.STATUS_CODE.*;

/**
 * Integrates all game components to create a 2 player Scrabble game.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class Scrabble {
    private Pool pool;
    private Board board;
    private Player player1;
    private Player player2;
    private String drawnTiles;
    private int opponentScore;
    private boolean isChallengeSuccessful;
    private UIConstants.STATUS_CODE gameState;
    private HashSet<String> dictionary;
    private GameController controller;

    /**
     * Starts a new game of Scrabble.
     */
    public Scrabble() {
        resetGame();
        fillDictionary();
    }

    /**
     * Display the given String to the command panel.
     *
     * @param text to be displayed
     */
    public static void printToOutput(String text) {
        try {
            CLIView.HISTORY_VIEW.printText(text);
        } catch (Error ignored) {
            // Ignore printing errors due to method call in JUnit tests
        }
    }

    /**
     * Sets the controller for the current game.
     *
     * @param controller GUI controller for the current game
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    // Resets the game
    private void resetGame() {
        pool = new Pool();
        board = new Board();
        player1 = new Player(new Frame(pool));
        player2 = new Player(new Frame(pool));
        drawnTiles = "";
        opponentScore = 0;
        isChallengeSuccessful = false;
        gameState = P1_NAME;
        CLIView.clearHistoryView();
        printWelcome();
    }

    // Display the welcome message
    private void printWelcome() {
        printToOutput("> Welcome to Scrabble by DarkMode.");
        printToOutput("> Player #1, please enter your name: ");
    }

    /**
     * Accessor for board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Check if both players have entered their names.
     *
     * @return {@code true} if both players have names
     */
    public boolean arePlayersReady() {
        return player1.getName() != null && player2.getName() != null;
    }

    /**
     * Accessor for player1.
     *
     * @return player1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Accessor for player2.
     *
     * @return player2
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Accessor for the game's state.
     *
     * @return the status code of the game at present
     */
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

    /**
     * Respond to the text input from the user.
     *
     * @param command user's command in the text box
     * @return {@code true} if the board needs to be updated after the command processing
     */
    public boolean processCommand(String command) {
        // Process help and restart commands
        if (command.equalsIgnoreCase("HELP")) {
            PopupView.displayHelpPopup();
            return false;
        } else if (command.equalsIgnoreCase("RESTART")) {
            boolean restart = PopupView.displayRestartPopup();
            if (restart) {
                resetGame();
                Scoring.reset();
            }
            return restart;
        }
        // Process other commands
        Scrabble.printToOutput(command);
        switch (gameState) {
            case P1_NAME:
                try {
                    player1.setName(command);
                    gameState = P2_NAME;
                    printToOutput("> Player #2, please enter your name: ");
                } catch (IllegalArgumentException e) {
                    printToOutput("> Player #1, please enter your name: ");
                }
                return false;
            case P2_NAME:
                try {
                    Player.validateNames(player1.getName(), command);
                    player2.setName(command);
                    gameState = P1_TURN;
                    startGame();
                } catch (IllegalArgumentException e) {
                    printToOutput(e.getMessage());
                    printToOutput("> Player #2, please enter your name: ");
                }
                return false;
            default:
                Player player = (gameState == P1_TURN ? player1 : player2);
                Player opponent = (player.equals(player1) ? player2 : player1);
                // Process the NAME command (to change a player's name)
                try {
                    if (command.toUpperCase().startsWith("NAME")) {
                        String newName = command.substring(5);
                        Player.validateNames(newName, opponent.getName());
                        player.setName(newName);
                        askForMove(player);
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    printToOutput(e.getMessage());
                    return false;
                }
                command = command.toUpperCase();
                // Process other commands
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
                    printToOutput("> Invalid move! Try again.");
                    promptUser();
                    return false;
                }
        }
    }

    // Another welcome message and prompt user to enter move
    private void startGame() {
        printToOutput(String.format("> Welcome %s and %s!", player1.getName(), player2.getName()));
        askForMove(player1);
    }

    // Prompt user to enter move
    private void askForMove(Player player) {
        printDashes();
        printToOutput(String.format("> %s, it's your turn!", player.getName()));
        promptUser();
    }

    // Helper message
    private void promptUser() {
        printToOutput("> Enter your move (HELP for details): ");
    }

    // Check if a move is valid
    private boolean isValidMove(String move, Frame frame) {
        return move.equals("QUIT") || move.equals("PASS") ||
                ((move.startsWith("EXCHANGE") && isExchangeLegal(move, frame))) ||
                move.equals("CHALLENGE") || isPlacementLegal(move, frame);
    }

    // Check if an exchange is Valid
    private boolean isExchangeLegal(String move, Frame frame) {
        if (move.matches("EXCHANGE [A-Z-]+")) {
            try {
                Frame tempFrame = new Frame(pool);
                tempFrame.setFrame(new ArrayList<>(frame.getFrame()));
                exchange(move, tempFrame, true);
                return true;
            } catch (Exception e) {
                printToOutput(e.getMessage());
                return false;
            }
        } else {
            printToOutput("> Exchange must of the format: EXCHANGE <letters (no spaces)>");
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
    private void makeMove(String move, Player player, Frame frame, Player opponent) {
        printDashes();
        if (move.equals("QUIT")) {
            quit();
        } else if (move.equals("PASS")) {
            pass(player, false);
        } else if (move.startsWith("EXCHANGE")) {
            exchange(move, frame, false);
        } else if (move.equals("CHALLENGE")) {
            isChallengeSuccessful = challenge(opponent);
            if (isChallengeSuccessful) {
                // If challenge is successful, pass opponent's turn
                pass(opponent, true);
            } else {
                // Pass player's turn
                pass(player, false);
            }
        } else {
            placeAndScore(move, player, frame);
        }
        if (isGameOver()) {
            quit();
        }
    }

    // Quit game
    private void quit() {
        gameState = GAME_OVER;
        // Final move display before closing game
        controller.updateGame(null);
        PopupView.displayQuitPopup(player1, player2);
        System.exit(0);
    }

    // Pass move
    private void pass(Player player, boolean removeLastScore) {
        printToOutput(String.format("> Turn passed for %s!", player.getName()));
        Scoring.passMove(removeLastScore);
    }

    // Exchange tiles between frame and pool
    private void exchange(String move, Frame frame, boolean isTest) {
        String to_exchange = move.substring(move.indexOf(' ')).trim();
        String newLetters = frame.exchange(to_exchange);
        if (!isTest) {
            Scoring.addScoreToList(0);
            Scrabble.printToOutput("> Number of tiles in pool: " + pool.size());
            printToOutput(String.format("> Letters (%s) exchanged with (%s)!", to_exchange, newLetters));
        }
    }

    // Challenge opponent's previous move and change scores accordingly
    private boolean challenge(Player opponent) {
        boolean success = false;
        if (Scoring.CHALLENGE_INDICES.isEmpty()) {
            printToOutput("> Cannot challenge! No word placed by opponent.");
        } else {
            if (wordsInDictionary()) {
                printToOutput("> Challenge unsuccessful!");
            } else {
                removeTiles(opponent.getFrame());
                opponent.decreaseScore(opponentScore);
                printToOutput(String.format("> Challenge successful! %s's tiles removed!",
                        opponent.getName()));
                if (board.isEmpty()) {
                    board.setFirstMove(true);
                }
                Scrabble.printToOutput("> Number of tiles in pool: " + pool.size());
                success = true;
            }
            Scoring.CHALLENGE_INDICES.clear();
        }
        return success;
    }

    // Look up all last formed words in the dictionary
    private boolean wordsInDictionary() {
        for (String word : Scoring.WORDS_FORMED) {
            if (!dictionary.contains(word)) {
                return false;
            }
        }
        return true;
    }

    // Remove challenged tiles from board and put them back into frame
    private void removeTiles(Frame frame) {
        // Remove drawn tiles from frame and put them back in pool
        StringBuilder addToPool = new StringBuilder();
        int size = frame.getFrame().size();
        for (int j = 1; j <= drawnTiles.length(); j++) {
            addToPool.append(frame.getFrame().remove(size - j).getType());
        }
        if (addToPool.length() > 0) {
            pool.addTiles(addToPool.toString());
        }
        // Remove tiles from board and put them back in frame
        for (Index index : Scoring.CHALLENGE_INDICES) {
            int row = index.getRow();
            int column = index.getColumn();
            Tile tile = board.getBoard()[row][column].getTile();
            if (tile.getPoints() == 0) {
                tile.setType('-');
            }
            frame.getFrame().add(tile);
            board.getBoard()[row][column].setTile(null);
        }
    }

    // Place word and award the score for a player's move
    private void placeAndScore(String move, Player player, Frame frame) {
        Word word = Word.parseMove(move);
        board.placeWord(word, frame);
        Scoring.WORDS_FORMED.clear();
        int score = Scoring.calculateScore(word, board);
        player.increaseScore(score);
        opponentScore = score; // Current player is opponent for next player's move
        printToOutput("WORD(S) FORMED: " + Scoring.WORDS_FORMED.toString());
        printToOutput("POINTS AWARDED: " + score);
        printDashes();
        try {
            drawnTiles = frame.refillFrame();
        } catch (Exception e) {
            printToOutput("> " + e.getMessage());
        }
        Scrabble.printToOutput("> Number of tiles in pool: " + pool.size());
    }

    // Checks if the game is over
    private boolean isGameOver() {
        checkLastSixScores();
        return (player1.getFrame().isEmpty() || player2.getFrame().isEmpty()) && pool.isEmpty();
    }

    // End game if six consecutive scoreless moves occur
    private void checkLastSixScores() {
        Scoring.printLastSixScores();
        if (Scoring.isLastSixZero()) {
            printToOutput("> Six consecutive scoreless turns have occurred! Game over.");
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