package game;

import java.io.Serializable;
import pawns.Pawn.Color;;

/**
 * Object that holds the players information.
 * 
 * @author André Hansson
 */
public class Player implements Serializable {

	private static final long serialVersionUID = -2271115596602637758L;
	private Color color;

	/**
	 * Constructor.
	 * 
	 * @param color The players color
	 */
	public Player(Color color) {
		this.color = color;
	}

	/**
	 * Returns the players color
	 * 
	 * @return The color
	 */
	public Color getColor() {
		return color;
	}

}