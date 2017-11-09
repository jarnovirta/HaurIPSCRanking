package fi.haur_ranking.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.repository.winmss_repository.WinMssDatabaseUtil;

public class CompetitorService {
	
	public String findCompetitorEmail(int memberId) {
		String email = null;		
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = WinMssDatabaseUtil.getConnection();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT Email FROM tblMember WHERE MemberId = " + memberId);
			while (resultSet.next()) {
				email = resultSet.getString(1);
			}
			resultSet.close();
			statement.close();
			
			return email;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	public List<Competitor> findDisqualifiedCompetitors(Match match) {
				
		Statement statement = null;
		ResultSet resultSet = null;
		List<Competitor> disqualifiedCompetitors = new ArrayList<Competitor>();
		Connection connection = WinMssDatabaseUtil.getConnection();
		try {
			System.out.println("Searching for DQed competitors for match " + match.getWinMssMatchId());
			statement = connection.createStatement();
			
			resultSet = statement.executeQuery("SELECT MemberId, CompetitorId, TypeDisqualifyRuleId FROM tblMatchCompetitor WHERE "
					+ "IsDisqualified = 1 AND MatchId = " + match.getWinMssMatchId());
			while (resultSet.next()) {
				System.out.println(resultSet.getRow());
				System.out.println("Found DQed competitors");
				Competitor competitor = new Competitor();
				competitor.setWinMssMemberId(resultSet.getLong(1));
				competitor.setWinMssCompetitorId(resultSet.getLong(2));
				competitor.setWinMssTypeDisqualifyRuleId(resultSet.getInt(3));
				disqualifiedCompetitors.add(competitor);
			}
			resultSet.close();
			statement.close();
			
			return disqualifiedCompetitors;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}

}
