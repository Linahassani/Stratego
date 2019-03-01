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
 * @author yun 
 *
 */
public class HSDatabase {
	private final String url = "jdbc:postgresql://pgserver.mah.se/stratego";
	private final String userID = "ah8378";
	private final String password = "2o0hd5wd";
	private Connection conn;
	
	public HSDatabase() {
		conn = connect();
	}
	
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
	
	public void gameWon(String userName) throws SQLException  {
		int wins = getGamesWon(userName);
		wins++;
		String query = "Insert into user(victories) values ('" + wins + "') where username = '" + userName + "'";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.executeUpdate();
	}
	
	public void gamePlayed(String user1, String user2) throws SQLException {
		int games1 = getGamesPlayed(user1);
		int games2 = getGamesPlayed(user2);
		games1++;
		games2++;
		String queryPlayer1 = "Select victories from users where username = '" + user1 + "'";
		String queryPlayer2 = "Select victories from users where username = '" + user2 + "'";
		
		PreparedStatement pst1 = conn.prepareStatement(queryPlayer1);
		pst1.executeUpdate();
		
		PreparedStatement pst2 = conn.prepareStatement(queryPlayer2);
		pst2.executeUpdate();
		
	}
	
	public ArrayList<Score> getHighScore() throws SQLException {
		ArrayList<Score> list = new ArrayList<Score>();
		String highscoreQuery = "Select username, victories from users order by victories desc";
		
		PreparedStatement pst = conn.prepareStatement(highscoreQuery);
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()) {
			 String user = rs.getString("username");
			 int score = rs.getInt(2);
			 list.add(new Score(user, score));
		}
		
		return list;
	}
	
	public static void main (String[] args) {
		HSDatabase comm = new HSDatabase();
				
		try {
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
