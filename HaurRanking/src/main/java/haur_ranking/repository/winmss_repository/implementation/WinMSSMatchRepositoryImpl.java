package haur_ranking.repository.winmss_repository.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
			connection = WinMSSDatabaseUtil.createConnection(fileLocation);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT MatchId, MatchName, MatchDt FROM tblMatch WHERE TypeFirearmId=1 ORDER BY MatchDt DESC");
			while (resultSet.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
				Calendar calendar = new GregorianCalendar(2013, 0, 31);
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
