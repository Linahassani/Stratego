package highscore;

import java.io.Serializable;

/**
 * A simple class that connects userName and the score (number of wins)
 * in a score object
 * @author Anders Qvist
 *
 */
public class Score implements Serializable, Comparable<Score> {
	private int score;
	private String userName;

	
	/**
	 * Constructor that takes a userName and a score
	 * @param userName The userName for the object
	 * @param score The score for the object
	 */
	public Score(String userName, int score) {
		this.score = score;
		this.userName = userName;
	}

	public int getScore() {
		return score;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
    public int compareTo(Score score) {              
        return ((Integer)(score.getScore())).compareTo(getScore());
    }
}