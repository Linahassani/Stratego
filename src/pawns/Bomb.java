package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a bomb. Inherit from the Pawn class
 * @author Anders Qvist
 *
 */
public class Bomb extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Bomb(Pawn.Color color) {
		setRange(0);
		setValue(100);
		setColor(color);

		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/bomb.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/bomb.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
