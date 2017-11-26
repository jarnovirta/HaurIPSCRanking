package haur_ranking.service;

import java.util.List;

import haur_ranking.domain.Match;

public class ImportWinMSSResultsTask implements Runnable {

	private List<Match> matches;

	public ImportWinMSSResultsTask(List<Match> matches) {
		this.matches = matches;
	}

	@Override
	public void run() {
		MatchService.importSelectedResultsFromWinMSSDatabase(matches);
	}
}
