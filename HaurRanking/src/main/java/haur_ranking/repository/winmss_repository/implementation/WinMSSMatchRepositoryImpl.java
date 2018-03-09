package haur_ranking.repository.winmss_repository.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import haur_ranking.domain.Match;
import haur_ranking.repository.winmss_repository.WinMSSMatchRepository;

public class WinMSSMatchRepositoryImpl implements WinMSSMatchRepository {
	@Override
	public List<Match> findAll(String fileLocation) {
		List<Match> matchList = new ArrayList<Match>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			connection = WinMSSDatabaseUtil.createConnection(fileLocation);
			statement = connection.createStatement();
			String queryString = "SELECT MatchId, MatchName, MatchDt FROM tblMatch WHERE TypeFirearmId=1 "
					+ "ORDER BY MatchDt DESC";
			resultSet = statement.executeQuery(queryString);
			while (resultSet.next()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateFormat.parse(resultSet.getString(3)));
				matchList.add(new Match(resultSet.getString(2), resultSet.getLong(1), calendar));
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return matchList;
	}
}
