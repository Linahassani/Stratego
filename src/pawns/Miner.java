package pawns;

import javax.swing.ImageIcon;

/** Class that sets the range, value and color and the picture for a miner. Inherit from the Pawn class
 * @author Anders Qvist
 */
public class Miner extends Pawn {

	
	/**
	 * @param color The color to use, blue or red
	 */
	public Miner(Pawn.Color color) {
		setRange(1);
		setValue(3);
		setColor(color);
		
		if (Pawn.Color.BLUE == color) {
			setShowIcon(new ImageIcon("files/images/pawns/blue/miner.png"));
			setHideIcon(new ImageIcon("files/images/pawns/blue/enemy.png"));
			
		} else if (Pawn.Color.RED == color) {
			setShowIcon(new ImageIcon("files/images/pawns/red/miner.png"));
			setHideIcon(new ImageIcon("files/images/pawns/red/enemy.png"));
		}
	}
}
