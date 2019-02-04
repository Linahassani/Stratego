package server;

import java.util.HashMap;

/**
 * class that stores the available to play clients in a hashmap.Singleton class.
 * 
 * @author Anders Qvist
 *
 */
public class Available {
	private static Available instance;
	private HashMap<String, ClientHandler> available = new HashMap<String, ClientHandler>();

	
	/**
	 * creates instance if none exist
	 * 
	 * @return the current instance of the class
	 */
	public static Available getInstance() {
		if (instance == null) {
			instance = new Available();
		}
		return instance;
	}

	/**
	 * adds new client to the map with username as key
	 * @param userName the key
	 * @param client the value is the clienthandler for the client
	 */
	public synchronized void put(String userName, ClientHandler client) {
		available.put(userName, client);
	}

	/**
	 * gets the clienthandler for associated username
	 * 
	 * @param userName  the name chosen by the client
	 * @return the clienthandler for the given username
	 */
	public synchronized ClientHandler get(String userName) {
		return available.get(userName);
	}

	/**
	 * remove the entry from the hashmap
	 * 
	 * @param userName  the key for the entry
	 */
	public synchronized void remove(String userName) {
		available.remove(userName);
	}

	/**
	 * method that goes through the hashmap and adds the usernames to a list
	 * @return a list of users, ready to send to clients
	 */
	public synchronized String getUserList() {
		String str = "USER_LIST";
		
		for (String user : available.keySet()) {
			str += "," + user;
		}
		return str;
	}
	
	/**
	 * checks if username exists in the hashmap
	 * @param userName the name to check
	 */

	public synchronized boolean nameExist(String userName) {
		boolean exist = available.containsKey(userName);
		
		return exist;
	}
	
	/**
	 * checks if a player object with the name username exists
	 * @param available the list to return
	 */
	public synchronized HashMap<String, ClientHandler> getMap() {
		return available;
	}
}