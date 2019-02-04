package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * class that creates the serversocket and listens for connecting clients and
 * starts new thread for each client
 * 
 * @author Anders Qvist
 *
 */
@SuppressWarnings("deprecation")
public class Server implements Runnable {

	protected boolean isStopped = false;
    protected Thread runningThread= null;
    protected ExecutorService threadPool =
        Executors.newFixedThreadPool(20);
	private final int PORT = 63050;
	private ServerSocket serverSocket = null;
	private Log log = Log.getInstance();
	private Controller controller;
	private AllConnected allConnected = AllConnected.getInstance();
	

	/**
	 * constructor that creates serversocket
	 */
	public Server(Controller controller) {
		this.controller=controller;
		this.controller.addObserver(new observerImpl());
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			log.addToLog(" ServerStart : constructor: " + e.toString() + System.lineSeparator());
		}
	}

	/**
	 * run method that accepts connections and starts new clienthandlers
	 * and put them to the threadpool to be executed. server runs forever
	 * since no button to stop it was implemented
	 */
	public void run() {
		     synchronized(this){
	            this.runningThread = Thread.currentThread();
	        }
	         while(! isStopped()){
	            Socket socket = null;
	            
	            System.out.println("Server listening for incomming clients");
	            try {
	            	socket = this.serverSocket.accept();
	            } catch (IOException e) {
	            	log.addToLog(" ServerStart : run: " + e.toString() + System.lineSeparator());
	                if(isStopped()) {
	                    System.out.println("Server Stopped.") ;
	                    log.addToLog(" Server stopped " + System.lineSeparator());
	                    break;
	                }
	                throw new RuntimeException(
	                    "Error accepting client connection", e);
	                
	            }
	            ClientHandler client = new ClientHandler(socket); 
	            
	            client.addClientListener(new CallbackImpl());	//add listener to to each client
	            this.threadPool.execute(client);
	        }
	        this.threadPool.shutdown();
	        System.out.println("Server Stopped.") ;
	        log.addToLog(" Server stopped " + System.lineSeparator());
	    }

	/**
	 * currently the server is never stopped
	 */
	    private synchronized boolean isStopped() {
	        return this.isStopped;
	    }
	    
	    /**
		 * not yet used method to stop server
		 */
	    
	    public synchronized void stop(){
	        this.isStopped = true;
	        try {
	            this.serverSocket.close();
	        } catch (IOException e) {
	        	log.addToLog(" ServerStart : stop: " + e.toString() + System.lineSeparator());
	            throw new RuntimeException("Error closing server", e);
	        }
	    }
	    
	    /**
		 * method that force disconnects the socket connected with a username
		 * 
		 * @param userToDisconnect the user to disconnect
		 */
	    public void forceDisconnect(String userToDisconnect) {
	    	allConnected.get(userToDisconnect).forceDisconnect(userToDisconnect);
	    }
	    
	    /**
		 * inner class implementing interface to inform controller that ui needs to be updated
		 * after a change in number of connected users
		 */
	    private class CallbackImpl implements CallbackToServer {

			@Override
			public void updateUI() {
				controller.updateUI();
				
			}
	    }
	    /**
		 * inner class implementing observer to update the server with who to disconnect
		 */
	    private class observerImpl implements Observer{

			@Override
			public void update(Observable arg0, Object arg1) {
				String str = (String) arg1;
				
				forceDisconnect(str);
			}
		}
}