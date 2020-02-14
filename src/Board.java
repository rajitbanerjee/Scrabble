import javax.swing.*;

public class Board {
    Square[][] board = new Square[15][15];

    Board() {
        // set Centre and Normal squares
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (i == 7 && j == 7) {
                    board[i][j] = new Square(Square.Multiplier.CENTRE);
                } else {
                    board[i][j] = new Square(Square.Multiplier.NORMAL);
                }
            }
        }
        int[][] double_ls = {{0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7},
                {3, 14}, {6, 2}, {6, 6}, {6, 8}, {6, 12}, {7, 3}, {7, 11},
                {8, 2}, {8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7}, {11, 14},
                {12, 6}, {12, 8}, {14, 3}, {14, 11}};
        int[][] triple_ls = {{1, 5}, {1, 9}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, {9, 1},
                {9, 5}, {9, 9}, {9, 13}, {13, 5}, {13, 9}};
        int[][] double_ws = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {10, 10}, {11, 11},
                {12, 12}, {13, 13}, {1, 13}, {2, 12}, {3, 11}, {4, 10}, {10, 4},
                {11, 3}, {12, 2}, {13, 1}};
        int[][] triple_ws = {{0, 0}, {0, 7}, {0, 14}, {7, 0},
                {7, 14}, {14, 0}, {14, 7}, {14, 14}};

        for (int[] index : double_ls) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.DOUBLE_LS);
        }
        for (int[] index : triple_ls) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.TRIPLE_LS);
        }
        for (int[] index : double_ws) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.DOUBLE_WS);
        }
        for (int[] index : triple_ws) {
            board[index[0]][index[1]] = new Square(Square.Multiplier.TRIPLE_WS);
        }

    }

    public void display() {
        printLine();
        System.out.print("\t|");
        for (char ch = 'A'; ch <= 'O'; ch++) {
            System.out.print("  " + ch + "  |");
        }
        printLine();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (j == 0) {
                    System.out.print((i + 1) + "\t|");
                }
                System.out.print(board[i][j] + "|");
            }
            printLine();
        }
    }

    private void printLine() {
        System.out.print("\n");
        for (int i = 0; i < 95; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Board b = new Board();
        b.display();
    }
}
