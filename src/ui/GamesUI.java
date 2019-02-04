package ui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import game.Game;
import user.UserGames;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * JPanel window for browsing & loading saved games.
 * @author Henrik Sandström
 */
public class GamesUI extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2364727130215855586L;
	private Viewer viewer;
	private JButton btnLoad, btnBack;
	private GamesList gamesList;
	private ArrayList<Game> games;
	private JLabel[] lblGames;
	private Game selectedGame;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		
	/**
	 * Constructs a new window & initializes variables.
	 * @param viewer
	 */
	public GamesUI(Viewer viewer) {		
		this.viewer = viewer;
		setLayout(new BorderLayout());
		
		add(Common.newTitle("Your saved games"),BorderLayout.NORTH);	
				
		gamesList = new GamesList();
		add(gamesList,BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setPreferredSize(new Dimension(600, 100));		
		btnLoad = Common.newButton("Load game", this);
		buttonPanel.add(btnLoad);
		btnBack = Common.newButton("Back to menu", this);	
		buttonPanel.add(btnBack);		
		add(buttonPanel,BorderLayout.SOUTH);
	}
	
	/**
	 * Updates the game list panel.
	 */
	public void initialize() {
		gamesList.initialize();
	}
	
	/**
	 * JPanel with a visible list of saved games.
	 */
	private class GamesList extends JPanel implements MouseListener{		
		
		public GamesList(){
			setOpaque(false);
			setLayout(new GridLayout(10,1,0,0));
			initialize();
		}
		
		/**
		 * Updates the visible list of saved games.
		 */
		public void initialize() {
			removeAll();
			
			games = UserGames.getInstance().getGames();
			selectedGame = null;			
			
			lblGames = new JLabel[games.size()];			
			add(new JLabel());			
			
			for(int i = 0; i < games.size(); i++) {
				Game game = games.get(i);
				lblGames[i] = new JLabel(game.getGameName() + "     " + DATE_FORMAT.format(game.getGameTime()));
				lblGames[i].setHorizontalAlignment(SwingConstants.CENTER);
				lblGames[i].addMouseListener(this);
				add(lblGames[i]);
			}
		}
		
		protected void paintComponent(Graphics g) {	        
			super.paintComponent(g);	       
			Common.paintComponent(g, this, Common.getInnerBackground());      
	   	}

		public void mouseClicked(MouseEvent e) {
			JLabel lblGame = (JLabel)e.getSource();
			int gameIndex = Arrays.asList(lblGames).indexOf(lblGame);
			if(lblGame.getForeground() != Color.GREEN) {
				for(int k = 0; k < lblGames.length; k++) {
					lblGames[k].setForeground(new Color(36, 58, 91));
				}
				selectedGame = games.get(gameIndex);
				lblGames[gameIndex].setForeground(Color.GREEN);
			}else {
				selectedGame = null;
				lblGames[gameIndex].setForeground(new Color(36, 58, 91));					
			}				
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnLoad) {
			if(selectedGame != null) {
				viewer.loadGame(selectedGame);
			}else {
				JOptionPane.showMessageDialog(null, "You must select a game to load");
			}			
		}else if(e.getSource() == btnBack) {
			viewer.switchToMenu();
		}
	}

	protected void paintComponent(Graphics g) {        
		super.paintComponent(g);       
		Common.paintComponent(g, this, Common.getNormalBackground());    
   	}

}
