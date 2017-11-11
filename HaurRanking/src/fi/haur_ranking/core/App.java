package fi.haur_ranking.core;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.gui.MainWindow;

public class App {
	
	public static void main(String[] args) {
		
		// Start GUI
		MainWindow gui = new MainWindow();
		gui.prepareGUI();
		gui.showHaurRankingGui();
		
		// FOR TESTING:
//		List<Match> matches = MatchService.findAllFromWinMSSDb();
//		MatchService.saveToHaurRankingDb(matches);
//		System.out.println("TOTAL COUNT: " + MatchService.getTotalMatchCount());
		////////
	}

}
