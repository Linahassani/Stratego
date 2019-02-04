package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a flag. Inherit from the Pawn class
 * @author Anders Qvist
 */
public class Flag extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Flag(Pawn.Color color) {
		setRange(0);
		setValue(0);
		setColor(color);

		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/flag.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/flag.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
