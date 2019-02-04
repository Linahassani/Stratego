package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a major. Inherit from the Pawn class
 * @author Anders Qvist
 */
public class Major extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Major(Pawn.Color color) {
		setRange(1);
		setValue(7);
		setColor(color);

		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/major.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/major.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
