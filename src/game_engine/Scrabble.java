package game_engine;

import constants.GameConstants;
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
    private Pool pool = new Pool();
    private Board board = new Board();
    private Player player1 = new Player(new Frame(pool));
    private Player player2 = new Player(new Frame(pool));
    private int opponentScore = 0;
    private UIConstants.STATUS_CODE gameState = P1_NAME;
    private boolean isChallengeSuccessful = false;
    private HashSet<String> dictionary;

    public Scrabble() {
        fillDictionary();
        printWelcome();
    }

    /**
     * Display the given String to the command panel.
     *
     * @param text to be displayed
     */
    public static void printToOutput(String text) {
        try {
            CLIView.historyView.printText(text);
        } catch (Error ignored) {
            // Ignore printing errors that occur in unit testing
        }
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
     * Accessor for player 1's frame.
     *
     * @return Player 1's frame, null if player1 has not been initialised
     */
    public Frame getPlayer1Frame() {
        if (player1.getName() != null) {
            return player1.getFrame();
        } else {
            return null;
        }
    }

    /**
     * Accessor for Player 2's frame.
     *
     * @return Player2's frame, null if player2 has not been initialised
     */
    public Frame getPlayer2Frame() {
        if (player2.getName() != null) {
            return player2.getFrame();
        } else {
            return null;
        }
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

    // Display the welcome message
    private void printWelcome() {
        printToOutput("> Welcome to Scrabble by DarkMode.");
        printToOutput("> Player #1, please enter your name: ");
    }

    /**
     * Respond to the text input from the user.
     *
     * @param command user's command in the text box
     * @return {@code true} if the board needs to be updated after the command processing
     */
    public boolean processCommand(String command) {
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
                    player2.setName(command);
                    gameState = P1_TURN;
                    startGame();
                } catch (IllegalArgumentException e) {
                    printToOutput("> Player #2, please enter your name: ");
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
                } else if (command.equalsIgnoreCase("HELP")) {
                    PopupView.displayHelpPopup();
                    askForMove(player);
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
        printToOutput("> Enter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\")");
        printToOutput("or QUIT/PASS/EXCHANGE <letters (no spaces)>/CHALLENGE/HELP: ");
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
    private boolean isExchangeLegal(String move, Frame frame) {
        if (move.matches("EXCHANGE [A-Z-]+")) {
            try {
                Frame tempFrame = new Frame(pool);
                tempFrame.setFrame(new ArrayList<>(frame.getFrame()));
                exchange(move, tempFrame, true);
                return true;
            } catch (Exception e) {
                printToOutput("> " + e.getMessage());
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
        if (move.equalsIgnoreCase("QUIT")) {
            quit();
        } else if (move.equalsIgnoreCase("PASS")) {
            pass(player, false);
        } else if (move.startsWith("EXCHANGE")) {
            exchange(move, frame, false);
        } else if (move.equalsIgnoreCase("CHALLENGE")) {
            isChallengeSuccessful = challenge(opponent);
            if (isChallengeSuccessful) {
                // If challenge is successful, pass opponent's turn
                pass(opponent, true);
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

    // Quit game
    private void quit() {
        gameState = GAME_OVER;
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
        if (Scoring.challengeIndices.isEmpty()) {
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
                success = true;
            }
            Scoring.challengeIndices.clear();
        }
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
    private void scoreMove(String move, Player player, Frame frame) {
        Word word = Word.parseMove(move);
        board.placeWord(word, frame);
        Scoring.wordsFormed.clear();
        int score = Scoring.calculateScore(word, board);
        player.increaseScore(score);
        opponentScore = score; // current player is opponent for next player's move
        printToOutput("WORD(S) FORMED: " + Scoring.wordsFormed.toString());
        printToOutput("POINTS AWARDED: " + score);
        printDashes();
        try {
            frame.refillFrame();
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