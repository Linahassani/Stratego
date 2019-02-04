
package pawns;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 * Class that provides methods for the pawns, that contains the 2 color states
 * blue and red and that can fade out the jbutton to make it invisible.
 * 
 * @author Anders Qvist
 *
 */
public abstract class Pawn extends JButton {
	public enum Color {
		RED, BLUE;
	}
	private int range;
	private int value;
	private ImageIcon showIcon;
	private ImageIcon hideIcon;
	private boolean hidden;
	private Color color;
	private Timer timer;
	private float alpha = 1.0f;


	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public ImageIcon getShowIcon() {
		return showIcon;
	}

	public void setShowIcon(ImageIcon showIcon) {
		this.showIcon = showIcon;
	}

	public ImageIcon getHideIcon() {
		return hideIcon;
	}

	public void setHideIcon(ImageIcon hideIcon) {
		this.hideIcon = hideIcon;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Icon getIcon() {
		if (hidden) {
			return hideIcon;
			
		} else {
			return showIcon;
		}
	}
	
	public void setIcon(ImageIcon icon) {
		if (hidden) {
			hideIcon = icon;
			
		} else {
			showIcon = icon;
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Method to over 2 seconds fade out a pawn(jButton) by reducing the alpha value
	 */
	public void fadeOut() {
		timer = new Timer(20, new ActionListener() {
			// private float alpha = 1f;
			public void actionPerformed(ActionEvent arg0) {
				if (alpha >= 0) {
					alpha -= 0.01f;
					repaint();
				} else {
					timer.stop();
				}
			}

		});
		timer.start();
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		super.paint(g);
	}
	/*
	 * public void actionPerformed(ActionEvent e) { // TODO Auto-generated method
	 * stub fadeOut(); }
	 */
}