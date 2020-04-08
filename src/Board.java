import java.util.ArrayList;

public class Board implements BoardAPI {

	public static final int BOARD_SIZE = 15;
	public static final int BOARD_CENTRE = 7;
	private static int BONUS = 50;

	public static final int WORD_INCORRECT_FIRST_PLAY = 0;
	public static final int WORD_OUT_OF_BOUNDS = 1;
	public static final int WORD_LETTER_NOT_IN_FRAME = 2;
	public static final int WORD_LETTER_CLASH = 3;
	public static final int WORD_NO_LETTER_PLACED = 4;
	public static final int WORD_NO_CONNECTION = 5;
	public static final int WORD_EXCLUDES_LETTERS = 6;
	public static final int WORD_ONLY_ONE_LETTER = 7;

	private static final int[][] LETTER_MULTIPLIER =
			{ 	{1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1},
				{2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
				{1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
				{1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
				{1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1},
				{1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2},
				{1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} };
	private static final int[][] WORD_MULTIPLIER =
			{   {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3},
				{1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
				{1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
				{1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
				{1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
				{1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
				{1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
				{1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
				{3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3} };

	private Square[][] squares;
	private int errorCode;
	private int numPlays;
	private ArrayList<Coordinates> newLetterCoords;

	Board() {
		squares = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int r=0; r<BOARD_SIZE; r++)  {
			for (int c=0; c<BOARD_SIZE; c++)   {
				squares[r][c] = new Square(LETTER_MULTIPLIER[r][c],WORD_MULTIPLIER[r][c]);
			}
		}
		numPlays = 0;
	}

	public boolean isLegalPlay(Frame frame, Word word) {
		boolean isLegal = true;
		//check for invalid first play
		if (numPlays == 0 &&
				((word.isHorizontal() && (word.getRow()!=BOARD_CENTRE || word.getFirstColumn()>BOARD_CENTRE ||
						word.getLastColumn()<BOARD_CENTRE)) ||
						(word.isVertical() && (word.getColumn()!=BOARD_CENTRE || word.getFirstRow()>BOARD_CENTRE ||
								word.getLastRow()<BOARD_CENTRE)))) {
			isLegal = false;
			errorCode = WORD_INCORRECT_FIRST_PLAY;
		}
		// check for word out of bounds
		if (isLegal &&
				((word.getRow() >= BOARD_SIZE) ||
				 (word.getFirstColumn() >= BOARD_SIZE) ||
				 (word.getLastRow()>= BOARD_SIZE) ||
				 (word.getLastColumn()>= BOARD_SIZE)) ) {
			isLegal = false;
			errorCode = WORD_OUT_OF_BOUNDS;
		}
		// check that letters in the word do not clash with those on the board
		String lettersPlaced = "";
		if (isLegal) {
			int r = word.getFirstRow();
			int c = word.getFirstColumn();
			for (int i = 0; i < word.length() && isLegal; i++) {
				if (squares[r][c].isOccupied() && squares[r][c].getTile().getLetter() != word.getLetter(i)) {
					isLegal = false;
					errorCode = WORD_LETTER_CLASH;
				} else if (!squares[r][c].isOccupied()) {
					lettersPlaced = lettersPlaced + word.getLetter(i);
				}
				if (word.isHorizontal()) {
					c++;
				} else {
					r++;
				}
			}
		}
		// check that more than one letter is placed
		if (isLegal && lettersPlaced.length() == 0) {
			isLegal = false;
			errorCode = WORD_NO_LETTER_PLACED;
		}
		// check that the letters placed are in the frame
		if (isLegal && !frame.isAvailable(lettersPlaced)) {
			isLegal = false;
			errorCode = WORD_LETTER_NOT_IN_FRAME;
		}
		// check that the letters placed connect with the letters on the board
		if (isLegal && numPlays>0) {
			int boxTop = Math.max(word.getFirstRow()-1,0);
			int boxBottom = Math.min(word.getLastRow()+1, BOARD_SIZE-1);
			int boxLeft = Math.max(word.getFirstColumn()-1,0);
			int boxRight = Math.min(word.getLastColumn()+1, BOARD_SIZE-1);
			boolean foundConnection = false;
			for (int r=boxTop; r<=boxBottom && !foundConnection; r++) {
				for (int c=boxLeft; c<=boxRight && !foundConnection; c++) {
					if (squares[r][c].isOccupied()) {
						foundConnection = true;
					}
				}
			}
			if (!foundConnection) {
				isLegal = false;
				errorCode = WORD_NO_CONNECTION;
			}
		}
		// check there are no tiles before the word
		if (isLegal &&
				(word.isHorizontal() && word.getFirstColumn()>0 &&
				squares[word.getRow()][word.getFirstColumn()-1].isOccupied()) ||
				(word.isHorizontal() && word.getLastColumn()<BOARD_SIZE-1 &&
				squares[word.getRow()][word.getLastColumn()+1].isOccupied()) ||
				(word.isVertical() && word.getFirstRow()>0 &&
				squares[word.getFirstRow()-1][word.getColumn()].isOccupied()) ||
				(word.isVertical() && word.getLastRow()<BOARD_SIZE-1 &&
				squares[word.getLastRow()+1][word.getColumn()].isOccupied())) {
			isLegal = false;
			errorCode = WORD_EXCLUDES_LETTERS;
		}
		// check more than one letter
		if (isLegal && word.length()==1) {
			isLegal = false;
			errorCode = WORD_ONLY_ONE_LETTER;
		}
		return isLegal;
	}

	// getCheckCode precondition: isLegal is false
	public int getErrorCode() {
		return errorCode;
	}

	// place precondition: isLegal is true
	public void place(Frame frame, Word word) {
		newLetterCoords = new ArrayList<>();
		int r = word.getFirstRow();
		int c = word.getFirstColumn();
		for (int i = 0; i<word.length(); i++) {
			if (!squares[r][c].isOccupied()) {
				char letter = word.getLetter(i);
				Tile tile = frame.getTile(letter);
				if (tile.isBlank()) {
					tile.designate(word.getDesignatedLetter(i));
				}
				squares[r][c].add(tile);
				frame.removeTile(tile);
				newLetterCoords.add(new Coordinates(r,c));
			}
			if (word.isHorizontal()) {
				c++;
			} else {
				r++;
			}
		}
		numPlays++;
	}

	public ArrayList<Tile> pickupLatestWord() {
		ArrayList<Tile> tiles = new ArrayList<>();
		for (Coordinates coord : newLetterCoords) {
			Tile tile = squares[coord.getRow()][coord.getCol()].removeTile();
			if (tile.isBlank()) {
				tile.removeDesignation();
			}
			tiles.add(tile);
		}
		return tiles;
	}

	private boolean isAdditionalWord(int r, int c, boolean isHorizontal) {
		if ((isHorizontal &&
				(r>0 && squares[r-1][c].isOccupied() || (r<BOARD_SIZE-1 && squares[r+1][c].isOccupied()))) ||
				(!isHorizontal) &&
				(c>0 && squares[r][c-1].isOccupied() || (c<BOARD_SIZE-1 && squares[r][c+1].isOccupied())) ) {
			return true;
		}
		return false;
	}

	private Word getAdditionalWord(int mainWordRow, int mainWordCol, boolean mainWordIsHorizontal) {
		int firstRow = mainWordRow;
		int firstCol = mainWordCol;
		// search up or left for the first letter
		while (firstRow >= 0 && firstCol >= 0 && squares[firstRow][firstCol].isOccupied()) {
			if (mainWordIsHorizontal) {
				firstRow--;
			} else {
				firstCol--;
			}
		}
		// went too far
		if (mainWordIsHorizontal) {
			firstRow++;
		} else {
			firstCol++;
		}
		// collect the letters by moving down or right
		String letters = "";
		int r = firstRow;
		int c = firstCol;
		while (r<BOARD_SIZE && c<BOARD_SIZE && squares[r][c].isOccupied()) {
			letters = letters + squares[r][c].getTile().getLetter();
			if (mainWordIsHorizontal) {
				r++;
			} else {
				c++;
			}
		}
		return new Word (firstRow, firstCol, !mainWordIsHorizontal, letters);
	}

	public ArrayList<Word> getAllWords(Word mainWord) {
		ArrayList<Word> words = new ArrayList<>();
		words.add(mainWord);
		int r = mainWord.getFirstRow();
		int c = mainWord.getFirstColumn();
		for (int i=0; i<mainWord.length(); i++) {
			if (newLetterCoords.contains(new Coordinates(r,c))) {
				if (isAdditionalWord(r, c, mainWord.isHorizontal())) {
					words.add(getAdditionalWord(r, c, mainWord.isHorizontal()));
				}
			}
			if (mainWord.isHorizontal()) {
				c++;
			} else {
				r++;
			}
		}
		return words;
	}

	private int getWordPoints(Word word) {
		int wordValue = 0;
		int wordMultipler = 1;
		int r = word.getFirstRow();
		int c = word.getFirstColumn();
		for (int i = 0; i<word.length(); i++) {
			int letterValue = squares[r][c].getTile().getValue();
			if (newLetterCoords.contains(new Coordinates(r,c))) {
				wordValue = wordValue + letterValue * squares[r][c].getLetterMuliplier();
				wordMultipler = wordMultipler * squares[r][c].getWordMultiplier();
			} else {
				wordValue = wordValue + letterValue;
			}
			if (word.isHorizontal()) {
				c++;
			} else {
				r++;
			}
		}
		return wordValue * wordMultipler;
	}

	public int getAllPoints(ArrayList<Word> words) {
		int points = 0;
		for (Word word : words) {
			points = points + getWordPoints(word);
		}
		if (newLetterCoords.size() == Frame.MAX_TILES) {
			points = points + BONUS;
		}
		return points;
	}

	public Square getSquare(int row, int col) {
		return squares[row][col];
	}

	public Square getSquareCopy(int row, int col) {
		return new Square(squares[row][col]);
	}

	public boolean isFirstPlay() {
		return numPlays == 0;
	}

}