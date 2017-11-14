package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import fi.haur_ranking.domain.Competitor;

public class WinMSSCompetitorRepository {
	public static Competitor findCompetitor(Long winMSSMemberId, Long matchId) {
		Competitor competitor = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = WinMssDatabaseUtil.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT Firstname, Lastname FROM tblMember WHERE MemberId = " + winMSSMemberId);
			if (resultSet.next()) {
				competitor = new Competitor(resultSet.getString(1), resultSet.getString(2));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			WinMssDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return competitor;
		
	}
	public static boolean isDisqualified(Long winMssMemberId, Long matchId) {
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		boolean isDisqualified = false;
		
		try {
			connection = WinMssDatabaseUtil.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT IsDisqualified FROM tblMatchCompetitor WHERE MatchId=" + matchId + " AND MemberId = " + winMssMemberId);
			if (resultSet.next()) {
				isDisqualified = resultSet.getBoolean(1);
			}
			statement.close();
			resultSet.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			WinMssDatabaseUtil.closeStatementResultSet(statement, resultSet);
		}
		return isDisqualified;		
	}
}
