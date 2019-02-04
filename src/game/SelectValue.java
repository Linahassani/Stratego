package game;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used as return value on selectPawn in Logic class. Contains a boolean
 * called allowed which tells if the pawn is allowed to be selected. Also
 * contains the positions the pawn can move to.
 * 
 * @author André Hansson
 */
public class SelectValue {

	private boolean allowed;
	private ArrayList<Position> positions;

	/**
	 * Constructor.
	 * 
	 * @param allowed If the pawn is allowed to be selected
	 */
	public SelectValue(boolean allowed) {
		this.allowed = allowed;
	}

	/**
	 * Returns allowed
	 * 
	 * @return allowed true/false
	 */
	public boolean isAllowed() {
		return allowed;
	}

	/**
	 * Sets allowed
	 * 
	 * @param allowed true/false
	 */
	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	/**
	 * Returns the positions the pawn can move to
	 * 
	 * @return The positions
	 */
	public ArrayList<Position> getPositions() {
		return positions;
	}

	/**
	 * Sets the positions the pawn can move to.
	 * 
	 * @param positions The positions
	 */
	public void setPositions(ArrayList<Position> positions) {
		this.positions = positions;
	}
	
	@Override
	public String toString() {
		return "Allowed: " + allowed + ". Positions: " + positions; // Arrays.toString(positions.toArray())
	}

}