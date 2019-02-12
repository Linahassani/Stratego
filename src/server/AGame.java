package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//Test comment on DEVELOPER BRANCH

/**
 * class that holds player object with its opponent player. Singleton class
 * @author Anders Qvist
 *
 */
public class AGame {
	private static AGame instance;
	private HashMap<Player, String> aGame = new HashMap<Player, String>();

	
	/**
	 * creates instance if none exists
	 * @return the current instance
	 */
	public static AGame getInstance() {
		if (instance == null) {

			instance = new AGame();
		}
		return instance;
	}

	/**
	 * adds new player as key and opponent as value
	 * @param player the player object(that contains username and clienthandler)
	 * @param opponent the username of the opponent
	 */
	public synchronized void put(Player player, String opponent) {
		aGame.put(player, opponent);
	}

	/**
	 * class that return the player object for the given username
	 * if name doesn't exist null is returned
	 * @param userName is used to search for the player object
	 * @return the player object or null 
	 */
	public synchronized Player getPlayer(String userName) {
		Set<Entry<Player, String>> set = aGame.entrySet();
		Iterator<Entry<Player, String>> i = set.iterator();
		
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			Player player = (Player) me.getKey();
			if (player.getUserName().equals(userName)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * removes the player for the given username
	 * @param userName
	 */
	public synchronized void remove(String userName) {
		aGame.remove(getPlayer(userName));
	}
	
	/**
	 * checks if a player object with the name username exists
	 * @param userName the name to check
	 */
	public synchronized boolean nameExist(String userName) {
		boolean exist = aGame.containsKey(getPlayer(userName));
		
		return exist;
	}
	
	/**
	 * returns the opponent for the player object
	 * @return opponent
	 */
	public String getOpponent(Player player) {
		String opponent=(String)aGame.get(player);
		
		return opponent;
	}
}