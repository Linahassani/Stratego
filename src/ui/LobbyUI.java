package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import help.HelpButtonSingleton;
import highscore.HSDatabase;

/**
 * Multiplayer lobby UI. Holds information and statistics for the current user and show a list off available players.
 * @author Henrik
 */
public class LobbyUI extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3327783821994539729L;
	private Viewer viewer;
	private ArrayList<String> availablePlayers;
	private ArrayList<JButton> btnPlayers;
	private PlayerList playerList;
	private JButton btnHighScores, btnDisconnect;
	private JLabel lblUserName, lblUserIcon, lblGamesPlayed, lblGamesWon, lblConnect; // username 
	private String userName;
	private boolean entered;
	private LobbyHeader header;
	private boolean headerInit = false;
	private HSDatabase database;
		
	public LobbyUI(Viewer viewer) {		
		this.viewer = viewer;
		entered = false;
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(50,0,0,0));
		setPreferredSize(new Dimension(600,600));
		database = viewer.getDatabase();
		
		lblConnect = new JLabel("Connecting to server..");
		lblConnect.setFont(lblConnect.getFont().deriveFont(30f));
		lblConnect.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblConnect, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setPreferredSize(new Dimension(600, 100));	
		btnHighScores = Common.newButton("Highscores", this);
		btnHighScores.setVisible(false);
		buttonPanel.add(btnHighScores);
		btnDisconnect = Common.newButton("Cancel", this);
		buttonPanel.add(btnDisconnect);
		add(buttonPanel,BorderLayout.SOUTH);
	}
	
	/**
	 * Message from server
	 * @param message
	 */
	public void newMessage(String message) {
		//logic for what hapens with message(String)
		String messageSplit[] = message.split(",");
		if(messageSplit[0].equals("USER_LIST")) {
			System.out.println("message in from server test  in LobbyUI..."+message);

			if(entered) {
				updateAvailablePlayers(message);
			} else {
				enterLobby(message);// userList with header
				
			}
			
		}
	}
	
	/**
	 * Updates the list of available players.
	 * @param list List of available players.
	 */
	public void userList(String list) {
		if(entered) {
			updateAvailablePlayers(list);
		} else {
			enterLobby(list);
			
		}
	}
	
	/**
	 * 
	 * Requests a user name and sends it to the server 
	 */
	public void attemptLogin() {
		userName = JOptionPane.showInputDialog("Enter your username to sign in");
		if(userName != null) {
			while(userName.length() <= 3) {
				userName = JOptionPane.showInputDialog("The username was to short, enter a new one.");
			}	
			viewer.startClient("USERNAME,"+userName);
			try {
				database.addPlayer(userName);
			}catch(SQLException e) {}
			viewer.showCard("Lobby");
		}	
	}
	
	public void userNameExists() {
		userName = JOptionPane.showInputDialog("The username you entered was already taken. Try another one.");
		if(userName != null) {
			while(userName.length() <= 3) {
				userName = JOptionPane.showInputDialog("The username was to short, enter a new one.");
			}	
			viewer.sendObject(("USERNAME,"+userName));
		}
	}
	
	/**
	 * Initializes and shows the multiplayer lobby.
	 * @param players
	 */
	public void enterLobby(String players) {
		System.out.println("enterLobby - players: " + players);
		btnHighScores.setVisible(true);
		entered = true;
		remove(lblConnect);
		btnDisconnect.setText("Disconnect");
		if(!headerInit) {
			header = new LobbyHeader();
			headerInit = true;
		}
		add(header,BorderLayout.NORTH);
		playerList = new PlayerList(this);
		add(playerList,BorderLayout.CENTER);
		playerList.initialize(players);
		updateUserInfo();
		reDraw();
	}
	
	/**
	 * Updates the list of available players.
	 * @param players
	 */
	public void updateAvailablePlayers(String players) {
		System.out.println("Availible Players: " + players);
		playerList.initialize(players);
	}
	
	/**
	 * Updates the lobbyheader with username and statistics from database
	 * - Jakob
	 */
	public void updateUserInfo() {
		lblUserName.setText(userName);
		double gamesPlayed = 0;
		double gamesWon = 0;
		try {
			gamesPlayed = database.getGamesPlayed(userName);
			gamesWon = database.getGamesWon(userName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		double winRatio = gamesWon/gamesPlayed * 100;
		int winR = (int) winRatio;
		int played = (int) gamesPlayed;
		int won = (int) gamesWon;
		lblGamesPlayed.setText("Games played: " +  played);
		lblGamesWon.setText("Games won: " + won + " (" + winR + "%)");
	}
	
	/**
	 * Asks the current user for permission to start a new game with the give opponent.
	 * @param opponentName
	 * @return
	 */
	public void showGameRequest(String opponentName) {
		if(JOptionPane.showConfirmDialog(null, "Game request received from " + opponentName + 
				". Would you like to accept?", "Game request", JOptionPane.YES_OPTION) == 0) {
			viewer.sendObject("OPPONENT_ACCEPT," + opponentName);
		} else {
			viewer.sendObject("OPPONENT_DECLINE," + opponentName);
		}
	}
	
	/**
	 * Redraws the lobby.
	 */
	private void reDraw() {
		revalidate();
		repaint();
	}
	
	/**
	 * JPanel with all available players.
	 */
	private class PlayerList extends JPanel{		
		
		private ActionListener actionListener;
		
		public PlayerList(ActionListener actionListener){
			this.actionListener = actionListener;
			setOpaque(false);
			setLayout(new GridLayout(10,2));
			setBorder(new EmptyBorder(20,0,0,0));
		}
		
		/**
		 * Updates the list of available players
		 * @param players
		 */
		public void initialize(String players) {			
			removeAll();
			
			availablePlayers = new ArrayList<String>(Arrays.asList(players.split(",")));		
			availablePlayers.remove(0);	//Removes object header
			availablePlayers.remove(userName);		
			
			btnPlayers = new ArrayList<JButton>();
			
			for(int i = 0; i < availablePlayers.size(); i++) {
				String player = availablePlayers.get(i);
				JLabel lblPlayer = new JLabel(player);
				lblPlayer.setHorizontalAlignment(SwingConstants.CENTER);
				JButton btnPlayer = Common.resizeButton(Common.newButton("Send game request", actionListener), 200, 30);
				btnPlayer.setHorizontalAlignment(SwingConstants.LEFT);
				btnPlayers.add(btnPlayer);
				add(lblPlayer);
				add(btnPlayer);
			}

			//Fills in the remaining free spaces
			for(int k = 0; k < 10-availablePlayers.size(); k++) {
				add(new JLabel());
				add(new JLabel());
			}
			
			reDraw();
		}
		
		protected void paintComponent(Graphics g) {	        
			super.paintComponent(g);	       
			Common.paintComponent(g, this, Common.getInnerBackground());      
	   	}
	}
	
	/**
	 * JPanel with information about the current user.
	 */
	private class LobbyHeader extends JPanel {		
		public LobbyHeader() {		
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(0,120,0,120));
			setOpaque(false);
			
			JPanel userInfo = new JPanel();
			lblUserName = new JLabel();
			lblUserIcon = new JLabel(new ImageIcon("images/user.png"));
			lblUserIcon.setBorder(new LineBorder(Color.BLACK,2));			
			userInfo.setOpaque(false);
			userInfo.add(lblUserIcon);
			userInfo.add(lblUserName);
			add(userInfo, BorderLayout.WEST);
			
			JPanel userStatistics = new JPanel();
			userStatistics.setLayout(new GridLayout(0,2));
			lblGamesPlayed = new JLabel("Games played: 0");
			lblGamesWon = new JLabel("Games won: 0 (0%)");
			userStatistics.setOpaque(false);
			userStatistics.add(lblGamesPlayed);
			userStatistics.add(lblGamesWon);			
			add(userStatistics, BorderLayout.EAST);	
		}
		
		protected void paintComponent(Graphics g) {        
			super.paintComponent(g);       
			Common.paintComponent(g, this, Common.getInnerBackground());    
	   	}		
	}

	protected void paintComponent(Graphics g) {        
		super.paintComponent(g);       
		Common.paintComponent(g, this, Common.getNormalBackground());    
   	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton tempBtn = (JButton)e.getSource();
			
			if(tempBtn == btnDisconnect) {
				viewer.disconnect();
				btnDisconnect.setText("Cancel");
				viewer.switchToMenu();
				entered = false;
				btnHighScores.setVisible(false);
			} else if(tempBtn == btnHighScores) {
				viewer.sendObject("HIGHSCORES");
			} else {
				viewer.sendObject("OPPONENT_REQUEST,"+availablePlayers.get(btnPlayers.indexOf(tempBtn)));
			}
		}
	}
}
