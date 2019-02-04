package client;

import java.util.LinkedList;

/**
 * Class for handling message strings. Class is synchronized and blocks if no messages.
 * @author kuras
 *
 */
public class InBox {
	private LinkedList<String> messages = new LinkedList<String>();
	private Connect connect;
	
	
	public InBox(Connect connect) {
		this.connect=connect;
	}
	
	/**
	 * Adds a message string to the list
	 * @param message
	 * @return
	 */
	public synchronized boolean push(String message) {
		messages.push(message);
		notify();
		return true;
	}
	
	/**WARNING!!!!!!!!!  this method is blocking 
	 * separate thread should access this.
	 * returns message string and deletes it from the InBox
	 * @return
	 */
	public synchronized String pop() {
		if(this.size()==0) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Thread is waiting for a message string in a MessageBox class");
				e.printStackTrace();
			}
		}
		return messages.pop();
	}
	/**
	 * Shows a messages string without deleting it
	 * @return
	 */
	public synchronized String peek() {
		return messages.peek();
	}
	/**
	 * Show the number of pending messages strings in the InBox
	 * @return
	 */
	public synchronized int size() {
		return messages.size();
	}
}
