package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a scout. Inherit from the Pawn class
 * @author Anders Qvist
 */
public class Scout extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Scout(Pawn.Color color) {
		setRange(9);
		setValue(2);
		setColor(color);
		
		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/scout.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/scout.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
