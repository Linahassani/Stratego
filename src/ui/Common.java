package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import game.SoundPlayer;

/**
 * Class for creating & customizing UI common components.
 * @author Henrik Sandström
 */
public abstract class Common {
	
	private final static ImageIcon NORMAL_BUTTON = new ImageIcon("files/images/buttons/buttonLong_brown.png");
	private final static ImageIcon PRESSED_BUTTON = new ImageIcon("files/images/buttons/buttonLong_brown_pressed.png");
	private final static ImageIcon NORMAL_CHECKBOX = new ImageIcon("files/images/checkBoxes/checkBox.png");
	private final static ImageIcon SELECTED_CHECKBOX = new ImageIcon("files/images/checkBoxes/checkBoxChecked.png");
	private final static ImageIcon CURSOR_IMAGE = new ImageIcon("files/images/cursor.png");
	private final static File NORMAL_BACKGROUND = new File("files/images/background/panel_beige.png");
	private final static File INNER_BACKGROUND = new File("files/images/background/panel_beigeLight.png");
	private final static File MENU_BACKGROUND = new File("files/images/background/menuBackground.jpg");
	private final static File FONT_IMAGE = new File("files/font/kenvector_future.ttf");	
	
	/**
	 * Creates & returns a game common JButton.
	 * @param text Text on the button
	 * @param actionListener The buttons ActionListener
	 */
	public static JButton newButton(String text, ActionListener actionListener) {
		JButton button = new JButton(text, NORMAL_BUTTON);
		button.setPressedIcon(PRESSED_BUTTON);
		button.setContentAreaFilled(false);
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.CENTER);
		button.setFocusPainted(false); 
		button.setOpaque(false);
		button.setBorderPainted(false);
		button.addActionListener(actionListener);
		button.addActionListener(SoundPlayer.getInstance());
		return button;
	}
	
	/**
	 * Creates and returns a game common JCheckBox.
	 */
	public static JCheckBox newCheckBox() {
		JCheckBox checkBox = new JCheckBox(NORMAL_CHECKBOX);
		checkBox.setSelectedIcon(SELECTED_CHECKBOX);
		checkBox.setOpaque(false);
		checkBox.setHorizontalAlignment(SwingConstants.RIGHT);
		checkBox.addActionListener(SoundPlayer.getInstance());
		return checkBox;
	}
	
	/**
	 * Resizes & returns a JButton to the width & height parameters.
	 * @param button The button to be resized.
	 * @param newWidth The new width of the button.
	 * @param newHeight The new height of the button.
	 */
	public static JButton resizeButton(JButton button, int newWidth, int newHeight) {
		ImageIcon newIcon = (ImageIcon)button.getIcon();
		newIcon = new ImageIcon(newIcon.getImage().getScaledInstance(newWidth, newHeight,  java.awt.Image.SCALE_SMOOTH));
		button.setIcon(newIcon);
		ImageIcon newPressedIcon = (ImageIcon)button.getPressedIcon();
		newPressedIcon = new ImageIcon(newPressedIcon.getImage().getScaledInstance(newWidth, newHeight,  java.awt.Image.SCALE_SMOOTH));
		button.setPressedIcon(newPressedIcon);
		return button;
	}
	
	/**
	 * Resizes & returns a ImageIcon to the width & height parameters.
	 * @param icon The ImageIcon to be resized.
	 * @param newWidth The new width of the ImageIcon.
	 * @param newHeight The new height of the imageIcon.
	 */
	public static ImageIcon resizeImageIcon(ImageIcon icon, int newWidth, int newHeight) {
		icon.setImage(icon.getImage().getScaledInstance(newWidth, newHeight,  java.awt.Image.SCALE_SMOOTH));
		return icon;
	}
	
	/**
	 * Creates & returns a game common JLabel title.
	 * @param text The title.
	 */
	public static JLabel newTitle(String text) {
		JLabel lblTitle = new JLabel(text);
		lblTitle.setForeground(new Color(36, 58, 91));
		lblTitle.setFont(lblTitle.getFont().deriveFont(40f));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		lblTitle.setPreferredSize(new Dimension(600, 100));
		return lblTitle;
	}
	
	public static File getNormalBackground() {
		return NORMAL_BACKGROUND;
	}
	
	public static File getInnerBackground() {
		return INNER_BACKGROUND;
	}
	
	public static File getMenuBackground() {
		return MENU_BACKGROUND;
	}
	
	public static void paintComponent(Graphics g, JPanel panel, File file) {
		try {
			g.drawImage(ImageIO.read(file), 0, 0, panel.getWidth(), panel.getHeight(), panel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the main application font.
	 */
	public static void setFont() {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, FONT_IMAGE);
			font = font.deriveFont(Font.BOLD, 12f);
			UIManager.put("Label.font", font);
			UIManager.put("Button.font", font);
			UIManager.put("TextArea.font", font);
			UIManager.put("TextField.font", font);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a game cursor.
	 */
	public static Cursor getCursor() {
		return Toolkit.getDefaultToolkit().createCustomCursor(CURSOR_IMAGE.getImage(),new Point(0,0),"Sword");
	}

}
