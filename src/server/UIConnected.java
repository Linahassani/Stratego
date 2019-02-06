package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 
 * @author 
 *
 */
public class UIConnected extends JFrame implements ActionListener {
	private AllConnected allconnected = AllConnected.getInstance();
	private HashMap<String, JButton> buttons = new HashMap<String, JButton>();
	private Controller ctrl;

	
	/**
	 * Constructor that creates the UI.
	 * 
	 * @param ctrl The Controller with which the UI interacts.
	 */
	public UIConnected(Controller ctrl) {
		this.ctrl = ctrl;
		
		//Added code below.
		setLayout(new BorderLayout());
		this.add(new JLabel("listening for incomming clients.."),BorderLayout.NORTH);
		
		setMinimumSize(new Dimension(250, 400));
		setPreferredSize(new Dimension(250, 400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	/**
	 * method that updates the list of connected users
	 * using the singleton class AllConnected to get the list of users
	 * and that creates a panel with a button and a label for each user
	 * it also adds the user and the button for each user in a hashmap
	 * the buttons get actionlisteners aswell
	 * 
	 */
	public void updateList() {
		ArrayList<String> tempusers = allconnected.getArray();
		
		System.out.println("UI"+tempusers);
		getContentPane().removeAll();
		setMinimumSize(new Dimension(250, 400));
		setPreferredSize(new Dimension(250, 400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		JPanel panel = new JPanel(new GridLayout(tempusers.size(), 1));
		panel.setSize(250, 150);
		add(panel);
		pack();
		setVisible(true);
		for (String clients : tempusers) {
			JPanel clientPnl = new JPanel(new GridLayout(1, 2));
			clientPnl.setBorder(BorderFactory.createLineBorder(Color.black));
			JLabel clientLbl = new JLabel(clients);
			clientLbl.setHorizontalAlignment(JLabel.CENTER);
			clientLbl.setVerticalAlignment(JLabel.CENTER);
			JButton clientBtn = new JButton("Disconnect");
			clientBtn.setPreferredSize(new Dimension(50, 150));
			clientBtn.setMaximumSize(new Dimension(50, 150));
			buttons.put(clients, clientBtn);
			clientBtn.addActionListener(this);
			clientPnl.add(clientLbl);
			clientPnl.add(clientBtn);
			panel.add(clientPnl);
		}
			panel.revalidate();
			panel.repaint();
		}
	
	/**
	 * method that determine which button was clicked and tells controller to disconnect the user
	 * associated with that button
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		for (Object value : buttons.values()) {
			if (value == source) {
				for(Entry<String, JButton> entry : buttons.entrySet()) {
					if(entry.getValue()==value) {
						ctrl.forceDisconnect(entry.getKey());
						
					}
				}
			}
		}
	}

//	public static void main(String[] args) {
//		Controller controller = new Controller();
//		UIConnected ui = new UIConnected(controller);
//	}
}
