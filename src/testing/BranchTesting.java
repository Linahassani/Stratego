package testing;

import controller.Controller;
import game.HotseatLogic;
import game.OnlineLogic;
import game.Position;
import game.Game.State;
import pawns.Bomb;
import pawns.Captain;
import pawns.Colonel;
import pawns.Flag;
import pawns.Lieutenant;
import pawns.Marshall;
import pawns.Miner;
import pawns.Pawn;
import pawns.Spy;
import pawns.Pawn.Color;

/**
 * Isolated class for White-Box Branch testing.
 * @author Henrik Sandström
 */
public class BranchTesting {
	
	public static void main(String[] args) {
		BranchTesting test = new BranchTesting();
		test.testShowHidePawn();
	}
	
	public void testFight() {
		OnlineLogic logic = new OnlineLogic(new Controller());
		logic.newGame(Color.BLUE);
		
		Pawn defender, attacker;
		
		defender = new Flag(Pawn.Color.BLUE);
		attacker = new Marshall(Pawn.Color.RED);		
		System.out.println("Testcase 1: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
		
		defender = new Flag(Pawn.Color.RED);
		attacker = new Marshall(Pawn.Color.BLUE);		
		System.out.println("Testcase 2: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
		
		defender = new Bomb(Pawn.Color.BLUE);
		attacker = new Marshall(Pawn.Color.RED);
		System.out.println("Testcase 3: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
		
		defender = new Bomb(Pawn.Color.RED);
		attacker = new Miner(Pawn.Color.BLUE);
		System.out.println("Testcase 4: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
		
		defender = new Marshall(Pawn.Color.BLUE);
		attacker = new Spy(Pawn.Color.RED);
		System.out.println("Testcase 5: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
		
		defender = new Marshall(Pawn.Color.BLUE);
		attacker = new Captain(Pawn.Color.RED);
		System.out.println("Testcase 6: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
		
		defender = new Lieutenant(Pawn.Color.BLUE);
		attacker = new Colonel(Pawn.Color.RED);
		System.out.println("Testcase 7: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
		
		defender = new Lieutenant(Pawn.Color.BLUE);
		attacker = new Lieutenant(Pawn.Color.RED);
		System.out.println("Testcase 8: " + logic.fight(attacker, defender).getClass().getSimpleName() + " won");
	}
	
	public void testTurnDone() {
		HotseatLogic logic = new HotseatLogic(new Controller());
		logic.newGame();
		
		Pawn[][] userSetup = new Pawn[4][10];
		for(int row = 0; row < userSetup.length; row++) {
			for(int col = 0; col < userSetup[row].length; col++) {
				userSetup[row][col] = new Bomb(Pawn.Color.BLUE);
			}
		}
		
		logic.getGame().setCurrentState(State.BLUE);
		logic.turnDone();
		System.out.println("Testcase 9: " + logic.getGame().getCurrentState());
		
		logic.getGame().setCurrentState(State.RED);
		logic.turnDone();
		System.out.println("Testcase 10: " + logic.getGame().getCurrentState());		
		
		logic.getGame().setCurrentState(State.SETUP_BLUE);
		logic.loadFromUserSetup(userSetup);
		logic.turnDone();
		System.out.println("Testcase 11: " + logic.getGame().getCurrentState());
		
		logic.getGame().setCurrentState(State.SETUP_RED);
		logic.loadFromUserSetup(userSetup);
		logic.turnDone();
		System.out.println("Testcase 12: " + logic.getGame().getCurrentState());
	}
	
	public void testPlaceRemovePawn() {
		OnlineLogic logic = new OnlineLogic(new Controller());	
		logic.newGame(Color.RED);
		
		Pawn newPawn = new Bomb(Color.RED);
		
		logic.placePawn(newPawn, logic.getGame().getPawn(new Position(0,0)));
		System.out.println("Testcase 13: " + (logic.getGame().getPawn(new Position(0,0))).getClass().getSimpleName());
		
		logic.removePawn(newPawn);
		System.out.println("Testcase 14: " + (logic.getGame().getPawn(new Position(0,0))).getClass().getSimpleName());
	}
	
	public void testIsInPlayerArea() {
		OnlineLogic logic = new OnlineLogic(new Controller());	
		
		Position position = new Position(0,0);
		
		logic.newGame(Color.RED);
		System.out.println("Testcase 15: " + logic.isInPlayerArea(position));
		
		logic.newGame(Color.BLUE);
		System.out.println("Testcase 16: " + logic.isInPlayerArea(position));
	}
	
	public void testPawnIsEnemy() {
		OnlineLogic logic = new OnlineLogic(new Controller());	
		
		Pawn pawn = new Bomb(Pawn.Color.BLUE);
		
		logic.newGame(Color.RED);
		System.out.println("Testcase 17: " + logic.pawnIsEnemy(pawn));
		
		logic.newGame(Color.BLUE);
		System.out.println("Testcase 18: " + logic.pawnIsEnemy(pawn));
	}
	
	public void testShowHidePawn() {
		OnlineLogic logic = new OnlineLogic(new Controller());
		
		Pawn pawn = new Bomb(Color.BLUE);
		
		logic.hidePawn(pawn);
		System.out.println("Testcase 19: " + pawn.isHidden());
		
		logic.showPawn(pawn);
		System.out.println("Testcase 20: " + pawn.isHidden());
	}
	

}
