package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import highscore.HighScoreList;

/**
 * class that handles the clients and their messages, sends them to the correct
 * user. The class determine action depending on the header in the message
 * 
 * @author Anders Qvist
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Available available = Available.getInstance();
	private String userName;
	private String opponent;
	private AGame aGame = AGame.getInstance();
	private AllConnected allConnected = AllConnected.getInstance();
	private Log log = Log.getInstance();
	private HighScoreList highscore = HighScoreList.getInstance();
	private CallbackToServer listener;
	
	
	/**
	 * constructor that opens the streams for input and output
	 * 
	 * @param socket
	 */
	public ClientHandler(Socket socket) {
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			log.addToLog("ClientHandler : constructor " + e.toString() + e.getStackTrace()[0].getLineNumber()
					+ System.lineSeparator());
		}
	}

	/**
	 * run method that receives objects from the client and determine what to do
	 * with them depending on the header in the message received.
	 * 
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Object obj = ois.readObject();

				if (obj instanceof String) {
					String str = (String) obj;

					System.out.println("ClientHandler message received: " + str + " from: "
							+ ((userName != null) ? userName : "Unknown"));

					if (str.startsWith("USERNAME")) {
						boolean exist = allConnected.nameExist(str.substring(str.indexOf(",") + 1));
						
						if (!exist) {
							userName = str.substring(str.indexOf(",") + 1);
							System.out.println(userName);
							log.addToLog("New user : " + userName + " connected with ip : " + socket.getInetAddress()
									+ System.lineSeparator());
							connected();
							updateAvailable();
							opponent = null;

						} else {
							String res = "USERNAME_EXIST";
							
							log.addToLog("Username rejected : " + userName + "from ip : " + socket.getInetAddress()
									+ System.lineSeparator());
							send(res);
						}

					} else if (str.startsWith("OPPONENT_REQUEST")) {
						opponent = str.substring(str.indexOf(",") + 1);
						
						System.out.println(opponent);
						available.get(opponent).send("OPPONENT_REQUEST," + userName);

					} else if (str.startsWith("OPPONENT_DECLINE")) {
						opponent = str.substring(str.indexOf(",") + 1);
						
						available.get(opponent).send("OPPONENT_DECLINE," + userName);

					} else if (str.startsWith("OPPONENT_ACCEPT")) {
						opponent = str.substring(str.indexOf(",") + 1);

						send("START_GAME,BLUE," + userName + "," + opponent);
						available.get(opponent).send("START_GAME,RED," + opponent + "," + userName);
						aGame.put(new Player(userName, this), opponent);
						available.remove(userName);
						available.remove(opponent);
						updateAvailable();
						log.addToLog("New game between : " + userName + " and " + opponent + " from ip : "
								+ socket.getInetAddress() + " and ip: "
								+ allConnected.get(opponent).socket.getInetAddress() + System.lineSeparator());

					} else if (str.startsWith("WIN")) {
						highscore.addScore(userName, 1);
						sendToOpponent("WIN," + userName);
						log.addToLog("Username : " + userName + " has WON the game" + socket.getInetAddress()
								+ System.lineSeparator());

					} else if (str.startsWith("FORFEIT")) {
						highscore.addScore(opponent, 1); // opponent wins
						sendToOpponent("FORFEIT," + userName);
						log.addToLog("Username : " + userName + " has FORFEIT the game" + socket.getInetAddress()
								+ System.lineSeparator());
						available.put(userName, this);
						opponent = null;
						aGame.remove(userName);
						updateAvailable();

					} else if (str.startsWith("END_GAME")) {
						sendToOpponent("END_GAME," + userName);
						available.put(userName, this);
						opponent = null;
						aGame.remove(userName);
						updateAvailable();

					} else if (str.startsWith("HIGHSCORE")) {
						send("HIGHSCORE," + highscore.getHighscore());

					} else if (str.startsWith("DISCONNECT")) {
						disconnected();
						updateAvailable();
						
					} else if (str.startsWith("PLAYER_DISCONNECTED")) {
						highscore.addScore(opponent, 1);	 // opponent wins										
						log.addToLog("Username : " + userName + " has FORFEIT the game" + socket.getInetAddress()
						+ System.lineSeparator());
						available.put(userName, this);
						opponent = null;
						aGame.remove(userName);
						updateAvailable();

					} else if (str.startsWith("DRAW")){
						sendToOpponent("DRAW");
						log.addToLog("The game between " + userName + " and " + opponent + " has resulted in a draw"
								+ System.lineSeparator());

					} else {
						sendToOpponent(str);
					}
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			log.addToLog("ClientHandler : run " + e.toString() + e.getStackTrace()[0].getLineNumber()
					+ System.lineSeparator());
			disconnected(); // client disconnected
		}
	}

	/**
	 * the send method that send whatever object that is to be sent
	 * 
	 * @param obj object to send, normally a String
	 */
	public void send(Object obj) {
		ClientHandler temp = allConnected.get(userName);
		
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			log.addToLog("ClientHandler : send " + userName + " with ip :" + temp.socket.getInetAddress()
					+ "has disconnected" + e.toString() + e.getStackTrace()[0].getLineNumber()
					+ System.lineSeparator());
			disconnected(); // client disconnected
		}
	}

	/**
	 * Sends the object to the opponent, if there is one.
	 * 
	 * @param obj The object to send
	 * @author André Hansson
	 */
	public void sendToOpponent(Object obj) {
		if (opponent != null) {
			allConnected.get(opponent).send(obj);
			
		} else {
			System.out.println(((userName != null) ? userName : "Unknown" + " has no opponent to send to: " + obj));
		}
	}

	/**
	 * method that sends a list of users to all connected users
	 */
	public void updateAvailable() {
		for (ClientHandler client : allConnected.getAllClientHandlers()) {
			client.send(available.getUserList());
		}
	}

	/**
	 * put the newly connected client in the allconnected and the available lists
	 */
	public void connected() {

		allConnected.put(userName, this);
		available.put(userName, this); // this ClientHandler
		listener.updateUI();

	}

	/**
	 * removes the client from the 3 lists of users, the available to play, the
	 * players and the list of all clients
	 */
	public void disconnected() {
		ClientHandler temp = allConnected.get(userName);
		if (aGame.nameExist(userName)) {
			aGame.remove(userName);
		}
		
		if (available.nameExist(userName)) {
			available.remove(userName);
		}
		allConnected.remove(userName);
		try {
			socket.close();
			ois.close();
			oos.close();
		} catch (IOException e) {
			log.addToLog("ClientHandler : disconnected " + userName + " with ip :" + temp.socket.getInetAddress()
					+ "has disconnected" + e.toString() + e.getStackTrace()[0].getLineNumber()
					+ System.lineSeparator());
		}
		listener.updateUI();
	}
	
	/**
	 * method that connect the listener(the server) with client objects to tell server to update
	 *  the ui via the controller when someone connects or disconnects
	 */
	
	public void addClientListener(CallbackToServer listener) {
		this.listener = listener;
	}
	
	/**
	 * method that force disconnects the user recieved as a string, informs opponent if one exists
	 * that the player disconnected and removes the user from the lists. also adds the event to the log
	 * and tells controller to update the ui. Sockets and streams are also closed
	 * 
	 * * @param userToDisconnect the user to disconnect
	 */

	public void forceDisconnect(String userToDisconnect) {
		ClientHandler temp = allConnected.get(userToDisconnect);
		
		if (aGame.nameExist(userToDisconnect)) {
			String opponent=aGame.getOpponent(aGame.getPlayer(userToDisconnect));
			allConnected.get(opponent).send("PLAYER_DISCONNECTED,"+userToDisconnect);
			highscore.addScore(opponent, 1);
			aGame.remove(userToDisconnect);
		}
		
		if (available.nameExist(userToDisconnect)) {
			available.remove(userToDisconnect);
		}
		allConnected.remove(userToDisconnect);
		try {
		temp.socket.close();
		temp.ois.close();
		temp.oos.close();
		temp.send("END_GAME,"+userToDisconnect);
		} catch (IOException e) {
			log.addToLog("ClientHandler : forcedisconnected " + userName + " with ip :" + temp.socket.getInetAddress()
					+ "has been forcedisconnected" + e.toString() + e.getStackTrace()[0].getLineNumber()
					+ System.lineSeparator());
		}
		listener.updateUI();
	}
}
