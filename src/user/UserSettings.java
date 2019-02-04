package user;

import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * User settings. Singleton
 * @author Henrik
 */
public class UserSettings{
	
	private static UserSettings instance;
	private HashMap<String, Integer> userSettings;
	private static final String FILE_NAME = "files/user/settings.dat";
	private static final HashMap<String, Integer> STANDARD_SETTINGS = new HashMap<String, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2089722304571265291L;

		{
			put("music", 1);
			put("audioEffects", 1);
			put("musicVolume", -10);
			put("effectsVolume", 0);
			put("fullscreen", 0);
		}
	};	
	
	/**
	 * Constructs itself with the values in the user-settings file.
	 */
	private UserSettings() {
		readSettings();
	}
	
	/**
	 * Checks if an instance exists, if not, creates a new one.
	 * @return instance
	 */
	public static UserSettings getInstance() {
		if(instance == null) {
			instance = new UserSettings();
		}
		return instance;
	}
	
	/**
	 * Reads settings from the user-file or uses the standard settings.
	 */
	private void readSettings() {
		Object object = FileHandler.readObject(FILE_NAME);
		if(object instanceof HashMap) {
			userSettings = (HashMap<String, Integer>)object;
		}else {
			userSettings = STANDARD_SETTINGS;
		}
	}
	
	/**
	 * Attempts to save/write the newly updates user settings.
	 * @param userSettings 
	 */
	public void writeSettings(HashMap<String, Integer> userSettings) {
		this.userSettings = userSettings;
		if(FileHandler.writeObject(FILE_NAME, userSettings)) {
			JOptionPane.showMessageDialog(null, "Settings were successfully saved!");
		}
	}
	
	/**
	 * @return The user settings.
	 */
	public HashMap<String, Integer> getUserSettings(){
		return userSettings;
	}

	public boolean playMusic() {
		return userSettings.get("music") == 1;
	}

	public boolean playAudioEffects() {
		return userSettings.get("audioEffects") == 1;
	}
	
	public Integer getMusicVolume() {
		return userSettings.get("musicVolume");
	}
	
	public Integer getEffectsVolume() {
		return userSettings.get("effectsVolume");
	}
	
	public boolean useFullscreen() {
		return userSettings.get("fullscreen") == 1;
	}
	

}
