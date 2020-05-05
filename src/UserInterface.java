import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class UserInterface implements UserInterfaceAPI {

    Scrabble scrabble;
    boolean gameOver, opponentMadePlay, turnOver;
    Bots bots;
    String latestInfo;
    StringBuilder allInfo;
    int gameOverDelayCount;
    PrintStream originalStream, dummyStream;

    UserInterface(Scrabble scrabble) {
        this.scrabble = scrabble;
        gameOver = false;
        opponentMadePlay = false;
        latestInfo = "";
        allInfo = new StringBuilder();
        gameOverDelayCount = 0;
    }

    public void setBots(Bots bots) {
        this.bots = bots;
    }

    // Stage display methods

    public void playGame() {
        int inputCount;
        printGameStart();
        int INPUT_LIMIT = 10;
        do {
            printBoard();
            printScores();
            printPrompt();
            inputCount = 0;
            do {
                stopSystemOut();
                String input = bots.getBot(scrabble.getCurrentPlayerId()).getCommand();
                restartSystemOut();
                printLine("> " + input);
                processInput(input);
                inputCount++;
            } while (!turnOver && inputCount < INPUT_LIMIT);
            pause();
        } while (!gameOver && inputCount < INPUT_LIMIT);
        if (inputCount >= INPUT_LIMIT) {
            System.out.println("ABORT: " + scrabble.getCurrentPlayer() + " exceeded number of attempts.");
            System.out.println(scrabble.getOpposingPlayer() + " WINS THE MATCH");
            printGameOver();
        }
    }

    private void pause() {
        try {
            Thread.sleep(Main.BOT_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printBoard() {
        Board board = scrabble.getBoard();
        System.out.print("    ");
        for (int c = 0; c < Board.BOARD_SIZE; c++) {
            System.out.printf("%c  ", (char) ((int) 'A' + c));
        }
        System.out.println();
        for (int r = 0; r < Board.BOARD_SIZE; r++) {
            System.out.printf("%2d  ", r + 1);
            for (int c = 0; c < Board.BOARD_SIZE; c++) {
                if (board.getSquare(r, c).isOccupied()) {
                    System.out.printf("%c ", board.getSquare(r, c).getTile().getLetter());
                } else {
                    if (board.getSquare(r, c).isDoubleLetter()) {
                        System.out.print(". ");
                    } else if (board.getSquare(r, c).isTripleLetter()) {
                        System.out.print("..");
                    } else if (board.getSquare(r, c).isDoubleWord()) {
                        System.out.print(": ");
                    } else if (board.getSquare(r, c).isTripleWord()) {
                        System.out.print("::");
                    } else {
                        System.out.print("  ");
                    }
                }
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    // Input methods

    private void processInput(String input) {
        // this deals with user commands
        turnOver = false;
        String command = input.trim().toUpperCase();
        Player currentPlayer = scrabble.getCurrentPlayer();
        if (!gameOver && (command.equals("PASS") || command.equals("P"))) {
            turnOver = true;
            scrabble.zeroScorePlay();
            if (scrabble.isZeroScorePlaysOverLimit()) {
                printZeroScorePlaysOverLimit();
                gameOver = true;
            } else {
                opponentMadePlay = false;
                scrabble.nextPlayer();
            }
        } else if (!gameOver && (command.equals("HELP") || command.equals("H"))) {
            printHelp();
        } else if (!gameOver && (command.equals("SCORE") || command.equals("S"))) {
            printScores();
        } else if (!gameOver && (command.equals("POOL") || command.equals("O"))) {
            printPoolSize();
        } else if (!gameOver &&
                (command.matches("[A-O](\\d){1,2}( )+[A,D]( )+([A-Z]){1,15}") ||                // no blanks
                        (command.matches("[A-O](\\d){1,2}( )+[A,D]( )+([A-Z_]){1,17}( )+([A-Z]){1,2}")  // with blanks
                                && isValidPlayWithBlanks(command)))) {
            // no blanks
            Word word = parsePlay(command);
            if (!scrabble.getBoard().isLegalPlay(currentPlayer.getFrame(), word)) {
                printPlayError(scrabble.getBoard().getErrorCode());
            } else {
                turnOver = true;
                scrabble.play(word);
                printPoints(scrabble.getLatestPoints());
                if (scrabble.framesAreEmpty()) {
                    printAllTilesPlayed();
                    gameOver = true;
                } else {
                    opponentMadePlay = true;
                    scrabble.nextPlayer();
                }
            }
        } else if (!gameOver && (command.matches("EXCHANGE( )+([A-Z_]){1,7}") || command.matches("X( )+([A-Z_]){1,7}"))) {
            String[] parts = command.split("( )+");
            String letters = parts[1];
            if (!currentPlayer.getFrame().isLegalExchange(scrabble.getPool(), letters)) {
                printExchangeError(currentPlayer.getFrame().getErrorCode());
            } else {
                turnOver = true;
                currentPlayer.getFrame().exchange(scrabble.getPool(), letters);
                printTiles();
                scrabble.zeroScorePlay();
                if (scrabble.isZeroScorePlaysOverLimit()) {
                    printZeroScorePlaysOverLimit();
                    gameOver = true;
                } else {
                    opponentMadePlay = false;
                    scrabble.nextPlayer();
                }
            }
        } else if (!gameOver && (command.matches("NAME( )+[A-Z][A-Z0-9]*") || command.matches("N( )+[A-Z][A-Z0-9]*"))) {
            String[] parts = input.split("( )+");
            currentPlayer.setName(parts[1]);
            printNewName();
        } else if (!gameOver && opponentMadePlay && (command.equals("CHALLENGE") || command.equals("C"))) {
            if (scrabble.getDictionary().areWords(scrabble.getLatestWords())) {
                turnOver = true;
                printChallengeFail();
                scrabble.nextPlayer();
            } else {
                scrabble.undoPlay();
                opponentMadePlay = false;
                printChallengeSuccess();
            }
        } else {
            printLine("Error: command syntax incorrect. See help.");
        }
        if (gameOver) {
            scrabble.adjustScores();
            printScores();
            printWinner();
            printGameOver();
        }
    }

    private Word parsePlay(String command) {
        // this converts the play command into a Word
        String[] parts = command.split("( )+");
        String gridText = parts[0];
        int column = ((int) gridText.charAt(0)) - ((int) 'A');
        String rowText = parts[0].substring(1);
        int row = Integer.parseInt(rowText) - 1;
        String directionText = parts[1];
        boolean isHorizontal = directionText.equals("A");
        String letters = parts[2];
        Word word;
        if (parts.length == 3) {
            word = new Word(row, column, isHorizontal, letters);
        } else {
            String designatedBlanks = parts[3];
            word = new Word(row, column, isHorizontal, letters, designatedBlanks);
        }
        return word;
    }

    private boolean isValidPlayWithBlanks(String command) {
        // this just checks that the number of blanks matches the number of designated blanks
        String[] parts = command.split("( )+");
        int blankCount = 0;
        for (int i = 0; i < parts[2].length(); i++) {
            if (parts[2].charAt(i) == Tile.BLANK) {
                blankCount++;
            }
        }
        return blankCount != 0 && parts[3].length() == blankCount;
    }

    // Print methods

    private void print(String text) {
        System.out.print(text);
        latestInfo = text;
        allInfo.append(text);
    }

    private void printLine(String text) {
        print(text + "\n");
    }

    private void printGameStart() {
        printLine("WELCOME TO SCRABBLE");
    }

    private void printNewName() {
        printLine("Player " + scrabble.getCurrentPlayer().getPrintableId() +
                " is now named " + scrabble.getCurrentPlayer().getName() + ".");
    }

    private void printTiles() {
        printLine(scrabble.getCurrentPlayer() + " has the following tiles:");
        for (Tile tile : scrabble.getCurrentPlayer().getFrame().getTiles()) {
            if (!Main.BOT_GAME) {
                print(tile + " ");
            } else {
                print("? ");
            }
        }
        printLine("");
    }

    private void printPrompt() {
        printLine(scrabble.getCurrentPlayer() + "'s turn:");
        printTiles();   // suppressed for the Bots
    }

    private void printPoints(int points) {
        printLine(scrabble.getCurrentPlayer() + " scored " + points + " points.");
    }

    private void printPoolSize() {
        printLine("Pool has " + scrabble.getPool().size() + " tiles");
    }

    private void printHelp() {
        printLine("Command options: Q (quit), N (name), P (pass), X (exchange), C (challenge), S (scores), " +
                "O (pool) or play");
        printLine("Names must begin with a letter.");
        printLine("For an exchange, enter the letters that you wish to exchange. E.g. X ABC");
        printLine("For a play, enter the grid reference of the first letter, A (across) or D (down), " +
                " and the word including any letters already on the board. E.g. A1 D HELLO");
        printLine("For a play with a blank, enter the grid reference of the first letter, " +
                "A (across) or D (down), the word with underscores for blanks including any letters already on " +
                "the board, and the designated letter(s) for the blanks. E.g. A1 D H_LLO E");
    }

    private void printPlayError(int errCode) {
        String message = "";
        switch (errCode) {
            case Board.WORD_OUT_OF_BOUNDS:
                message = "Error: Word does not fit on the board.";
                break;
            case Board.WORD_LETTER_NOT_IN_FRAME:
                message = "Error: You do not have the necessary letters.";
                break;
            case Board.WORD_LETTER_CLASH:
                message = "Error: The word entered does not fit with the letters on the board.";
                break;
            case Board.WORD_NO_LETTER_PLACED:
                message = "Error: The word does not use any of your letters.";
                break;
            case Board.WORD_NO_CONNECTION:
                message = "Error: The word is not connected with the words on the board. ";
                break;
            case Board.WORD_INCORRECT_FIRST_PLAY:
                message = "Error: The first word must be in the centre of the board.";
                break;
            case Board.WORD_EXCLUDES_LETTERS:
                message = "Error: The word places excludes letters already on the board";
                break;
            case Board.WORD_ONLY_ONE_LETTER:
                message = "Error: Only one letter in the word.";
                break;
        }
        printLine(message);
    }

    public void printExchangeError(int errCode) {
        String message = "";
        switch (errCode) {
            case Frame.EXCHANGE_NOT_AVAILABLE:
                message = "Error: Letter not available in the frame.";
                break;
            case Frame.EXCHANGE_NOT_ENOUGH_IN_POOL:
                message = "Error: No enough tiles in the pool.";
                break;
        }
        printLine(message);
    }

    private void printZeroScorePlaysOverLimit() {
        printLine("The number of zero score plays is over the limit.");
    }

    private void printScores() {
        for (Player player : scrabble.getPlayers()) {
            printLine(player + " has " + player.getScore() + " points.");
        }
    }

    private void printChallengeFail() {
        printLine("Challenge fail - the words are in the dictionary.");
        printLine("Turn over.");
    }

    private void printChallengeSuccess() {
        printLine("Challenge success - the words are not in the dictionary.");
        printLine(scrabble.getOpposingPlayer() + " loses " + scrabble.getLatestPoints() + ".");
    }

    private void printAllTilesPlayed() {
        printLine("All tiles played.");
    }

    private void printWinner() {
        int maxScore = -1000;
        ArrayList<Player> winners = new ArrayList<>();
        boolean draw = false;
        for (Player player : scrabble.getPlayers()) {
            if (player.getScore() > maxScore) {
                draw = false;
                winners.clear();
                winners.add(player);
                maxScore = player.getScore();
            } else if (player.getScore() == maxScore) {
                draw = true;
                winners.add(player);
            }
        }
        if (!draw) {
            printLine(winners.get(0) + " WINS THE MATCH");
        } else {
            printLine("The game is a draw");
            for (Player winner : winners) {
                printLine(winner + " DRAWS THE MATCH");
            }
        }
    }

    private void printGameOver() {
        printLine("GAME OVER");
    }

    // Methods for Bots

    public String getLatestInfo() {
        return latestInfo;
    }

    public String getAllInfo() {
        return allInfo + "";
    }

    // Suppress Bot Chat

    private void stopSystemOut() {
        // STOP BOT CHAT
        originalStream = System.out;
        dummyStream = new PrintStream(new OutputStream() {
            public void write(int b) {
                //NO-OP
            }
        });
        System.setOut(dummyStream);
    }

    private void restartSystemOut() {
        System.setOut(originalStream);
    }

}