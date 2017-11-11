package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.MatchRepository;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;
import fi.haur_ranking.repository.winmss_repository.WinMSSMatchRepository;

public class MatchService {
			
	public static List<Match> findAllFromWinMSSDb(String fileLocation) {
		return WinMSSMatchRepository.findAllHandgunMatches(fileLocation);
	}
//	public static Match findMatchFromWinMSSDb(int matchId) {
//		List<Competitor> competitors = findAllCompetitorsFromWinMSSDb();
//		for (Competitor competitor : competitors) {
//			System.out.println(competitor.getFirstName() + " " + competitor.getLastName());
//		}
//		return null;
//	}
	
//	private static List<Competitor> findAllCompetitorsFromWinMSSDb() {
//		Connection connection = WinMssDatabaseUtil.getConnection();
//		Statement statement = null;
//		ResultSet resultSet = null;
//		List<Competitor> competitors = new ArrayList<Competitor>();
//		try {
//			statement = connection.createStatement();
//			resultSet = statement.executeQuery("SELECT * FROM tblMember");
//			while (resultSet.next()) {
//				competitors.add(new Competitor(resultSet.getString(3), resultSet.getString(2), resultSet.getString(1)));
//			}
//			resultSet.close();
//			statement.close();
//			return competitors;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		}
//	}
	
	public static List<Match> saveToHaurRankingDb(List<Match> matches) {
		return MatchRepository.saveAll(matches);
	}
	public static int getTotalMatchCount() {
		return MatchRepository.getTotalMatchCount();
	}
	
	public static void importWinMssDatabase(String winMssDbLocation) {
		System.out.println("Starting import");
		List<Match> winMssMatches = WinMSSMatchRepository.findAllHandgunMatches(winMssDbLocation);
		 // MatchRepository.saveAll(winMssMatches);
		// Check if match has score sheets which are not in ranking database. Mark classifier stages.
		
		List<StageScoreSheet> sheets = new ArrayList<StageScoreSheet>();
		for (Match match : winMssMatches) {
			
			for (Stage stage : match.getStages()) {
				if (ClassifierStage.contains(stage.getName())) {
					stage.setClassifierStage(ClassifierStage.parseString(stage.getName()));
				}
				sheets.addAll(stage.getStageScoreSheets());
			}
		}
		System.out.println("Sending request for " + sheets.size() + " sheets");
		StageScoreSheetRepository.isInHaurRankingDatabase(sheets);
				
		// TEST: lis‰‰ classifierit jotka eiv‰t ole tietokannassa ja tee ranking-haku niille.
		
		List<StageScoreSheet> newClassifierStageScoreSheets = new ArrayList<StageScoreSheet>();
		for (Match match : winMssMatches) {
			for (Stage stage : match.getStages()) {
				if (stage.getClassifierStage() != null) {
					
				}
			}
		}
		
		
		// StageScoreSheetService.saveToHaurRankingDB(winMssMatches);
		
		
		System.out.println("\n\n *** Found " + newClassifierStageScoreSheets.size() + " matches from WinMSS:");
		
		System.out.println("\nIMPORT DONE");
		////////
	}
}