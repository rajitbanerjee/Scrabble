package game_engine;

import game.*;

import java.util.ArrayList;
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
    private static Board board;
    private static Pool pool;
    private static Frame frame1, frame2;

    public Scrabble() {
        board = new Board();
        pool = new Pool();
        frame1 = new Frame(pool);
        frame2 = new Frame(pool);
    }

    public static void main(String[] args) {
        // New game
        new Scrabble();
        // Print start message
        System.out.println("\nWelcome to Scrabble by DarkMode.");
        System.out.print("Player #1, please enter your name: ");
        Player player1 = new Player(sc.nextLine(), frame1);

        System.out.print("Player #2, please enter your name: ");
        Player player2 = new Player(sc.nextLine(), frame2);
        System.out.printf("\nWelcome %s and %s!", player1.getName(), player2);

        do {
            move(player1, frame1);
            move(player2, frame2);
        } while (!pool.isEmpty());
        sc.close();
    }

    private static void move(Player player, Frame frame) {
        // Display board, print frame content and helper message
        board.display();
        System.out.printf("\n%s, it's your turn!", player.getName());
        System.out.printf("\n%s's frame: ", player.getName());
        frame.printFrame();
        System.out.printf("%s's score: %d\n", player.getName(), player.getScore());
        promptUser();

        String move = sc.nextLine().toUpperCase().trim();
        while (!(move.equalsIgnoreCase("quit") || move.equalsIgnoreCase("pass") ||
                move.startsWith("EXCHANGE") || isMoveLegal(move, board, frame))) {
            System.out.println("Invalid move placement! Try again.");
            promptUser();
            move = sc.nextLine().trim().toUpperCase();
        }

        if (move.equalsIgnoreCase("quit")) {
            // Quit game
            System.out.println("\nThanks for playing!");
            System.exit(0);
        } else if (move.equalsIgnoreCase("pass")) {
            // Pass turn
            System.out.printf("\n\nTurn passed for %s!\n", player.getName());
        } else if (move.startsWith("EXCHANGE")) {
            // Exchange tiles
            String to_exchange = move.substring(move.indexOf(' ')).trim();
            System.out.printf("\nLetters (%s) have been exchanged!\n", to_exchange);
            frame.exchange(to_exchange);
            pool.printSize();
        } else {
            // If valid, parse inputs
            String[] inputArguments = move.split("\\s+");
            char column = inputArguments[0].charAt(0);
            int row = Integer.parseInt(inputArguments[0].substring(1));
            char orientation = inputArguments[1].charAt(0);
            String letters = inputArguments[2];
            Word word = new Word(letters, column, row, orientation);
            // Place word
            board.placeWord(word, frame);
            // Add word score
            int score = calculateScore(word);
            player.increaseScore(score);
            System.out.println("\n----------------------------");
            System.out.println("Word placed: " + word.getLetters());
            System.out.println("Points awarded: " + score);
            System.out.println("----------------------------\n");
            try {
                System.out.printf("%s's frame: ", player.getName());
                frame.printFrame();
                System.out.print("Refilled frame: ");
                frame.fillFrame();
                frame.printFrame();
                System.out.printf("%s's score: %d\n", player.getName(), player.getScore());
                pool.printSize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Helper message
    private static void promptUser() {
        System.out.println("\nEnter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\")");
        System.out.print("or QUIT/PASS/EXCHANGE <letters (no spaces)>: ");
    }

    /**
     * Check if the player enters a move in the correct format and
     * that the word placement is valid.
     *
     * @param move  the player's move (starting index, orientation, word to place)
     * @param board the 15x15 game Board of Squares
     * @param frame the player's frame
     * @return {@code true} if player's move is valid
     */
    private static boolean isMoveLegal(String move, Board board, Frame frame) {
        // Simple regex check that ensures input parsing won't fail
        if (move == null || !move.matches("^[A-Z]\\d+\\s+[A-Z]\\s+[A-Z]+$")) {
            return false;
        }
        // Parse move, and check validity
        String[] inputArguments = move.split("\\s+");
        char column = inputArguments[0].charAt(0);
        int row = Integer.parseInt(inputArguments[0].substring(1));
        char orientation = inputArguments[1].charAt(0);
        String letters = inputArguments[2];
        Word word = new Word(letters, column, row, orientation);
        return board.isWordLegal(word, frame);
    }

    private static int calculateScore(Word word) {
        ArrayList<Index> lastCoveredIndices = board.getLastCoveredIndices();
        int bonus = (lastCoveredIndices.size() == 7) ? 50 : 0;
        int score = extensionScore(word, lastCoveredIndices) +
                hookScore(word, lastCoveredIndices) +
                parallelScore(word, lastCoveredIndices) + bonus;
        lastCoveredIndices.clear();
        return score;
    }

    // WORKS!!
    private static int extensionScore(Word word, ArrayList<Index> lastCoveredIndices) {
        int score = 0;
        int wordMultiplier = 1;
        int row = word.getRow();
        int column = word.getColumn();
        if (word.isHorizontal()) {
            for (int i = column; i < column + word.length(); i++) {
                Square square = board.getBoard()[row][i];
                if (isRecentlyCovered(row, i, lastCoveredIndices)) {
                    score += square.getTile().getPoints() * square.getLetterMultiplier();
                    wordMultiplier *= square.getWordMultiplier();
                } else {
                    score += square.getTile().getPoints();
                }
            }
        } else {
            for (int i = row; i < row + word.length(); i++) {
                Square square = board.getBoard()[i][column];
                if (isRecentlyCovered(i, column, lastCoveredIndices)) {
                    score += square.getTile().getPoints() * square.getLetterMultiplier();
                    wordMultiplier *= square.getWordMultiplier();
                } else {
                    score += square.getTile().getPoints();
                }
            }

        }
        return score * wordMultiplier;
    }


    private static boolean isRecentlyCovered(int row, int column, ArrayList<Index> lastCoveredIndices) {
        return lastCoveredIndices.contains(new Index(row, column));
    }

    private static int hookScore(Word word, ArrayList<Index> lastCoveredIndices) {
        return 0;

    }

    private static int parallelScore(Word word, ArrayList<Index> lastCoveredIndices) {
        return 0;
    }


}
