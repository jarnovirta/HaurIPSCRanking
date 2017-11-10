package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.DriverManager;

// Methods for using a Ucanaccess connection to a Microsoft Access database file (.mdb).
public class WinMssDatabaseUtil {
	static Connection connection = null;
	public static Connection getConnection() {
		return connection;
	}
 	public static Connection getConnection(String fileLocation) {
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

	public static void closeConnecion() {
		try {
			if (connection != null) {
				connection.commit();
				connection.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
