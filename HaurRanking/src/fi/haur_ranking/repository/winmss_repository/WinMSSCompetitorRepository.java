package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Competitor;

public class WinMSSCompetitorRepository {
	public static Competitor findCompetitor(Long winMSSMemberId, Long matchId) {
		try {
			Connection connection = WinMssDatabaseUtil.getConnection();
			Statement statement;
			ResultSet resultSet;
			Competitor competitor = null;
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT Firstname, Lastname, ICS FROM tblMember WHERE MemberId = " + winMSSMemberId);
			if (resultSet.next()) {
				competitor = new Competitor(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
			}

			statement.close();
			resultSet.close();
			return competitor;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static boolean isDisqualified(Long winMssMemberId, Long matchId) {
		try {
			Connection connection = WinMssDatabaseUtil.getConnection();
			Statement statement;
			ResultSet resultSet;
			statement = connection.createStatement();
			boolean isDisqualified = false;
			resultSet = statement.executeQuery("SELECT IsDisqualified FROM tblMatchCompetitor WHERE MatchId=" + matchId + " AND MemberId = " + winMssMemberId);
			if (resultSet.next()) {
				isDisqualified = resultSet.getBoolean(1);
			}
			statement.close();
			resultSet.close();
			return isDisqualified;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
