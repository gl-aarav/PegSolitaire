import java.util.ArrayList; // For using ArrayList

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
	}

	/**
	 * Main entry point for the Peg Solitaire application.
	 * Creates a new game instance and starts the game loop.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		// Create a new instance of the game
		PegSolitaire game = new PegSolitaire();

		// Start the game
		game.run();
	}

	/**
	 * Main game loop that controls the flow of the game.
	 * 
	 * This method handles:
	 * - Displaying the introduction
	 * - Processing player moves
	 * - Checking for game end conditions
	 * - Displaying the final score
	 * 
	 * The game ends when:
	 * - The player quits by entering 'q'
	 * - No valid moves remain
	 * - Only one peg remains (winning condition)
	 */
	public void run() {
		// Display game introduction and rules
		this.printIntroduction();

		// Show the initial board state
		this.board.printBoard();

		// Flag to track if the game should end
		boolean gameOver = false;

		// Main game loop
		while (true) {
			String[] input;

			do {
				// Check if the game has ended
				if (gameOver) {
					// Display final score (lower is better)
					System.out.println("\nYour score: " + this.board.pegCount() + " pegs remaining\n");
					System.out.println("\nThanks for playing Peg Solitaire!\n");
					return;
				}

				// Get player input for the peg to move
				input = this.getInput();

				// Process the move if valid input provided (not quit command)
				if (input.length == 2) {
					// Convert string coordinates to integers
					int row = this.convertToInt(input[0]);
					int col = this.convertToInt(input[1]);

					// Validate the selected peg position
					if (this.board.isValidLocation(row, col) && this.board.isPeg(row, col)) {
						// Execute the peg jump
						this.playPeg(row, col);
					} else {
						// Inform player of invalid selection
						System.out.println("\nInvalid jumper peg -> " + row + " " + col);
					}
				}

				// Display updated board after the move
				this.board.printBoard();

				// Continue game loop while:
				// - At least 2 pegs remain (need 2 to make a jump)
				// - Valid moves are still available
				// - Player hasn't quit (input[0] != "q")
			} while (this.board.pegCount() >= 2 && this.hasValidMove() && (input.length <= 0 || !input[0].equals("q")));

			// Set game over flag to exit after next iteration
			gameOver = true;
		}
	}

	/**
	 * Prints the game introduction including ASCII art title and game rules.
	 * 
	 * The introduction includes:
	 * - ASCII art banner for "Peg Solitaire"
	 * - Welcome message
	 * - Brief explanation of game rules
	 * - Board layout description
	 * - How to play instructions
	 */
	public void printIntroduction() {
		// Display ASCII art title
		System.out.println("  _____              _____       _ _ _        _ ");
		System.out.println(" |  __ \\            / ____|     | (_) |      (_)");
		System.out.println(" | |__) |__  __ _  | (___   ___ | |_| |_ __ _ _ _ __ ___ ");
		System.out.println(" |  ___/ _ \\/ _` |  \\___ \\ / _ \\| | | __/ _` | | '__/ _ \\");
		System.out.println(" | |  |  __/ (_| |  ____) | (_) | | | || (_| | | | |  __/");
		System.out.println(" |_|   \\___|\\__, | |_____/ \\___/|_|_|\\__\\__,_|_|_|  \\___|");
		System.out.println("             __/ |");
		System.out.println("            |___/");

		// Display welcome message
		System.out.println("\nWelcome to Peg Solitaire!!!\n");

		// Explain the game objective and board layout
		System.out.println(
				"Peg Solitaire is a game for one player. The goal is to remove all\nbut one of the 32 pegs from a special board. The board is a 7x7\ngrid with the corners cut out (shown below). Pegs are placed in all");
		System.out.println(
				"grid locations except the center which is left empty. Pegs jump\nover other pegs either horizontally or vertically into empty\nlocations and the jumped peg is removed. Play continues until\nthere are no more jumps possible, or there is one peg remaining.");

		// Encourage the player to begin
		System.out.println("\nLet's play!!!\n");
	}

	/**
	 * Gets and validates player input for selecting a peg to jump.
	 * 
	 * This method prompts the user to enter the row and column coordinates
	 * of the peg they want to move. It validates that:
	 * - The location is valid on the board
	 * - There is actually a peg at that location
	 * - The peg has at least one valid move available
	 * 
	 * The method loops until valid input is provided or the user quits.
	 * 
	 * @return String array containing [row, col] or ["q"] if player quits
	 */
	public String[] getInput() {
		// Flag to track if valid input has been received
		boolean validInput = false;

		// Array to store the input coordinates
		String[] input = new String[0];

		// Loop until valid input is received
		while (true) {
			while (!validInput) {
				// Prompt user for peg coordinates
				String inputStr = Prompt.getString("Jumper peg - row col (ex. 3 5, q=quit)");

				// Split input by one or more spaces
				input = inputStr.split(" +");

				// Check if player wants to quit
				if (input.length > 0 && input[0].equals("q")) {
					validInput = true;
				}
				// Validate the coordinates provided
				else if (input.length > 1) {
					// Parse coordinates
					int row = this.convertToInt(input[0]);
					int col = this.convertToInt(input[1]);

					// Check if location is valid and has possible moves
					if (this.board.isValidLocation(row, col) && this.openValidLocations(row, col).size() > 0) {
						validInput = true;
					} else {
						// Inform player that the peg cannot be moved
						System.out.println("Invalid jumper peg -> " + inputStr);
					}
				}
			}

			// Return the validated input
			return input;
		}
	}

	/**
	 * Executes a peg jump move from the specified position.
	 * 
	 * This method:
	 * 1. Finds all valid jump destinations for the selected peg
	 * 2. If multiple options exist, prompts the player to choose one
	 * 3. Removes the jumped-over peg
	 * 4. Removes the original peg
	 * 5. Places the peg at the destination
	 * 
	 * @param row the row position of the peg to move
	 * @param col the column position of the peg to move
	 */
	public void playPeg(int row, int col) {
		// Get all valid jump destinations for this peg
		ArrayList<Location> validMoves = this.openValidLocations(row, col);

		// Check if there are any valid moves
		if (validMoves.size() < 1) {
			System.out.println("There is no play for the peg (" + row + ", " + col + ").");
		} else {
			// Variable to store the chosen destination
			Location destination = null;

			// If multiple valid moves exist, let player choose
			if (validMoves.size() > 1) {
				System.out.println("\nPossible peg jump locations:");

				// Display all possible jump locations
				int choice;
				for (choice = 0; choice < validMoves.size(); ++choice) {
					System.out.println(" " + choice + " " + validMoves.get(choice));
				}

				// Get player's choice
				choice = Prompt.getInt("Enter location", 0, validMoves.size() - 1);
				destination = (Location) validMoves.get(choice);
			} else {
				// Only one valid move, use it automatically
				destination = (Location) validMoves.get(0);
			}

			// Execute the jump:
			// 1. Remove the peg that was jumped over (midpoint between start and end)
			this.board.removePeg((row + destination.getRow()) / 2, (col + destination.getCol()) / 2);

			// 2. Remove the peg from its starting position
			this.board.removePeg(row, col);

			// 3. Place the peg at its destination
			this.board.putPeg(destination.getRow(), destination.getCol());
		}
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
