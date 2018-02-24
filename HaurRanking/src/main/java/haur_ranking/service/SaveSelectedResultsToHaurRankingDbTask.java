package haur_ranking.service;

import java.util.List;

import haur_ranking.domain.Match;

public class SaveSelectedResultsToHaurRankingDbTask implements Runnable {

	private List<Match> matches;
	private SaveToHaurRankingDBExceptionHandler exceptionHandler;

	public SaveSelectedResultsToHaurRankingDbTask(List<Match> matches, SaveToHaurRankingDBExceptionHandler handler) {
		this.matches = matches;
		this.exceptionHandler = handler;
	}

	@Override
	public void run() {
		try {
			WinMSSDataImportService.importSelectedResults(matches);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}
}
