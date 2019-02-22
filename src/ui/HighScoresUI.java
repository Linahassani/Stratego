package ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import highscore.HSDatabase;
import highscore.HighScoreList;

/**
 * JPanel window/card for showing the server high scores.
 * Added a new things. Look at the notes from developer Yun.
 * @author Henrik Sandstr�m
 */
public class HighScoresUI extends JPanel implements ActionListener{
	private static final long serialVersionUID = 9152300781099331929L;
	private Viewer viewer;
	private JLabel[] lblScore;
	private JButton btnBack;
	private ScoreList highScoreList;
	private HSDatabase database = new HSDatabase();
	private ArrayList<String> workers;
	private String workersInString;
	private JTextArea txt = new JTextArea();

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
//	public void initialize(String highScore) {
//		highScoreList.initialize(highScore);
//	}
	
	/**
	 * JPanel with a visible list of the server high scores.
	 * Made some changes and added everything from the database to a TextArea.
	 * Hopefully it will be better then before. -Yun
	 */
	private class ScoreList extends JPanel {		
		
		public ScoreList(){
			setOpaque(false);
			setBorder(new EmptyBorder(20,0,0,0));
			initialize();
		}
		
		public void initialize() {
			workers = database.getUsers();
			
			for (String work : workers ) {
				workersInString += work + " \n";
			}
			txt.setText(workersInString);
			txt.setAlignmentX(SwingConstants.CENTER);
			txt.setEditable(false);
			txt.setOpaque(false);
			add(txt);
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
