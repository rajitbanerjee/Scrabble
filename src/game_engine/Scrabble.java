package game_engine;

import constants.Constants;
import game.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Two-player Scrabble implementation
 * in the command line.
 *
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @team DarkMode
 */
public class Scrabble {
    private static Scanner sc = new Scanner(System.in);
    private static Pool pool;
    private static Board board;
    private static Player player1, player2;

    private static HashSet<String> dictionary;
    private static int opponentScore;

    public Scrabble() {
        pool = new Pool();
        board = new Board();
        System.out.println("\nWelcome to Scrabble by DarkMode.");
        System.out.print("Player #1, please enter your name: ");
        player1 = new Player(sc.nextLine(), new Frame(pool));
        System.out.print("Player #2, please enter your name: ");
        player2 = new Player(sc.nextLine(), new Frame(pool));
        System.out.printf("\nWelcome %s and %s!", player1.getName(), player2.getName());
        opponentScore = 0;
        fillDictionary();
    }

    // Scan the SOWPODS dictionary file and store the words
    private static void fillDictionary() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream in = classLoader.getResourceAsStream("resources/sowpods.txt");
        assert in != null;
        Scanner sc = new Scanner(in);
        dictionary = new HashSet<>();
        while (sc.hasNext()) {
            dictionary.add(sc.next().toUpperCase());
        }
        sc.close();
    }

    public static void main(String[] args) {
        // New game
        new Scrabble();
        while (!isGameOver()) {
            makeMove(player1, player1.getFrame(), player2);
            makeMove(player2, player2.getFrame(), player1);
        }
        sc.close();
    }

    // Checks if the game is over
    private static boolean isGameOver() {
        return pool.isEmpty() && (player1.getFrame().isEmpty() || player2.getFrame().isEmpty());
    }

    // Current player makes a move against their opponent
    private static void makeMove(Player player, Frame frame, Player opponent) {
        String move = askForMove(player, frame);
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
                makeMove(player, frame, opponent);
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

    // Ask the current player to enter their move
    private static String askForMove(Player player, Frame frame) {
        board.display();
        System.out.printf("\n%s, it's your turn!", player.getName());
        displayFrameScore(player, frame);
        promptUser();
        String move = sc.nextLine().toUpperCase().trim();
        while (!(move.equalsIgnoreCase("QUIT") ||
                move.equalsIgnoreCase("PASS") ||
                (move.startsWith("EXCHANGE") && isExchangeLegal(move, frame)) ||
                move.equalsIgnoreCase("CHALLENGE") ||
                isMoveLegal(move, board, frame))) {
            System.out.println("\nInvalid move! Try again.");
            promptUser();
            move = sc.nextLine().trim().toUpperCase();
        }
        return move;
    }

    // Display frame and score
    private static void displayFrameScore(Player player, Frame frame) {
        System.out.printf("\n%s's frame: ", player.getName());
        frame.printFrame();
        System.out.printf("%s's score: %d\n", player.getName(), player.getScore());
    }

    // Helper message
    private static void promptUser() {
        System.out.println("\nEnter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\")");
        System.out.print("or QUIT/PASS/EXCHANGE <letters (no spaces)>/CHALLENGE: ");
    }

    // Check that tile exchange command is legal
    private static boolean isExchangeLegal(String move, Frame frame) {
        if (move.matches("EXCHANGE [A-Z-]+")) {
            try {
                Frame tempFrame = new Frame(pool);
                tempFrame.setFrame(new ArrayList<>(frame.getFrame()));
                exchangeTiles(move, tempFrame, true);
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        } else {
            System.out.println("\nExchange must of the format: EXCHANGE <letters (no spaces)>");
            return false;
        }
    }

    // Check move input format validity and word placement validity
    private static boolean isMoveLegal(String move, Board board, Frame frame) {
        if (move == null || !move.matches("^[A-Z]\\d+\\s+[A-Z]\\s+[A-Z]+$")) {
            return false;
        }
        Word word = parseMove(move);
        return board.isWordLegal(word, frame);
    }

    // Parses the move from the expected String format to a word object
    private static Word parseMove(String move) {
        String[] inputArguments = move.split("\\s+");
        char column = inputArguments[0].charAt(0);
        int row = Integer.parseInt(inputArguments[0].substring(1));
        char orientation = inputArguments[1].charAt(0);
        String letters = inputArguments[2];
        return new Word(letters, column, row, orientation);
    }

    // Quit game
    private static void quit() {
        System.out.println("---------------------------------------------------------");
        System.out.println("Final Scores:");
        System.out.printf("\n%s's score: %d", player1.getName(), player1.getScore());
        System.out.printf("\n%s's score: %d\n", player2.getName(), player2.getScore());
        int difference = player1.getScore() - player2.getScore();
        if (difference == 0) {
            System.out.println("\nGame is a tie!");
        } else {
            Player winner = (difference > 0) ? player1 : player2;
            System.out.printf("\n%s wins the game! Well done.\n", winner.getName());
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("\nThanks for playing!");
        System.exit(0);
    }

    // Pass move
    private static void pass(Player player, boolean removeLastScore) {
        System.out.printf("\nTurn passed for %s!\n", player.getName());
        Scoring.passMove(removeLastScore);
        displayFrameScore(player, player.getFrame());
        checkLastSixScores();
    }

    // Exchange tiles between frame and pool
    private static void exchangeTiles(String move, Frame frame, boolean isTest) {
        String to_exchange = move.substring(move.indexOf(' ')).trim();
        frame.exchange(to_exchange);
        if (!isTest) {
            Scoring.addScoreToList(0);
            pool.printSize();
            System.out.printf("\nLetters (%s) have been exchanged!\n", to_exchange);
            checkLastSixScores();
        }
    }

    // Challenge opponent's previous move and change scores accordingly
    private static boolean challenge(Player opponent) {
        boolean success = false;
        if (Scoring.challengeIndices.isEmpty()) {
            System.out.println("\nCannot challenge! No word placed by opponent.");
        } else {
            if (wordsInDictionary()) {
                System.out.println("\nChallenge unsuccessful!");
            } else {
                removeTiles(opponent.getFrame());
                opponent.decreaseScore(opponentScore);
                System.out.printf("\n\nChallenge successful! %s's tiles removed!", opponent.getName());
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
    private static boolean wordsInDictionary() {
        for (String word : Scoring.wordsFormed) {
            if (!dictionary.contains(word)) {
                return false;
            }
        }
        return true;
    }

    // Remove tile from board and put them back into frame
    private static void removeTiles(Frame frame) {
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

    // Award the score for a player's move
    private static void scoreMove(String move, Player player, Frame frame) {
        Word word = parseMove(move);
        board.placeWord(word, frame);
        Scoring.wordsFormed.clear();
        int score = Scoring.calculateScore(word, board);
        player.increaseScore(score);
        opponentScore = score; // current player is opponent for next player's move
        System.out.println("\n----------------------------");
        System.out.println("Word(s) placed: " + Scoring.wordsFormed.toString());
        System.out.println("Points awarded: " + score);
        System.out.println("----------------------------\n");
        try {
            frame.refillFrame();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        displayFrameScore(player, frame);
        pool.printSize();
        checkLastSixScores();
    }

    // End game if six consecutive scoreless moves occur
    public static void checkLastSixScores() {
        // TODO remove printing scores later, added only for testing
        Scoring.printLastSixScores();
        if (Scoring.isLastSixZero()) {
            System.out.println("\nSix consecutive scoreless turns have occurred! Game over.");
            quit();
        }
    }

}
