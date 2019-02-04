package help;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ui.Common;

/**
 * video tutorials for different parts of the game. dont use directly but via
 * HelpButtonSingleton class
 * 
 * @author kuras
 *
 */
public class MenuSingleton extends JPanel implements ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5483192513044588581L;

	private enum Menu {
		PLACE, MOVE, ATTACK, ONLINE
	};

	private String[] listItems = { "Objectives", "Place", "Remove", "Move", "Attack", "Online","Pawns" };
	private JList<String> list = new JList<String>(listItems);
	private JScrollPane scrollList = new JScrollPane(list);
	private JLabel listLabel = new JLabel("Choose a help topic");
	private JTextArea textArea = new JTextArea("\n \n \n \n Choose a help topic");
	private JScrollPane scrollTextArea = new JScrollPane(textArea);
	private HelpTopics helpTopics = new HelpTopics();
	private static MenuSingleton menuSingleton= null;

	private MenuSingleton() {
		System.out.println("MenuSingleton - init");
		this.setPreferredSize(new Dimension(500, 300));
		this.setLayout(new FlowLayout());
		this.setOpaque(true);
//		scrollList.setPreferredSize(new Dimension(80, 200));
		scrollList.setAlignmentX(CENTER_ALIGNMENT);
		scrollList.setOpaque(true);
		list.addListSelectionListener(this);
		list.setOpaque(false);
		textArea.setPreferredSize(new Dimension(400, 250));
		textArea.setAlignmentY(CENTER_ALIGNMENT);
		textArea.setAlignmentX(CENTER_ALIGNMENT);
		textArea.setOpaque(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		this.add(scrollList);
		this.add(textArea);

		JFrame frame = new JFrame();
//		frame.setPreferredSize(new Dimension(500, 400));
		
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		// frame.add(listLabel);
		// frame.add(scrollList);
		// frame.add(scrollTextArea);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		Common.setFont();
		this.setCursor(Common.getCursor());

		// repaint();

	}
	public static MenuSingleton getMenuSingleton() {
		if(menuSingleton==null) {
			menuSingleton=new MenuSingleton();
		}
		return menuSingleton;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Common.paintComponent(g, this, Common.getNormalBackground());
	}

	// playies videos according to chosen topic
	@Override
	public void valueChanged(ListSelectionEvent l) {
		int index = list.getSelectedIndex();
		String listValue = list.getSelectedValue();
		this.textArea.setText(helpTopics.getTopic(listValue));

	}

}
