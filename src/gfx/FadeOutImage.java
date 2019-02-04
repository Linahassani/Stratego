package gfx;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * Class handling animation of pawns making them disappear slowly/ fading out
 * @author 
 *
 */
public class FadeOutImage extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Image myImage,otherImage;
	 private float alpha = 1f;
	private  Timer timer = new Timer(20, this);
	
	public FadeOutImage(Image winner, Image loser) {
		this.myImage=loser;
		this.otherImage=winner;
	    timer.start();
	  }
	
	
	/**
	 * paints graphics in a component
	 */
	public void paint(Graphics g) {
		
	    super.paint(g);
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.drawImage(otherImage, 10, 65, null);
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.drawImage(myImage, 10, 10, null);
	  }

	  public void actionPerformed(ActionEvent e) {
		  
	    alpha += -0.01f;
	    if (alpha <= 0) {
	      alpha = 0;
	      timer.stop();
	    }
	    repaint();
	  }
	  
	  public static void main(String[] args) {
		  
	    JFrame frame = new JFrame("Fade out");
	    Image myImage = new ImageIcon("files/images/pawns/blue/captain.png").getImage();
	    Image otherImage = new ImageIcon("files/images/pawns/red/colonel.png").getImage();
	    frame.add(new FadeOutImage(myImage,otherImage));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300, 250);
	    frame.setVisible(true);
	  }
	}
