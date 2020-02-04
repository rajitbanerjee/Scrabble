import java.util.ArrayList;

/**
 * The Pool holds 100 tiles initially, and players refill their
 * frames by drawing tiles at random from the Pool.
 *
 * @author Rajit Banerjee, 18202817
 */

public class Pool {
    private ArrayList<Tile> pool;

    Pool() {
        pool = new ArrayList<>();
        resetPool();
    }

    public void resetPool() {
        pool.clear();

        // different types of tiles, "-" represents the blank tile
        String[] tileTypes = {"-", "E", "AI", "O", "NRT", "LSU",
                "D", "G", "BCMP", "FHVWY", "K", "JX", "QZ"};
        // number of tiles of each type
        int[] numTiles = {2, 12, 9, 8, 6, 4, 4, 3, 2, 2, 1, 1, 1};
        // points for each type of tile
        int[] points = {0, 1, 1, 1, 1, 1, 2, 2, 3, 4, 5, 8, 10};

        // add required number of tiles of each type to the pool
        for (int i = 0; i < tileTypes.length; i++) {
            for (int j = 0; j < numTiles[i]; j++) {
                addTiles(tileTypes[i], points[i]);
            }
        }
    }

    private void addTiles(String letters, int points) {
        for (char ch : letters.toCharArray()) {
            pool.add(new Tile(ch, points));
        }
    }

    int countTiles() {
        return pool.size();
    }

    public void displayTileCount() {
        System.out.println("Number of tiles in pool: " + pool.size());
    }

    public boolean isPoolEmpty() {
        return pool.isEmpty();
    }

    public Tile drawTile() {
        // returns a random index between [0, pool.size())
        int index = (int) (Math.random() * pool.size());

        Tile t = pool.get(index);
        pool.remove(index);
        return t;
    }

    public int getTileValue(Tile t) {
        return t.getPoints();
    }

    public ArrayList<Tile> getPool() {
        return pool;
    }
}
