package fi.haur_ranking.service;

import java.util.List;

import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.MatchRepository;
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
		List<Match> winMssMatches = WinMSSMatchRepository.findAllHandgunMatches(winMssDbLocation);
		
		// TEST
		System.out.println("\n\n *** Found " + winMssMatches.size() + " matches from WinMSS:");
		boolean foundHeliScorecard = false;
		boolean foundSamiScorecard = false;
		//
		for (Match match : winMssMatches) {
			System.out.println("\n*** " + match.getMatchName() + " - " + match.getWinMssDateString() + " - ");
			for (Stage stage : match.getStages()) {
				System.out.println("\n STAGE: " + stage.getName());
				for (StageScoreSheet sheet : stage.getStageScoreSheets()) {
					if (sheet.getCompetitor().getLastName().equals("de Szejko") && sheet.getCompetitor().getFirstName().equals("Heli")) foundHeliScorecard = true;
					if (sheet.getCompetitor().getLastName().equals("Haanp‰‰") && sheet.getCompetitor().getFirstName().equals("Sami")) foundSamiScorecard = true;
					System.out.println(sheet.getCompetitor().getFirstName() + " " + sheet.getCompetitor().getLastName() + " " + 
				sheet.getLastModifiedInWinMSSDatabaseString() + " hf: " + sheet.getHitfactor() + " DQ: " + sheet.isDisqualified() + " Zeroed: " + sheet.isScoresZeroedForStage());
				}
			}
		}
		System.out.println("HELI card found: " + foundHeliScorecard);
		System.out.println("SAMi card found: " + foundSamiScorecard);
		System.out.println("\nIMPORT DONE");
	}
}