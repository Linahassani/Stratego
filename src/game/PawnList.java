package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import pawns.*;

/**
 * Creates and handles the pawns during the setup stage
 * 
 * @author André Hansson
 */
public class PawnList {

	private ArrayList<Pawn> pawnList;
	private Pawn[][] pawnGrid; // 6x2
	private int[][] counterGrid; // 6x2
	private HashMap<String, Integer> counterMap;
	private HashMap<String, Pawn> pawnMap;

	/**
	 * Constructor that takes a color for the pawns
	 * 
	 * @param color
	 *            The color for the pawns
	 */
	public PawnList(Pawn.Color color) {

		pawnList = new ArrayList<Pawn>();
		pawnGrid = new Pawn[6][2];
		counterGrid = new int[6][2];
		pawnMap = new HashMap<String, Pawn>();
		counterMap = new HashMap<String, Integer>();

		createPawns(color);
		addToMaps();
		updateGrids();
	}

	/**
	 * Creates the pawns with the specified color
	 * 
	 * @param color
	 *            The color the pawns will have
	 */
	private void createPawns(Pawn.Color color) {  

		pawnList.add(new Flag(color));

		for (int i = 0; i < 6; i++) {
			pawnList.add(new Bomb(color));
		}

		pawnList.add(new Spy(color));

		for (int i = 0; i < 8; i++) {
			pawnList.add(new Scout(color));
		}

		for (int i = 0; i < 5; i++) {
			pawnList.add(new Miner(color));
		}

		for (int i = 0; i < 4; i++) {
			pawnList.add(new Sergeant(color));
		}

		for (int i = 0; i < 4; i++) {
			pawnList.add(new Lieutenant(color));
		}

		for (int i = 0; i < 4; i++) {
			pawnList.add(new Captain(color));
		}

		for (int i = 0; i < 3; i++) {
			pawnList.add(new Major(color));
		}

		for (int i = 0; i < 2; i++) {
			pawnList.add(new Colonel(color));
		}

		
		pawnList.add(new General(color));
		pawnList.add(new Marshall(color));
	}

	/**
	 * Adds the pawns and their counters to the maps
	 */
	private void addToMaps() {

		String key;

		for (Pawn pawn : pawnList) {

			key = pawn.getClass().getSimpleName();

			if (!pawnMap.containsKey(key)) {
				pawnMap.put(key, pawn);
			}

			if (!counterMap.containsKey(key)) {
				counterMap.put(key, 1);
			} else {
				counterMap.put(key, counterMap.get(key) + 1);
			}

		}

	}

	/**
	 * Removes the pawn
	 * 
	 * @param pawn
	 *            The pawn
	 */
	public void removePawn(Pawn pawn) {
		pawnList.remove(pawn);
		updateMaps();
	}

	/**
	 * Adds the pawn
	 * 
	 * @param pawn
	 *            The pawn
	 */
	public void addPawn(Pawn pawn) {
		pawnList.add(pawn);
		updateMaps();
	}

	/**
	 * Updates the maps. Checks for pawns that are empty
	 */
	private void updateMaps() {

		pawnMap.clear();
		counterMap.clear();

		addToMaps();

		String[] keys = { "Flag", "Bomb", "Spy", "Scout", "Miner", "Sergeant", "Lieutenant", "Captain", "Major",
				"Colonel", "General", "Marshall" };

		for (String key : keys) {
			if (!pawnMap.containsKey(key)) {
				pawnMap.put(key, new Empty());
				counterMap.put(key, 0);
			}
		}

		updateGrids();
	}

	/**
	 * Updates the 2D arrays
	 */
	private void updateGrids() {

		pawnGrid[0][0] = pawnMap.get("Flag");
		counterGrid[0][0] = counterMap.get("Flag");

		pawnGrid[0][1] = pawnMap.get("Bomb");
		counterGrid[0][1] = counterMap.get("Bomb");

		pawnGrid[1][0] = pawnMap.get("Spy");
		counterGrid[1][0] = counterMap.get("Spy");

		pawnGrid[1][1] = pawnMap.get("Scout");
		counterGrid[1][1] = counterMap.get("Scout");

		pawnGrid[2][0] = pawnMap.get("Miner");
		counterGrid[2][0] = counterMap.get("Miner");

		pawnGrid[2][1] = pawnMap.get("Sergeant");
		counterGrid[2][1] = counterMap.get("Sergeant");

		pawnGrid[3][0] = pawnMap.get("Lieutenant");
		counterGrid[3][0] = counterMap.get("Lieutenant");

		pawnGrid[3][1] = pawnMap.get("Captain");
		counterGrid[3][1] = counterMap.get("Captain");

		pawnGrid[4][0] = pawnMap.get("Major");
		counterGrid[4][0] = counterMap.get("Major");

		pawnGrid[4][1] = pawnMap.get("Colonel");
		counterGrid[4][1] = counterMap.get("Colonel");

		pawnGrid[5][0] = pawnMap.get("General");
		counterGrid[5][0] = counterMap.get("General");

		pawnGrid[5][1] = pawnMap.get("Marshall");
		counterGrid[5][1] = counterMap.get("Marshall");
	}
	
	/**
	 * Returns the size of the pawnList
	 * 
	 * @return The size of the pawnList
	 */
	public int pawnListSize() {
		return pawnList.size();
	}

	/**
	 * Returns the pawns
	 * 
	 * @return A 6x2 2D array of pawns
	 */
	public Pawn[][] getPawnGrid() {
		return pawnGrid;
	}

	/**
	 * Returns the counters of the different pawns
	 * 
	 * @return A 6x2 2D array of ints
	 */
	public int[][] getCounterGrid() {
		return counterGrid;
	}
	
	/**
	 * Fully clears the PawnList object of pawns.
	 */
	public void clearPawnList() {
		pawnList.clear();
		updateMaps();
	}

}