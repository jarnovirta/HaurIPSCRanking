package fi.haur_ranking.core;

import java.util.List;

import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.service.StageScoreService;

public class App {
	public static void main(String[] args) {
		// System.out.println("Starting GUI");
		// MainWindow mainWindow = new MainWindow();
		
		System.out.println("Starting IPSCResultsUploader and reading database...");
		StageScoreService scoreService = new StageScoreService();
		
		// FOR TESTING:
		Match match = new Match();
		match.setWinMssMatchId(1);
		
		List<StageScoreSheet> sheets = scoreService.findScoreSheetsForMatch(match);
		System.out.println("GOT " + sheets.size() + " SHEETS");
		scoreService.testSave(sheets.get(0));
		System.out.println("DONE");
	}
}
