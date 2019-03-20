package user;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import game.Game;
import pawns.Pawn;

/**
 * Class for handling saving/loading of Game-objects. Singleton
 * @author Henrik
 *
 */
public class UserGames {

	private static UserGames instance;
	private ArrayList<Game> games;;	
	private static final String FILE_PATH = "files/user/games.dat";

	/**
	 * Constructs itself with the values in games.dat
	 */
	private UserGames() {
		readGames();
	}
	
	/**
	 * Checks if an instance exits, if not, creates one.
	 * @return instance
	 */
	public static UserGames getInstance() {
		if(instance == null) {
			instance = new UserGames();
		}
		return instance;
	}

	/**
	 * Reads games.dat and updates the list of saved game-objects.
	 */
	public void readGames() {
		Object object = FileHandler.readObject(FILE_PATH);
		if(object instanceof ArrayList) {
			games = (ArrayList<Game>)object;
		}
		else {
			games = new ArrayList<Game>();
		}
	}
	
	/**
	 * Writes the current list of saved game-object to games.dat.
	 */
	public void writeGames() {	
		FileHandler.writeObject(FILE_PATH, games);
	}
	
	/** 
	 * Adds a new game-object to the list.
	 * @param game Game object
	 */
	public void addGame(Game game) {
		removeListeners(game);
		games.add(game);
		writeGames();
	}	
	
	/**
	 * Updates a game in the game list.
	 * @param game
	 */
	public void updateGame(Game game) {
		removeListeners(game);
		for(Game g : games) {
			if(g.getGameName()  == game.getGameName()) {
				games.set(games.indexOf(g), game);
			}
		}
		writeGames();
	}
	
	/**
	 * Removes MouseListeners & ActionListeners from all pawn-objects of a Game.
	 * If not done, the Game-object will not be Serializable.
	 * @param game
	 */
	public void removeListeners(Game game) {
		Pawn[][] boardGrid = game.getBoardGrid();
		for(int row = 0; row < boardGrid.length; row++) {
			for(int col = 0; col < boardGrid[row].length; col++) {
				Pawn pawn = boardGrid[row][col];
				for(MouseListener mouseListener : pawn.getMouseListeners()) {
					pawn.removeMouseListener(mouseListener);
				}	
				for(ActionListener actionListener : pawn.getActionListeners()) {
					pawn.removeActionListener(actionListener);
				}
			}
		}
	}
	
	/**
	 * Updates the game-object list and returns it.
	 * @return Saved game-objects list.
	 */
	public ArrayList<Game> getGames(){
		readGames();
		return games;
	}
	
	/**
	 * Checks if a game-object already exists.
	 * @param game The game-object too search for.
	 * @return boolean
	 */
	public boolean gameExists(Game game) {
		return games.contains(game);
	}

}
