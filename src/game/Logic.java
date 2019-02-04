package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JOptionPane;
import controller.Controller;
import game.Game.State;
import pawns.*;
import pawns.Pawn.Color;

/**
 * Class that handles the game logic. Abstract class, the subclasses OnlineLogic
 * and HotseatLogic are used for the different game modes.
 * 
 * @author André Hansson
 */
public abstract class Logic {

	protected Controller controller;
	protected Game game;
	protected PawnList pawnList;

	@SuppressWarnings("serial")
	protected static final HashMap<State, String> STATUS = new HashMap<State, String>() {
		{
			put(State.WINNER_RED, "Red player won");
			put(State.WINNER_BLUE, "Blue player won");
			put(State.DRAW, "It's a draw");
			put(State.BLUE, "Blue players turn");
			put(State.RED, "Red players turn");
			put(State.SETUP_BLUE, "Blue player setup");
			put(State.SETUP_RED, "Red player setup");
			put(State.SETUP_ONLINE, "Setup");
		}
	};

	/**
	 * Constructor. Sets the controller.
	 * 
	 * @param controller The controller
	 */
	protected Logic(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Used to set the game from the subclasses.
	 * 
	 * @param game The game
	 */
	protected void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Used to set the PawnList from the subclasses.
	 * 
	 * @param pawnList The PawnList
	 */
	protected void setPawnList(PawnList pawnList) {
		this.pawnList = pawnList;
	}

	/**
	 * Creates a new game. Called from subclasses to finalize game creation. 
	 */
	protected void newGame() {
		addLakesAndEmpty();
		updateMatrix();
		updateSidePanel();
	}

	/**
	 * Adds the lakes to the boardGrid
	 */
	private void addLakesAndEmpty() {

		// Lake 1
		game.addPawn(new Lake(), new Position(4, 2));
		game.addPawn(new Lake(), new Position(4, 3));
		game.addPawn(new Lake(), new Position(5, 2));
		game.addPawn(new Lake(), new Position(5, 3));

		// Lake 2
		game.addPawn(new Lake(), new Position(4, 6));
		game.addPawn(new Lake(), new Position(4, 7));
		game.addPawn(new Lake(), new Position(5, 6));
		game.addPawn(new Lake(), new Position(5, 7));

		// Empty
		for (int row = 0; row < 10; row++) {
			for (int column = 0; column < 10; column++) {
				if (game.getPawn(row, column) == null) {
					game.addPawn(new Empty(), new Position(row, column));
				}
			}
		}
	}

	/**
	 * Sets the game name and date.
	 * 
	 * @param gameName The name
	 * @author Henrik Sandström
	 */
	public void updateGameDetails(String gameName) {
		game.setGameName(gameName);
		game.setGameTime();
	}

	/**
	 * Returns the Game object.
	 * 
	 * @return The game
	 * @author Henrik Sandström
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Changes the state of the game. Also sends update to controller.
	 * 
	 * @param state The state to change to
	 */
	public void setState(State state) {
		game.setCurrentState(state);
		controller.updateGameStatus(STATUS.get(state));
	}

	/**
	 * Called when the player is done with their setup.
	 */
	protected abstract void setupDone();

	/**
	 * Called at the end of every turn.
	 */
	public abstract void turnDone();

	/**
	 * Ends the game.
	 * 
	 * @param winner The winner
	 */
	public void winner(Player winner) {
		if(winner == null) {
			setState(State.DRAW);
		} else if(winner.getColor() == Color.BLUE) {
			setState(State.WINNER_BLUE);
		} else {
			setState(State.WINNER_RED);
		}
		showPawns(Color.BLUE);
		showPawns(Color.RED);
	}

	/**
	 * Returns the board position of a pawn.
	 * 
	 * @param pawn The pawn
	 * @return The position
	 */
	public Position getPawnPosition(Pawn pawn) {
		for (int row = 0; row < game.getBoardGrid().length; row++) {
			for (int column = 0; column < game.getBoardGrid()[0].length; column++) {
				if (game.getPawn(row, column) == pawn) {
					return new Position(row, column);
				}
			}
		}
		return null;
	}

	/**
	 * Used during Setup state to place a pawn.
	 * 
	 * @param pawn The pawn
	 * @param position The pawn at the position to place on
	 */
	public void placePawn(Pawn pawn, Pawn pawnPosition) {
		if (pawnPosition instanceof Empty && pawn != null) {
			game.addPawn(pawn, getPawnPosition(pawnPosition));
			pawnList.removePawn(pawn);
			updateMatrix();
			updateSidePanel();
		}
	}

	/**
	 * Used during Setup state to remove a pawn.
	 * 
	 * @param pawn
	 */
	public void removePawn(Pawn pawn) {
		if (!(pawn instanceof Empty)) {
			game.removePawn(getPawnPosition(pawn));
			pawnList.addPawn(clonePawn(pawn, pawn.getColor()));
			updateMatrix();
			updateSidePanel();
		}
	}

	/**
	 * Show the pawns face side.
	 * 
	 * @param pawn The pawn
	 */
	public void showPawn(Pawn pawn) {
		pawn.setHidden(false);
		controller.reDrawMatrix();
	}

	/**
	 * Shows the face side of all the pawns with the specified color.
	 * 
	 * @param color The color
	 */
	public void showPawns(Pawn.Color color) {
		Pawn pawn;
		for (int row = 0; row < game.getBoardGrid().length; row++) {
			for (int column = 0; column < game.getBoardGrid()[0].length; column++) {
				pawn = game.getPawn(row, column);
				if (pawn != null) {
					if (pawn.getColor() == color) {
						pawn.setHidden(false);
					}
				}
			}
		}
		controller.reDrawMatrix();
	}

	/**
	 * Hide the pawns face side.
	 * 
	 * @param pawn The pawn
	 */
	public void hidePawn(Pawn pawn) {
		pawn.setHidden(true);
		controller.reDrawMatrix();
	}

	/**
	 * Hides the face side of all the pawns with the specified color.
	 * 
	 * @param color The color
	 */
	public void hidePawns(Pawn.Color color) {
		Pawn pawn;
		for (int row = 0; row < game.getBoardGrid().length; row++) {
			for (int column = 0; column < game.getBoardGrid()[0].length; column++) {
				pawn = game.getPawn(row, column);
				if (pawn != null) {
					if (pawn.getColor() == color) {
						pawn.setHidden(true);
					}
				}
			}
		}
		controller.reDrawMatrix();
	}

	/**
	 * Selects the pawn.
	 * 
	 * @param pawn The pawn
	 * @param position The position of the pawn
	 * @return {@link SelectValue}
	 */
	public abstract SelectValue selectPawn(Pawn pawn, Position position);

	/**
	 * Called by subclasses from their implementation of selectPawn
	 * 
	 * @param pawn The pawn
	 * @param position The position of the pawn
	 * @param value The {@link SelectValue} from the subclass
	 * @return {@link SelectValue}
	 */
	protected SelectValue select(Pawn pawn, Position position, SelectValue value) {

		int pawnRange = pawn.getRange();
		int row = position.getRow();
		int column = position.getColumn();
		ArrayList<Position> positions = new ArrayList<Position>();

		// Top
		if (row != 0) {
			for (int i = 1; i <= pawnRange; i++) {
				if (row - i < 0) {
					// The position is outside the board. Can't move further.
					break;
				} else {
					if (game.getPawn(row - i, column) instanceof Empty) {
						// The position is empty. Can move further.
						positions.add(new Position(row - i, column));
					} else if (pawnIsEnemy(game.getPawn(row - i, column))) {
						// The position contains an enemy. Can't move further.
						positions.add(new Position(row - i, column));
						break;
					} else {
						// The position is not empty or enemy. Own pawn or lake. Can't move further.
						break;
					}
				}
			}
		}

		// Right
		if (column != 9) {
			for (int i = 1; i <= pawnRange; i++) {
				if (column + i > 9) {
					break;
				} else {
					if (game.getPawn(row, column + i) instanceof Empty) {
						positions.add(new Position(row, column + i));
					} else if (pawnIsEnemy(game.getPawn(row, column + i))) {
						positions.add(new Position(row, column + i));
						break;
					} else {
						break;
					}
				}
			}
		}

		// Bottom
		if (row != 9) {
			for (int i = 1; i <= pawnRange; i++) {
				if (row + i > 9) {
					break;
				} else {
					if (game.getPawn(row + i, column) instanceof Empty) {
						positions.add(new Position(row + i, column));
					} else if (pawnIsEnemy(game.getPawn(row + i, column))) {
						positions.add(new Position(row + i, column));
						break;
					} else {
						break;
					}
				}
			}
		}

		// Left
		if (column != 0) {
			for (int i = 1; i <= pawnRange; i++) {
				if (column - i < 0) {
					break;
				} else {
					if (game.getPawn(row, column - i) instanceof Empty) {
						positions.add(new Position(row, column - i));
					} else if (pawnIsEnemy(game.getPawn(row, column - i))) {
						positions.add(new Position(row, column - i));
						break;
					} else {
						break;
					}
				}
			}
		}

		value.setPositions(positions);
		return value;
	}

	/**
	 * Checks if a position is within the players area.
	 * 
	 * @param position The position
	 * @return true/false
	 */
	public abstract boolean isInPlayerArea(Position position);

	/**
	 * Checks whether a given position contains an Empty pawn or not.
	 * 
	 * @param position The position
	 * @return true/false
	 */
	public boolean positionIsEmpty(Position position) {
		if (position != null) {
			return (game.getPawn(position) instanceof Empty);
		}
		return true;
	}

	/**
	 * Checks if all of the setup pawns of the current player has been placed.
	 * 
	 * @return true/false
	 */
	public boolean allPawnsPlaced() {
		return (pawnList.pawnListSize() == 0);
	}

	/**
	 * Checks if the pawn is an enemy pawn
	 * 
	 * @param pawn The pawn
	 * @return true/false
	 */
	public abstract boolean pawnIsEnemy(Pawn pawn);

	/**
	 * Moves the pawn
	 * 
	 * @param pawn The pawn
	 * @param from Start position
	 * @param to End position
	 */
	public void move(Pawn pawn, Position from, Position to) {
		// Remove from current position
		game.addPawn(new Empty(), from);

		Pawn toPawn = game.getPawn(to);

		if (toPawn instanceof Empty) {
			game.addPawn(pawn, to);
		} else {
			// Add the winner of the fight
			game.addPawn(fight(pawn, toPawn), to);
		}

		checkMovablePawns();
		
		controller.showPawnMovement(pawn, from, to, game.getBoardGrid());
		turnDone();
	}

	/**
	 * Calculate the result of a fight between two pawns.
	 * 
	 * @param attacker The attacking pawn
	 * @param defender The defending pawn
	 * @return {@link Pawn} The winner
	 */
	public Pawn fight(Pawn attacker, Pawn defender) {

		if (defender instanceof Flag) {
			if (attacker.getColor() == Color.BLUE) {
				winner(game.getBluePlayer());
				System.out.println("Blue player won");
			} else {
				winner(game.getRedPlayer());
				System.out.println("Red player won");
			}
			return attacker;

		} else if (defender instanceof Bomb) {
			if (attacker instanceof Miner) {
				return attacker;
			} else {
				return defender;
			}

		} else if (attacker instanceof Spy && defender instanceof Marshall) {
			return attacker;

		} else {
			if (attacker.getValue() == defender.getValue()) {
				return new Empty(); // both lose
			} else if (attacker.getValue() > defender.getValue()) {
				return attacker;
			} else {
				return defender;
			}
		}
	}
	
	/**
	 * Checks the game board for remaining movable pawns.
	 * Ends the game if any player has run out of movable pawns.
	 */
	public void checkMovablePawns() {
		if(!inOverState()) {
			int blueMovable = 0, redMovable = 0;
			for (int row = 0; row < game.getBoardGrid().length; row++) {
				for (int column = 0; column < game.getBoardGrid()[row].length; column++) {
					Pawn pawn = game.getPawn(row, column);
					if (!select(pawn, getPawnPosition(pawn), new SelectValue(true))
							.getPositions().isEmpty()) {						
						if(pawn.getColor() == Pawn.Color.BLUE) {
							blueMovable++;
						} else {
							redMovable++;
						}
					}
				}
			}

			if(redMovable <= 0 && blueMovable <= 0) {
				winner(null); //Draw
			} else if(blueMovable > 0 && redMovable <= 0) {
				winner(game.getBluePlayer());
			} else if(blueMovable <= 0 && redMovable > 0){
				winner(game.getRedPlayer());
			}
		}
	}

	/**
	 * Sends an update to the matrix
	 */
	public void updateMatrix() {
		controller.sendToMatrix(game.getBoardGrid());
	}

	/**
	 * Sends an update to the sidePanel
	 */
	public void updateSidePanel() {
		controller.sendToSidePanel(pawnList.getPawnGrid(), pawnList.getCounterGrid());
	}

	/**
	 * Checks and returns a boolean whether or not the game is in setup state.
	 * 
	 * @return true/false
	 */
	public boolean inSetupState() {
		return (game.getCurrentState() == State.SETUP_BLUE || game.getCurrentState() == State.SETUP_RED
				|| game.getCurrentState() == State.SETUP_ONLINE);
	}
	
	/**
	 * Checks and returns a boolean whether or not the game is in the over state.
	 * 
	 * @return true/false
	 */
	public boolean inOverState() {
		State state = game.getCurrentState();
		return (state == State.DRAW || state == State.WINNER_BLUE || state == State.WINNER_RED);
	}

	/**
	 * Shows a message in a confirm dialog
	 * 
	 * @param text The text
	 * @param title The title
	 */
	public void showMessage(String text, String title) {
		JOptionPane.showConfirmDialog(null, text, title, JOptionPane.DEFAULT_OPTION);
	}

	/**
	 * Fetches the current user setup.
	 * 
	 * @return The user setup
	 */
	public abstract Pawn[][] getUserSetup();

	/**
	 * Places pawns on the game board from a given user setup grid.
	 * 
	 * @param userSetupGrid
	 */
	public abstract void loadFromUserSetup(Pawn[][] userSetupGrid);

	/**
	 * Creates a new identical instance of a given pawn.
	 * 
	 * @param pawn Existing pawn
	 * @param color Pawn color
	 */
	public Pawn clonePawn(Pawn pawn, Color color) {
		Pawn clonedPawn = null;
		if (pawn instanceof Bomb) {
			clonedPawn = new Bomb(color);
		} else if (pawn instanceof Captain) {
			clonedPawn = new Captain(color);
		} else if (pawn instanceof Colonel) {
			clonedPawn = new Colonel(color);
		} else if (pawn instanceof Flag) {
			clonedPawn = new Flag(color);
		} else if (pawn instanceof General) {
			clonedPawn = new General(color);
		} else if (pawn instanceof Lieutenant) {
			clonedPawn = new Lieutenant(color);
		} else if (pawn instanceof Major) {
			clonedPawn = new Major(color);
		} else if (pawn instanceof Marshall) {
			clonedPawn = new Marshall(color);
		} else if (pawn instanceof Miner) {
			clonedPawn = new Miner(color);
		} else if (pawn instanceof Scout) {
			clonedPawn = new Scout(color);
		} else if (pawn instanceof Sergeant) {
			clonedPawn = new Sergeant(color);
		} else if (pawn instanceof Spy) {
			clonedPawn = new Spy(color);
		}
		return clonedPawn;
	}

	/**
	 * Adds a pawn to game. Only used for white-box testing.
	 * @param pawn The pawn
	 * @param position The position
	 */
	public void addPawn(Pawn pawn, Position position) {
		game.addPawn(pawn, position);
	}
	
}