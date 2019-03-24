package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.Controller;

/**
 * class for connecting a player with the server To get and send messages
 * how to use this:
 * 1. make instance of this Connect class (made in controller)
 * 2. make a reference to the InBox class by using Connect.getInBox class
 * 3. Use Connect.startConnection() method to start handler for incoming messages (messages go to InBox)
 * 4. All getting messages is done by methods in InBox   (blocking)
 * 5. to send messages use Connect.sendMessage(String) can be done with instance of the SendMessageCallback instance
 * 6. there is a couple of methods for getting messages all in InBox class
 * *************or **************************
 * Every new message goes directly to the controller 
 * @author kuras
 *
 */
public class Connect implements SendMessageCallback {
	private Socket socket = null;
	private int PORT;
	private String ip;
	private InBox inBox;
	private ObjectOutputStream oos;
	private Controller controler;

	/**
	 * 
	 * @param ip
	 * @param port
	 */
	public Connect(String ip, int port,Controller controler) {
		this.controler=controler;
		this.PORT = port;
		this.ip = ip;
		this.inBox = new InBox(this);
	}

	/**
	 * reference to a InBox
	 * 
	 * @return InBox
	 */
	public InBox getInBox() {
		return inBox;
	}

	/**
	 * Setter for ip
	 * 
	 * @param ip
	 *            String
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * setter for the port used in connection
	 * 
	 * @param PORT
	 *            int
	 * @param ip
	 *            String
	 */
	public void setPORT(int port) {
		this.PORT = port;
	}

	/**
	 * starts the connection if possible
	 */
	public void startConnecion() {
		if (socket == null) {
			try {
				socket = new Socket(ip, PORT);
				oos = new ObjectOutputStream(socket.getOutputStream());
				new ClientHandler(socket).start();
				System.out.println("Connection established");

			} catch (Exception e) {
				System.out.println("Connection cant be established");
				e.printStackTrace();
			}
		}
	}

	/**
	 * stops the connection
	 */
	public void stopConnection() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Connection closed");
			}
			socket = null;
		}

	}

	/**
	 * Sends Message to the Server
	 * 
	 * @param obj String
	 */
	@Override
	public void sendMessage(String obj) {
		if (socket != null) {
			try {
				oos.writeObject(obj);
				oos.flush();
			} catch (IOException e) {
				System.out.println("Cant send message");
				e.printStackTrace();
			}
		}

	}
	

	/**
	 * Thread for incoming streams
	 * 
	 * @author kuras
	 *
	 */
	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private boolean isRunning = true;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			try {
				this.ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				System.out.println("Cant start outputStream");
				e.printStackTrace();
			}
		}

		public void run() {
			if (socket != null) {
				while (isRunning) {
					try {
						controler.newMessage((String)ois.readObject()); // every new message from server goes to controller 
					//	inBox.push((String) ois.readObject());
					} catch (Exception e) {
						e.printStackTrace();
						//System.out.println("Something went wrong with reading stream CLOSING connection try reconnect");
						stopConnection();// stops this client
						isRunning=false;
					}

				}
			}
		}

		public void closeInputStream() {
			isRunning=false;
		}
	}
}
