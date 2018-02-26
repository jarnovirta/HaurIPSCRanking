package haur_ranking.repository.winmss_repository.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

			// Only fetch data for matches after 13.3.2017. This is a hack, to
			// exclude duplicate data to that imported from Excel-file.
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateFormat.parse("2017-03-14 00:00:00"));
			Date cutOffDate = calendar.getTime();

			connection = WinMSSDatabaseUtil.createConnection(fileLocation);
			statement = connection.createStatement();
			String queryString = "SELECT MatchId, MatchName, MatchDt FROM tblMatch WHERE TypeFirearmId=1 "
					+ "AND MatchDt >='" + dateFormat.format(cutOffDate) + "' ORDER BY MatchDt DESC";
			resultSet = statement.executeQuery(queryString);
			while (resultSet.next()) {
				calendar = Calendar.getInstance();
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
