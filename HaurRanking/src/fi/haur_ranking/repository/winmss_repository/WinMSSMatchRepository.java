package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Match;

public class WinMSSMatchRepository {
	public static List<Match> findAll(String fileLocation) {
		List<Match> matchList = new ArrayList<Match>();
		try {
			Connection connection = WinMssDatabaseUtil.getConnection(fileLocation);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT MatchId, MatchName, MatchDt, TypeFirearmId FROM tblMatch");
			while (resultSet.next()) {
				Match match = new Match(resultSet.getString(2), resultSet.getLong(1), 
						resultSet.getString(3), resultSet.getInt(4));
				System.out.println("MATCH DATE: " + match.getWinMssDateString());
				match.setStageScoreSheets(WinMSSStageScoreSheetRepository.findScoreSheetsForMatch(match.getWinMssMatchId()));
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
