package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// Methods for using a Ucanaccess connection to WinMSS database (Microsoft Access database file, .mdb).
public class WinMssDatabaseUtil {
	static Connection connection = null;
	public static Connection getConnection() {
		return connection;
	}
 	public static Connection createConnection(String fileLocation) {
		if (connection != null) return connection;
		
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String dbConnectionString = "jdbc:ucanaccess://" + fileLocation + ";jackcessOpener=fi.haur_ranking.repository.winmss_repository.CryptCodecOpener";
			connection = DriverManager.getConnection(dbConnectionString, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

 	public static void closeConnection() {
 		
		try {
			if (connection != null) connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		} 		
 	}
	public static void closeStatementResultSet(Statement statement, ResultSet resultSet) {
		try {
			if (statement != null) statement.close();
			if (resultSet != null) resultSet.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
