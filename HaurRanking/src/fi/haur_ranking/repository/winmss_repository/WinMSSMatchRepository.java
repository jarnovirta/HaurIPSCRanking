package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class WinMSSMatchRepository {

	Connection connection; 
	
	public List<Match> findAll() {
		List<Match> matchList = new ArrayList<Match>();
		
		try {
			WinMSSStageScoreSheetRepository winMSSStageScoreSheetRepository = new WinMSSStageScoreSheetRepository();
			connection = WinMssDatabaseUtil.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT MatchId, MatchName FROM tblMatch");
			while (resultSet.next()) {
				Match match = new Match(resultSet.getString(2), resultSet.getLong(1));
				match.setStageScoreSheets(winMSSStageScoreSheetRepository.findScoreSheetsForMatch(match.getWinMssMatchId()));
				matchList.add(match);
			}
			resultSet.close();
			statement.close();
			return matchList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
}
