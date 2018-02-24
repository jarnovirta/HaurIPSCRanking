package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.Ranking;
import haur_ranking.exception.DatabaseException;

public interface RankingRepository {
	public void removeRankingRowsForCompetitor(Competitor competitor, EntityManager entityManager)
			throws DatabaseException;

	public Ranking findCurrentRanking(EntityManager entityManager);

	public void delete(Ranking ranking, EntityManager entityManager) throws DatabaseException;

	public void deleteAll(EntityManager entityManager) throws DatabaseException;

	public int getCount(EntityManager entityManager);

	public List<Ranking> getRankingsListPage(int page, int pageSize, EntityManager entityManager);

	public void save(Ranking ranking, EntityManager entityManager) throws DatabaseException;
}
