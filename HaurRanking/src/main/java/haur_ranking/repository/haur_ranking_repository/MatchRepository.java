package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import haur_ranking.domain.Match;

public interface MatchRepository {
	public Match find(Match match);

	public List<Match> getMatchListPage(int page, int pageSize);

	public void delete(Match match);

	public Match findNewestMatch();

	public List<Match> findAll();

	public int getMatchCount();

	public Match merge(Match match);

}
