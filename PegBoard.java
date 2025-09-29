/**
 * PegBoard for the Peg Solitaire game.
 *
 * @author Aarav Goyal
 *
 *         This is the English version of the board.
 *         It is a 7x7 board without the corners. The game starts with pegs in
 *         all the locations except the center, as shown below.
 *
 *         col 0 1 2 3 4 5 6
 *         row -------------
 *         0 | P | P | P |
 *         -------------
 *         1 | P | P | P |
 *         -----------------------------
 *         2 | P | P | P | P | P | P | P |
 *         -----------------------------
 *         3 | P | P | P | | P | P | P |
 *         -----------------------------
 *         4 | P | P | P | P | P | P | P |
 *         -----------------------------
 *         5 | P | P | P |
 *         -------------
 *         6 | P | P | P |
 *         -------------
 *
 */

public class PegBoard {

	private char[][] board; // the peg board of characters

	private final int BOARD_SIZE = 7; // the side length of the square board

	/* constructor */
	public PegBoard() {
		// initialize board
		board = new char[BOARD_SIZE][BOARD_SIZE];

		// Fill board with pegs
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
				putPeg(row, col);

		// empty the corners
		removePeg(0, 0);
		removePeg(0, 1);
		removePeg(0, 5);
		removePeg(0, 6);
		removePeg(1, 0);
		removePeg(1, 1);
		removePeg(1, 5);
		removePeg(1, 6);
		removePeg(5, 0);
		removePeg(5, 1);
		removePeg(5, 5);
		removePeg(5, 6);
		removePeg(6, 0);
		removePeg(6, 1);
		removePeg(6, 5);
		removePeg(6, 6);

		// remove the center peg
		removePeg(3, 3);
	}

	/**
	 * Print the peg board to the screen.
	 */
	public void printBoard() {
		System.out.println();
		System.out.println(" col 0   1   2   3   4   5   6");
		System.out.println("row        -------------");
		System.out.print(" 0         |");
		for (int a = 2; a < 5; a++)
			System.out.printf(" %c |", board[0][a]);
		System.out.println("\n           -------------");
		System.out.print(" 1         |");
		for (int a = 2; a < 5; a++)
			System.out.printf(" %c |", board[1][a]);
		System.out.println("\n   -----------------------------");
		System.out.print(" 2 |");
		for (int a = 0; a < 7; a++)
			System.out.printf(" %c |", board[2][a]);
		System.out.println("\n   -----------------------------");
		System.out.print(" 3 |");
		for (int a = 0; a < 7; a++)
			System.out.printf(" %c |", board[3][a]);
		System.out.println("\n   -----------------------------");
		System.out.print(" 4 |");
		for (int a = 0; a < 7; a++)
			System.out.printf(" %c |", board[4][a]);
		System.out.println("\n   -----------------------------");
		System.out.print(" 5         |");
		for (int a = 2; a < 5; a++)
			System.out.printf(" %c |", board[5][a]);
		System.out.println("\n           -------------");
		System.out.print(" 6         |");
		for (int a = 2; a < 5; a++)
			System.out.printf(" %c |", board[6][a]);
		System.out.println("\n           -------------");
		System.out.println();
	}

	/**
	 * Returns a count of the number of pegs still on the board.
	 * 
	 * @return number of pegs remaining on the board
	 */
	public int pegCount() {
		int count = 0;
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
				if (isPeg(row, col))
					count++;
		return count;
	}

	/**
	 * Returns true if row/column location is on the board
	 * 
	 * @param row the subject row
	 * @param col the subject column
	 * @return true if location on the board; false otherwise
	 */
	public boolean isValidLocation(int row, int col) {
		// locations outside the square
		if (row < 0 || row > 6 || col < 0 || col > 6)
			return false;
		// locations inside corners
		if ((row == 0 || row == 1 || row == 5 || row == 6) &&
				(col < 2 || col > 4))
			return false;
		return true;
	}

	/**
	 * Put a peg into the location.
	 * Precondition: (row, col) must be a valid location.
	 * 
	 * @param row row to put peg
	 * @param col column to put peg
	 */
	public void putPeg(int row, int col) {
		board[row][col] = 'P';
	}

	/**
	 * Remove a peg from the location.
	 * Precondition: (row, col) must be a valid location.
	 * 
	 * @param row row to remove peg
	 * @param col column to remove peg
	 */
	public void removePeg(int row, int col) {
		board[row][col] = ' ';
	}

	/**
	 * Determine if peg is in location
	 * Precondition: (row, col) must be a valid location.
	 * 
	 * @param row row of location to check
	 * @param col column of location to check
	 * @return true if peg in location; false otherwise
	 */
	public boolean isPeg(int row, int col) {
		return board[row][col] == 'P';
	}

	/** @return size of the board */
	public int getBoardSize() {
		return BOARD_SIZE;
	}
}