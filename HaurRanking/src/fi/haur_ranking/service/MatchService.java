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
	Connection connection;
			
	public List<Match> findAllFromWinMSSDb() {
		WinMSSMatchRepository winMSSMatchRepository = new WinMSSMatchRepository();
		return winMSSMatchRepository.findAll();
	}
	public Match findMatchFromWinMSSDb(int matchId) {
		connection = WinMssDatabaseUtil.getConnection();
		List<Competitor> competitors = findAllCompetitorsFromWinMSSDb();
		for (Competitor competitor : competitors) {
			System.out.println(competitor.getFirstName() + " " + competitor.getLastName());
		}
		return null;
	}
	
	private List<Competitor> findAllCompetitorsFromWinMSSDb() {
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
	
	public List<Match> saveToHaurRankingDb(List<Match> matches) {
		return MatchRepository.saveAll(matches);
	}
}
