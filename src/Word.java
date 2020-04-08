public class Word {

	private int row, column; // first letter position
	private boolean isHorizontal;  // true = horizontal, false = vertical
	private String letters;
	private String designatedLetters;

	Word(int row, int column, boolean isHorizontal, String letters) {
		this.row = row;
		this.column = column;
		this.isHorizontal = isHorizontal;
		this.letters = letters;
		this.designatedLetters = letters;
	}

	Word(int row, int column, boolean isHorizontal, String letters, String blankDesignations) {
		this.row = row;
		this.column = column;
		this.isHorizontal = isHorizontal;
		this.letters = letters;
		// merge blank designations into the letters
		StringBuilder designatedLettersBuilder = new StringBuilder(letters);
		int j =0 ;
		for (int i=0; i<letters.length(); i++) {
			if (letters.charAt(i) == Tile.BLANK) {
				designatedLettersBuilder.setCharAt(i,blankDesignations.charAt(j));
				j++;
			}
		}
		designatedLetters = designatedLettersBuilder + "";
	}

	// getRow pre-condition: isHorizontal is true
	public int getRow() {
		return row;
	}

	// getColumn pre-condition: isHorizonal is flase
	public int getColumn() {
		return column;
	}

	public int getFirstRow() {
		return row;
	}

	public int getLastRow() {
		if (isHorizontal) {
			return row;
		} else {
			return row + letters.length() - 1;
		}
	}

	public int getFirstColumn() {
		return column;
	}

	public int getLastColumn() {
		if (!isHorizontal) {
			return column;
		} else {
			return column + letters.length() - 1;
		}
	}

	public String getLetters() {
		return letters;
	}

	public char getLetter(int i) {
		return letters.charAt(i);
	}

	public String getDesignatedLetters() {
		return designatedLetters;
	}

	public char getDesignatedLetter(int index) {
		return designatedLetters.charAt(index);
	}

	public int length() {
		return letters.length();
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public boolean isVertical() {
		return !isHorizontal;
	}

	@Override
	public String toString() {
		return letters;
	}


}
