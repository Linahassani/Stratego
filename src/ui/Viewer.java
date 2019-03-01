package ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;
import game.Game;
import game.Position;
import game.SelectValue;
import game.SoundPlayer;
import highscore.HSDatabase;
import pawns.Pawn;

/**
 * The main game JFrame/window. Holds all UI panels.
 * Handles communication between panels/cards and the controller.
 * @author Henrik Sandstr�m
 */
public class Viewer extends JFrame{

	private static final long serialVersionUID = -1522813252612261702L;
	private JPanel mainPanel;
	private MenuUI menu;
	private BoardUI board;
	private GamesUI gamesList;
	private SettingsUI settings;
	private CardLayout cards;
	private LobbyUI lobby;
	private HighScoresUI highScores;
	private HelpUI help; //Nytt
	private Controller controller;
	private Matrix matrix;
	private int defaultWidth, defaultHeight;

	/**
	 * Creates all UI panels and puts them in a CardLayout.
	 * @param controller
	 */
	public Viewer(Controller controller) {

		//Close/exit event
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				SoundPlayer.getInstance().stopSound();
				System.exit(0);		    	
			}
		});

		Common.setFont();
		setCursor(Common.getCursor());

		this.controller = controller;
		matrix = new Matrix();
		matrix.setViewer(this);
		cards = new CardLayout();

		mainPanel = new JPanel(cards);		
		menu = new MenuUI(this);
		mainPanel.add(menu, "Menu");
		board = new BoardUI(matrix, this);		
		mainPanel.add(board, "Board");
		gamesList = new GamesUI(this);
		mainPanel.add(gamesList, "Games");
		settings = new SettingsUI(this);
		mainPanel.add(settings, "Settings");
		lobby = new LobbyUI(this);
		mainPanel.add(lobby, "Lobby");
		highScores = new HighScoresUI(this);
		mainPanel.add(highScores, "HighScores");
		help = new HelpUI(this);
		mainPanel.add(help, "Help");

		add(mainPanel);	
		pack();		
		setVisible(true);		
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		SoundPlayer.getInstance().playStartGame();
		Toolkit.getDefaultToolkit().setDynamicLayout( false );
		defaultWidth = 1200;
		defaultHeight = 900;		
	}
	
	/**
	 * Switches window and if necessary resizes the frame to the minimum window values.
	 * @param cardName The window id/name.
	 */
	public void showCard(String cardName) {
		cards.show(mainPanel, cardName);
		for(Component comp : mainPanel.getComponents()) {
			if(comp.isVisible() && (getContentPane().getWidth() < comp.getWidth() || getContentPane().getHeight() < comp.getHeight())) {							
				comp.setPreferredSize(new Dimension(comp.getWidth(), comp.getHeight()));	
				pack();
			}
		}
	}

	/**
	 * Handles a server message.
	 * @param message String message from server
	 */
	public void newMessage(String message) {
		//logic for what hapens with message(String)
		System.out.println("message in from server test in Viewer..."+message);
		String messageSplit[] = message.split(",");
		if(messageSplit[0].equals("USER_LIST")) {
			lobby.newMessage(message); //sends userList to lobbyUI
		} else if(messageSplit[0].equals("MESSAGE")) {
			board.addOpponentMessage(messageSplit[1]); //sends message to the chat panel
		}
	}
	
	/**
	 * Toggles between window and fullscreen mode.
	 * @param useFullscreen
	 */
	public void toggleFullscreen(boolean useFullscreen) {
		dispose();		
		if(useFullscreen) {	
			defaultWidth = getWidth();
			defaultHeight = getHeight();
			setExtendedState(JFrame.MAXIMIZED_BOTH); 
			setUndecorated(true);

		} else {
			setPreferredSize(new Dimension(defaultWidth, defaultHeight));
			setExtendedState(JFrame.NORMAL); 
			setUndecorated(false);
			pack();
		}
		setVisible(true);
		setPreferredSize(null);
	}
	
	/**
	 * Returns the database object from controller
	 * @return
	 */
	public HSDatabase getDatabase() {
		return controller.getDatabase();
	}
	
	/**
	 * Starts a new Hotseat game and switches to board.
	 * {@link Controller#startGame()}
	 */
	public void startNewGame() {
		controller.startGame();
		board.addSidePanel();
		showCard("Board");
		SoundPlayer.getInstance().playGameMusic();
	}
	
	/**
	 * Starts a new Online game and switches to board.
	 * {@link BoardUI#startMultiplayerGame(String, String)}
	 */
	public void startMultiplayerGame(String userName, String opponentName) {
		board.startMultiplayerGame(userName, opponentName);
		showCard("Board");
		SoundPlayer.getInstance().playGameMusic();
	}
	
	/**
	 * Resumes a loaded game and switches to board.
	 * {@link Controller#loadGame(Game)}
	 */
	public void loadGame(Game game) {
		controller.loadGame(game);
		board.removeSidePanel();
		showCard("Board");
		SoundPlayer.getInstance().playGameMusic();
	}
	
	/**
	 * Initializes and switches to the highScore panel
	 * @param highScoreList List of players with their high scores
	 */
	public void showHighScores(String highScoreList) {
	//	highScores.initialize(highScoreList);
		showCard("HighScores");
	}
	
	/**
	 * Removes all components from a pawn.
	 * {@link Controller#placePawn(Pawn, Pawn)}
	 */
	public void placePawn(Pawn pawnPosition, Pawn pawn) {
		if(pawn != null) {
			SoundPlayer.getInstance().playPawnPlace();
			pawnPosition.removeAll();
			controller.placePawn(pawn, pawnPosition);
		}		
	}
	
	/**
	 * Switches to the menu panel.
	 */
	public void switchToMenu() {
		showCard("Menu");
		SoundPlayer.getInstance().playStartGame();
	}

	/**
	 * Initializes and switches to the list of saved games
	 */
	public void switchToGames() {
		gamesList.initialize();
		showCard("Games");
	}

	/**
	 * {@link LobbyUI#attemptLogin()}
	 */
	public void switchToLobby() {
		lobby.attemptLogin();
	}

	/**
	 * {@link LobbyUI#userNameExists()}
	 */
	public void userNameExists() {
		lobby.userNameExists();
	}

	/**
	 * {@link BoardUI#addInfoMessage(String)}
	 */
	public void addInfoMessage(String message) {
		board.addInfoMessage(message);
	}

	/**
	 * {@link Controller#startClient(String)}
	 */
	public void startClient(String userName) {
		controller.startClient(userName);
	}

	/**
	 * {@link Controller#disconnect()}
	 */
	public void disconnect() {
		controller.disconnect();
	}
	
	/**
	 * {@link BoardUI#updateGameStatus(String)}
	 */
	public void updateGameStatus(String gameStatus) {
		board.updateGameStatus(gameStatus);
	}

	/**
	 * {@link Controller#saveGame(String)}
	 */
	public void saveGame(String gameName) {
		controller.saveGame(gameName);
	}

	/**
	 * {@link Controller#saveExistingGame()}
	 */
	public void saveExistingGame() {
		controller.saveExistingGame();
	}	

	/**
	 * {@link Controller#gameExists()}
	 */
	public boolean gameExists() {
		return controller.gameExists();
	}
	

	/**
	 * {@link Controller#settingsChanged()}
	 */
	public void settingsChanged() {
		controller.applySettings();
	}

	/**
	 * {@link Matrix#updateBoard(Pawn[][])}
	 */
	public void sendToMatrix(Pawn[][] pawns) {
		matrix.updateBoard(pawns);
	}

	/**
	 * {@link Matrix#reDraw()}
	 */
	public void reDrawMatrix() {
		matrix.reDraw();
	}

	/**
	 * {@link BoardUI#sendToSidePanel(Pawn[][], Integer[][])}
	 */
	public void sendToSidePanel(Pawn[][] pawns, int[][] counters) {
		board.sendToSidePanel(pawns, counters);
	}

	/**
	 * {@link Controller#select(Pawn, Position)}
	 */
	public SelectValue select(Pawn pawn, Position position) {
		return controller.select(pawn, position);
	}

	/**
	 * {@link Controller#movePawn(Pawn, Position, Position)}
	 */
	public void movePawn(Pawn pawn, Position positionFrom, Position positionTo) {
		controller.movePawn(pawn, positionFrom, positionTo);
	}

	/**
	 * {@link Controller#inSetupState()}
	 */
	public boolean inSetupState(){
		return controller.inSetupState();
	}

	/**
	 * {@link Controller#inOverState()}
	 */
	public boolean inOverState(){
		return controller.inOverState();
	}

	/**
	 * {@link Controller#isOnlineGame()}
	 */
	public boolean isOnlineGame() {
		return controller.isOnlineGame();
	}

	/**
	 * {@link Controller#setupDone()}
	 */
	public void setupDone() {
		controller.setupDone();		
	}

	/**
	 * {@link Controller#removePawn(Pawn)}
	 */
	public void removePawn(Pawn pawn) {
		controller.removePawn(pawn);
	}

	/**
	 * {@link Controller#isInPlayerArea(Position)}
	 */
	public boolean isInPlayerArea(Position position) {
		return controller.isInPlayerArea(position);
	}

	/**
	 * {@link Controller#getPawnPosition(Pawn)}
	 */
	public Position getPawnPosition(Pawn pawn) {
		return controller.getPawnPosition(pawn);
	}

	/**
	 * {@link Controller#positionContainsEmptyPawn(Position)}
	 */
	public boolean positionIsEmpty(Position selectedPosition) {
		return controller.positionContainsEmptyPawn(selectedPosition);
	}

	/**
	 * {@link Controller#allPawnsPlaced()}
	 */
	public boolean allPawnsPlaced() {
		return controller.allPawnsPlaced();
	}

	/**
	 * {@link Controller#saveUserSetup(String)}
	 */
	public void saveUserSetup(String userSetupName) {
		controller.saveUserSetup(userSetupName);		
	}

	/**
	 * {@link Controller#loadFromUserSetup(String)}
	 */
	public void loadFromUserSetup(String userSetupName) {
		controller.loadFromUserSetup(userSetupName);
	}

	/**
	 * {@link BoardUI#removeSidePanel()}
	 */
	public void removeSidePanel() {
		board.removeSidePanel();		
	}

	/**
	 * {@link Controller#sendObject(String)}
	 */
	public void sendObject(String clientObject) {
		controller.sendObject(clientObject);
	}

	/**
	 * {@link LobbyUI#attemptLogin()}
	 */
	public void requestNewUserName() {
		lobby.attemptLogin();
	}

	/**
	 * {@link LobbyUI#userList(String)}
	 */
	public void userList(String list) {
		lobby.userList(list);
	}

	/**
	 * {@link LobbyUI#enterLobby(String)}
	 */
	public void enterLobby(String players) {
		lobby.enterLobby(players);
	}

	/**
	 * {@link LobbyUI#showGameRequest(String)}
	 */
	public void showGameRequest(String opponent) {
		lobby.showGameRequest(opponent);
	}

	/**
	 * {@link LobbyUI#updateAvailablePlayers(String)}
	 */
	public void updateAvailablePlayers(String players) {
		lobby.updateAvailablePlayers(players);
	}	

	/**
	 * {@link Matrix#showPawnMovement(Pawn, Position, Position, Pawn[][])}
	 */
	public void showPawnMovement(Pawn pawn, Position from, Position to, Pawn[][] boardGrid) {
		matrix.showPawnMovement(pawn, from, to, boardGrid);		
	}
	
	/**
	 * Shows animation if user wins
	 */
	public void showEndAnimation(String string) {
		matrix.showEndAnimation(string);
	}
}
