import java.util.ArrayList; // For using ArrayList
import javax.swing.SwingUtilities;

/**
 * This class is the main controller for the Peg Solitaire game. It manages
 * the game flow, user interactions, and game logic.
 *
 * @author Aarav Goyal
 * @since September 30, 2025
 */
public class PegSolitaire {
	/** The game board containing all peg positions */
	private PegBoard board = new PegBoard();

	/**
	 * Default constructor for PegSolitaire.
	 * Initializes a new game with a fresh board.
	 */
	public PegSolitaire() {
		this.board = new PegBoard(); // Initialize board here
	}

	/**
	 * Main entry point for the Peg Solitaire application.
	 * Creates a new game instance and starts the GUI.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		// Create a new instance of the game
		PegSolitaire game = new PegSolitaire();

		// Start the GUI
		SwingUtilities.invokeLater(() -> {
			PegSolitaireGUI gui = new PegSolitaireGUI(game);
			gui.setVisible(true);
		});
	}

	/**
	 * Returns the current game board.
	 * 
	 * @return the PegBoard instance
	 */
	public PegBoard getBoard() {
		return this.board;
	}

	/**
	 * Resets the game to its initial state.
	 */
	public void resetGame() {
		this.board = new PegBoard(); // Create a new board
	}

	/**
	 * Main game loop that controls the flow of the game.
	 * This method is no longer used with the GUI.
	 */
	public void run() {
		// This method is no longer used with the GUI.
		// The game logic is now driven by user interactions in the GUI.
	}

	/**
	 * Executes a peg jump move from the specified position to a given destination.
	 * This method is used by the GUI.
	 * 
	 * @param startRow    the row position of the peg to move
	 * @param startCol    the column position of the peg to move
	 * @param destination the Location object representing the jump destination
	 */
	public void playPeg(int startRow, int startCol, Location destination) {
		// Execute the jump:
		// 1. Remove the peg that was jumped over (midpoint between start and end)
		this.board.removePeg((startRow + destination.getRow()) / 2, (startCol + destination.getCol()) / 2);

		// 2. Remove the peg from its starting position
		this.board.removePeg(startRow, startCol);

		// 3. Place the peg at its destination
		this.board.putPeg(destination.getRow(), destination.getCol());
	}

	/**
	 * Finds all valid jump destinations for a peg at the given position.
	 * 
	 * A valid jump requires:
	 * - A peg at the starting position (row, col)
	 * - An adjacent peg in the jump direction
	 * - An empty space two positions away in that direction
	 * - All positions must be valid board locations
	 * 
	 * This method checks all four directions: up, down, left, right.
	 * 
	 * @param row the row position of the peg to check
	 * @param col the column position of the peg to check
	 * @return ArrayList of Location objects representing valid jump destinations
	 */
	public ArrayList<Location> openValidLocations(int row, int col) {
		// List to store all valid jump destinations
		ArrayList<Location> validLocations = new ArrayList<Location>();

		// Check jump UP (row - 2)
		// Requires: destination empty, middle peg exists, both locations valid
		if (this.board.isValidLocation(row - 2, col) && !this.board.isPeg(row - 2, col)
				&& this.board.isPeg(row - 1, col)) {
			validLocations.add(new Location(row - 2, col));
		}

		// Check jump DOWN (row + 2)
		// Requires: destination empty, middle peg exists, both locations valid
		if (this.board.isValidLocation(row + 2, col) && !this.board.isPeg(row + 2, col)
				&& this.board.isPeg(row + 1, col)) {
			validLocations.add(new Location(row + 2, col));
		}

		// Check jump LEFT (col - 2)
		// Requires: destination empty, middle peg exists, both locations valid
		if (this.board.isValidLocation(row, col - 2) && !this.board.isPeg(row, col - 2)
				&& this.board.isPeg(row, col - 1)) {
			validLocations.add(new Location(row, col - 2));
		}

		// Check jump RIGHT (col + 2)
		// Requires: destination empty, middle peg exists, both locations valid
		if (this.board.isValidLocation(row, col + 2) && !this.board.isPeg(row, col + 2)
				&& this.board.isPeg(row, col + 1)) {
			validLocations.add(new Location(row, col + 2));
		}

		// Return the list of valid jump destinations
		return validLocations;
	}

	/**
	 * Checks if any valid moves remain on the board.
	 * 
	 * This method scans the entire board looking for any empty space
	 * that could be the destination of a valid jump. For each empty space,
	 * it checks all four directions (up, down, left, right) to see if
	 * there are two consecutive pegs that could jump into that space.
	 * 
	 * @return true if at least one valid move exists, false otherwise
	 */
	public boolean hasValidMove() {
		// Scan every position on the board
		for (int row = 0; row < this.board.getBoardSize(); ++row) {
			for (int col = 0; col < this.board.getBoardSize(); ++col) {
				// Only check empty spaces as potential jump destinations
				if (this.board.isValidLocation(row, col) && !this.board.isPeg(row, col)) {

					// Check if a peg can jump DOWN into this empty space
					// Requires: peg at row-2 and peg at row-1 (to be jumped over)
					if (this.board.isValidLocation(row - 2, col) && this.board.isValidLocation(row - 1, col)
							&& this.board.isPeg(row - 2, col) && this.board.isPeg(row - 1, col)) {
						return true;
					}

					// Check if a peg can jump UP into this empty space
					// Requires: peg at row+2 and peg at row+1 (to be jumped over)
					if (this.board.isValidLocation(row + 2, col) && this.board.isValidLocation(row + 1, col)
							&& this.board.isPeg(row + 2, col) && this.board.isPeg(row + 1, col)) {
						return true;
					}

					// Check if a peg can jump RIGHT into this empty space
					// Requires: peg at col-2 and peg at col-1 (to be jumped over)
					if (this.board.isValidLocation(row, col - 2) && this.board.isValidLocation(row, col - 1)
							&& this.board.isPeg(row, col - 2) && this.board.isPeg(row, col - 1)) {
						return true;
					}

					// Check if a peg can jump LEFT into this empty space
					// Requires: peg at col+2 and peg at col+1 (to be jumped over)
					if (this.board.isValidLocation(row, col + 2) && this.board.isValidLocation(row, col + 1)
							&& this.board.isPeg(row, col + 2) && this.board.isPeg(row, col + 1)) {
						return true;
					}
				}
			}
		}

		// No valid moves found anywhere on the board
		return false;
	}

	/**
	 * Converts a string to an integer safely.
	 * 
	 * This method attempts to parse a string into an integer value.
	 * If the string cannot be parsed (e.g., contains non-numeric characters),
	 * it returns -1 instead of throwing an exception.
	 * 
	 * @param str the string to convert
	 * @return the integer value of the string, or -1 if conversion fails
	 */
	public int convertToInt(String str) {
		// Default value if conversion fails
		int result = -1;

		try {
			// Attempt to parse the string
			result = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			// Silently handle parse errors by returning -1
			// This allows the calling code to detect invalid input
		}

		// Return the parsed value or -1 if parsing failed
		return result;
	}
}
