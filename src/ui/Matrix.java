package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import game.Position;
import game.SelectValue;
import game.SoundPlayer;
import pawns.Empty;
import pawns.Lake;
import pawns.Pawn;

/**
 * Shows a Matrix build of a double Pawn array. Used to signal for placement,
 * removal and movement of pawns.
 * 
 * @author Lukas Kuras & Henrik Sandström
 *
 */
public class Matrix extends JPanel implements MouseListener, ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 965791810993289223L;
	private Pawn[][] boardGrid;
	private Viewer viewer;
	private Pawn movePawn;
	private Position selectedPosition, positionTo;
	private ImageIcon[] footSteps, fightSymbol;
	private final static ImageIcon ADD_PAWN_BLUE = new ImageIcon("files/images/addPawnBlue.png");
	private final static ImageIcon ADD_PAWN_RED = new ImageIcon("files/images/addPawnRed.png");
	private final static int DIRECTION_UP = 0, DIRECTION_DOWN = 1, DIRECTION_LEFT = 2, DIRECTION_RIGHT = 3;
	private final static int BLUE_ROW_START = 6;
	private int cellWidth;
	private SidePanel sidePanel;

	public Matrix() {
		cellWidth = 60;
		boardGrid = new Pawn[10][10];
		setLayout(new GridLayout(boardGrid.length, boardGrid[0].length));
		setPreferredSize(new Dimension(600, 600));
		setVisible(true);
		setDoubleBuffered(true);
		setOpaque(true);
		footSteps = new ImageIcon[] { new ImageIcon("files/images/stepsUp.png"),
				new ImageIcon("files/images/stepsDown.png"), new ImageIcon("files/images/stepsLeft.png"),
				new ImageIcon("files/images/stepsRight.png") };
		fightSymbol = new ImageIcon[] { new ImageIcon("files/images/fightUp.png"),
				new ImageIcon("files/images/fightDown.png"), new ImageIcon("files/images/fightLeft.png"),
				new ImageIcon("files/images/fightRight.png") };
		addComponentListener(this);
	}

	/**
	 * Sets the viewer.
	 * 
	 * @param viewer
	 */
	public void setViewer(Viewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Sets the sidePanel.
	 * 
	 * @param sidePanel
	 */
	public void setSidePanel(SidePanel sidePanel) {
		this.sidePanel = sidePanel;
	}

	/**
	 * Updates the board and it's components.
	 * 
	 * @param board
	 *            The new board
	 */
	public void updateBoard(Pawn[][] board) {
		boardGrid = board;
		redoBoard();
		if (viewer.inSetupState()) {
			showSetupArea();
			sidePanel.updateButtons(viewer.allPawnsPlaced());
		} else {
			resizeImages();
		}
	}

	/**
	 * Recreates and redraws the matrix/board.
	 */
	private void redoBoard() {
		removeAll();
		for (int i = 0; i < boardGrid.length; i++) {
			for (int k = 0; k < boardGrid[i].length; k++) {
				Pawn pawn = (Pawn) boardGrid[i][k];
				pawn.removeMouseListener(this);
				pawn.addMouseListener(this);
				pawn.setBorder(BorderFactory.createEmptyBorder());
				pawn.setContentAreaFilled(false);
				add(pawn);
			}
		}
		reDraw();
	}

	/**
	 * Redraws the matrix/board.
	 */
	public void reDraw() {
		revalidate();
		repaint();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				resizeImages();
			}
		});
	}

	/**
	 * Marks the current player's setup area.
	 */
	public void showSetupArea() {
		removeHighlightedPawnRange();
		ImageIcon imgAddPawnBlue = Common.resizeImageIcon(ADD_PAWN_BLUE, cellWidth, cellWidth);
		ImageIcon imgAddPawnRed = Common.resizeImageIcon(ADD_PAWN_RED, cellWidth, cellWidth);
		for (int row = 0; row < boardGrid.length; row++) {
			for (int col = 0; col < boardGrid[row].length; col++) {
				Pawn pawn = (Pawn) boardGrid[row][col];
				if (pawn instanceof Empty && viewer.isInPlayerArea(new Position(row, col))) {
					if (row >= BLUE_ROW_START) {
						pawn.add(new JLabel(imgAddPawnBlue));
					} else {
						pawn.add(new JLabel(imgAddPawnRed));
					}

				}
			}
		}
	}

	/**
	 * Handles pawn selection. Either shows possible moves, moves the pawn to the
	 * selected position, or does nothing.
	 * 
	 * @param pawn
	 *            The button/pawn that was clicked
	 * @param boardBtnPosition
	 *            The position of the clicked button/pawn
	 */
	public void selectPawn(Pawn pawn, Position boardBtnPosition) {
		SelectValue selectedPawn = viewer.select(pawn, boardBtnPosition);

		if (selectedPawn.isAllowed()) {
			movePawn = pawn;
			selectedPosition = boardBtnPosition;
			pawn.setBorder(BorderFactory.createDashedBorder(Color.WHITE, 5, 2));
			showPossibleMoves(selectedPawn.getPositions());
		} else if (pawn.getComponentCount() > 0) {
			removeHighlightedPawnRange();
			positionTo = boardBtnPosition;
			viewer.movePawn(movePawn, selectedPosition, boardBtnPosition);
		}
	}

	/**
	 * Draws the possible moves for the recently selected pawn.
	 * 
	 * @param possibleMoves
	 *            Positions that the pawn can be moved to.
	 */
	public void showPossibleMoves(ArrayList<Position> possibleMoves) {
		removeHighlightedPawnRange();
		for (Position position : possibleMoves) {
			int iconDirection;
			if (selectedPosition.getRow() > position.getRow()) {
				iconDirection = DIRECTION_UP;
			} else if (selectedPosition.getRow() < position.getRow()) {
				iconDirection = DIRECTION_DOWN;
			} else if (selectedPosition.getColumn() > position.getColumn()) {
				iconDirection = DIRECTION_LEFT;
			} else {
				iconDirection = DIRECTION_RIGHT;
			}
			if (boardGrid[position.getRow()][position.getColumn()] instanceof Empty) {
				boardGrid[position.getRow()][position.getColumn()]
						.add(new JLabel(Common.resizeImageIcon(footSteps[iconDirection], cellWidth, cellWidth)));
			} else {
				boardGrid[position.getRow()][position.getColumn()]
						.add(new JLabel(Common.resizeImageIcon(fightSymbol[iconDirection], cellWidth, cellWidth)));
			}
		}
	}

	/**
	 * Animates the pawn movement by incrementing/decreasing the X or Y position of
	 * the pawn before updating the board.
	 * 
	 * @param board
	 *            The new board.
	 */
	public void showPawnMovement(Pawn pawn, Position from, Position to, Pawn[][] boardGrid) {
		final int toX = to.getColumn() * cellWidth;
		final int toY = to.getRow() * cellWidth;
		final int desync = 5;

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int steps = cellWidth;
			int moveX = pawn.getX(), moveY = pawn.getY();

			public void run() {
				if (moveX == toX && moveY == toY) {
					timer.cancel();
					Matrix.this.boardGrid = boardGrid;
					redoBoard();
				} else {
					if (moveX < toX) {
						moveX++;
					} else if (moveX > toX) {
						moveX--;
					} else if (moveY < toY) {
						moveY++;
					} else {
						moveY--;
					}
					pawn.setLocation(moveX, moveY);

					if (steps > (cellWidth + desync)) {
						SoundPlayer.getInstance().playPawnStep();
						steps = 0;
					}
					steps++;
				}
			}
		}, 0, 5);
	}

	/**
	 * Removes any highlighted pawn range on the board.
	 */
	public void removeHighlightedPawnRange() {
		for (int row = 0; row < boardGrid.length; row++) {
			for (int col = 0; col < boardGrid.length; col++) {
				boardGrid[row][col].removeAll();
				boardGrid[row][col].setBorder(BorderFactory.createEmptyBorder());
			}
		}
		repaint();
	}

	/**
	 * Returns the position of the clicked cell/pawn.
	 * 
	 * @param btn
	 * @return
	 */
	public Position getPawnPosition(JButton btn) {
		Position position = null;
		for (int row = 0; row < boardGrid.length; row++) {
			for (int col = 0; col < boardGrid[row].length; col++) {
				if (boardGrid[row][col] == btn) {
					position = new Position(row, col);
				}
			}
		}
		return position;
	}

	/**
	 * Removes a pawn from the matrix during setup state.
	 * 
	 * @param pawn
	 *            Pawn to be removed.
	 */
	private void removePawn(Pawn pawn) {
		pawn.removeAll();
		pawn.removeMouseListener(this);
		viewer.removePawn(pawn);
		showSetupArea();
		SoundPlayer.getInstance().playPawnRemove();
	}

	/**
	 * Selects/deselects a pawn.
	 * 
	 * @param pawn
	 *            Pawn to be selected/deselected.
	 */
	private void selectPawn(Pawn pawn) {
		Position pawnPosition = viewer.getPawnPosition(pawn);

		if (movePawn != null && movePawn == pawn) {
			movePawn = null;
			removeHighlightedPawnRange();
			SoundPlayer.getInstance().playPawnRemove();
		} else {
			selectPawn(pawn, pawnPosition);
			SoundPlayer.getInstance().playPawnPlace();
		}
	}

	/**
	 * Handles pawn/cell click.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof Pawn) {
			Pawn pawn = (Pawn) e.getSource();
			if (SwingUtilities.isRightMouseButton(e)) {
				if (viewer.inSetupState() && !(pawn instanceof Empty) && viewer.isInPlayerArea(getPawnPosition(pawn))) {
					removePawn(pawn);
				}
			} else if (SwingUtilities.isLeftMouseButton(e)) {
				if (viewer.inSetupState()) {
					if (viewer.isInPlayerArea(getPawnPosition(pawn))) {
						viewer.placePawn(pawn, sidePanel.getSelectedPawn());
					}
				} else {
					selectPawn(pawn);
				}
			}
		}
	}

	/**
	 * Resizes all Image-components on the board relative to the size of a
	 * cell/grid.
	 */
	public void resizeImages() {
		if (this.isDisplayable()) {
			final int cellPadding = 4;
			cellWidth = getWidth() / boardGrid.length;
			for (int row = 0; row < boardGrid.length; row++) {
				for (int col = 0; col < boardGrid[row].length; col++) {
					if (boardGrid[row][col] instanceof Pawn) {
						Pawn pawn = boardGrid[row][col];
						if (!(pawn instanceof Empty) && !(pawn instanceof Lake)) {
							ImageIcon newIcon = new ImageIcon(pawn.getIcon().toString());
							pawn.setIcon(
									Common.resizeImageIcon(newIcon, cellWidth - cellPadding, cellWidth - cellPadding));
						}
						if (pawn.getComponentCount() > 0) {
							JLabel lblPawn = (JLabel) pawn.getComponent(0);
							ImageIcon newIcon = new ImageIcon(lblPawn.getIcon().toString());
							lblPawn.setIcon(Common.resizeImageIcon(newIcon, cellWidth, cellWidth));
						}
					}
				}
			}
			revalidate();
			repaint();
		}
	}

	public void componentResized(ComponentEvent e) {
		resizeImages();
	}

	public final Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		Dimension prefSize = null;
		Component c = getParent();
		if (c == null) {
			prefSize = new Dimension((int) d.getWidth(), (int) d.getHeight());
		} else if (c != null && c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight()) {
			prefSize = c.getSize();
		} else {
			prefSize = d;
		}
		int w = (int) prefSize.getWidth();
		int h = (int) prefSize.getHeight();
		int s = (w > h ? h : w);
		return new Dimension(s, s);
	}

	/**
	 * Shows animation at the end of a round Uses Graphics.setXORMode for showing
	 * images overlapping other images Runs in a thread for 8 sec. showing image 4
	 * times
	 */
	public void showEndAnimation(String s) {
		SwingUtilities.invokeLater(new Runnable() {
			int timer = 0;

			public void run() {
				while (timer <= 4) {
					Graphics animationPanelGraphics = Matrix.this.getGraphics();
					try {
						animationPanelGraphics.setXORMode(Color.BLACK);
						
						if (s.equals("LOOSER")) {
							animationPanelGraphics.drawImage(ImageIO.read(new File("files/images/skull.gif")), 0, 0,
									getWidth(), getHeight(), Matrix.this);
						} else if (s.equals("WINNER")) {
							animationPanelGraphics.drawImage(ImageIO.read(new File("files/images/fireworks.gif")), 0, 0,
									getWidth(), getHeight(), Matrix.this);
						}
						repaint();
						timer++;
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			g.drawImage(ImageIO.read(new File("files/images/background/newMatrixSmall.png")), 0, 0, getWidth(),
					getHeight(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent arg0) {
	}

	public void componentMoved(ComponentEvent arg0) {
	}

	public void componentShown(ComponentEvent arg0) {
	}

}
