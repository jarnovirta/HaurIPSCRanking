package fi.haur_ranking.core;

import java.util.ArrayList;
import java.util.List;

import fi.haur_ranking.domain.Match;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.winmss_repository.WinMSSMatchRepository;
import fi.haur_ranking.repository.winmss_repository.WinMssDatabaseUtil;
import fi.haur_ranking.service.MatchService;

public class App {
	
	public static void main(String[] args) {
		// System.out.println("Starting GUI");
		// MainWindow mainWindow = new MainWindow();
		
		System.out.println("Starting IPSCResultsUploader and reading database...");
		// StageScoreSheetService scoreService = new StageScoreSheetService();
		
		// FOR TESTING:
		Match match = new Match();
		match.setWinMssMatchId(new Long(1));
		
//		List<StageScoreSheet> sheets = scoreService.findScoreSheetsForMatch(match);
//		System.out.println("GOT " + sheets.size() + " SHEETS");
		
		List<Match> matches = new ArrayList<Match>();
		WinMSSMatchRepository matchRepo = new WinMSSMatchRepository();
		matches = matchRepo.findAll();
		System.out.println("Found " + matches.size() + " Matches:");
		for (Match foundMatch: matches) {
			System.out.println(foundMatch.getWinMssMatchId() + " - " + foundMatch.getMatchName());
		}
		MatchService matchService = new MatchService();
		matchService.saveToHaurRankingDb(matches);
		System.out.println("DONE SAVING");
		
		
//		for (StageScoreSheet sheet : sheets) 
//			scoreService.testSave(sheet);
//		for (int y = 0; y < 200; y++) {
//			List<StageScoreSheet> sheetList = new ArrayList<StageScoreSheet>();
//		for (int i = 0; i < 50; i++) {
//			StageScoreSheet sheet = new StageScoreSheet();
//			sheet.setaHits(5);
//			sheet.setbHits(6);
//			sheet.setcHits(1);
//			sheet.setDeductedPoints(10);
//			sheet.setWinMssStageId(8);
//			sheetList.add(sheet);
//			
//		}
//			scoreService.testSave(sheetList);
//		}
//		System.out.println("DONE WRITING DATA");
		matches = matchService.findAllFromWinMSSDb();
		System.out.println("Got " + matches.size() + " matches");
		int sheetcount = 0;
		for (Match foundMatch : matches) {
			System.out.println("\n**** " + foundMatch.getMatchName());
			for (StageScoreSheet sheet : foundMatch.getStageScoreSheets()) {
				System.out.println("SHEET: " + sheet.getCompetitor().getFirstName() + " " + sheet.getCompetitor().getLastName());
				System.out.println("A - " + sheet.getaHits());
				System.out.println("C - " + sheet.getcHits());
				System.out.println("D - " + sheet.getdHits());
				sheetcount++;
			}
		}
		System.out.println("Got " + sheetcount + " sheets. \n**** DONE");
		
		WinMssDatabaseUtil.closeConnecion();
	}
}
