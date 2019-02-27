package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Chat window for multiplayer games.
 * @author Henrik Sandström
 */
public class ChatPanel extends JPanel implements ActionListener, FocusListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4567307357843649216L;
	private Viewer viewer;
	private JTextArea txtMessages;
	private JTextField txtNewMessage;
	private JButton btnSend;
	private String userName, opponentName;
	private boolean chatInitiated;
	
	public ChatPanel(Viewer viewer, String userName, String opponentName) {
		this.viewer = viewer;
		this.userName = userName;
		this.opponentName = opponentName;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(200,600));
		setBorder(new MatteBorder(5,5,5,5, new Color(177, 160, 119)));
		
		txtMessages = new JTextArea();
		txtMessages.setEditable(false);
		txtMessages.setBackground(new Color(211, 191, 143));
		txtMessages.setBorder(new EmptyBorder(5,5,5,5));
		txtMessages.setLineWrap(true);
		txtMessages.setWrapStyleWord(true);
		JScrollPane pane = new JScrollPane(txtMessages);
		pane.setPreferredSize(new Dimension(200,500));
		pane.setBorder(null);
		add(pane, BorderLayout.NORTH);
		chatInitiated = false;
		txtNewMessage = new JTextField("Enter message");
		txtNewMessage.addFocusListener(this);
		txtNewMessage.setBorder(new CompoundBorder(
				new MatteBorder(5,0,0,0, new Color(177, 160, 119)), 
			    new EmptyBorder(5,5,5,5)));
		txtNewMessage.setPreferredSize(new Dimension(200,50));
		add(txtNewMessage, BorderLayout.CENTER);
		btnSend = Common.newButton("Send", this);
		add(btnSend, BorderLayout.SOUTH);
	}	
	
	/**
	 * Appends a new message to the chat list.
	 * @param opponentName
	 * @param message
	 */
	public void addOpponentMessage(String message) {
		txtMessages.append(opponentName + ": " + message + System.lineSeparator() + System.lineSeparator());
	}
	
	/**
	 * Appends a new info message to the chat list. 
	 * @param message The message
	 * @author André Hansson
	 */
	public void addInfoMessage(String message) {
		txtMessages.append(message + System.lineSeparator() + System.lineSeparator());
	}
	
	/**
	 * Clears the chat list
	 */
	public void clear() {
		txtMessages.setText("");
	}
	
	/**
	 * Now only listens to the Send button, sends the user's message
	 * to viewer if it contain any characters
	 * @author Jakob
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnSend) {
			String message = txtNewMessage.getText();
			if(!message.equals("") && message != null) {
				txtMessages.append(userName + ": " + message + System.lineSeparator() + System.lineSeparator());
				viewer.sendObject("MESSAGE,"+message);
				txtNewMessage.setText("");	
			}
		}
	}

	/**
	 * Removes the "Enter message" when textarea is clicked on for the first time
	 * @author Jakob
	 */
	public void focusGained(FocusEvent e) {
		if(!chatInitiated) {
			txtNewMessage.setText("");
			chatInitiated = true;
		}
	}

	public void focusLost(FocusEvent e) {}

}
