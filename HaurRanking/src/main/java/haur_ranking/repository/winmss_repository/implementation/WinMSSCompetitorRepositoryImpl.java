package haur_ranking.repository.winmss_repository.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import haur_ranking.domain.Competitor;
import haur_ranking.repository.winmss_repository.WinMSSCompetitorRepository;

public class WinMSSCompetitorRepositoryImpl implements WinMSSCompetitorRepository {
	public Competitor findCompetitor(Long winMSSMemberId, Long matchId) {
		Competitor competitor = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = WinMSSDatabaseUtil.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT Firstname, Lastname, Comment FROM tblMember WHERE MemberId = " + winMSSMemberId);
			if (resultSet.next()) {
				competitor = new Competitor(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return competitor;

	}

	public boolean isDisqualified(Long winMssMemberId, Long matchId) {
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		boolean isDisqualified = false;

		try {
			connection = WinMSSDatabaseUtil.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT IsDisqualified FROM tblMatchCompetitor WHERE MatchId=" + matchId
					+ " AND MemberId = " + winMssMemberId);
			if (resultSet.next()) {
				isDisqualified = resultSet.getBoolean(1);
			}
			statement.close();
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			WinMSSDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return isDisqualified;
	}
}
