import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * The Pool holds 100 tiles initially, and players refill their
 * frames by drawing tiles at random from the Pool.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */

public class Pool {
    // the pool stores the tiles to be used by players
    private ArrayList<Tile> pool;

    public Pool() {
        pool = new ArrayList<>();
        resetPool();
    }

    /**
     * Resets the pool to its initial state with 100 tiles.
     */
    public void resetPool() {
        pool.clear();

        // different types of tiles, "-" represents the blank tile grouped...
        // according to equality of associated points
        String[] tileTypes = {"-", "E", "AI", "O", "NRT", "LSU",
                "D", "G", "BCMP", "FHVWY", "K", "JX", "QZ"};
        // number of tiles for each tile type in every group of tile types
        int[] numTiles = {2, 12, 9, 8, 6, 4, 4, 3, 2, 2, 1, 1, 1};
        // points for the different groups of tile types
        int[] points = {0, 1, 1, 1, 1, 1, 2, 2, 3, 4, 5, 8, 10};

        //E.g. tiles in the last group "QZ" each have quantity = 1, points = 10

        // add required number of tiles of each type to the pool
        for (int i = 0; i < tileTypes.length; i++) {
            for (int j = 0; j < numTiles[i]; j++) {
                addTiles(tileTypes[i], points[i]);
            }
        }
    }

    /**
     * Add given group of tiles to the pool, each with the given associated points.
     *
     * @param tiles  group of tiles with the same number of points
     * @param points points associated with each tile in the given group of tiles
     */
    private void addTiles(String tiles, int points) {
        for (char ch : tiles.toCharArray()) {
            pool.add(new Tile(ch, points));
        }
    }

    /**
     * Returns the current size of the pool.
     *
     * @return current number of tiles in the pool
     */
    public int countTiles() {
        return pool.size();
    }

    /**
     * Display the number of tiles currently in the pool.
     */
    public void displayTileCount() {
        System.out.println("Number of tiles in pool: " + countTiles());
    }

    /**
     * Checks if the pool is empty.
     *
     * @return {@code true} if pool has 0 tiles, {@code false} otherwise
     */
    public boolean isPoolEmpty() {
        return pool.isEmpty();
    }

    /**
     * Draw a tile at random from the pool.
     *
     * @return the randomly drawn Tile object
     * @throws NoSuchElementException if player tries to draw Tile from empty Pool
     */
    public Tile drawTile() throws NoSuchElementException {
        if (isPoolEmpty()) {
            throw new NoSuchElementException("Pool has no tiles!");
        } else {
            // returns a random index between [0, pool.size())
            int index = (int) (Math.random() * pool.size());

            Tile t = pool.get(index);
            pool.remove(index);
            return t;
        }
    }

    /**
     * Gets the points associated with a given tile.
     *
     * @param t Tile object whose points are to be queried
     * @return the points associated with the given Tile object
     */
    public int getTileValue(Tile t) {
        return t.getPoints();
    }

    /**
     * Accessor for pool variable.
     *
     * @return the pool of tiles
     */
    public ArrayList<Tile> getPool() {
        return pool;
    }
}