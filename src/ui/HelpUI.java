package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

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
		
	}
	
	public void rules() {
		
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
