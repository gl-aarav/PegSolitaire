
import java.util.ArrayList;

public class PegSolitaire {
	private PegBoard board = new PegBoard();
	private final boolean DEBUG = false;

	public PegSolitaire() {
	}

	public static void main(String[] var0) {
		PegSolitaire var1 = new PegSolitaire();
		var1.run();
	}

	public void run() {
		this.printIntroduction();
		this.board.printBoard();
		boolean var1 = false;

		while (true) {
			String[] var2;
			do {
				if (var1) {
					System.out.println("\nYour score: " + this.board.pegCount() + " pegs remaining\n");
					System.out.println("\nThanks for playing Peg Solitaire!\n");
					return;
				}

				var2 = this.getInput();
				if (var2.length == 2) {
					int var3 = this.convertToInt(var2[0]);
					int var4 = this.convertToInt(var2[1]);
					if (this.board.isValidLocation(var3, var4) && this.board.isPeg(var3, var4)) {
						this.playPeg(var3, var4);
					} else {
						System.out.println("\nInvalid jumper peg -> " + var3 + " " + var4);
					}
				}

				this.board.printBoard();
			} while (this.board.pegCount() >= 2 && this.hasValidMove() && (var2.length <= 0 || !var2[0].equals("q")));

			var1 = true;
		}
	}

	public void printIntroduction() {
		System.out.println("  _____              _____       _ _ _        _ ");
		System.out.println(" |  __ \\            / ____|     | (_) |      (_)");
		System.out.println(" | |__) |__  __ _  | (___   ___ | |_| |_ __ _ _ _ __ ___ ");
		System.out.println(" |  ___/ _ \\/ _` |  \\___ \\ / _ \\| | | __/ _` | | '__/ _ \\");
		System.out.println(" | |  |  __/ (_| |  ____) | (_) | | | || (_| | | | |  __/");
		System.out.println(" |_|   \\___|\\__, | |_____/ \\___/|_|_|\\__\\__,_|_|_|  \\___|");
		System.out.println("             __/ |");
		System.out.println("            |___/");
		System.out.println("\nWelcome to Peg Solitaire!!!\n");
		System.out.println(
				"Peg Solitaire is a game for one player. The goal is to remove all\nbut one of the 32 pegs from a special board. The board is a 7x7\ngrid with the corners cut out (shown below). Pegs are placed in all");
		System.out.println(
				"grid locations except the center which is left empty. Pegs jump\nover other pegs either horizontally or vertically into empty\nlocations and the jumped peg is removed. Play continues until\nthere are no more jumps possible, or there is one peg remaining.");
		System.out.println("\nLet's play!!!\n");
	}

	public String[] getInput() {
		boolean var1 = false;
		String[] var2 = new String[0];

		while (true) {
			while (!var1) {
				String var3 = Prompt.getString("Jumper peg - row col (ex. 3 5, q=quit)");
				var2 = var3.split(" +");
				if (var2.length > 0 && var2[0].equals("q")) {
					var1 = true;
				} else if (var2.length > 1) {
					int var4 = this.convertToInt(var2[0]);
					int var5 = this.convertToInt(var2[1]);
					if (this.board.isValidLocation(var4, var5) && this.openValidLocations(var4, var5).size() > 0) {
						var1 = true;
					} else {
						System.out.println("Invalid jumper peg -> " + var3);
					}
				}
			}

			return var2;
		}
	}

	public void playPeg(int var1, int var2) {
		ArrayList var3 = this.openValidLocations(var1, var2);
		if (var3.size() < 1) {
			System.out.println("There is no play for the peg (" + var1 + ", " + var2 + ").");
		} else {
			Location var4 = null;
			if (var3.size() > 1) {
				System.out.println("\nPossible peg jump locations:");

				int var5;
				for (var5 = 0; var5 < var3.size(); ++var5) {
					System.out.println(" " + var5 + " " + var3.get(var5));
				}

				var5 = Prompt.getInt("Enter location", 0, var3.size() - 1);
				var4 = (Location) var3.get(var5);
			} else {
				var4 = (Location) var3.get(0);
			}

			this.board.removePeg((var1 + var4.getRow()) / 2, (var2 + var4.getCol()) / 2);
			this.board.removePeg(var1, var2);
			this.board.putPeg(var4.getRow(), var4.getCol());
		}
	}

	public ArrayList<Location> openValidLocations(int var1, int var2) {
		ArrayList var3 = new ArrayList();
		if (this.board.isValidLocation(var1 - 2, var2) && !this.board.isPeg(var1 - 2, var2)
				&& this.board.isPeg(var1 - 1, var2)) {
			var3.add(new Location(var1 - 2, var2));
		}

		if (this.board.isValidLocation(var1 + 2, var2) && !this.board.isPeg(var1 + 2, var2)
				&& this.board.isPeg(var1 + 1, var2)) {
			var3.add(new Location(var1 + 2, var2));
		}

		if (this.board.isValidLocation(var1, var2 - 2) && !this.board.isPeg(var1, var2 - 2)
				&& this.board.isPeg(var1, var2 - 1)) {
			var3.add(new Location(var1, var2 - 2));
		}

		if (this.board.isValidLocation(var1, var2 + 2) && !this.board.isPeg(var1, var2 + 2)
				&& this.board.isPeg(var1, var2 + 1)) {
			var3.add(new Location(var1, var2 + 2));
		}

		return var3;
	}

	public boolean hasValidMove() {
		for (int var1 = 0; var1 < this.board.getBoardSize(); ++var1) {
			for (int var2 = 0; var2 < this.board.getBoardSize(); ++var2) {
				if (this.board.isValidLocation(var1, var2) && !this.board.isPeg(var1, var2)) {
					if (this.board.isValidLocation(var1 - 2, var2) && this.board.isValidLocation(var1 - 1, var2)
							&& this.board.isPeg(var1 - 2, var2) && this.board.isPeg(var1 - 1, var2)) {
						return true;
					}

					if (this.board.isValidLocation(var1 + 2, var2) && this.board.isValidLocation(var1 + 1, var2)
							&& this.board.isPeg(var1 + 2, var2) && this.board.isPeg(var1 + 1, var2)) {
						return true;
					}

					if (this.board.isValidLocation(var1, var2 - 2) && this.board.isValidLocation(var1, var2 - 1)
							&& this.board.isPeg(var1, var2 - 2) && this.board.isPeg(var1, var2 - 1)) {
						return true;
					}

					if (this.board.isValidLocation(var1, var2 + 2) && this.board.isValidLocation(var1, var2 + 1)
							&& this.board.isPeg(var1, var2 + 2) && this.board.isPeg(var1, var2 + 1)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public int convertToInt(String var1) {
		int var2 = -1;

		try {
			var2 = Integer.parseInt(var1);
		} catch (NumberFormatException var4) {
		}

		return var2;
	}
}
