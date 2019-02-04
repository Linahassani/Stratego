package testing;

import controller.Controller;
import game.Game.State;
import game.OnlineLogic;
import game.Position;
import pawns.Pawn.Color;
import pawns.*;

/**
 * White-box testing. Path Coverage.
 * 
 * @author André Hansson
 */
public class PathCoverage {

	/**
	 * Runs the test
	 */
	public static void main(String[] args) {
		PathCoverage.fight();
		// PathCoverage.select();
		// PathCoverage.place();
	}

	/**
	 * Tests the fight method in Logic. OnlineLogic is used because Logic is an
	 * abstract class, but the method that is run is in Logic.
	 */
	public static void fight() {

		OnlineLogic logic = new OnlineLogic(new Controller());
		logic.newGame(Color.BLUE);

		fightTestCase(1, new Sergeant(Color.BLUE), new Flag(Color.RED), logic);
		fightTestCase(2, new Sergeant(Color.RED), new Flag(Color.BLUE), logic);
		fightTestCase(3, new Miner(Color.BLUE), new Bomb(Color.RED), logic);
		fightTestCase(4, new Sergeant(Color.BLUE), new Bomb(Color.RED), logic);
		fightTestCase(5, new Spy(Color.BLUE), new Marshall(Color.RED), logic);
		fightTestCase(6, new Major(Color.BLUE), new Major(Color.RED), logic);
		fightTestCase(7, new Major(Color.BLUE), new Captain(Color.RED), logic);
		fightTestCase(8, new Spy(Color.BLUE), new General(Color.RED), logic);
	}

	/**
	 * Runs a fight test case
	 * 
	 * @param testCase The test case number
	 * @param attacker The attacking pawn
	 * @param defender The defending pawn
	 * @param logic The logic to run on
	 */
	public static void fightTestCase(int testCase, Pawn attacker, Pawn defender, OnlineLogic logic) {
		System.out.println("Test case " + testCase + ": " + logic.fight(attacker, defender).getClass().getSimpleName());
	}

	/**
	 * Tests the select method in Logic. OnlineLogic is used because Logic is an
	 * abstract class, but the method that is run is in Logic.
	 */
	public static void select() {

		OnlineLogic logic = new OnlineLogic(new Controller());
		logic.newGame(Color.BLUE);
		logic.setState(State.BLUE);

		selectTestCase(1, new Scout(Color.BLUE), new Position(6, 5), logic);
		selectTestCase(2, new Major(Color.BLUE), new Position(0, 0), logic);
		selectTestCase(3, new Major(Color.BLUE), new Position(9, 9), logic);
		selectTestCase(4, new Major(Color.BLUE), new Position(0, 9), logic);
		selectTestCase(5, new Major(Color.BLUE), new Position(9, 0), logic);
		selectTestCase(6, new Major(Color.BLUE), new Position(0, 5), logic);
		selectTestCase(7, new Major(Color.BLUE), new Position(9, 5), logic);
		selectTestCase(8, new Major(Color.BLUE), new Position(5, 0), logic);
		selectTestCase(9, new Major(Color.BLUE), new Position(5, 9), logic);

		addPawn(new Major(Color.RED), new Position(3, 0), logic);
		addPawn(new Major(Color.RED), new Position(3, 7), logic);
		addPawn(new Major(Color.RED), new Position(2, 4), logic);
		addPawn(new Major(Color.RED), new Position(7, 4), logic);
		selectTestCase(10, new Scout(Color.BLUE), new Position(3, 4), logic);

		addPawn(new Major(Color.BLUE), new Position(3, 4), logic);
		addPawn(new Major(Color.BLUE), new Position(5, 4), logic);
		addPawn(new Major(Color.BLUE), new Position(4, 5), logic);
		selectTestCase(11, new Scout(Color.BLUE), new Position(4, 4), logic);
	}

	/**
	 * Adds a pawn to the board
	 * 
	 * @param pawn The pawn
	 * @param position The position
	 * @param logic The logic
	 */
	public static void addPawn(Pawn pawn, Position position, OnlineLogic logic) {
		logic.addPawn(pawn, position);
	}

	/**
	 * Runs a select test case
	 * 
	 * @param testCase The test case number
	 * @param pawn The pawn
	 * @param position The position
	 * @param logic The logic to run on
	 */
	public static void selectTestCase(int testCase, Pawn pawn, Position position, OnlineLogic logic) {
		logic.addPawn(pawn, position);
		System.out.println("Test case " + testCase + ": " + logic.selectPawn(pawn, position));
	}

	/**
	 * Tests the placePawn method in Logic. OnlineLogic is used because Logic is an
	 * abstract class, but the method that is run is in Logic.
	 */
	public static void place() {
		OnlineLogic logic = new OnlineLogic(new Controller());
		logic.newGame(Color.BLUE);
		
		placeTestCase(1, new Major(Color.BLUE), new Position(0, 0), logic); 	// Major & Empty
		placeTestCase(2, new Major(Color.BLUE), new Position(4, 2), logic); 	// Major & Lake
		placeTestCase(3, null, new Position(9, 9), logic); 					// null & Empty
	}

	/**
	 * Runs a place test case
	 * 
	 * @param testCase The test case number
	 * @param pawn The pawn
	 * @param position The position
	 * @param logic The logic to run on
	 */
	public static void placeTestCase(int testCase, Pawn pawn, Position position, OnlineLogic logic) {
		Pawn pawnPosition = logic.getGame().getPawn(position);
		logic.placePawn(pawn, pawnPosition);
		System.out.println("Test case " + testCase + ": " + logic.getGame().getPawn(position).getClass().getSimpleName());
	}

}