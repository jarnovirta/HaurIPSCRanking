package haur_ranking.service;

import java.util.List;

import haur_ranking.domain.Match;

public class SaveSelectedResultsToHaurRankingDbTask implements Runnable {

	private List<Match> matches;

	public SaveSelectedResultsToHaurRankingDbTask(List<Match> matches) {
		this.matches = matches;
	}

	@Override
	public void run() {
		MatchService.importSelectedResults(matches);
	}
}
