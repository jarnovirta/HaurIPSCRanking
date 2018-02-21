package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.Ranking;

public interface RankingRepository {
	public void removeRankingRowsForCompetitor(Competitor competitor, EntityManager entityManager);

	public Ranking findCurrentRanking(EntityManager entityManager);

	public void delete(Ranking ranking, EntityManager entityManager);

	public void deleteAll(EntityManager entityManager);

	public int getCount(EntityManager entityManager);

	public List<Ranking> getRankingsListPage(int page, int pageSize, EntityManager entityManager);

	public void save(Ranking ranking, EntityManager entityManager);
}
