package game_engine;

import constants.Constants;
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
    private static ArrayList<String> wordsFormed = new ArrayList<>();

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

    /**
     * The given player makes a move.
     *
     * @param player the player making the move
     * @param frame  the player's frame
     */
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
            Word word = parseMove(move);
            board.placeWord(word, frame);
            wordsFormed.clear();
            int score = calculateScore(word);
            player.increaseScore(score);

            System.out.println("\n----------------------------");
            System.out.println("Word(s) placed: " + wordsFormed.toString());
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
        Word word = parseMove(move);
        return board.isWordLegal(word, frame);
    }

    /**
     * Parses the move from the expected String format to a word object.
     *
     * @param move the move to be parsed
     * @return word placement object
     */
    private static Word parseMove(String move) {
        String[] inputArguments = move.split("\\s+");
        char column = inputArguments[0].charAt(0);
        int row = Integer.parseInt(inputArguments[0].substring(1));
        char orientation = inputArguments[1].charAt(0);
        String letters = inputArguments[2];
        return new Word(letters, column, row, orientation);
    }

    /**
     * Finds the total score for move involving given word placement.
     *
     * @param word being placed on the board
     * @return score that the word placement is awarded
     */
    private static int calculateScore(Word word) {
        ArrayList<Index> lastCoveredIndices = board.getLastCoveredIndices();
        wordsFormed.add(word.getLetters());
        int bonus = (lastCoveredIndices.size() == Constants.FRAME_LIMIT) ? 50 : 0;
        int score = extensionScore(word, lastCoveredIndices) +
                parallelScore(word, lastCoveredIndices) + bonus;
        // reset recently covered indices after current score calculation
        lastCoveredIndices.clear();
        return score;
    }

    /**
     * Partial score awarded due to simple extension of existing words
     * on the board.
     * This type of play only scores the trivial (single)
     * word placement on the board.
     *
     * @param word               being placed on the board
     * @param lastCoveredIndices list of recently covered board indices
     * @return the extension score
     */
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

    /**
     * Partial score awarded due to extra words formed by a word placement,
     * apart from the trivial (single) word itself.
     *
     * @param word               being placed on the board
     * @param lastCoveredIndices list of recently covered board indices
     * @return the score of parallel/hook words formed
     */
    private static int parallelScore(Word word, ArrayList<Index> lastCoveredIndices) {
        int score = 0;
        Square[][] b = board.getBoard();

        if (word.isHorizontal()) {
            for (Index index : lastCoveredIndices) {
                int wordScore = 0;
                int wordMultiplier = 1;
                int startRow = index.getRow();
                int endRow = index.getRow();
                int column = index.getColumn();

                // find the starting row index of the word
                while (Square.isValid(startRow, column) && !b[startRow - 1][column].isEmpty()) {
                    startRow--;
                }
                // find the tail row index of the word
                while (Square.isValid(endRow, column) && !b[endRow + 1][column].isEmpty()) {
                    endRow++;
                }
                // add word score for any extra words formed
                if (startRow != endRow) {
                    for (int i = startRow; i <= endRow; i++) {
                        Square square = b[i][column];
                        if (i == index.getRow()) {
                            wordScore += square.getTile().getPoints() * square.getLetterMultiplier();
                            wordMultiplier *= square.getWordMultiplier();
                        } else {
                            wordScore += square.getTile().getPoints();
                        }
                    }
                    score += wordScore * wordMultiplier;
                    wordsFormed.add(board.getVerticalWord(column, startRow, endRow));
                }
            }
        } else {
            for (Index index : lastCoveredIndices) {
                int wordScore = 0;
                int wordMultiplier = 1;
                int startColumn = index.getColumn();
                int endColumn = index.getColumn();
                int row = index.getRow();

                // find the starting column index of the word
                while (Square.isValid(row, startColumn) && !b[row][startColumn - 1].isEmpty()) {
                    startColumn--;
                }
                // find the tail column index of the word
                while (Square.isValid(row, endColumn) && !b[row][endColumn + 1].isEmpty()) {
                    endColumn++;
                }
                // add word score for any extra words formed
                if (startColumn != endColumn) {
                    for (int i = startColumn; i <= endColumn; i++) {
                        Square square = b[row][i];
                        if (i == index.getColumn()) {
                            wordScore += square.getTile().getPoints() * square.getLetterMultiplier();
                            wordMultiplier *= square.getWordMultiplier();
                        } else {
                            wordScore += square.getTile().getPoints();
                        }
                    }
                    score += wordScore * wordMultiplier;
                    wordsFormed.add(board.getHorizontalWord(row, startColumn, endColumn));
                }
            }
        }
        return score;
    }

    /**
     * Checks if a given board index (row, column) has been recently covered.
     *
     * @param row                integer between 0 - 14 specifying the board row
     * @param column             integer between 0 - 14 specifying the board column
     * @param lastCoveredIndices list of recently covered board indices
     * @return {@code true} if a provided index has indeed been recently covered
     */
    private static boolean isRecentlyCovered(int row, int column, ArrayList<Index> lastCoveredIndices) {
        return lastCoveredIndices.contains(new Index(row, column));
    }

}
