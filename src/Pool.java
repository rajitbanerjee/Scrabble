import org.jetbrains.annotations.NotNull;

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
        reset();
    }

    /**
     * Resets the pool to its initial state with 100 tiles.
     */
    public void reset() {
        pool.clear();
        // add required number of tiles of each type to the pool
        for (int i = 0; i < Constants.TILE_TYPES.length; i++) {
            for (int j = 0; j < Constants.NUM_TILES[i]; j++) {
                addTiles(Constants.TILE_TYPES[i]);
            }
        }
    }

    /**
     * Add given group of tiles to the pool, each with the given associated points.
     *
     * @param tiles  group of tiles to be added
     */
    private void addTiles(@NotNull String tiles) {
        for (char ch : tiles.toCharArray()) {
            pool.add(Tile.makeTile(ch));
        }
    }

    /**
     * Returns the current size of the pool.
     *
     * @return current number of tiles in the pool
     */
    public int size() {
        return pool.size();
    }

    /**
     * Display the number of tiles currently in the pool.
     */
    public void printSize() {
        System.out.println("Number of tiles in pool: " + size());
    }

    /**
     * Checks if the pool is empty.
     *
     * @return {@code true} if pool has 0 tiles, {@code false} otherwise
     */
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    /**
     * Draw a tile at random from the pool.
     *
     * @return the randomly drawn Tile object
     * @throws NoSuchElementException if player tries to draw Tile from empty Pool
     */
    public Tile drawTile() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Pool has no tiles!");
        }
        // returns a random index between [0, pool.size())
        int index = (int) (Math.random() * pool.size());

        Tile t = pool.get(index);
        pool.remove(index);
        return t;
    }

    /**
     * Gets the points associated with a given tile.
     *
     * @param t Tile object whose points are to be queried
     * @return the points associated with the given Tile object
     */
    public int getTileValue(@NotNull Tile t) {
        return t.getPoints();
    }

}
