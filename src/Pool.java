import java.util.ArrayList;

public class Pool {

	private static final int TILES_BLANK = 2;
	private static final int[] TILES_A_TO_Z = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};

	private ArrayList<Tile> pool;

	Pool() {
		pool = new ArrayList<>();
		for (int i=0; i<TILES_BLANK; i++) {
			pool.add(new Tile('_'));
		}
		for (int i=0; i<TILES_A_TO_Z.length; i++) {
			for (int j=0; j<TILES_A_TO_Z[i]; j++) {
				pool.add(new Tile((char) ((int) 'A' + i)));
			}
		}
	}

	public int size() {
		return pool.size();
	}

	public boolean isEmpty() {
		return pool.isEmpty();
	}

	public ArrayList<Tile> drawTiles(int numRequested) {
		int numGiven;
		if (numRequested > pool.size()) {
			numGiven = pool.size();
		} else {
			numGiven = numRequested;
		}
		ArrayList<Tile> draw = new ArrayList<>();
		for (int i=0; i<numGiven; i++) {
			int index = (int) (Math.random()*pool.size());
			Tile tile = pool.get(index);
			draw.add(tile);
			pool.remove(tile);
		}
		return draw;
	}

	public void addTiles(ArrayList<Tile> tiles) {
		pool.addAll(tiles);
	}

	@Override
	public String toString() {
		return pool.toString();
	}

}
