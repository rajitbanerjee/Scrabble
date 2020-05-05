import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Scrabble {

    private static final int ZERO_SCORE_PLAY_LIMIT = 6;
    public static int NUM_PLAYERS = 2;
    private final Board board;
    private final Pool pool;
    private final ArrayList<Player> players;
    private final Dictionary dictionary;
    ArrayList<Word> latestWords;
    ArrayList<Tile> latestRefill;
    private int currentPlayerId;
    private int numZeroScorePlays;
    int latestPoints;

    Scrabble() throws FileNotFoundException {
        board = new Board();
        pool = new Pool();
        players = new ArrayList<>();
        for (int i = 0; i < NUM_PLAYERS; i++) {
            players.add(new Player(i));
        }
        for (Player player : players) {
            player.getFrame().refill(pool);
        }
        currentPlayerId = 0;
        numZeroScorePlays = 0;
        dictionary = new Dictionary();
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerId);
    }

    public Player getOpposingPlayer() {
        if (currentPlayerId == 0) {
            return players.get(1);
        } else {
            return players.get(0);
        }
    }

    public void nextPlayer() {
        if (currentPlayerId == NUM_PLAYERS - 1) {
            currentPlayerId = 0;
        } else {
            currentPlayerId++;
        }
    }

    public Board getBoard() {
        return board;
    }

    public Pool getPool() {
        return pool;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void zeroScorePlay() {
        numZeroScorePlays++;
    }

    public void scorePlay() {
        numZeroScorePlays = 0;
    }

    public boolean isZeroScorePlaysOverLimit() {
        return numZeroScorePlays >= ZERO_SCORE_PLAY_LIMIT;
    }

    public void adjustScores() {
        for (Player player : players) {
            player.adjustScore();
        }
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void play(Word word) {
        board.place(getCurrentPlayer().getFrame(), word);
        latestWords = board.getAllWords(word);
        latestPoints = board.getAllPoints(latestWords);
        getCurrentPlayer().addPoints(latestPoints);
        latestRefill = getCurrentPlayer().getFrame().refill(pool);
        scorePlay();
    }

    public void undoPlay() {
        ArrayList<Tile> tilesPlayed = board.pickupLatestWord();
        getOpposingPlayer().getFrame().returnToPool(pool, latestRefill);
        getOpposingPlayer().getFrame().addTiles(tilesPlayed);
        getOpposingPlayer().subtractPoints(latestPoints);
    }

    public int getLatestPoints() {
        return latestPoints;
    }

    public ArrayList<Word> getLatestWords() {
        return latestWords;
    }

    public boolean framesAreEmpty() {
        boolean empty = true;
        for (Player player : players) {
            empty = empty && player.getFrame().isEmpty();
        }
        return empty;
    }

}