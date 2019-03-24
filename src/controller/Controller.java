package controller;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import client.Connect;
import game.Game;
import game.HotseatLogic;
import game.Logic;
import game.OnlineLogic;
import game.Position;
import game.SelectValue;
import game.SoundPlayer;
import highscore.HSDatabase;
import pawns.Pawn;
import pawns.Pawn.Color;
import ui.Viewer;
import user.UserGames;
import user.UserSettings;
import user.UserSetups;

/**
 * Main Controller. Starts the application.
 * Is responsible for distribution of information among other classes
 * 
 * @author André Hansson
 */
public class Controller {

	private Viewer viewer;
	private Logic logic;
	private UserGames userGames;
	private UserSettings userSettings;
	private UserSetups userSetups;
	private Connect connect;
	private ExecutorService executor;
	private HSDatabase db;

	private static final String IP = "localhost";

	/**
	 * Constructor
	 * Sets up and starts the game
	 */
	public Controller() {
		executor = Executors.newCachedThreadPool();

		userSettings = UserSettings.getInstance();
		userGames = UserGames.getInstance();
		userSetups = UserSetups.getInstance();
		db = new HSDatabase();

		SwingUtilities.invokeLater(() -> {
			viewer = new Viewer(Controller.this);
			applySettings();
		});

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			disconnect();

			executor.shutdown();

			try {
				executor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			executor.shutdownNow();

		}));
	}

	/**
	 * Messages from server end up here for sorting and distribution
	 * 
	 * @param message The message from the server
	 */
	public void newMessage(String message) {

		// Future is needed to catch exceptions
		Future<?> future = executor.submit(() -> {
			System.out.println("Controller newMessage: " + message);

			String messageSplit[] = message.split(",");

			if (messageSplit[0].equals("USER_LIST")) {
				userList(message);
			} else if (messageSplit[0].equals("OPPONENT_REQUEST")) {
				showGameRequest(messageSplit[1]);
			} else if (messageSplit[0].equals("OPPONENT_DECLINE")) {
				JOptionPane.showMessageDialog(null, messageSplit[1] + " did not want to play with you.");
			} else if (messageSplit[0].equals("START_GAME")) {
				String color = messageSplit[1];
				String user = messageSplit[2];
				String opponent = messageSplit[3];

				if (color.equals("BLUE")) {
					startOnlineGame(Color.BLUE, user, opponent);
				} else {
					startOnlineGame(Color.RED, user, opponent);
				}
			} else if (messageSplit[0].equals("PAWN_PLACEMENT")) {
				((OnlineLogic) logic).pawnPlacementReceived(message);
			} else if (messageSplit[0].equals("SETUP_DONE")) {
				((OnlineLogic) logic).opponentSetupDone(messageSplit[1]);
			} else if (messageSplit[0].equals("MOVE")) {
				((OnlineLogic) logic).move(Integer.parseInt(messageSplit[1]), Integer.parseInt(messageSplit[2]),
						Integer.parseInt(messageSplit[3]), Integer.parseInt(messageSplit[4]));
			} else if (messageSplit[0].equals("MESSAGE")) {
				viewer.newMessage(message);
			} else if (messageSplit[0].equals("FORFEIT")) {
				((OnlineLogic) logic).opponentForfeit(messageSplit[1], messageSplit[2]);
				viewer.addInfoMessage(messageSplit[1] + " forfeit the game");
			} else if (messageSplit[0].equals("END_GAME")) {
				viewer.addInfoMessage(messageSplit[1] + " left the game");
			} else if (messageSplit[0].equals("HIGHSCORE")) {
				//viewer.showHighScores(messageSplit[1]);
				viewer.showHighScores();
			} else if (messageSplit[0].equals("USERNAME_EXIST")) {
				viewer.userNameExists();
			} else if (messageSplit[0].equals("WIN")) {
				updateWin(messageSplit[1]);
			} else {
				System.out.println("Header not handled: " + messageSplit[0]);
			}
		});

		// Catch exceptions from the submit
		try {
			future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Starts a new online game
	 * 
	 * @param localColor The color of the local player
	 * @param user The username of the player
	 * @param opponent The username of the opponent
	 */
	public void startOnlineGame(Color localColor, String user, String opponent) {
		logic = new OnlineLogic(this);
		((OnlineLogic) logic).newGame(localColor);
		SwingUtilities.invokeLater(() -> {
			viewer.startMultiplayerGame(user, opponent);
		});
	}

	/**
	 * Starts the client socket and attempts to connect to the server
	 * 
	 * @param userName The username to use
	 */
	public void startClient(String userName, String serverIP) {
		executor.submit(() -> {
			connect = new Connect(serverIP, 63050, this);// for handling connection with server

			connect.startConnecion();
			connect.sendMessage(userName);
		});
	}

	/**
	 * Sends a disconnect message to the server
	 */
	public void disconnect() {
		sendObject("DISCONNECT");
	}

	/**
	 * Sends a message to the server
	 * 
	 * @param clientObject The message
	 */
	public void sendObject(String clientObject) {
		System.out.println("Controller - sendObject: " + clientObject);
		executor.submit(() -> {
			connect.sendMessage(clientObject);
		});
	}

	/**
	 * Saves the current game
	 * 
	 * @param gameName The name to save it as
	 */
	public void saveGame(String gameName) {
		executor.submit(() -> {
			logic.updateGameDetails(gameName);
			userGames.addGame(logic.getGame());
		});
	}

	/**
	 * Saves the current game, overwriting the existing save with the same name.
	 */
	public void saveExistingGame() {
		executor.submit(() -> {
			logic.updateGameDetails(logic.getGame().getGameName());
			userGames.updateGame(logic.getGame());
		});
	}

	/**
	 * Loads a game and starts it.
	 * 
	 * @param game The game
	 */
	public void loadGame(Game game) {
		executor.submit(() -> {
			logic = new HotseatLogic(Controller.this);
			((HotseatLogic) logic).loadGame(game);
		});
	}

	/**
	 * Starts a new hotseat game
	 */
	public void startGame() {
		executor.submit(() -> {
			logic = new HotseatLogic(Controller.this);
			((HotseatLogic) logic).newGame();
		});
	}

	/**
	 * Sends the game board to the matrix
	 * 
	 * @param pawns The game board
	 */
	public void sendToMatrix(Pawn[][] pawns) {
		SwingUtilities.invokeLater(() -> {
			viewer.sendToMatrix(pawns);
		});
	}

	/**
	 * Redraws the matrix
	 */
	public void reDrawMatrix() {
		SwingUtilities.invokeLater(() -> {
			viewer.reDrawMatrix();
		});
	}

	/**
	 * Sends the pawns and the available number of each pawn to the side panel.
	 * 
	 * @param pawns A 2d array of pawns
	 * @param counters A 2d array of int's. This is the number the user has of each
	 *            pawn.
	 */
	public void sendToSidePanel(Pawn[][] pawns, int[][] counters) {
		SwingUtilities.invokeLater(() -> {
			viewer.sendToSidePanel(pawns, counters);
		});
	}

	/**
	 * Selects the pawn at the position
	 * 
	 * @param pawn The pawn
	 * @param position The pawns position
	 * @return {@link SelectValue}
	 */
	public SelectValue select(Pawn pawn, Position position) {
		Future<SelectValue> future = executor.submit(() -> {
			return logic.selectPawn(pawn, position);
		});

		try {
			return future.get();
		} catch (Exception e) {
			System.out.println("Controller: select: future.get: error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Moves the pawn
	 * 
	 * @param pawn The pawn
	 * @param positionFrom The start position
	 * @param positionTo The end position
	 */
	public void movePawn(Pawn pawn, Position positionFrom, Position positionTo) {
		executor.submit(() -> {
			logic.move(pawn, positionFrom, positionTo);
		});
	}

	/**
	 * Checks to see if the game is in the setup state.
	 * 
	 * @return true/false
	 */
	public boolean inSetupState() {
		Future<Boolean> future = executor.submit(() -> {
			return logic.inSetupState();
		});

		try {
			return future.get().booleanValue();
		} catch (Exception e) {
			System.out.println("Controller: inSetupState: future.get: error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks to see if the game is in the setup state.
	 * 
	 * @return true/false
	 */
	public boolean inOverState() {
		Future<Boolean> future = executor.submit(() -> {
			return logic.inOverState();
		});

		try {
			return future.get().booleanValue();
		} catch (Exception e) {
			System.out.println("Controller: inOverState: future.get: error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks if the game is an online game
	 * 
	 * @return true/false
	 */
	public boolean isOnlineGame() {
		return logic instanceof OnlineLogic;
	}

	/**
	 * Sends the games status to the BoardUI
	 * 
	 * @param gameStatus The status string
	 */
	public void updateGameStatus(String gameStatus) {
		SwingUtilities.invokeLater(() -> {
			viewer.updateGameStatus(gameStatus);
		});
	}

	/**
	 * Checks if a game has been saved
	 * 
	 * @return true/false
	 */
	public boolean gameExists() {
		Future<Boolean> future = executor.submit(() -> {
			return userGames.gameExists(logic.getGame());
		});

		try {
			return future.get().booleanValue();
		} catch (Exception e) {
			System.out.println("Controller: gameExists: future.get: error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Applies the users settings
	 */
	public void applySettings() {
		executor.submit(() -> {
			//SoundPlayer.getInstance().updateSoundStatus(userSettings.playAudioEffects(), userSettings.playMusic(),
			//		userSettings.getEffectsVolume(), userSettings.getMusicVolume());
			SoundPlayer.getInstance().updateEffectsStatus(userSettings.playAudioEffects(), userSettings.getEffectsVolume());
			SoundPlayer.getInstance().updateMusicStatus(userSettings.playMusic(), userSettings.getMusicVolume());
			viewer.toggleFullscreen(userSettings.useFullscreen());
		});
	}
	
	public void volumeTest(int musicVolume) {
		if(musicVolume > 0) {
			executor.submit(() -> {
				SoundPlayer.getInstance().updateMusicStatus(true, -80);
				//SoundPlayer.getInstance().updateSoundStatus(userSettings.playAudioEffects(), true, userSettings.getEffectsVolume(), musicVolume);
			});
		}
		else {
			executor.submit(() -> {
				SoundPlayer.getInstance().updateMusicStatus(true, musicVolume);
				//SoundPlayer.getInstance().updateSoundStatus(userSettings.playAudioEffects(), true, userSettings.getEffectsVolume(), musicVolume);
			});
		}

	}
	
	public void effectsVolumeTest(int effectsVolume) {
		if(effectsVolume > 0) {
			executor.submit(() -> {
				SoundPlayer.getInstance().updateEffectsStatus(true, -80);
				//SoundPlayer.getInstance().updateSoundStatus(userSettings.playAudioEffects(), true, userSettings.getEffectsVolume(), musicVolume);
			});
		}
		else {
			executor.submit(() -> {
				SoundPlayer.getInstance().updateEffectsStatus(true, effectsVolume);
				//SoundPlayer.getInstance().updateSoundStatus(userSettings.playAudioEffects(), true, userSettings.getEffectsVolume(), musicVolume);
			});
		}

	}

	/**
	 * Places the pawn on the board
	 * 
	 * @param pawn The pawn to place
	 * @param pawnPosition The pawn in the position
	 */
	public void placePawn(Pawn pawn, Pawn pawnPosition) {
		executor.submit(() -> {
			logic.placePawn(pawn, pawnPosition);
		});
	}

	/**
	 * Called when the user is done with their setup
	 */
	public void setupDone() {
		executor.submit(() -> {
			logic.turnDone();
		});
	}

	/**
	 * Removes the side panel from the BoardUI
	 */
	public void removeSidePanel() {
		SwingUtilities.invokeLater(() -> {
			viewer.removeSidePanel();
		});
	}

	/**
	 * Removes the pawn from the game board
	 * 
	 * @param pawn The pawn
	 */
	public void removePawn(Pawn pawn) {
		executor.submit(() -> {
			logic.removePawn(pawn);
		});
	}

	/**
	 * Checks if a position is in the players setup area.
	 * 
	 * @param position The position
	 * @return true/false
	 */
	public boolean isInPlayerArea(Position position) {
		Future<Boolean> future = executor.submit(() -> {
			return logic.isInPlayerArea(position);
		});

		try {
			return future.get().booleanValue();
		} catch (Exception e) {
			System.out.println("Controller: isInPlayerArea: future.get: error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the position of the pawn
	 * 
	 * @param pawn The pawn
	 * @return {@link Position} The pawns position
	 */
	public Position getPawnPosition(Pawn pawn) {
		Future<Position> future = executor.submit(() -> {
			return logic.getPawnPosition(pawn);
		});

		try {
			return future.get();
		} catch (Exception e) {
			System.out.println("Controller: getPawnPosition: future.get: error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Checks if a position contains a Empty pawn.
	 * 
	 * @param selectedPosition The position to check
	 * @return true/false
	 */
	public boolean positionContainsEmptyPawn(Position selectedPosition) {
		Future<Boolean> future = executor.submit(() -> {
			return logic.positionIsEmpty(selectedPosition);
		});

		try {
			return future.get().booleanValue();
		} catch (Exception e) {
			System.out.println("Controller: positionContainsEmptyPawn: future.get: error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks if all the pawns have been placed
	 * 
	 * @return true/false
	 */
	public boolean allPawnsPlaced() {

		Future<Boolean> future = executor.submit(() -> {
			return logic.allPawnsPlaced();
		});

		try {
			return future.get().booleanValue();
		} catch (Exception e) {
			System.out.println("Controller: allPawnsPlaced: future.get: error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Saves a user setup
	 * 
	 * @param userSetupName The name to use
	 */
	public void saveUserSetup(String userSetupName) {
		executor.submit(() -> {
			userSetups.addUserSetup(userSetupName, logic.getUserSetup());
		});
	}

	/**
	 * Loads a saved user setup into the game
	 * 
	 * @param userSetupName The name of the setup
	 */
	public void loadFromUserSetup(String userSetupName) {
		executor.submit(() -> {
			logic.loadFromUserSetup(userSetups.getUserSetup(userSetupName));
		});
	}

	/**
	 * Sends a list of connected players to LobbyUI
	 * 
	 * @param list The list of connected players
	 */
	public void userList(String list) {
		SwingUtilities.invokeLater(() -> {
			viewer.userList(list);
		});
	}

	/**
	 * Shows a game request in the LobbyUI
	 * 
	 * @param opponent The opponent that sent the request
	 */
	public void showGameRequest(String opponent) {
		SwingUtilities.invokeLater(() -> {
			viewer.showGameRequest(opponent);
		});
	}

	/**
	 * Animates the pawn movement
	 * 
	 * @param pawn The pawn
	 * @param from The position to move from
	 * @param to The position to move to
	 * @param boardGrid The games board grid
	 */
	public void showPawnMovement(Pawn pawn, Position from, Position to, Pawn[][] boardGrid) {
		SwingUtilities.invokeLater(() -> {
			viewer.showPawnMovement(pawn, from, to, boardGrid);
		});
	}
	
	/**
	 * shows animation if player wins
	 */
	public void showEndAnimation(String string) {
		viewer.showEndAnimation(string);
	}
	
	/**
	 * Returns the instance of the database communication object
	 * @return
	 */
	public HSDatabase getDatabase() {
		return this.db;
	}
	
	public void opponentForfeit(String opponent, String user) {
		JOptionPane.showMessageDialog(null, "Congratulations, you won! " + opponent + " forfeited the game.");
		try {
			db.gameWon(user);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sendObject("OPPONENT_FORFEITED");
		viewer.updatetoLobby();
	}
	
	/**
	 * Updates the winner of a online game to the database
	 * @param winner
	 */
	public void updateWin(String winner) {
		try {
			db.gameWon(winner);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		// MacOS - set the name of the application
		System.setProperty("apple.awt.application.name", "Stratego");
		new Controller();
	}

}