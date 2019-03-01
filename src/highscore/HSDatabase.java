package highscore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Database connection class that will and should contain all the connections to the database.
 * Not finished yet, missing a few quaries. Look at the notes from prevrious developer Yun.
 * @author yun & Jakob
 *
 */
public class HSDatabase {
	private final String url = "jdbc:postgresql://pgserver.mah.se/stratego";
	private final String userID = "ah8378";
	private final String password = "2o0hd5wd";
	private int idNumber = 0;
	private Connection conn;
	
	/**
	 * Constructor
	 */
	public HSDatabase() {
		conn = connect();
		try {
			initUserID();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method which connects to the database
	 * @return the connection object
	 */
	public Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url,userID,password);
			System.out.println("Connected to the PostgreSQL server successfully.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
 		return conn;
	}
	
	/**
	 * Method which fetches the highest userID in the database and increments it by 1
	 * @throws SQLException
	 */
	public void initUserID() throws SQLException {
		String userIDQuery = "Select max(userid) from users";
		
		PreparedStatement pst = conn.prepareStatement(userIDQuery);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()) {
			idNumber = rs.getInt(1);
			idNumber++;
		}
		
	}
	
	/**
	 * Method which fetches number of games played by a user
	 * @param userName
	 * @return
	 * @throws SQLException
	 */
	public int getGamesPlayed(String userName) throws SQLException {
		int nrofgames = 0;
		String query = "Select nrofgames from users where username = '" + userName +"'";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()) {
			nrofgames = rs.getInt(1);
		}
		
		return nrofgames;
	}
	
	/**
	 * Method which fetches number of games won by a user
	 * @param userName
	 * @return
	 * @throws SQLException
	 */
	public int getGamesWon(String userName) throws SQLException {
		int wins = 0;
		String query = "Select victories from users where username = '" + userName + "'";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()) {
			wins = rs.getInt(1);
		}
		return wins;
	}
	
	/**
	 * Method which increments a user's amount of games won
	 * @param userName
	 * @throws SQLException
	 */
	public void gameWon(String userName) throws SQLException  {
		int wins = getGamesWon(userName);
		wins++;
		String query = "update users set victories = "+ wins +" where username = '" + userName + "'";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.executeUpdate();
	}
	
	/**
	 * Method which increments a user's amount of games played
	 * @param user1
	 * @param user2
	 * @throws SQLException
	 */
	public void gamePlayed(String user) throws SQLException {
		int games = getGamesPlayed(user);
		games++;
		String updateQuery = "update users set nrofgames = " + games + " where username = '" + user +"'";
		
		PreparedStatement pst1 = conn.prepareStatement(updateQuery);
		pst1.executeUpdate();
		
		
	}
	
	/**
	 * Method which fetches the highscore 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Score> getHighScore() throws SQLException {
		int count = 0;
		ArrayList<Score> list = new ArrayList<Score>();
		String highscoreQuery = "Select username, victories from users order by victories desc";
		
		PreparedStatement pst = conn.prepareStatement(highscoreQuery);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()) {
			 String user = rs.getString("username");
			 int score = rs.getInt(2);
			 list.add(new Score(user, score));
			 count++;
			 if(count >= 10) {
				 break;
			 }
		}
		
		return list;
	}
	
	/**
	 * Method which adds a username to the database if not already in it
	 * @param username
	 * @throws SQLException
	 */
	public void addPlayer(String username) throws SQLException {
		boolean playerexists = false;
		String checkPlayerQuery = "Select username from users";
		String addplayerQuery = "insert into users(userid,username, nrofgames, victories) values (" + idNumber + ",'" + username + "',0,0)";
		
		PreparedStatement pst = conn.prepareStatement(checkPlayerQuery);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()) {
			String user = rs.getString("username");
			if(user.equals(username)) {
				playerexists = true;
				System.out.println(user + " already exists in database");
				break;
			}
		}
		if(!playerexists) {
			PreparedStatement psAdd = conn.prepareStatement(addplayerQuery);
			psAdd.executeUpdate();
		}
		
	}
	
	public static void main (String[] args) {
		HSDatabase comm = new HSDatabase();
				
		try {
			//comm.addPlayer("Jakob");
			comm.gameWon("Hasse");
			//comm.gamePlayed("Hasse");
			ArrayList<Score> list = comm.getHighScore();
			for(Score sc : list) {
				System.out.println(sc.getUserName() + " , " + sc.getScore());
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
