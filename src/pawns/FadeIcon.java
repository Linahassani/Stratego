package pawns;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FadeIcon implements Icon {
	private float alpha = 1.0f;
	private ImageIcon icon;
	
	
	public FadeIcon(ImageIcon icon) {
		this.icon = icon;		
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void decAlpha(float dec) {
		alpha -= dec;
	}
	
	public int getIconWidth() {
		return icon.getIconWidth();
	}
	
	public int getIconHeight() {
		return icon.getIconHeight();
	}
	
	public void paintIcon(Component arg0, Graphics g, int x, int y) {
	    Graphics2D g2d = (Graphics2D) g;
	    
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.drawImage(icon.getImage(), x, y, null);
	}
}
