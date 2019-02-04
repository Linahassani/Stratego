package ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import highscore.HighScoreList;

/**
 * JPanel window/card for showing the server high scores.
 * @author Henrik Sandström
 */
public class HighScoresUI extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9152300781099331929L;
	private Viewer viewer;
	private JLabel[] lblScore;
	private JButton btnBack;
	private ScoreList highScoreList;

	/**
	 * Constructs the window & initializes variables.
	 * @param viewer
	 */
	public HighScoresUI(Viewer viewer) {
		this.viewer = viewer;
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0,0,40,0));
		
		add(Common.newTitle("Server highscores"), BorderLayout.NORTH);
		
		highScoreList = new ScoreList();
		add(highScoreList, BorderLayout.CENTER);
		
		btnBack = Common.newButton("Back to lobby", this);
		add(btnBack, BorderLayout.SOUTH);
	}

	/**
	 * Loads and initializes the high scores list.
	 */
	public void initialize(String highScore) {
		highScoreList.initialize(highScore);
	}
	
	/**
	 * JPanel with a visible list of the server high scores.
	 */
	private class ScoreList extends JPanel {		
		
		public ScoreList(){
			setOpaque(false);
			setLayout(new GridLayout(10,1,0,0));
			setBorder(new EmptyBorder(20,0,0,0));
		}
		
		/**
		 * Updates the top ten highscore list.
		 */
		public void initialize(String highScore) {
			removeAll();
			
			
			String[] highScores = highScore.replaceAll("\t", "    ").split(System.lineSeparator());

			for(String score : highScores) {
				add(new JLabel(score, SwingConstants.CENTER));
			}
		}
		
		protected void paintComponent(Graphics g) {	        
			super.paintComponent(g);	       
			Common.paintComponent(g, this, Common.getInnerBackground());      
	   	}

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnBack) {
			viewer.showCard("Lobby");
		}
	}
	
	protected void paintComponent(Graphics g) {        
		super.paintComponent(g);       
		Common.paintComponent(g, this, Common.getNormalBackground());     
	}

}
