package logic;

import game.*;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Implement the scoring logic for a Scrabble game.
 *
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * Team 15: DarkMode
 */
public class Scoring {
    public static final ArrayList<Index> CHALLENGE_INDICES = new ArrayList<>();
    public static final ArrayList<String> WORDS_FORMED = new ArrayList<>();
    public static final ArrayList<Index> LAST_COVERED_INDICES = new ArrayList<>();
    private static final ArrayDeque<Integer> LAST_SIX_SCORES = new ArrayDeque<>();

    /**
     * Resets the game scoring.
     */
    public static void reset() {
        CHALLENGE_INDICES.clear();
        WORDS_FORMED.clear();
        LAST_COVERED_INDICES.clear();
        LAST_SIX_SCORES.clear();
    }

    /**
     * Performs the operations required during a pass move.
     *
     * @param removeLastScore indicates whether the last score
     *                        in the list needs to be removed
     *                        before adding a 0 score for the pass move
     */
    public static void passMove(boolean removeLastScore) {
        if (removeLastScore) {
            LAST_SIX_SCORES.removeLast();
        }
        addScoreToList(0);
    }

    /**
     * Adds a given score to the list of last six scores of the game.
     * Also clear the challenge indices list if score is 0.
     *
     * @param score to be added to the list
     */
    public static void addScoreToList(int score) {
        LAST_SIX_SCORES.addLast(score);
        if (LAST_SIX_SCORES.size() > 6) {
            LAST_SIX_SCORES.removeFirst();
        }
        if (score == 0) {
            CHALLENGE_INDICES.clear();
        }
    }

    /**
     * Finds the total score for move involving given word placement.
     *
     * @param word  main word placed whose score is to be calculated
     * @param board the game board
     * @return the score awarded after placing the given word
     */
    public static int calculateScore(Word word, Board board) {
        int bonus = (LAST_COVERED_INDICES.size() == Frame.LIMIT) ? 50 : 0;
        int score = mainWordScore(word, board) + extraWordScore(word, board) + bonus;
        CHALLENGE_INDICES.clear();
        CHALLENGE_INDICES.addAll(LAST_COVERED_INDICES);
        addScoreToList(score);
        return score;
    }

    // Main word score (newly formed word with greatest number of newly placed tiles)
    private static int mainWordScore(Word word, Board board) {
        int score = 0;
        int wordMultiplier = 1;
        int row = word.getRow();
        int column = word.getColumn();
        WORDS_FORMED.add(word.getLetters());
        for (int i = 0; i < word.length(); i++) {
            Square square = board.getBoard()[row][column];
            if (isRecentlyCovered(row, column)) {
                score += square.getTile().getPoints() * square.getLetterMultiplier();
                wordMultiplier *= square.getWordMultiplier();
            } else {
                score += square.getTile().getPoints();
            }
            if (word.isHorizontal()) {
                column++;
            } else {
                row++;
            }
        }
        return score * wordMultiplier;
    }

    // Checks if a given board index (row, column) has been recently covered
    private static boolean isRecentlyCovered(int row, int column) {
        return LAST_COVERED_INDICES.contains(new Index(row, column));
    }

    // Scores the extra words formed by a word placement
    private static int extraWordScore(Word word, Board board) {
        if (word.isHorizontal()) {
            return scoreVerticalExtraWords(board);
        } else {
            return scoreHorizontalExtraWords(board);
        }
    }

    // Scores all the words hooked/parallel to a horizontally placed word
    private static int scoreVerticalExtraWords(Board board) {
        int score = 0;
        Square[][] b = board.getBoard();
        for (Index index : LAST_COVERED_INDICES) {
            int wordScore = 0;
            int wordMultiplier = 1;
            int startRow = index.getRow();
            int endRow = index.getRow();
            int column = index.getColumn();

            // Find the starting row index of the word
            while (Square.isValid(column, startRow - 1) && !b[startRow - 1][column].isEmpty()) {
                startRow--;
            }
            // Find the tail row index of the word
            while (Square.isValid(column, endRow + 1) && !b[endRow + 1][column].isEmpty()) {
                endRow++;
            }
            // Add word score for any extra words formed
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
                WORDS_FORMED.add(board.getVerticalWord(column, startRow, endRow));
            }
        }
        return score;
    }

    // Scores all the words hooked/parallel to a vertically placed word
    private static int scoreHorizontalExtraWords(Board board) {
        int score = 0;
        Square[][] b = board.getBoard();
        for (Index index : LAST_COVERED_INDICES) {
            int wordScore = 0;
            int wordMultiplier = 1;
            int startColumn = index.getColumn();
            int endColumn = index.getColumn();
            int row = index.getRow();

            // Find the starting column index of the word
            while (Square.isValid(startColumn - 1, row) && !b[row][startColumn - 1].isEmpty()) {
                startColumn--;
            }
            // Find the tail column index of the word
            while (Square.isValid(endColumn + 1, row) && !b[row][endColumn + 1].isEmpty()) {
                endColumn++;
            }
            // Add word score for any extra words formed
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
                WORDS_FORMED.add(board.getHorizontalWord(row, startColumn, endColumn));
            }
        }
        return score;
    }

    /**
     * Display the last six scores of the game.
     */
    public static void printLastSixScores() {
        Scrabble.printToOutput("> Last six scores: " + LAST_SIX_SCORES.toString());
    }

    /**
     * Checks if all of the last six scores were 0.
     *
     * @return {@code true} if last six scores in the game were all 0
     */
    public static boolean isLastSixZero() {
        int zeroes = 0;
        for (Integer score : LAST_SIX_SCORES) {
            if (score == 0) zeroes++;
        }
        return zeroes == 6;
    }

}