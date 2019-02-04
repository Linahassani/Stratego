package ui;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * The start/main menu of the game. Used to navigate through the application.
 * @author Henrik Sandström
 */
public class MenuUI extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2482189871131911541L;
	private JButton btnNewGame, btnExit, btnLoad, btnSettings, btnLobby;
	private Viewer viewer;

	/**
	 * Creates and aligns all of the navigational buttons.
	 * @param viewer
	 */
	public MenuUI(Viewer viewer) {
		this.viewer = viewer;
		setBorder(new EmptyBorder(50,30,50,30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel menuIcon = new JLabel(new ImageIcon("files/images/menu_title.png"));
		menuIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(menuIcon);		
		add( Box.createVerticalStrut(50) );
		btnNewGame = Common.newButton("Start New Game", this);
		btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnNewGame);
		add( Box.createVerticalStrut(10) );
		btnLoad = Common.newButton("Load game", this);
		btnLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnLoad);
		add( Box.createVerticalStrut(10) );
		btnLobby = Common.newButton("Multiplayer", this);
		btnLobby.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnLobby);
		add( Box.createVerticalStrut(10) );
		btnSettings = Common.newButton("Settings", this);
		btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnSettings);
		add( Box.createVerticalStrut(10) );
		btnExit = Common.newButton("Exit", this);		
		btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnExit);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnNewGame) {
			viewer.startNewGame();			
		}else if(e.getSource() == btnLoad) {
			viewer.switchToGames();
		}else if(e.getSource() == btnLobby) {
			viewer.switchToLobby();
		}else if(e.getSource() == btnSettings) {
			viewer.showCard("Settings");
		}else if(e.getSource() == btnExit) {
			viewer.dispatchEvent(new WindowEvent(viewer, WindowEvent.WINDOW_CLOSING));
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);       
		Common.paintComponent(g, this, Common.getMenuBackground());     
	}
}
