import java.util.Scanner;

/**
 * Allows the user to play Scrabble in Single Player Mode
 * in the command line.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Initialise objects
        Board board = new Board();
        Pool pool = new Pool();
        Frame frame = new Frame(pool);
        // Print start message
        System.out.println("\nWelcome to Scrabble by DarkMode.");
        System.out.print("Please enter your name: ");
        Player p = new Player(sc.nextLine(), frame);
        System.out.printf("\nWelcome %s! This is Scrabble in Single Player Mode.", p.getName());

        do {
            // Display board, print frame content and helper message
            board.display();
            System.out.print("\nFrame: ");
            frame.printFrame();
            promptUser();
            // Strip white space at both sides, convert argument String to uppercase
            String move = sc.nextLine().toUpperCase().strip();
            while (!(move.equalsIgnoreCase("q") || isMoveLegal(move, board, frame))) {
                System.out.println("Invalid word placement! Try again.");
                promptUser();
                move = sc.nextLine().strip().toUpperCase();
            }
            // Press q/Q to quit
            if (move.equalsIgnoreCase("q")) {
                break;
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
                System.out.print("Frame: ");
                frame.printFrame();
                System.out.print("Refilled frame: ");
                frame.fillFrame();
                frame.printFrame();
                pool.printSize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
        sc.close();
        System.out.println("\nThanks for playing!");
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
