import java.util.ArrayList;

public class Player implements PlayerAPI, OpponentAPI {

	private int id;
	private String name;
	private int score;
	private Frame frame;

	Player(int id)  {
		this.id = id;
		name = "";
		score = 0;
		frame = new Frame();
	}

	public int getPrintableId() {
		return id+1;
	}

	public void setName(String text) {
		name = text;
	}

	public String getName() {
		return name;
	}

	public void addPoints(int increase) {
		score = score + increase;
	}

	public void subtractPoints(int decrease) {
		score = score - decrease;
	}

	public int getScore() {
		return score;
	}

	public Frame getFrame() {
		return frame;
	}

	public String getFrameAsString() {return frame.toString();}

	public void adjustScore() {
		int unused = 0;
		ArrayList<Tile> tiles = frame.getTiles();
		for (Tile tile : tiles) {
			unused = unused + tile.getValue();
		}
		score = score - unused;
	}

	public String toString() {

		if (name.isEmpty()) {
			return "Player " + getPrintableId();
		} else {
			return name;
		}
	}

}
