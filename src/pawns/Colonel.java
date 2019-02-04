package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a colonel. Inherit from the Pawn class
 * @author Anders Qvist
 */
public class Colonel extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Colonel(Pawn.Color color) {
		setRange(1);
		setValue(8);
		setColor(color);

		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/colonel.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/colonel.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
