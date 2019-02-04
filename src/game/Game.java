package game;

import java.io.Serializable;
import java.util.Date;
import pawns.Empty;
import pawns.Pawn;

/**
 * Class that handles the game data
 * 
 * @author André Hansson
 */
public class Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176254298191209072L;
	private Pawn[][] boardGrid;
	private Player bluePlayer;
	private Player redPlayer;
	private String gameName;
	private Date gameTime;

	public enum State {
		SETUP_ONLINE, SETUP_BLUE, SETUP_RED, BLUE, RED, DRAW, WINNER_BLUE, WINNER_RED
	};

	private State currentState;

	/**
	 * Constructor.
	 * 
	 * @param blue The blue player
	 * @param red The red player
	 */
	public Game(Player blue, Player red) {

		boardGrid = new Pawn[10][10];

		bluePlayer = blue;
		redPlayer = red;
	}

	/**
	 * Adds a pawn to the board grid.
	 * 
	 * @param pawn The pawn
	 * @param position The position
	 */
	public void addPawn(Pawn pawn, Position position) {
		boardGrid[position.getRow()][position.getColumn()] = pawn;
	}

	/**
	 * Returns the pawn at the specified position.
	 * 
	 * @param row The row
	 * @param column The column
	 * @return The pawn
	 */
	public Pawn getPawn(int row, int column) {
		return boardGrid[row][column];
	}

	/**
	 * Returns the pawn at the specified position.
	 * 
	 * @param position The position
	 * @return The pawn
	 */
	public Pawn getPawn(Position position) {
		return boardGrid[position.getRow()][position.getColumn()];
	}

	/**
	 * Removes the pawn at the specified position by setting it to a new Empty pawn.
	 * 
	 * @param position The position
	 */
	public void removePawn(Position position) {
		boardGrid[position.getRow()][position.getColumn()] = new Empty();
	}

	/**
	 * Returns the board grid, a 2d array of pawns.
	 * 
	 * @return The board grid
	 */
	public Pawn[][] getBoardGrid() {
		return boardGrid;
	}

	/**
	 * Returns the current state of the game.
	 * 
	 * @return The current state
	 */
	public State getCurrentState() {
		return currentState;
	}

	/**
	 * Sets the state of the game.
	 * 
	 * @param currentState The state to set
	 */
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	/**
	 * Returns the blue player.
	 * 
	 * @return The blue player
	 */
	public Player getBluePlayer() {
		return bluePlayer;
	}

	/**
	 * Sets the blue player.
	 * 
	 * @param bluePlayer The blue player
	 */
	public void setBluePlayer(Player bluePlayer) {
		this.bluePlayer = bluePlayer;
	}

	/**
	 * Returns the red player.
	 * 
	 * @return The red player
	 */
	public Player getRedPlayer() {
		return redPlayer;
	}

	/**
	 * Sets the red player.
	 * 
	 * @param redPlayer The red player
	 */
	public void setRedPlayer(Player redPlayer) {
		this.redPlayer = redPlayer;
	}

	/**
	 * Sets the game name.
	 * 
	 * @param gameName The name
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * Returns the games name.
	 * 
	 * @return The name
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * Sets the game time.
	 */
	public void setGameTime() {
		gameTime = new Date();
	}

	/**
	 * Returns the game time.
	 * 
	 * @return The time
	 */
	public Date getGameTime() {
		return gameTime;
	}

}