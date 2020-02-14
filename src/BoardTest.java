public class BoardTest {
    public static void main(String[] args) {
        Board b = new Board();
        b.placeTile('A', 1, new Tile('Z', 10));
        b.display();
    }
}
