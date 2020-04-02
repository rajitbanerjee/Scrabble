package constants;

/**
 * Stores all the hard-coded constants used in multiple classes.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class GameConstants {
    public static final int FRAME_LIMIT = 7;
    public static final int BOARD_SIZE = 15;

    /* Different types of tiles ("-" represents the blank tile) grouped
    according to equality of associated points */
    public static final String[] TILE_TYPES_ARRAY = {"-", "E", "AI", "O", "NRT", "LSU",
            "D", "G", "BCMP", "FHVWY", "K", "JX", "QZ"};

    // Number of tiles for each tile type in every group of tile types
    public static final int[] NUM_TILES_ARRAY = {2, 12, 9, 8, 6, 4, 4, 3, 2, 2, 1, 1, 1};

    // Points associated for each tile in each group
    public static final int[] POINT_TYPES_ARRAY = {0, 1, 1, 1, 1, 1, 2, 2, 3, 4, 5, 8, 10};

    // Details (hard-coded indices) about the multipliers for all board indices
    public static final int[][] DOUBLE_LS_ARRAY = {{0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7},
            {3, 14}, {6, 2}, {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11},
            {8, 2}, {8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7}, {11, 14},
            {12, 6}, {12, 8}, {14, 3}, {14, 11}};
    public static final int[][] TRIPLE_LS_ARRAY = {{1, 5}, {1, 9}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, {9, 1},
            {9, 5}, {9, 9}, {9, 13}, {13, 5}, {13, 9}};
    public static final int[][] DOUBLE_WS_ARRAY = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {10, 10}, {11, 11},
            {12, 12}, {13, 13}, {1, 13}, {2, 12}, {3, 11}, {4, 10}, {10, 4},
            {11, 3}, {12, 2}, {13, 1}};
    public static final int[][] TRIPLE_WS_ARRAY = {{0, 0}, {0, 7}, {0, 14}, {7, 0},
            {7, 14}, {14, 0}, {14, 7}, {14, 14}};
    public static final int[][] NORMAL_SQ_ARRAY = {
            {0, 1}, {0, 2}, {0, 4}, {0, 5}, {0, 6}, {0, 8}, {0, 9}, {0, 10}, {0, 12}, {0, 13},
            {1, 0}, {1, 2}, {1, 3}, {1, 4}, {1, 6}, {1, 7}, {1, 8}, {1, 10}, {1, 11}, {1, 12},
            {1, 14}, {2, 0}, {2, 1}, {2, 3}, {2, 4}, {2, 5}, {2, 7}, {2, 9}, {2, 10}, {2, 11},
            {2, 13}, {2, 14}, {3, 1}, {3, 2}, {3, 4}, {3, 5}, {3, 6}, {3, 8}, {3, 9}, {3, 10},
            {3, 12}, {3, 13}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 5}, {4, 6}, {4, 7}, {4, 8},
            {4, 9}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {5, 0}, {5, 2}, {5, 3}, {5, 4}, {5, 6},
            {5, 7}, {5, 8}, {5, 10}, {5, 11}, {5, 12}, {5, 14}, {6, 0}, {6, 1}, {6, 3}, {6, 4},
            {6, 5}, {6, 7}, {6, 9}, {6, 10}, {6, 11}, {6, 13}, {6, 14}, {7, 1}, {7, 2}, {7, 4},
            {7, 5}, {7, 6}, {7, 8}, {7, 9}, {7, 10}, {7, 12}, {7, 13}, {8, 0}, {8, 1}, {8, 3},
            {8, 4}, {8, 5}, {8, 7}, {8, 9}, {8, 10}, {8, 11}, {8, 13}, {8, 14}, {9, 0}, {9, 2},
            {9, 3}, {9, 4}, {9, 6}, {9, 7}, {9, 8}, {9, 10}, {9, 11}, {9, 12}, {9, 14}, {10, 0},
            {10, 1}, {10, 2}, {10, 3}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 9}, {10, 11},
            {10, 12}, {10, 13}, {10, 14}, {11, 1}, {11, 2}, {11, 4}, {11, 5}, {11, 6}, {11, 8},
            {11, 9}, {11, 10}, {11, 12}, {11, 13}, {12, 0}, {12, 1}, {12, 3}, {12, 4}, {12, 5},
            {12, 7}, {12, 9}, {12, 10}, {12, 11}, {12, 13}, {12, 14}, {13, 0}, {13, 2}, {13, 3},
            {13, 4}, {13, 6}, {13, 7}, {13, 8}, {13, 10}, {13, 11}, {13, 12}, {13, 14}, {14, 1},
            {14, 2}, {14, 4}, {14, 5}, {14, 6}, {14, 8}, {14, 9}, {14, 10}, {14, 12}, {14, 13}
    };

    /**
     * Represents the type of square on the board.
     */
    public enum MULTIPLIER {DOUBLE_LS, TRIPLE_LS, DOUBLE_WS, TRIPLE_WS, NORMAL, CENTRE}

}