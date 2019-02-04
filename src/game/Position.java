package game;

import java.io.Serializable;

/**
 * Class that represents a position in a 2d array
 * 
 * @author André Hansson
 */
public class Position implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4717522058146507512L;
	private int row;
	private int column;

	/**
	 * Constructor.
	 * 
	 * @param row The row
	 * @param column The column
	 */
	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Returns the row.
	 * 
	 * @return The row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column.
	 * 
	 * @return The column
	 */
	public int getColumn() {
		return column;
	}
}