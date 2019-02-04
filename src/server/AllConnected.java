package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Anders Qvist
 * Class to hold all connected clients in a hashmap.The username is the key. Singleton class
 *
 */
public class AllConnected {
	private static AllConnected instance;
	private HashMap<String, ClientHandler> allConnected = new HashMap<String, ClientHandler>();

	
	/**
	 * resturns the instance, if none exist, one is created.
	 * @return current instance
	 */
	public static AllConnected getInstance() {
		if (instance == null) {
			instance = new AllConnected();
		}
		return instance;
	}

	/**
	 * adds the username and clienthandler to the hashmap
	 * @param userName the key of the hashmap, the username of the client
	 * @param client the clienthandler object
	 */
	public synchronized void put(String userName, ClientHandler client) {
		allConnected.put(userName, client);
	}

	/**
	 * returns the clienthandler object
	 * @param userName the key to the hashmap
	 * @return the clienthandler object for the given username
	 */
	public synchronized ClientHandler get(String userName) {
		return allConnected.get(userName);
	}

	/**
	 * removes the user from the hashmap
	 * @param userName the key for the entry in the hashmap that is to be removed
	 */
	public synchronized void remove(String userName) {
		allConnected.remove(userName);
	}
	/**
	 * method that puts all keys(usernames) from the hashmap into an array	 * 
	 * @return userArray the array of user names
	 */
	public synchronized  ArrayList<String> getArray() {
		ArrayList<String> userArray=new ArrayList<String>();
		
		userArray.addAll(allConnected.keySet());
		return userArray;
	}
	
	/**
	 * checks if the key already exists
	 * @param userName the key in the hashmap
	 * @return true or false if the username existed or not
	 */
	public synchronized boolean nameExist(String userName) {
		boolean exist = allConnected.containsKey(userName);
		
		return exist;
	}
	
	/**
	 * Returns all connected ClientHandlers
	 * @return all ClientHandlers
	 * @author André Hansson
	 */
	public synchronized Collection<ClientHandler> getAllClientHandlers(){
		return allConnected.values();
	}
}