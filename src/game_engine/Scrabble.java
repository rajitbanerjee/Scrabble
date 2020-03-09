package game_engine;

import constants.Constants;
import game.*;

import java.io.InputStream;
import java.util.*;

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
    private static ArrayDeque<Integer> lastSixScores;
    private static int opponentScore;
    private static int numberOfTurns;
    private static HashSet<String> dictionary;

    public Scrabble() {
        board = new Board();
        pool = new Pool();
        frame1 = new Frame(pool);
        frame2 = new Frame(pool);
        wordsFormed = new ArrayList<>();
        challengeIndices = new ArrayList<>();
        lastSixScores = new ArrayDeque<>();
        opponentScore = 0;
        numberOfTurns = 0;
        fillDictionary();
    }

    // Scan the SOWPODS dictionary file and store the words
    private static void fillDictionary() {
        InputStream in = Scrabble.class.getResourceAsStream("sowpods.txt");
        Scanner sc = new Scanner(in);
        dictionary = new HashSet<>();
        while (sc.hasNext()) {
            dictionary.add(sc.next().toUpperCase());
        }
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

    // Current player makes a move against their opponent
    private static void makeMove(Player player, Frame frame, Player opponent) {
        String move = askForMove(player, frame);
        if (move.equalsIgnoreCase("QUIT")) {
            quit();
        } else if (move.equalsIgnoreCase("PASS")) {
            pass(player, false);
        } else if (move.startsWith("EXCHANGE")) {
            exchangeTiles(move, frame);
        } else if (move.equalsIgnoreCase("CHALLENGE")) {
            boolean isChallengeSuccessful = challenge(opponent);
            if (isChallengeSuccessful) {
                pass(opponent, true);
                displayFrameScore(opponent, opponent.getFrame());

                move = askForMove(player, frame);
                while (move.startsWith("CHALLENGE")) {
                    System.out.println("\nCannot challenge twice!");
                    move = askForMove(player, frame);
                }

                if (move.equalsIgnoreCase("QUIT")) {
                    quit();
                } else if (move.equalsIgnoreCase("PASS")) {
                    pass(player, false);
                } else if (move.startsWith("EXCHANGE")) {
                    exchangeTiles(move, frame);
                } else {
                    scoreMove(move, player, frame);
                }
            } else {
                pass(player, false);
            }
        } else {
            scoreMove(move, player, frame);
        }
        numberOfTurns++;
        checkLastSixScores();
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
                move.startsWith("EXCHANGE") ||
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
        System.out.print("or QUIT/PASS/EXCHANGE <letters (no spaces)>: ");
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
        System.out.println("\nThanks for playing!");
        System.exit(0);
    }

    // Pass move
    private static void pass(Player player, boolean removeLastScore) {
        System.out.printf("\n\nTurn passed for %s!\n", player.getName());
        if (removeLastScore) {
            lastSixScores.removeLast();
        }
        lastSixScores.addLast(0);
    }

    // Exchange tiles between frame and pool
    private static void exchangeTiles(String move, Frame frame) {
        String to_exchange = move.substring(move.indexOf(' ')).trim();
        System.out.printf("\nLetters (%s) have been exchanged!\n", to_exchange);
        frame.exchange(to_exchange);
        pool.printSize();
        lastSixScores.addLast(0);
    }

    // Challenge opponent's previous move and change scores accordingly
    private static boolean challenge(Player opponent) {
        boolean success = false;
        if (challengeIndices.isEmpty()) {
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
        challengeIndices.clear();
        return success;
    }

    // Look up all last formed words in the dictionary
    private static boolean wordsInDictionary() {
        for (String word : wordsFormed) {
            if (!dictionary.contains(word)) {
                return false;
            }
        }
        return true;
    }

    // Remove tile from board and put them back into frame
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

    // Award the score for a player's move
    private static void scoreMove(String move, Player player, Frame frame) {
        Word word = parseMove(move);
        board.placeWord(word, frame);
        wordsFormed.clear();
        opponentScore = 0;
        lastCoveredIndices = board.getLastCoveredIndices();
        int score = calculateScore(word);
        player.increaseScore(score);
        opponentScore = score;
        lastSixScores.addLast(score);

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

    // Finds the total score for move involving given word placement
    private static int calculateScore(Word word) {
        int bonus = (lastCoveredIndices.size() == Constants.FRAME_LIMIT) ? 50 : 0;
        return mainWordScore(word) + extraWordScore(word) + bonus;
    }

    // Main word score (newly formed word with greatest number of newly placed tiles)
    private static int mainWordScore(Word word) {
        int score = 0;
        int wordMultiplier = 1;
        int row = word.getRow();
        int column = word.getColumn();
        wordsFormed.add(word.getLetters());
        if (word.isHorizontal()) {
            for (int i = column; i < column + word.length(); i++) {
                Square square = board.getBoard()[row][i];
                if (isRecentlyCovered(row, i)) {
                    score += square.getTile().getPoints() * square.getLetterMultiplier();
                    wordMultiplier *= square.getWordMultiplier();
                } else {
                    score += square.getTile().getPoints();
                }
            }
        } else {
            for (int i = row; i < row + word.length(); i++) {
                Square square = board.getBoard()[i][column];
                if (isRecentlyCovered(i, column)) {
                    score += square.getTile().getPoints() * square.getLetterMultiplier();
                    wordMultiplier *= square.getWordMultiplier();
                } else {
                    score += square.getTile().getPoints();
                }
            }

        }
        return score * wordMultiplier;
    }

    // Checks if a given board index (row, column) has been recently covered
    private static boolean isRecentlyCovered(int row, int column) {
        return lastCoveredIndices.contains(new Index(row, column));
    }

    // Scores the extra words formed by a word placement
    private static int extraWordScore(Word word) {
        if (word.isHorizontal()) {
            return scoreVerticalExtraWords();
        } else {
            return scoreHorizontalExtraWords();
        }
    }

    // Scores all the words hooked/parallel to a horizontally placed word
    private static int scoreVerticalExtraWords() {
        int score = 0;
        Square[][] b = board.getBoard();
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
        return score;
    }

    // Scores all the words hooked/parallel to a vertically placed word
    private static int scoreHorizontalExtraWords() {
        int score = 0;
        Square[][] b = board.getBoard();
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
        return score;
    }

    // end game if six consecutive scoreless moves
    private static void checkLastSixScores() {
        if (lastSixScores.size() > 6) {
            lastSixScores.removeFirst();
        }
        // TODO remove printing scores later, added only for testing
        System.out.println("\nLast six scores: " + lastSixScores.toString());
        if (numberOfTurns >= 6 && sumLastSixScores() == 0) {
            System.out.println("\nSix consecutive scoreless turns have occurred! Game over.");
            quit();
        }
    }

    // add up the scores for the last six moves
    private static int sumLastSixScores() {
        int total = 0;
        for (Integer score : lastSixScores) {
            total += score;
        }
        return total;
    }

}
