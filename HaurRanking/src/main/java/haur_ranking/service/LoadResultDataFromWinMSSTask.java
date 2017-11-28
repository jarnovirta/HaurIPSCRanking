package haur_ranking.service;

import java.util.List;

import haur_ranking.domain.Match;
import haur_ranking.gui.GUIDataService;

public class LoadResultDataFromWinMSSTask implements Runnable {
	private String winMSSFilePath;

	public LoadResultDataFromWinMSSTask(String winMSSFilePath) {
		this.winMSSFilePath = winMSSFilePath;
	}

	@Override
	public void run() {
		List<Match> matches = MatchService.findNewResultsInWinMSSDatabase(winMSSFilePath);
		GUIDataService.setImportResultsPanelMatchList(matches);
	}
}
