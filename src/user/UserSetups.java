package user;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import pawns.Pawn;

/**
 * Singleton class for saving, loading and handling setup board grids.
 * @author Henrik
 */
public class UserSetups {
	
	private static UserSetups instance;
	private HashMap<String, Pawn[][]> userSetups;
	private static final String FILE_PATH = "files/user/setups.dat";

	private UserSetups() {
		userSetups = new HashMap<String, Pawn[][]>();
		readSetups();
	}
	
	public static UserSetups getInstance() {
		if(instance == null) {
			instance = new UserSetups();
		}
		return instance;
	}
	
	private void readSetups() {
		Object object = FileHandler.readObject(FILE_PATH);
		if(object instanceof HashMap) {
			userSetups = (HashMap<String, Pawn[][]>)object;
		}
	}
	
	public void addUserSetup(String userSetupName, Pawn[][] userSetup) {
		removeListeners(userSetup);
		userSetups.put(userSetupName, userSetup);
		FileHandler.writeObject(FILE_PATH, userSetups);
	}
	
	public String[] getUserSetupsNames() {
		return userSetups.keySet().toArray(new String[userSetups.size()]);
	}
	
	public Pawn[][] getUserSetup(String userSetupName){
		return userSetups.get(userSetupName);
	}
	
	public void removeUserSetup(String userSetupName) {
		userSetups.remove(userSetupName);
		FileHandler.writeObject(FILE_PATH, userSetups);
	}
	
	/**
	 * Removes ActionListeners & MouseListeners from all pawn-objects of a board grid.
	 * If not done, the board grid will not be serializable.
	 * @param userSetupGrid
	 */
	public void removeListeners(Pawn[][] userSetupGrid) {
		for(int row = 0; row < userSetupGrid.length; row++) {
			for(int col = 0; col < userSetupGrid[row].length; col++) {
				Pawn pawn = userSetupGrid[row][col];
				for(MouseListener mouseListener : pawn.getMouseListeners()) {
					pawn.removeMouseListener(mouseListener);
				}
				for(ActionListener actionListener : pawn.getActionListeners()) {
					pawn.removeActionListener(actionListener);
				}
			}
		}
	}
	
	public boolean hasSetup() {
		return userSetups.size() > 0;
	}
	
}