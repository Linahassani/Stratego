package game;

import java.util.Random;

import controller.Controller;
import game.Game.State;
import pawns.Pawn;
import pawns.Pawn.Color;

/**
 * Logic for hotseat games
 * 
 * @author André Hansson
 */
public class HotseatLogic extends Logic {

	/**
	 * Constructor. Sets the controller.
	 * 
	 * @param controller The controller
	 */
	public HotseatLogic(Controller controller) {
		super(controller);
	}

	/**
	 * Creates a new game.
	 * 
	 * @see Logic#newGame()
	 */
	@Override
	public void newGame() {
		setGame(new Game(new Player(Color.BLUE), new Player(Color.RED)));

		setState(State.SETUP_BLUE);

		setPawnList(new PawnList(Pawn.Color.BLUE));

		super.newGame();
	}

	/**
	 * Loads a game
	 * 
	 * @param game The game to load
	 * @author Henrik Sandström
	 */
	public void loadGame(Game game) {
		this.game = game;
		controller.sendToMatrix(game.getBoardGrid());
		controller.updateGameStatus(STATUS.get(game.getCurrentState()));
	}

	@Override
	protected void setupDone() {
		// Randomly select who goes first
		Random random = new Random();

		if (random.nextInt(2) == 0) {
			setState(State.BLUE);
		} else {
			setState(State.RED);
		}

		controller.removeSidePanel();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Shows and hides the opponents pawns depending on the game state. Also shows
	 * messages when the players need to switch.
	 */
	@Override
	public void setState(State state) {
		super.setState(state);

		if (state == State.BLUE) {
			hidePawns(Pawn.Color.RED);
			showMessage(STATUS.get(state), "Change turn");
		} else if (state == State.RED) {
			hidePawns(Pawn.Color.BLUE);
			showMessage(STATUS.get(state), "Change turn");
		}

		if (state == State.BLUE || state == State.SETUP_BLUE) {
			showPawns(Pawn.Color.BLUE);
			hidePawns(Pawn.Color.RED);
		} else if (state == State.RED || state == State.SETUP_RED) {
			showPawns(Pawn.Color.RED);
			hidePawns(Pawn.Color.BLUE);
		}
	}

	@Override
	public void turnDone() {
		if (game.getCurrentState() == State.BLUE) {
			setState(State.RED);
		} else if (game.getCurrentState() == State.RED) {
			setState(State.BLUE);
		} else if (game.getCurrentState() == State.SETUP_BLUE) {
			if (pawnList.pawnListSize() == 0) {
				setState(State.SETUP_RED);
				pawnList = new PawnList(Pawn.Color.RED);
				updateSidePanel();
				
			} // else There are more pawns to place
		} else if (game.getCurrentState() == State.SETUP_RED) {
			if (pawnList.pawnListSize() == 0) {
				setupDone();
			} // else There are more pawns to place
		}
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Plays game end/win sound.
	 * and shows animation
	 */
	@Override
	public void winner(Player winner) {
		if(winner != null) {
			controller.showEndAnimation("WINNER");
			SoundPlayer.getInstance().playWinnerMusic();
		} else {
			SoundPlayer.getInstance().playLoserMusic();
			controller.showEndAnimation("LOOSER");
		}		
		super.winner(winner);
	}

	@Override
	public SelectValue selectPawn(Pawn pawn, Position position) {
		SelectValue value;

		// Check if selected pawn belongs to the player
		if (game.getCurrentState() == State.BLUE && pawn.getColor() == Color.BLUE
				|| game.getCurrentState() == State.RED && pawn.getColor() == Color.RED) {
			value = new SelectValue(true);
		} else {
			value = new SelectValue(false);
			return value;
		}

		return super.select(pawn, position, value);
	}

	@Override
	public boolean isInPlayerArea(Position position) {
		if (game.getCurrentState() == State.SETUP_RED) {
			return (position.getRow() <= 3);
		}
		return (position.getRow() >= 6);
	}

	@Override
	public boolean pawnIsEnemy(Pawn pawn) {
		if (game.getCurrentState() == State.BLUE && pawn.getColor() == Color.RED
				|| game.getCurrentState() == State.RED && pawn.getColor() == Color.BLUE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Hides the players pawns. Shows both the pawns in the fight and opens a dialog
	 * to start the fight so both players can watch it.
	 */
	@Override
	public Pawn fight(Pawn attacker, Pawn defender) {

		hidePawns(attacker.getColor());
		showPawn(defender);
		showPawn(attacker);
		showMessage("Show attack", "Attack");

		return super.fight(attacker, defender);
	}

	@Override
	public Pawn[][] getUserSetup() {
		Pawn[][] userSetupGrid = new Pawn[4][10];
		if (game.getCurrentState() == State.SETUP_BLUE) {
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 10; col++) {
					userSetupGrid[row][col] = game.getPawn(new Position((row + 6), col));
				}
			}
		} else { // Reverse pawn positions
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 10; col++) {
					userSetupGrid[row][col] = game.getPawn(new Position((3 - row), (9 - col)));
				}
			}
		}
		return userSetupGrid;
	}

	@Override
	public void loadFromUserSetup(Pawn[][] userSetupGrid) {

		if (game.getCurrentState() == State.SETUP_BLUE) {
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 10; col++) {
					game.addPawn(clonePawn(userSetupGrid[row][col], Pawn.Color.BLUE), new Position((row + 6), col));
				}
			}
		} else { // Reverse pawn positions
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 10; col++) {
					game.addPawn(clonePawn(userSetupGrid[3 - row][9 - col], Pawn.Color.RED), new Position(row, col));
				}
			}
		}

		pawnList.clearPawnList();
		updateMatrix();
		updateSidePanel();
	}

}