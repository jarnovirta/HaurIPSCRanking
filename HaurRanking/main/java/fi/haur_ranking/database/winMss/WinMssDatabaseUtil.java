package fi.haur_ranking.database.winMss;

import java.sql.Connection;
import java.sql.DriverManager;

// Methods for using a Ucanaccess connection to a Microsoft Access database file (.mdb).
public class WinMssDatabaseUtil {
	public static Connection connectToAccessDatabase() {
		Connection connection = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String dbConnectionString = "jdbc:ucanaccess://WinMSS.mdb;jackcessOpener=fi.haur_ranking.database.winMss.CryptCodecOpener";
			connection = DriverManager.getConnection(dbConnectionString, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeConnecion(Connection conn) {
		try {
			if (conn != null) {
				conn.commit();
				conn.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
