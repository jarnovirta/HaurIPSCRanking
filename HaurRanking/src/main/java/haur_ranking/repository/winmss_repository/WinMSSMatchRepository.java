package haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Match;

public class WinMSSMatchRepository {
	public static List<Match> findAll(String fileLocation) {
		List<Match> matchList = new ArrayList<Match>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = WinMssDatabaseUtil.createConnection(fileLocation);
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT MatchId, MatchName, MatchDt FROM tblMatch WHERE TypeFirearmId=1");
			while (resultSet.next()) {
				matchList.add(new Match(resultSet.getString(2), resultSet.getLong(1), resultSet.getString(3)));
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			WinMssDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return matchList;
	}
}
