package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import pawns.Pawn;
import user.UserSetups;

/**
 * Side panel needed at the beginning of the game for placing the pawns on the Matrix.
 * @author Lukas Kuras & Henrik Sandstr�m
 */
public class SidePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2079873228994123868L;
	private JButton[][] boardGrid = new JButton[6][2];
	private int[][] boardGridCounter = new int[6][2];// counter for pawns
	private JPanel leftCounterPanel = new JPanel();
	private JPanel rightCounterPanel = new JPanel();
	private GridLayout gridBordLayout = new GridLayout(boardGrid.length, boardGrid[0].length); // grid size = array size
	private GridLayout gridBordCounterLayout = new GridLayout(boardGridCounter.length, boardGridCounter[0].length); // grid size
	private JButton readyBtn, saveBtn, loadBtn, tempSelected;
	private JPanel buttonsPanel = new JPanel();
	private JPanel buttonsContainer = new JPanel();
	private BoardUI boardUI;
	private String[] userSetupsNames;
	private UserSetups userSetups;
	private String loadedSetupName;
	private Viewer viewer;

	public SidePanel(BoardUI boardUI, Viewer viewer) {
		this.boardUI=boardUI;
		this.viewer = viewer;

		userSetups = user.UserSetups.getInstance();

		buttonsPanel.setLayout(new GridLayout(3, 1, 0, 0));
		saveBtn = Common.resizeButton(Common.newButton("Save setup", this), 120,40);
		saveBtn.setEnabled(false);
		buttonsPanel.add(saveBtn);
		loadBtn = Common.resizeButton(Common.newButton("Load setup", this), 120,40);
		loadBtn.setEnabled(userSetups.hasSetup());		
		buttonsPanel.add(loadBtn);
		readyBtn = Common.resizeButton(Common.newButton("Ready", this), 120,40);
		readyBtn.setEnabled(false);
		buttonsPanel.add(readyBtn);				
		buttonsPanel.setOpaque(false);
		buttonsPanel.setPreferredSize(new Dimension(180, 220));
		buttonsPanel.setBorder(new MatteBorder(5, 0, 0, 0, new Color(177, 160, 119)));

		buttonsContainer.setLayout(gridBordLayout);
		buttonsContainer.setOpaque(false);
		buttonsContainer.setPreferredSize(new Dimension(120, 380));

		leftCounterPanel.setLayout(gridBordCounterLayout);
		leftCounterPanel.setOpaque(false);
		leftCounterPanel.setPreferredSize(new Dimension(30, 400));

		rightCounterPanel.setLayout(gridBordCounterLayout);
		rightCounterPanel.setOpaque(false);
		rightCounterPanel.setPreferredSize(new Dimension(30, 400));

		setPreferredSize(new Dimension(180, 600));
		setLayout(new BorderLayout(0,10));
		setVisible(true);
		setDoubleBuffered(true);
		setOpaque(false);
		setBorder(new MatteBorder(0, 5, 0, 0, new Color(177, 160, 119)));

		add(new JLabel(), BorderLayout.NORTH);
		add(buttonsContainer, BorderLayout.CENTER);				
		add(leftCounterPanel, BorderLayout.WEST);
		add(rightCounterPanel, BorderLayout.EAST);
		add(buttonsPanel, BorderLayout.SOUTH);
	}

	/**
	 * sets a new Array[][] on the board
	 * 
	 * 
	 * @param board
	 *            double array with Pawns [6][2]
	 * @param counter
	 *            double array with int of Pawn [6][2]
	 */
	public void updateBoard(JButton[][] board, int[][] counter) {
		this.boardGrid = board;
		this.boardGridCounter = counter;
		this.redoBoard(board);// after array update sets up the panel again
		this.redoCounters();// after counters update sets them up
	}


	/**
	 * Re-adds the setup pawns. 
	 */
	private void redoBoard(JButton[][] board) {

		buttonsContainer.removeAll();
		for (int i = 0; i < boardGrid.length; i++) {
			for (int k = 0; k < boardGrid[i].length; k++) {
				JButton tempBtn = boardGrid[i][k];
				if(tempBtn.equals(tempSelected)) {
					System.out.println("Hejhej");
					tempBtn.setBorder(BorderFactory.createDashedBorder(Color.WHITE, 5, 2));
					tempBtn.setSelected(true);
				} else {
					tempBtn.setSelected(false);
					tempBtn.setBorder(BorderFactory.createLineBorder(new Color(72, 74, 76), 2));
				}
				tempBtn.setContentAreaFilled(false);
				tempBtn.removeActionListener(this);
				tempBtn.addActionListener(this);
				buttonsContainer.add(tempBtn);

			}
		}

		//buttonsContainer.revalidate();
		this.revalidate();
		this.repaint();
	}


	/**
	 * Updates the pawn counters.
	 */
	private void redoCounters() {
		this.leftCounterPanel.removeAll();
		this.rightCounterPanel.removeAll();
		for (int i = 0; i < boardGridCounter.length; i++) {
			for (int j = 0; j < boardGridCounter[0].length; j++) {
				// add boardGridCounter values to left and right counterPanel
				JLabel temp = new JLabel("" + boardGridCounter[i][j]);
				temp.setHorizontalAlignment(SwingConstants.CENTER);
				if (j == 0) {
					leftCounterPanel.add(temp);
				}
				if (j != 0) {
					rightCounterPanel.add(temp);
				}
			}
		}
		revalidate();
	}



	/**
	 * Returns the currently selected pawn.
	 * @return
	 */
	public Pawn getSelectedPawn() {
		Pawn temp = null;

		for (int i = 0; i < boardGrid.length; i++) {
			for (int k = 0; k < boardGrid[0].length; k++) {
				JButton tempBtn = (JButton) boardGrid[i][k];

				if (tempBtn.isSelected()) {
					temp = (Pawn)tempBtn;
				}
			}
		}
		return temp;
	}

	/**
	 * Deselectes the pawn.
	 * @param pawn
	 */
	public void resetSelectedPawn(Pawn pawn) {
		if(pawn != null) {
			JButton oldSelected = (JButton) pawn;
			oldSelected.setSelected(false);
			oldSelected.setBorder(BorderFactory.createLineBorder(new Color(72, 74, 76), 2));
		}		
	}

	/**
	 * Changes the enabled of all SidePanel buttons.
	 * @param setupReady
	 */
	public void updateButtons(boolean setupReady) {
		readyBtn.setEnabled(setupReady);
		saveBtn.setEnabled(setupReady);
	}

	/**
	 * Sets the button as the selected setup pawn.
	 * @param setupPawnBtn
	 */
	private void selectSetupPawn(JButton setupPawnBtn) {
		if(setupPawnBtn.isSelected()) {
			System.out.println("Selected");
			setupPawnBtn.setSelected(false);
			setupPawnBtn.setBorder(BorderFactory.createLineBorder(new Color(72, 74, 76), 2));				
		}else{
			System.out.println("Not selected");
			resetSelectedPawn(getSelectedPawn());	
			setupPawnBtn.setSelected(true);
			tempSelected = setupPawnBtn;
			System.out.println(tempSelected.isSelected());
			setupPawnBtn.setBorder(BorderFactory.createDashedBorder(Color.WHITE, 5, 2));
		}
	}

	/**
	 * Sends a request to save the user setup with the given name.
	 */
	private void saveSetup() {
		String userSetupName;

		if(loadedSetupName != null && JOptionPane.showConfirmDialog(null, 
				"Would you like to overwrite (" + loadedSetupName + ")") == JOptionPane.YES_OPTION) {
			userSetupName = loadedSetupName;
		} else {
			userSetupName = JOptionPane.showInputDialog(null,"Enter a new setup name");
		}

		if(userSetupName != null && userSetupName != "") {
			viewer.saveUserSetup(userSetupName);
		} else {
			JOptionPane.showMessageDialog(null, "Please enter a setup name");
		}

		loadBtn.setEnabled(userSetups.hasSetup());
	}

	/**
	 * Selects and loads a user setup from the user setup list.
	 */
	private void loadSetup() {
		userSetupsNames = userSetups.getUserSetupsNames();
		String userSetupName = (String) JOptionPane.showInputDialog(null, "Select setup",
				"Load a saved setup", JOptionPane.QUESTION_MESSAGE, null, userSetupsNames, userSetupsNames[0]);
		if(userSetupName != null && userSetupName != "") {
			loadedSetupName = userSetupName;
			viewer.loadFromUserSetup(userSetupName);
		}	
	}

	public void actionPerformed(ActionEvent a) {
		if (a.getSource() instanceof Pawn) {
			selectSetupPawn((JButton)a.getSource());			
		}else {
			if (a.getSource() == readyBtn ) {
				boardUI.setupDone();
			}else if(a.getSource() == saveBtn) {
				saveSetup();
			}else if(a.getSource() == loadBtn) {
				loadSetup();
			}
		}
	}

}
