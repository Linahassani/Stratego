package server;

/**
 * class that is a player with username and clienthandler
 * @author Anders Qvist
 *
 */
public class Player {
	private String userName;
	private ClientHandler client;

	
	/**
	 * constructor that takes username and clienthandler
	 * @param userName the identifier for the client
	 * @param client the clienthandler object
	 */
	public Player(String userName, ClientHandler client) {
		this.userName = userName;
		this.client = client;
	}

	/**
	 * @return the username
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @return the clienthandler
	 */
	public ClientHandler getClient() {
		return this.client;
	}
}