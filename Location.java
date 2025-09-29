/**
 * Location of row and column in peg board
 *
 * @author Aarav Goyal
 */
public class Location {
	private int row;
	private int col;

	public Location(int var1, int var2) {
		this.row = var1;
		this.col = var2;
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public String toString() {
		return "(" + this.row + ", " + this.col + ")";
	}
}
