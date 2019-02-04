package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a captain. Inherit from the Pawn class
 * @author Anders Qvist
 */

public class Captain extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Captain(Pawn.Color color) {
		setRange(1);
		setValue(6);
		setColor(color);
		
		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/captain.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/captain.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
