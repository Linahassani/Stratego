package highscore;

import java.util.*;

import server.Log;

import java.io.*;

/**
 * Class that saves scores and username to an arraylist. Singleton
 * @author Anders Qvist
 *
 */
public class HighScoreList {
	private ArrayList<Score> scores;
	private static final String HIGHSCORE_FILE = "files/highscores.dat";
	private static HighScoreList instance;
	/**
	 * using the log file to write exceptions
	 */
	private Log log = Log.getInstance();

	
	/**
	 * Constructor that initialises the arraylist
	 */
	public HighScoreList() {
		scores = new ArrayList<Score>();
	}

	/**
	 * @return The instance
	 */
	public static HighScoreList getInstance() {
		if (instance == null) {
			instance = new HighScoreList();
		}
		return instance;
	}

	/**
	 * Returns the sorted list of scores
	 * @return score The sorted list
	 */
	public ArrayList<Score> getScores() {
		readHighScore();
		sort();
		return scores;

	}

	/**
	 * Sorts the scores arraylist
	 */
	private void sort() {
		Collections.sort(scores);
	}

	/**
	 * checks if a username exists in the scores arraylist
	 * @param name the username
	 * @return exist, 0 if username doesn't exist, otherwise the position in the list
	 */
	public int nameExist(String name) {
		int exist = -1;
		
		for (int i = 0; i < scores.size(); i++) {
			if (scores.get(i).getUserName().equals(name)) {
				exist = i;
			}
		}
		return exist;
	}

	/** adds scores to the list, if username exists increments that usernames score
	 * @param name the username
	 * @param score the score for the associated name
	 */
	public void addScore(String name, int score) {
		readHighScore();
		int index = nameExist(name);
		
		if (index != -1) {
			scores.get(index).setScore(scores.get(index).getScore() + score);
			
		} else {
			scores.add(new Score(name, score));
		}
		writeToFile();
	}

	/**
	 * reads the scores arraylist from file
	 */
	@SuppressWarnings("unchecked")
	public void readHighScore() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE))) {
			Object obj = ois.readObject();
			scores = (ArrayList<Score>) obj;
			
			ois.close();
		} catch (Exception e) {
			log.addToLog(" HighScoreList : readHighScore " + e.toString() + System.lineSeparator());

		}
	}

	/**
	 * writes the scores arraylist to the given file
	 */
	public void writeToFile() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE,false))) {
			oos.writeObject(scores);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			log.addToLog("HighScoreList : writeToFile " + e.toString() + System.lineSeparator());

		}

	}

	/**
	 * gets the scores arraylist and returns it in string format
	 * @return res, the top 10 in the highscorelist in string format
	 */
	public String getHighscore() {
		String res = "";
		int max = 10;
		int i = 0;
		ArrayList<Score> scores;
		scores = getScores();
		int x = scores.size();
		
		if (x > max) {
			x = max;
		}
		while (i < x) {
			res += (i + 1) + ".\t" + scores.get(i).getUserName() + "\t\t" + scores.get(i).getScore()
					+ System.lineSeparator();
			i++;
		}
		return res;
	}
}