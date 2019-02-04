package user;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for reading & writing objects from/to a .dat user-file.
 * @author Henrik Sandström
 */
public abstract class FileHandler {
	
	/**
	 * Reads object if available.
	 * @param filePath Path to the user-file.
	 * @return The read object.
	 */
	public static Object readObject(String filePath) {
		try (FileInputStream fis = new FileInputStream(filePath)){			
			if(fis.available()>0) {
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object object = ois.readObject();
				ois.close();
				return object;
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Writes the given object to a user-file.
	 * @param filePath Path to the user-file.
	 * @param object The object to write/save.
	 * @return The result of the write attempt.
	 */
	public static boolean writeObject(String filePath, Object object) {
		try(FileOutputStream fos = new FileOutputStream(filePath)){			
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
