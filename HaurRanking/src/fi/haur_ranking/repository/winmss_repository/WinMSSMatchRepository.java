package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Match;

public class WinMSSMatchRepository {
	public static List<Match> findAllHandgunMatches(String fileLocation) {
		List<Match> matchList = new ArrayList<Match>();
		try {
			Connection connection = WinMssDatabaseUtil.getConnection(fileLocation);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT MatchId, MatchName, MatchDt FROM tblMatch WHERE TypeFirearmId=1");
			while (resultSet.next()) {
				Match match = new Match(resultSet.getString(2), resultSet.getLong(1), 
						resultSet.getString(3));
				match.setStages(WinMSSStageRepository.findStagesForMatch(match));
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
