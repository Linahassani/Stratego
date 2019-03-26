package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Help/rules UI
 * Easiest to test it when all is connected 
 * @author yun
 *
 */

public class HelpUI extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4971352996518635125L;
	private Viewer viewer;
	private JButton btnBack;
	private Helplist help;
	private JTextArea txt = new JTextArea();
	private final String rules = "Stratego is a game in which you need to capture the flag of your opponent while defending your own flag."
			+ "\n" +"To capture the flag you use your army of 40 pieces."
			+ "\n" + "Pieces have a rank and represent individual officers and soldiers in an army."
			+ "\n" + "In addition to those ranked pieces you can use bombs to protect your flag."
			+ "\n" 
			+ "\n" + "Pieces move 1 square per turn, horizontally or vertically. Only the scout can move over multiple empty squares per turn." 
			+ "\n" + "Pieces cannot jump over another piece."
			+ "\n" 
			+ "\n" + "If a piece is moved onto a square occupied by an opposing piece, their identities are revealed."
			+ "\n" + "The weaker piece is removed from the board, and the stronger piece is moved into the place formerly occupied by the weaker piece. "
			+ "\n" + "If the engaging pieces are of equal rank, they are both removed. "
			+ "\n" + "Pieces may not move onto a square already occupied by another piece without attacking."
			+ "\n" + "However, when the marshal(level 10) attacks the spy(level 1), the spy loses. Bombs lose when they are defused by a miner(level 3)."
			+ "\n" + "The bombs and the flag cannot be moved. A bomb defeats every piece that tries to attack it, except the miner."
			+ "\n" 
			+ "\n" + "The flag loses from every other piece. When you capture the flag of your opponent you win the game."
			+ "\n" 
			+ "\n" + "The Stratego board consists of 10 x 10 squares." 
			+ "\n" + "Within the board there are two obstacles of 2 x 2 squares each. Pieces are not allowed to move there."
			+ "\n" 
			+ "\n" + " " 
			+ "\n" + "Keybindings"
			+ "\n" 
			+ "\n" + "Rightclick removes a placed piece during the placement phase. It places it back to the list of unplaced pieces." 
			+ "\n" + "Leftclick selects. It is the main button used to navigate in the application." 

;

	public HelpUI(Viewer viewer) {
		this.viewer = viewer;
		setLayout(new BorderLayout());

		add(Common.newTitle("Rules of Stratego"),BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setPreferredSize(new Dimension(600, 100));		
		btnBack = Common.newButton("Back to menu", this);	
		buttonPanel.add(btnBack);		
		add(buttonPanel,BorderLayout.SOUTH);
		help = new Helplist();
		add(help, BorderLayout.CENTER);
	}
	
/**
 * Inner class to make the inner background and where the text is set.
 * @author yun
 *
 */

	private class Helplist extends JPanel {	

		public Helplist(){
			setOpaque(false);
			setBorder(new EmptyBorder(20,0,0,0));
			rules();
		}
		public void rules(){
			txt.setText(rules);
			txt.setEditable(false);
			txt.setOpaque(false);
			add(txt);

		}
		protected void paintComponent(Graphics g) {	        
			super.paintComponent(g);	       
			Common.paintComponent(g, this, Common.getInnerBackground());      
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnBack) {
			viewer.switchToMenu();
		} 
	}
	protected void paintComponent(Graphics g) {        
		super.paintComponent(g);       
		Common.paintComponent(g, this, Common.getNormalBackground());     
	}


}
