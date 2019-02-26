package highscore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Database connection class that will and should contain all the connections to the database.
 * Not finished yet, missing a few quaries. Look at the notes from prevrious developer Yun.
 * @author yun test test test
 *
 */
public class HSDatabase {
	private final String url = "jdbc:postgresql://pgserver.mah.se/stratego";
	private final String userID = "ah8378";
	private final String password = "2o0hd5wd";
	
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
	 * All info of workers
	 * @return ArrayList of workers (all info)
	 */
	public ArrayList<String> getUsers() {
		
		String query = "Select * From Users";
		ArrayList<String> workers = new ArrayList<String>();
		
		try(Connection conn = connect()) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				workers.add(rs.getInt("UserID" ) + ", " + 
						rs.getString("username") + ", " + 
						rs.getInt("nrofgames") + ", " + 
						rs.getString("victories"));

			}
			
		} catch (SQLException e) {

		}
		
		return workers;
	}	
	
	public static void main (String[] args) {
		HSDatabase comm = new HSDatabase();
		
		ArrayList<String> getWorkerName = comm.getUsers();
		for (String workers : getWorkerName) {
			System.out.println(workers);
		}
		
	}

}
