package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Observable;

/**
 * Connects the ServerUI and the Log
 * it also inherits Observable to be able to tell the server someone is to be disconnected by force
 * @author Anders Qvist
 */
@SuppressWarnings("deprecation")
public class Controller extends Observable {
	private Log log = Log.getInstance();
	private UIConnected uic;
	private ServerUI serverUI;

	
	/**
	 * Constructor that creates a new ServerUI and new ui that shows connected users
	 */
	public Controller() {
		serverUI = new ServerUI(this);
		serverUI.setIPtoConnect(getIP());
		uic=new UIConnected(this);
	}
	
	/**
	 * Returns the ip of the machine that is currently running this
	 * program.
	 * @return The IP address of this machine.
	 */
	public String getIP() {
		try {
			InetAddress localIP = InetAddress.getLocalHost();
			return localIP.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "Failed to resolve IP";
	}

	/**
	 * Gets the log from ServerLog as a String
	 * 
	 * @return The log as a String
	 */
	public String getLog(String filter) {
		return log.getLog(filter);
	}

	/**
	 * Gets the log between the specified times as a String.  
	 * @param filter The filter to use
	 * @param fromDate The from time
	 * @param toDate   The to time
	 * @return The log between the times, with or without filter
	 */
	public String getLog(String filter,Date from, Date to) {
		return log.getLog(filter,from, to);
	}
	
	/**
	 * tells the ui to update list of users  
	 */
	public void updateUI() {
		uic.updateList();
	}
	
	/**
	 * method that sends update to the server of use to be disconnected by force
	 */
	public void forceDisconnect(String userName) {
		String userToDisconnect=userName;
		
		setChanged();
		notifyObservers(userToDisconnect);
	}
}