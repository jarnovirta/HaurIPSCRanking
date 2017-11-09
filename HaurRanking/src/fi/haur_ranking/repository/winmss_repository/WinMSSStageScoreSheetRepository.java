package fi.haur_ranking.repository.winmss_repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.StageScoreSheet;

public class WinMSSStageScoreSheetRepository {
	public List<StageScoreSheet> findScoreSheetsForMatch(Long winMssDbMatchId) {
		
		List<StageScoreSheet> stageScoreSheets= new ArrayList<StageScoreSheet>();
		Connection connection = WinMssDatabaseUtil.getConnection();		
		Statement statement = null;
		ResultSet resultSet = null;
		WinMSSCompetitorRepository winMSSCompetitorRepository = new WinMSSCompetitorRepository();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tblMatchStageScore WHERE MatchId = " + winMssDbMatchId);
			
			while (resultSet.next()) {
				StageScoreSheet sheet = new StageScoreSheet();
				sheet.setWinMssStageId(resultSet.getLong(2));
				sheet.setWinMssMemberId(resultSet.getLong(3));
				sheet.setaHits(resultSet.getInt(4));
				sheet.setbHits(resultSet.getInt(5));
				sheet.setcHits(resultSet.getInt(6));
				sheet.setdHits(resultSet.getInt(7));
				sheet.setMisses(resultSet.getInt(8));
				sheet.setPenalties(resultSet.getInt(9));
				sheet.setProcedurals(resultSet.getInt(10));
				sheet.setTime(resultSet.getDouble(11));
				sheet.setScoresZeroedForStage(resultSet.getBoolean(12));
				sheet.setCompetitor(winMSSCompetitorRepository.findCompetitor(sheet.getWinMssMemberId()));
				stageScoreSheets.add(sheet);
			}
			resultSet.close();
			statement.close();
			
			return stageScoreSheets;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
}
