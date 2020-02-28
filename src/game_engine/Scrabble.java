package game_engine;

import game.*;

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
    }

    private static void move(Player player, Frame frame) {
        // Display board, print frame content and helper message
        board.display();
        System.out.printf("\n%s, it's your turn!", player.getName());
        System.out.printf("\n%s's frame: ", player.getName());
        frame.printFrame();
        promptUser();
        // Strip white space at both sides, convert argument String to uppercase
        String move = sc.nextLine().toUpperCase().trim();
        while (!(move.equalsIgnoreCase("q") || isMoveLegal(move, board, frame))) {
            System.out.println("Invalid word placement! Try again.");
            promptUser();
            move = sc.nextLine().trim().toUpperCase();
        }
        // Press q/Q to quit
        if (move.equalsIgnoreCase("q")) {
            System.out.println("\nThanks for playing!");
            System.exit(0);
        }
        // If valid, parse inputs
        String[] inputArguments = move.split("\\s+");
        char column = inputArguments[0].charAt(0);
        int row = Integer.parseInt(inputArguments[0].substring(1));
        char orientation = inputArguments[1].charAt(0);
        String letters = inputArguments[2];
        Word word = new Word(letters, column, row, orientation);
        // Place word
        board.placeWord(word, frame);
        System.out.println("\n----------------------------");
        System.out.println("Word placed: " + word.getLetters());
        System.out.println("----------------------------\n");
        try {
            System.out.printf("%s's frame: ", player.getName());
            frame.printFrame();
            System.out.print("Refilled frame: ");
            frame.fillFrame();
            frame.printFrame();
            pool.printSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper message
    private static void promptUser() {
        System.out.println("\nEnter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\"), (q/Q to exit): ");
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
}
