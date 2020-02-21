import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialise objects
        Board board = new Board();
        Pool pool = new Pool();
        Frame frame = new Frame(pool);
        // Create Scanner instance for reading input
        Scanner sc = new Scanner(System.in);
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
            // Strip white space at both sides, convert argument string to uppercase
            String move = sc.nextLine().toUpperCase().strip();
            while (!(move.equalsIgnoreCase("q") || isMoveValid(move, board, frame))) {
                System.out.println("Invalid word placement! Try again.");
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
            String word = inputArguments[2];
            // Place word
            board.placeWord(column, row, orientation, word, frame);
            System.out.println("\n-------------------------");
            System.out.println("Word placed: " + word);
            System.out.println("-------------------------\n");
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
        System.out.println("Thanks for playing.");
    }

    private static void promptUser() {
        System.out.println("Enter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\"), (q/Q to exit): ");
    }

    /**
     * @param move  the player's move (starting index, orientation, word to place)
     * @param board the 15x15 game Board of Squares
     * @param frame the player's frame
     * @return {@code true} if player's move is valid
     */
    private static boolean isMoveValid(String move, Board board, Frame frame) {
        // Simple regex check that ensures input parsing won't fail
        if (move == null || !move.matches("^[A-Z]\\d+\\s+[A-Z]\\s+[A-Z]+$")) {
            return false;
        }
        // Parse move, and check validity
        String[] inputArguments = move.split("\\s+");
        char column = inputArguments[0].charAt(0);
        int row = Integer.parseInt(inputArguments[0].substring(1));
        char orientation = inputArguments[1].charAt(0);
        String word = inputArguments[2];
        return board.isWordPlacementValid(column, row, orientation, word, frame);
    }
}
