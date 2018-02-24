package haur_ranking.repository.haur_ranking_repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.DivisionRankingRow;
import haur_ranking.domain.Ranking;
import haur_ranking.exception.DatabaseException;
import haur_ranking.repository.haur_ranking_repository.RankingRepository;

public class RankingRepositoryImpl implements RankingRepository {
	@Override
	public void save(Ranking ranking, EntityManager entityManager) throws DatabaseException {
		try {
			entityManager.persist(ranking);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<Ranking> getRankingsListPage(int page, int pageSize, EntityManager entityManager) {
		List<Ranking> rankings = null;
		try {
			String queryString = "SELECT r FROM Ranking r ORDER BY r.date DESC";
			TypedQuery<Ranking> query = entityManager.createQuery(queryString, Ranking.class);
			int firstResult = (page - 1) * pageSize + 1;
			query.setFirstResult(firstResult);
			query.setMaxResults(pageSize);
			rankings = query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rankings;
	}

	@Override
	public int getCount(EntityManager entityManager) {
		int count = -1;
		try {
			count = ((Long) entityManager.createQuery("SELECT COUNT(r) from Ranking r").getSingleResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public void deleteAll(EntityManager entityManager) throws DatabaseException {
		try {
			String queryString = "DELETE FROM Ranking r";
			entityManager.createQuery(queryString).executeUpdate();
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void delete(Ranking ranking, EntityManager entityManager) throws DatabaseException {
		try {
			if (!entityManager.contains(ranking))
				ranking = entityManager.merge(ranking);
			entityManager.remove(ranking);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Ranking findCurrentRanking(EntityManager entityManager) {
		Ranking ranking = null;
		try {
			String queryString = "SELECT r FROM Ranking r ORDER BY r.date DESC";
			TypedQuery<Ranking> query = entityManager.createQuery(queryString, Ranking.class);
			query.setMaxResults(1);
			List<Ranking> result = query.getResultList();
			if (result.size() > 0) {
				ranking = result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ranking;
	}

	@Override
	public void removeRankingRowsForCompetitor(Competitor competitor, EntityManager entityManager)
			throws DatabaseException {
		try {
			String queryString = "SELECT dvrr FROM  DivisionRankingRow dvrr WHERE dvrr.competitor = :competitor";
			TypedQuery<DivisionRankingRow> query = entityManager.createQuery(queryString, DivisionRankingRow.class);
			query.setParameter("competitor", competitor);
			List<DivisionRankingRow> divisionRankingRows = query.getResultList();
			for (DivisionRankingRow row : divisionRankingRows) {
				row.setCompetitor(null);
				row.getDivisionRanking().getDivisionRankingRows().remove(row);
				entityManager.remove(row);
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
}
