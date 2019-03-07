package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pawns.Bomb;
import pawns.Flag;
import pawns.Major;
import pawns.Marshall;
import pawns.Miner;
import pawns.Pawn;
import pawns.Spy;

/**
 * Class representing the panel used when a combat occurs in Hotseat mode
 * @author JakobK98
 *
 */
public class FightPanel extends JPanel {
	private Pawn attacker, defender;
	private JLabel lblWinnerAttacker = new JLabel(); 
	private JLabel lblWinnerDefender = new JLabel();
	private JLabel lblVS = new JLabel("VS");
	private JLabel lblAttacker = new JLabel();
	private JLabel lblDefender = new JLabel();
	private final static File FONT_IMAGE = new File("files/font/Roboto-Regular.ttf");	
	private Font font;
	
	/**
	 * Constructor
	 * @param attacker
	 * @param defender
	 */
	public FightPanel(Pawn attacker, Pawn defender) {
		this.attacker = attacker;
		this.defender = defender;
		setLayout(null);
		setPreferredSize(new Dimension(500, 250));
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, FONT_IMAGE);
			font = font.deriveFont(Font.BOLD, 25f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lblAttacker.setBounds(50, 75, 125, 125);
		lblDefender.setBounds(325,75, 125, 125);
		lblWinnerAttacker.setBounds(50, 0, 125, 75);
		lblWinnerAttacker.setHorizontalAlignment(JLabel.CENTER);
		lblWinnerAttacker.setFont(font);
		lblWinnerAttacker.setForeground(Color.YELLOW);
		lblWinnerDefender.setBounds(325,0 ,125 ,75);
		lblWinnerDefender.setHorizontalAlignment(JLabel.CENTER);
		lblWinnerDefender.setFont(font);
		lblWinnerDefender.setForeground(Color.YELLOW);
		lblVS.setBounds(200, 75, 100, 100);
		lblVS.setHorizontalAlignment(JLabel.CENTER);
		lblVS.setFont(font);
		lblAttacker.setIcon(Common.resizeImageIcon(attacker.getShowIcon(), 125,125));
		lblDefender.setIcon(Common.resizeImageIcon(defender.getShowIcon(), 125,125));
		calculateWinner();
		add(lblAttacker);
		add(lblDefender);
		add(lblWinnerAttacker);
		add(lblWinnerDefender);
		add(lblVS);
	}
	
	/**
	 * Method to calculate the winner of the fight
	 */
	public void calculateWinner() {
		if (defender instanceof Flag) {
			lblWinnerAttacker.setText("WINNER");
		} else if (defender instanceof Bomb) {
			if (attacker instanceof Miner) {
				lblWinnerAttacker.setText("WINNER");
			} else {
				lblWinnerDefender.setText("WINNER");
			}
		} else if (attacker instanceof Spy && defender instanceof Marshall) {
			lblWinnerAttacker.setText("WINNER");
		} else {
			if (attacker.getValue() == defender.getValue()) {
				lblWinnerAttacker.setText("DRAW");
				lblWinnerDefender.setText("DRAW");
				lblWinnerAttacker.setForeground(Color.RED);
				lblWinnerDefender.setForeground(Color.RED);
			} else if (attacker.getValue() > defender.getValue()) {
				lblWinnerAttacker.setText("WINNER");
			} else {
				lblWinnerDefender.setText("WINNER");
			}
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Common.paintComponent(g, this, Common.getNormalBackground());
	}

	public static void main(String[] args) {
		FightPanel panel = new FightPanel(new Major(Pawn.Color.BLUE), new Major(Pawn.Color.RED));
		JOptionPane.showMessageDialog(null,panel,"Battle",JOptionPane.INFORMATION_MESSAGE);
	}

}
