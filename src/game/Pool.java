package game;

import constants.GameConstants;

import java.util.ArrayList;

/**
 * The Pool holds 100 tiles initially, and players refill their
 * frames by drawing tiles at random from the Pool.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class Pool {
    private ArrayList<Tile> pool;

    /**
     * Creates a new pool of tiles.
     */
    public Pool() {
        pool = new ArrayList<>();
        for (int i = 0; i < GameConstants.TILE_TYPES_ARRAY.length; i++) {
            for (int j = 0; j < GameConstants.NUM_TILES_ARRAY[i]; j++) {
                addTiles(GameConstants.TILE_TYPES_ARRAY[i]);
            }
        }
    }

    /**
     * Add given group of tiles to the pool, each with the given associated points.
     *
     * @param tiles group of tiles to be added
     */
    public void addTiles(String tiles) {
        for (char ch : tiles.toCharArray()) {
            pool.add(Tile.makeTile(ch));
        }
    }

    /**
     * Accessor for pool.
     *
     * @return pool
     */
    public ArrayList<Tile> getPool() {
        return pool;
    }

    /**
     * Mutator for pool.
     *
     * @param pool the new pool
     */
    public void setPool(ArrayList<Tile> pool) {
        this.pool.clear();
        this.pool.addAll(pool);
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
     * Draw a tile at random from the pool.
     *
     * @return the randomly drawn Tile object
     * @throws IllegalStateException if player tries to draw tile from empty pool
     */
    public Tile drawTile() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("> Pool has no tiles!");
        }
        // Returns a random index between [0, pool.size())
        int index = (int) (Math.random() * pool.size());

        Tile t = pool.get(index);
        pool.remove(index);
        return t;
    }

    /**
     * Checks if the pool is empty.
     *
     * @return {@code true} if pool has 0 tiles,
     */
    public boolean isEmpty() {
        return pool.isEmpty();
    }

}