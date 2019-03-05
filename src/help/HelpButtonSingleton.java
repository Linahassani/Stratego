package help;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ui.Common;

/**
 * Button for the list with help topics this should be added to the panel.
 * WARNING this is singleton access with getInstance
 * 
 * @author kuras
 *
 */
public class HelpButtonSingleton extends JButton implements ActionListener {
	private static HelpButtonSingleton helpButton = null;
	private final static ImageIcon NORMAL_BUTTON = new ImageIcon("files/images/buttons/buttonLong_brown.png");
	private final static ImageIcon PRESSED_BUTTON = new ImageIcon("files/images/buttons/buttonLong_brown_pressed.png");
	private MenuSingleton helpMenu;

	private HelpButtonSingleton() {
		new Thread(new Runnable() {
			public void run() {
				HelpButtonSingleton.this.setText("Help");
				HelpButtonSingleton.this.setPressedIcon(PRESSED_BUTTON);
				HelpButtonSingleton.this.setIcon(NORMAL_BUTTON);
				HelpButtonSingleton.this.setContentAreaFilled(false);
				HelpButtonSingleton.this.setHorizontalTextPosition(JButton.CENTER);
				HelpButtonSingleton.this.setVerticalTextPosition(JButton.CENTER);
				HelpButtonSingleton.this.setFocusPainted(false);
				HelpButtonSingleton.this.setOpaque(false);
				HelpButtonSingleton.this.setBorderPainted(false);
				HelpButtonSingleton.this.addActionListener(HelpButtonSingleton.this);
				HelpButtonSingleton.this.setCursor(Common.getCursor());
				Common.setFont();
			}
		}).start();

	}

	/**
	 * method for making custom button size
	 * 
	 * @param button
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static JButton resizeButton(JButton button, int newWidth, int newHeight) {
		ImageIcon newIcon = (ImageIcon) button.getIcon();
		newIcon = new ImageIcon(newIcon.getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH));
		button.setIcon(newIcon);
		ImageIcon newPressedIcon = (ImageIcon) button.getPressedIcon();
		newPressedIcon = new ImageIcon(
				newPressedIcon.getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH));
		button.setPressedIcon(newPressedIcon);
		return button;
	}

	/**
	 * method for getting reference to this singleton object
	 * 
	 * @return
	 */
	public static HelpButtonSingleton getInstance() {
		if (helpButton == null) {
			helpButton = new HelpButtonSingleton();
		}
		return helpButton;
	}
	
	public void setActive(boolean bol) {
		this.setEnabled(bol);
	}

	// //TEST
	// public static void main(String[] args) {
	// HelpButtonSingleton btnS = HelpButtonSingleton.getInstance();
	// JFrame frame = new JFrame();
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// frame.add(btnS);
	// frame.setVisible(true);
	// frame.pack();
	// }
	// protected void paintComponent(Graphics g) {
	// super.paintComponent(g);
	//
	// }

	/**
	 * 
	 * Starts new Menu with help topics in a new Thread
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this) {
			if (helpMenu == null) {
				helpMenu = MenuSingleton.getMenuSingleton();	
			}
			setActive(false);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					helpMenu.showHelpMenu();
				}
			});
		}

	}

}
