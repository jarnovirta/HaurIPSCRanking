package fi.haur_ranking.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.repository.haur_ranking_repository.MatchRepository;
import fi.haur_ranking.repository.winmss_repository.WinMSSMatchRepository;
import fi.haur_ranking.repository.winmss_repository.WinMssDatabaseUtil;

public class MatchService {
			
	public static List<Match> findAllFromWinMSSDb(String fileLocation) {
		return WinMSSMatchRepository.findAll(fileLocation);
	}
	public static Match findMatchFromWinMSSDb(int matchId) {
		List<Competitor> competitors = findAllCompetitorsFromWinMSSDb();
		for (Competitor competitor : competitors) {
			System.out.println(competitor.getFirstName() + " " + competitor.getLastName());
		}
		return null;
	}
	
	private static List<Competitor> findAllCompetitorsFromWinMSSDb() {
		Connection connection = WinMssDatabaseUtil.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		List<Competitor> competitors = new ArrayList<Competitor>();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tblMember");
			while (resultSet.next()) {
				competitors.add(new Competitor(resultSet.getString(3), resultSet.getString(2), resultSet.getString(1)));
			}
			resultSet.close();
			statement.close();
			return competitors;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static List<Match> saveToHaurRankingDb(List<Match> matches) {
		return MatchRepository.saveAll(matches);
	}
	public static int getTotalMatchCount() {
		return MatchRepository.getTotalMatchCount();
	}
	
	public static void importWinMssDatabase(String winMssDbLocation) {
		List<Match> winMSSMatches = findAllFromWinMSSDb(winMssDbLocation);
		
		saveToHaurRankingDb(findAllFromWinMSSDb(winMssDbLocation));
		System.out.println("\nIMPORT DONE");
	}
}
