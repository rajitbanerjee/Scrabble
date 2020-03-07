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
    private static ArrayList<String> wordsFormed;
    private static ArrayList<Index> lastCoveredIndices;
    private static ArrayList<Index> challengeIndices;
    private static int previousScore;

    public Scrabble() {
        board = new Board();
        pool = new Pool();
        frame1 = new Frame(pool);
        frame2 = new Frame(pool);
        wordsFormed = new ArrayList<>();
        previousScore = 0;
        lastCoveredIndices = board.getLastCoveredIndices();
        challengeIndices = new ArrayList<>();
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
            makeMove(player1, frame1, player2);
            makeMove(player2, frame2, player1);
        } while (!(pool.isEmpty() &&
                (player1.getFrame().isEmpty() || player2.getFrame().isEmpty())));
        sc.close();
    }

    /**
     * The given player makes a move.
     *
     * @param player   the player making the move
     * @param frame    the player's frame
     * @param opponent the opponent of the current player
     */
    private static void makeMove(Player player, Frame frame, Player opponent) {
        String move = askForMove(player, frame);

        if (move.equalsIgnoreCase("QUIT")) {
            quit();
        } else if (move.equalsIgnoreCase("PASS")) {
            pass(player);
        } else if (move.startsWith("EXCHANGE")) {
            exchangeTiles(move, frame);
        } else if (move.equalsIgnoreCase("CHALLENGE")) {
            boolean isChallengeSuccessful = challenge(opponent);
            challengeIndices.clear();
            if (isChallengeSuccessful) {
                pass(opponent);
                displayFrameScore(opponent, opponent.getFrame());

                move = askForMove(player, frame);
                while (move.startsWith("CHALLENGE")) {
                    System.out.println("\nCannot challenge twice!");
                    move = askForMove(player, frame);
                }

                if (move.equalsIgnoreCase("QUIT")) {
                    quit();
                } else if (move.equalsIgnoreCase("PASS")) {
                    pass(player);
                } else if (move.startsWith("EXCHANGE")) {
                    exchangeTiles(move, frame);
                } else {
                    scoreMove(move, player, frame);
                }

            } else {
                pass(player);
            }
        } else {
            scoreMove(move, player, frame);
        }
    }

    // Ask player to enter move, and take and return valid input
    private static String askForMove(Player player, Frame frame) {
        board.display();
        System.out.printf("\n%s, it's your turn!", player.getName());
        displayFrameScore(player, frame);
        promptUser();

        String move = sc.nextLine().toUpperCase().trim();
        while (!(move.equalsIgnoreCase("QUIT") ||
                move.equalsIgnoreCase("PASS") ||
                move.startsWith("EXCHANGE") ||
                move.equalsIgnoreCase("CHALLENGE") ||
                isMoveLegal(move, board, frame))) {
            System.out.println("Invalid move placement! Try again.");
            promptUser();
            move = sc.nextLine().trim().toUpperCase();
        }
        return move;
    }

    // Helper message
    private static void promptUser() {
        System.out.println("\nEnter your move (E.g. \"H8 A HELLO\" or \"H10 D HI\")");
        System.out.print("or QUIT/PASS/EXCHANGE <letters (no spaces)>: ");
    }

    // Display frame and score
    private static void displayFrameScore(Player player, Frame frame) {
        System.out.printf("\n%s's frame: ", player.getName());
        frame.printFrame();
        System.out.printf("%s's score: %d\n", player.getName(), player.getScore());
    }

    // Quit game
    private static void quit() {
        System.out.println("\nThanks for playing!");
        System.exit(0);
    }

    // Pass move
    private static void pass(Player player) {
        System.out.printf("\n\nTurn passed for %s!\n", player.getName());
    }

    // Exchange tiles
    private static void exchangeTiles(String move, Frame frame) {
        String to_exchange = move.substring(move.indexOf(' ')).trim();
        System.out.printf("\nLetters (%s) have been exchanged!\n", to_exchange);
        frame.exchange(to_exchange);
        pool.printSize();
    }

    // Challenge previous move
    private static boolean challenge(Player opponent) {
        boolean success = false;
        if (challengeIndices.isEmpty()) {
            System.out.println("\nCannot challenge! No word placed by opponent.");
        } else {
            System.out.print("\n\nIs challenge successful? (y/n): ");
            char op = sc.nextLine().toLowerCase().charAt(0);
            if (op == 'y') {
                // manually implement challenge for opponent's move
                removeTiles(opponent.getFrame());
                opponent.decreaseScore(previousScore);
                System.out.println("\nOpponent's tiles removed!");
                if (board.isEmpty()) {
                    board.setFirstMove(true);
                }
                success = true;
            } else {
                System.out.println("\nChallenge unsuccessful!");
            }
        }
        return success;
    }

    // Challenge successful - remove opponent's tiles
    private static void removeTiles(Frame frame) {
        StringBuilder addToPool = new StringBuilder();
        int i = Constants.FRAME_LIMIT - challengeIndices.size();
        for (Index index : challengeIndices) {
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

    // Do the scoring for the player's move
    private static void scoreMove(String move, Player player, Frame frame) {
        Word word = parseMove(move);
        board.placeWord(word, frame);
        wordsFormed.clear();
        previousScore = 0;
        lastCoveredIndices = board.getLastCoveredIndices();
        int score = calculateScore(word, lastCoveredIndices);
        player.increaseScore(score);
        previousScore = score;

        System.out.println("\n----------------------------");
        System.out.println("Word(s) placed: " + wordsFormed.toString());
        System.out.println("Points awarded: " + score);
        System.out.println("----------------------------\n");

        try {
            frame.fillFrame();
            displayFrameScore(player, frame);
            pool.printSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        challengeIndices.clear();
        challengeIndices.addAll(lastCoveredIndices);
        lastCoveredIndices.clear();
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
    private static int calculateScore(Word word, ArrayList<Index> lastCoveredIndices) {
        wordsFormed.add(word.getLetters());
        int bonus = (lastCoveredIndices.size() == Constants.FRAME_LIMIT) ? 50 : 0;
        return mainWordScore(word, lastCoveredIndices) +
                parallelScore(word, lastCoveredIndices) + bonus;
    }

    /**
     * Partial score awarded to the main word (newly formed word
     * which contains the greatest number of newly placed tiles).
     *
     * @param word               being placed on the board
     * @param lastCoveredIndices list of recently covered board indices
     * @return the extension score
     */
    private static int mainWordScore(Word word, ArrayList<Index> lastCoveredIndices) {
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
                while (Square.isValid(startRow - 1, column) && !b[startRow - 1][column].isEmpty()) {
                    startRow--;
                }
                // find the tail row index of the word
                while (Square.isValid(endRow + 1, column) && !b[endRow + 1][column].isEmpty()) {
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
                while (Square.isValid(row, startColumn - 1) && !b[row][startColumn - 1].isEmpty()) {
                    startColumn--;
                }
                // find the tail column index of the word
                while (Square.isValid(row, endColumn + 1) && !b[row][endColumn + 1].isEmpty()) {
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
