/**
 * Location of row and column in peg board
 *
 * @author Aarav Goyal
 */
public class Location {
	private int row;
	private int col;

	/**
	 * Constructs a Location with the specified row and column.
	 * 
	 * @param var1 the row position
	 * @param var2 the column position
	 */
	public Location(int var1, int var2) {
		this.row = var1;
		this.col = var2;
	}

	/**
	 * Returns the row of this location.
	 * 
	 * @return the row position
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * Returns the column of this location.
	 * 
	 * @return the column position
	 */
	public int getCol() {
		return this.col;
	}

	/**
	 * Returns a string representation of this location in the format "(row, col)".
	 * 
	 * @return string representation of the location
	 */
	public String toString() {
		return "(" + this.row + ", " + this.col + ")";
	}
}
