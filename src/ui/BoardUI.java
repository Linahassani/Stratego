package ui;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import help.HelpButtonSingleton;
import pawns.Pawn;

/**
 * The game window. Holds the matrix and actively shows information about the
 * current game.
 * 
 * @author Henrik Sandström
 *
 */
public class BoardUI extends JPanel implements ActionListener, ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7889877154840522022L;
	private Viewer viewer;
	private JLabel lblPlayers, lblStatus;
	private JButton btnExit;
	private JPanel boardMenu;
	private SidePanel sidePanel;
	private Matrix matrix;
	private JPanel centerPanel;
	private ChatPanel chatPanel;
	private int minWidth, minHeight;
	private String userName, opponentName;
	

	/**
	 * Instantiates the board and creates components for a local setup mode.
	 */
	public BoardUI(Matrix matrix, Viewer viewer) {
		this.viewer = viewer;
		this.matrix = matrix;
		setLayout(new BorderLayout());
		minWidth = 850;
		minHeight = 722;

		boardMenu = new JPanel();
		boardMenu.setOpaque(false);
		boardMenu.setBorder(new MatteBorder(0, 0, 5, 0, new Color(177, 160, 119)));
		boardMenu.setPreferredSize(new Dimension(600, 65));
		lblStatus = new JLabel();
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		boardMenu.add(lblStatus);
		btnExit = Common.newButton("End game", this);
		
		boardMenu.add(HelpButtonSingleton.getInstance()); // *****************Lukas added help button
	
		boardMenu.add(btnExit);
		add(boardMenu, BorderLayout.NORTH);

		centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		centerPanel.setOpaque(false);
		centerPanel.add(matrix);
		addSidePanel();
		add(centerPanel, BorderLayout.CENTER);
		addComponentListener(this);	
		
		lblPlayers = new JLabel();
	}

	/**
	 * Adds the side panel and show information about the players.
	 * 
	 * @param userName
	 * @param opponentName
	 */
	public void startMultiplayerGame(String userName, String opponentName) {
		this.userName = userName;
		this.opponentName = opponentName;
		lblPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		boardMenu.add(lblPlayers);
		lblPlayers.setText(userName + " VS " + opponentName);
		addSidePanel();
	}

	/**
	 * Removes the SidePanel & ChatPanel.
	 */
	public void endGame() {
		removeSidePanel();
		removeChatPanel();
	}

	/**
	 * Updates the game status.
	 * 
	 * @param gameStatus
	 */
	public void updateGameStatus(String gameStatus) {
		lblStatus.setText(gameStatus);
	}

	/**
	 * Removes side panel from this object
	 */
	public void removeSidePanel() {
		if (sidePanel != null) {
			centerPanel.remove(sidePanel);
			sidePanel = null;
			minWidth = 700;
			repaint();
			if (lblPlayers != null && lblPlayers.getText() != "") {
				addChatPanel();
			}
		}
	}

	/**
	 * adds side panel with Pawns to this object
	 */
	public void addSidePanel() {
		if (sidePanel == null) {
			sidePanel = new SidePanel(this, viewer);
			matrix.setSidePanel(sidePanel);
			centerPanel.add(sidePanel);
			minWidth = 870;
			repaint();
		}
	}

	/**
	 * Appends the ChatPanel to the board.
	 */
	public void addChatPanel() {
		if (chatPanel == null) {
			chatPanel = new ChatPanel(viewer, userName, opponentName);
			centerPanel.add(chatPanel);
			repaint();
			minWidth = 870;
			setPreferredSize(new Dimension(minWidth, minHeight));
		}
	}

	/**
	 * Removes the ChatPanel from the board.
	 */
	public void removeChatPanel() {
		if (chatPanel != null) {
			centerPanel.remove(chatPanel);
			chatPanel = null;
			minWidth = 700;
			repaint();
		}
	}

	/**
	 * Sends a new pawnlist to the sidepanel.
	 * 
	 * @param pawns
	 * @param counters
	 */
	public void sendToSidePanel(Pawn[][] pawns, int[][] counters) {
		if(sidePanel != null) {
			sidePanel.updateBoard(pawns, counters);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnExit) {
			if (!viewer.inSetupState() && !viewer.inOverState() && !viewer.isOnlineGame()
					&& JOptionPane.showConfirmDialog(null, "Would you like to save the game before exiting?") == 0) {
				if (viewer.gameExists()) {
					viewer.saveExistingGame();
				} else {
					viewer.saveGame(JOptionPane.showInputDialog("Enter a game name"));
				}
			}

			if (viewer.isOnlineGame()) {
				if (viewer.inOverState()) {
					// END_GAME
					viewer.sendObject("END_GAME");
					close();
				} else {
					// FORFEIT
					if (JOptionPane.showConfirmDialog(null, "Are you sure you want to forfeit the game?") == 0) {
						viewer.sendObject("FORFEIT");
						endGame();
						close();
					}
				}

				

			} else {
				viewer.switchToMenu();
				endGame();
			}

		}
	}
	
	public void close() { 
		viewer.updateLobbyHeader();
		viewer.showCard("Lobby");
		if(chatPanel != null) {
			chatPanel.clear();
		}
		endGame();
		
		lblPlayers.setText("");
		remove(lblPlayers);
	}

	/**
	 * Sends a request to the Game-object to end the setup state and then redraws
	 * the board/matrix.
	 */
	public void setupDone() {
		viewer.setupDone();
		matrix.reDraw();
		matrix.showSetupArea();
	}

	/**
	 * Adds an opponent message to the chat panel.
	 * 
	 * @param message Opponent String message
	 */
	public void addOpponentMessage(String message) {
		if (chatPanel != null) {
			chatPanel.addOpponentMessage(message);
		}
	}

	/**
	 * Adds a server message to the chat panel.
	 * 
	 * @param message Server String message
	 */
	public void addInfoMessage(String message) {
		if (chatPanel != null) {
			chatPanel.addInfoMessage(message);
		}
	}

	/**
	 * Overrides and returns the minimum acceptable width if the frame is to small.
	 */
	public int getWidth() {
		if (minWidth > viewer.getContentPane().getWidth()) {
			return minWidth;
		}
		return viewer.getContentPane().getWidth();
	}

	/**
	 * Overrides and returns the minimum acceptable height if the frame is to small.
	 */
	public int getHeight() {
		if (minHeight > viewer.getContentPane().getHeight()) {
			return minHeight;
		}
		return viewer.getContentPane().getHeight();
	}

	/**
	 * Resizes the window if the width or height of the main frame is less than the
	 * acceptable value.
	 */
	public void componentResized(ComponentEvent arg0) {
		if (isVisible() && ((minWidth > viewer.getContentPane().getWidth())
				|| minHeight > viewer.getContentPane().getHeight())) {
			setPreferredSize(new Dimension(minWidth, minHeight));
			viewer.pack();
		}
		int newWidth = getWidth() / 25;
		int newHeight = getHeight() / 25;
		setBorder(new EmptyBorder(newHeight, newWidth, newHeight, newWidth));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Common.paintComponent(g, this, Common.getNormalBackground());
	}

	public void componentHidden(ComponentEvent arg0) {
	}

	public void componentMoved(ComponentEvent arg0) {
	}

	public void componentShown(ComponentEvent arg0) {
	}

}
