package game;

import java.util.Random;


import javax.swing.JOptionPane;

import controller.Controller;
import game.Game.State;
import pawns.*;
import pawns.Pawn.Color;;

/**
 * Logic for online games
 * 
 * @author André Hansson
 */
public class OnlineLogic extends Logic {

	private Player local;
	private Player opponent;
	private boolean opponentSetupDone = false;

	/**
	 * Constructor. Sets the controller.
	 * 
	 * @param controller The controller
	 */
	public OnlineLogic(Controller controller) {
		super(controller);
	}

	/**
	 * Creates a new game. Needs a color for the local player.
	 * 
	 * @param localColor The color for the local player
	 * @see Logic#newGame()
	 */
	public void newGame(Color localColor) {
		local = new Player(localColor);

		if (localColor == Color.BLUE) {
			opponent = new Player(Color.RED);
			setGame(new Game(local, opponent));
		} else {
			opponent = new Player(Color.BLUE);
			setGame(new Game(opponent, local));
		}

		setState(State.SETUP_ONLINE);

		setPawnList(new PawnList(local.getColor()));

		super.newGame();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sends the pawn placement to the opponent. If the opponent have already sent
	 * it's pawn placement, the method also randomly selects who starts and notifies
	 * the opponent.
	 */
	@Override
	protected void setupDone() {

		sendPawnPlacement();

		if (opponentSetupDone) {
			// Randomly select who goes first
			Random random = new Random();

			State first;

			if (random.nextInt(2) == 0) {
				first = State.BLUE;
			} else {
				first = State.RED;
			}

			setState(first);

			controller.sendObject("SETUP_DONE," + first);
		}

		controller.removeSidePanel();
	}

	/**
	 * Receives the message with who starts from the opponent.
	 * 
	 * @param state The beginning state
	 */
	public void opponentSetupDone(String state) {
		if (state.equals("BLUE")) {
			setState(State.BLUE);
		} else {
			setState(State.RED);
		}
	}

	@Override
	public void turnDone() {
		if (game.getCurrentState() == State.BLUE) {
			setState(State.RED);
		} else if (game.getCurrentState() == State.RED) {
			setState(State.BLUE);

		} else if (game.getCurrentState() == State.SETUP_ONLINE) {
			if (pawnList.pawnListSize() == 0) {
				setupDone();
			} // else There are more pawns to place
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sends a message to the server about the win.
	 * Plays sounds and shows animation
	 */
	@Override
	public void winner(Player winner) {
		if(winner == null) {
			controller.sendObject("DRAW");
			SoundPlayer.getInstance().playLoserMusic();
		} else if(winner == local) {
			controller.sendObject("WIN");
			controller.showEndAnimation("WINNER");
			SoundPlayer.getInstance().playWinnerMusic();

		} else if(winner == opponent) {
			SoundPlayer.getInstance().playLoserMusic();
			controller.showEndAnimation("LOOSER");
		}
		super.winner(winner);
	}

	/**
	 * Receives forfeit from opponent. The local player wins. 
	 * @see Logic#winner(Player)
	 */
	public void opponentForfeit() {
		super.winner(local);
	}

	@Override
	public SelectValue selectPawn(Pawn pawn, Position position) {
		SelectValue value;

		// Check if selected pawn belongs to the player
		if (local.getColor() == Color.BLUE && game.getCurrentState() == State.BLUE && pawn.getColor() == Color.BLUE
				|| local.getColor() == Color.RED && game.getCurrentState() == State.RED
				&& pawn.getColor() == Color.RED) {
			value = new SelectValue(true);
		} else {
			value = new SelectValue(false);
			return value;
		}

		return super.select(pawn, position, value);
	}

	@Override
	public boolean isInPlayerArea(Position position) {
		if (local.getColor() == Color.RED) {
			return (position.getRow() <= 3);
		}
		return (position.getRow() >= 6);
	}

	@Override
	public boolean pawnIsEnemy(Pawn pawn) {
		if (local.getColor() == Color.BLUE && pawn.getColor() == Color.RED
				|| local.getColor() == Color.RED && pawn.getColor() == Color.BLUE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Shows the fighting pawns to both players. When the fight is over the pawns
	 * are hidden for the opponent again.
	 * 
	 * @see Logic#showPawn(Pawn)
	 * @see Logic#hidePawn(Pawn)
	 */
	@Override
	public Pawn fight(Pawn attacker, Pawn defender) {

		if (opponent.getColor() == attacker.getColor()) {
			showPawn(attacker);
		} else {
			showPawn(defender);
		}

		Pawn winner = super.fight(attacker, defender);

		// sleep - shows the pawns before the move animation shows
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// start a new thread to hide the pawn after 1 second
		new Thread(() -> {
			try {
				Thread.sleep(1300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (winner.getColor() == opponent.getColor()) {
				hidePawn(winner);
			}
		}).start();

		return winner;
	}

	/**
	 * Sends the move to the opponent.
	 * <p>
	 * Then {@inheritDoc}
	 */
	@Override
	public void move(Pawn pawn, Position from, Position to) {

		// send move to opponent
		controller.sendObject(
				"MOVE," + from.getRow() + "," + from.getColumn() + "," + to.getRow() + "," + to.getColumn());

		super.move(pawn, from, to);
	}

	/**
	 * Receives move from opponent
	 * 
	 * @param fromRow The pawns start row
	 * @param fromColumn The pawns start column
	 * @param toRow The pawns end row
	 * @param toColumn The pawns end column
	 */
	public void move(int fromRow, int fromColumn, int toRow, int toColumn) {

		Position from = new Position(fromRow, fromColumn);
		Position to = new Position(toRow, toColumn);

		Pawn pawn = game.getPawn(from);

		super.move(pawn, from, to);
	}

	/**
	 * Sends the pawn placement to the opponent
	 */
	private void sendPawnPlacement() {
		Pawn[][] pawns = getUserSetup();
		String message = "PAWN_PLACEMENT";

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 10; col++) {
				message += "," + pawns[row][col].getClass().getSimpleName();
			}
		}

		controller.sendObject(message);
	}

	/**
	 * Receives the opponents pawn placement
	 * 
	 * @param message The pawn placement
	 */
	public void pawnPlacementReceived(String message) {

		opponentSetupDone = true;

		String withoutHeader = message.substring(message.indexOf(",") + 1);

		String[] pawnNames = withoutHeader.split(",");

		Color color;

		if (local.getColor() == Color.BLUE) {
			// Opponent is RED
			color = Color.RED;
		} else {
			// Opponent is BLUE
			color = Color.BLUE;
		}

		Pawn[][] pawns = new Pawn[4][10];

		int index = 0;

		if (color == Color.BLUE) {
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 10; col++) {
					Pawn pawn = createPawn(pawnNames[index++], color);
					pawn.setHidden(true);
					pawns[row][col] = pawn;
					game.addPawn(pawns[row][col], new Position(row + 6, col));
				}
			}
		} else {
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 10; col++) {
					Pawn pawn = createPawn(pawnNames[index++], color);
					pawn.setHidden(true);
					pawns[row][col] = pawn;
					game.addPawn(pawns[row][col], new Position(3 - row, 9 - col));
				}
			}
		}

		updateMatrix();
	}

	/**
	 * Creates a pawn from a string and color
	 * 
	 * @param pawnName The name of the pawn class
	 * @param color The color
	 * @return The pawn
	 */
	private Pawn createPawn(String pawnName, Color color) {
		Pawn pawn = null;
		if (pawnName.equals("Bomb")) {
			pawn = new Bomb(color);
		} else if (pawnName.equals("Captain")) {
			pawn = new Captain(color);
		} else if (pawnName.equals("Colonel")) {
			pawn = new Colonel(color);
		} else if (pawnName.equals("Flag")) {
			pawn = new Flag(color);
		} else if (pawnName.equals("General")) {
			pawn = new General(color);
		} else if (pawnName.equals("Lieutenant")) {
			pawn = new Lieutenant(color);
		} else if (pawnName.equals("Major")) {
			pawn = new Major(color);
		} else if (pawnName.equals("Marshall")) {
			pawn = new Marshall(color);
		} else if (pawnName.equals("Miner")) {
			pawn = new Miner(color);
		} else if (pawnName.equals("Scout")) {
			pawn = new Scout(color);
		} else if (pawnName.equals("Sergeant")) {
			pawn = new Sergeant(color);
		} else if (pawnName.equals("Spy")) {
			pawn = new Spy(color);
		}
		return pawn;
	}

	@Override
	public Pawn[][] getUserSetup() {
		Pawn[][] userSetupGrid = new Pawn[4][10];

		if (local.getColor() == Color.BLUE) {
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

		if (local.getColor() == Color.BLUE) {
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