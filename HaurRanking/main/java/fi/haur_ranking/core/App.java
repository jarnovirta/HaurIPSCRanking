package fi.haur_ranking.core;

import fi.haur_ranking.database.StageScoreService;
import fi.haur_ranking.domain.Match;

public class App {
	public static void main(String[] args) {
		// System.out.println("Starting GUI");
		// MainWindow mainWindow = new MainWindow();
		
		System.out.println("Starting IPSCResultsUploader and reading database...");
		StageScoreService scoreService = new StageScoreService();
		
		// FOR TESTING:
		Match match = new Match();
		match.setWinMssMatchId(1);
		
	}
}
