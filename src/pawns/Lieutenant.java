package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a lieutenant. Inherit from the Pawn class
 * @author Anders Qvist
 */
public class Lieutenant extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Lieutenant(Pawn.Color color) {
		setRange(1);
		setValue(5);
		setColor(color);

		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/lieutenant.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/lieutenant.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
