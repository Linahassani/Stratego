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
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Attributes;

/**
 * Chat window for multiplayer games.
 * @author Henrik Sandström
 */
public class ChatPanel extends JPanel implements ActionListener, FocusListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4567307357843649216L;
	public static final String LINE_BREAK_ATTRIBUTE_NAME="line_break_attribute";
	private Viewer viewer;
	private JTextPane txtMessages;
	//private JTextArea txtMessages;
	private JTextField txtNewMessage;
	private MutableAttributeSet a;
	private StyledDocument sd;
	private JButton btnSend;
	private String userName, opponentName;
	private boolean chatInitiated;
	
	private Highlighter.HighlightPainter userPainter, opponentPainter;
	
	public ChatPanel(Viewer viewer, String userName, String opponentName) {
		this.viewer = viewer;
		this.userName = userName;
		this.opponentName = opponentName;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(200,600));
		setBorder(new MatteBorder(5,5,5,5, new Color(177, 160, 119)));
		
		txtMessages = new JTextPane();
		txtMessages.setEditable(false);
		txtMessages.setBackground(new Color(211, 191, 143));
		txtMessages.setBorder(new EmptyBorder(5,5,5,5));
		
		//txtMessages.setLineWrap(true);
		//txtMessages.setWrapStyleWord(true);
		txtMessages.setMinimumSize(new Dimension(0,0));
		a = txtMessages.getInputAttributes();
		sd = txtMessages.getStyledDocument();
		//userPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.BLUE);
		//opponentPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
		
		JScrollPane pane = new JScrollPane(txtMessages);
		pane.setPreferredSize(new Dimension(200,500));
		pane.setBorder(null);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
		//txtMessages.append(opponentName + ": " + message + System.lineSeparator() + System.lineSeparator());
		try {
			//txtMessages.getHighlighter().addHighlight(0, opponentName.length(), opponentPainter);
			sd.insertString(0, printMessage(message), a);
			sd.insertString(0, printName(opponentName), a);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Insertion of line break. Inspired by Stanislav Lapitsky
	 * 
	 * ...and partially copied :)
	 * 
	 * ...and will likely not be used :(
	 */
    private void insertLineBreak() {
        try {
            int offs = txtMessages.getCaretPosition();
            SimpleAttributeSet attrs;
            AttributeSet as = sd.getCharacterElement(offs).getAttributes();
            attrs = new SimpleAttributeSet(as);
            attrs.addAttribute(LINE_BREAK_ATTRIBUTE_NAME,Boolean.TRUE);
            sd.insertString(offs, System.lineSeparator(), attrs);
            txtMessages.setCaretPosition(offs+1);
        }
        catch (BadLocationException ex) {
            //should never happens
            ex.printStackTrace();
        }
    }
	
	public void addMessage(String message) {
		try {
			txtMessages.getStyledDocument().insertString(0, printMessage(message), a);
			txtMessages.getStyledDocument().insertString(0, printName(userName), a);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	
	public String printName (String name) {
		StyleConstants.setBold(a, true);
		StyleConstants.setItalic(a, true);
		return name + ": ";
		
	}
	
	public String printMessage (String message) {
		StyleConstants.setBold(a, false);
		StyleConstants.setItalic(a, false);
		return message + System.lineSeparator() + System.lineSeparator();
	}
	
	/**
	 * Appends a new info message to the chat list. 
	 * @param message The message
	 * @author André Hansson
	 */
	public void addInfoMessage(String message) {
		//txtMessages.append(message + System.lineSeparator() + System.lineSeparator());
		try {
			sd.insertString(0, message, a);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
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
//				txtMessages.append(userName + ": " + message + System.lineSeparator() + System.lineSeparator());
//				try {
//					txtMessages.getHighlighter().addHighlight(0, userName.length(), userPainter);
//				} catch (BadLocationException x) {
//					x.printStackTrace();
//				}
				addMessage(message);
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
