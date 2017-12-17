package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.Ranking;

public interface RankingRepository {
	public void removeRankingRowsForCompetitor(Competitor competitor);

	public Ranking findCurrentRanking();

	public void delete(Ranking ranking);

	public void deleteAll();

	public int getCount();

	public List<Ranking> getRankingsListPage(int page, int pageSize);

	public void save(Ranking ranking);
}
