package fi.haur_ranking.core;

import java.util.ArrayList;
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
		sheets = scoreService.findAll();
		System.out.println("Got " + sheets.size() + " back");
		System.out.println("DONE");
	}
}
