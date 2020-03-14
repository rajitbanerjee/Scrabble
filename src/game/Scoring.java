package game;

import constants.Constants;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Scoring {
    public static ArrayDeque<Integer> lastSixScores = new ArrayDeque<>();
    public static ArrayList<String> wordsFormed = new ArrayList<>();
    public static ArrayList<Index> lastCoveredIndices;

    /**
     * Checks if all of the last six scores were 0.
     *
     * @return {@code true} if last six scores in the game were all 0
     */
    public static boolean lastSixScoresZero() {
        int zeroes = 0;
        for (Integer score : lastSixScores) {
            if (score == 0) zeroes++;
        }
        return zeroes == 6;
    }


    /**
     * Finds the total score for move involving given word placement.
     *
     * @param word  main word placed whose score is to be calculated
     * @param board the game board
     * @return the score awarded after placing the given word
     */
    public static int calculateScore(Word word, Board board) {
        int bonus = (lastCoveredIndices.size() == Constants.FRAME_LIMIT) ? 50 : 0;
        return mainWordScore(word, board) + extraWordScore(word, board) + bonus;
    }

    // Main word score (newly formed word with greatest number of newly placed tiles)
    private static int mainWordScore(Word word, Board board) {
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
    private static int scoreHorizontalExtraWords(Board board) {
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

}
