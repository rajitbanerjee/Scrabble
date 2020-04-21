import java.util.ArrayList;

public class Player implements PlayerAPI, OpponentAPI {

    private final int id;
    private final Frame frame;
    private String name;
    private int score;

    Player(int id) {
        this.id = id;
        name = "";
        score = 0;
        frame = new Frame();
    }

    @Override
    public int getPrintableId() {
        return id + 1;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String text) {
        name = text;
    }

    public void addPoints(int increase) {
        score = score + increase;
    }

    public void subtractPoints(int decrease) {
        score = score - decrease;
    }

    @Override
    public int getScore() {
        return score;
    }

    public Frame getFrame() {
        return frame;
    }

    @Override
    public String getFrameAsString() {
        return frame.toString();
    }

    public void adjustScore() {
        int unused = 0;
        ArrayList<Tile> tiles = frame.getTiles();
        for (Tile tile : tiles) {
            unused = unused + tile.getValue();
        }
        score = score - unused;
    }

    @Override
    public String toString() {

        if (name.isEmpty()) {
            return "Player " + getPrintableId();
        } else {
            return name;
        }
    }

}