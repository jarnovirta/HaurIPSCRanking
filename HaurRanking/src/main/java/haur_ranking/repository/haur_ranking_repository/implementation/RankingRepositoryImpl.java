package haur_ranking.repository.haur_ranking_repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.DivisionRankingRow;
import haur_ranking.domain.Ranking;
import haur_ranking.repository.haur_ranking_repository.RankingRepository;

public class RankingRepositoryImpl implements RankingRepository {
	@Override
	public void save(Ranking ranking) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(ranking);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
	}

	@Override
	public List<Ranking> getRankingsListPage(int page, int pageSize) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
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
		entityManager.close();
		return rankings;
	}

	@Override
	public int getCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int count = -1;
		try {
			count = ((Long) entityManager.createQuery("SELECT COUNT(r) from Ranking r").getSingleResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return count;
	}

	@Override
	public void deleteAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		try {
			String queryString = "DELETE FROM Ranking r";
			entityManager.createQuery(queryString).executeUpdate();
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
	}

	@Override
	public void delete(Ranking ranking) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (!entityManager.contains(ranking))
				ranking = entityManager.merge(ranking);
			entityManager.remove(ranking);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
	}

	@Override
	public Ranking findCurrentRanking() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
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
		entityManager.close();
		return ranking;
	}

	@Override
	public void removeRankingRowsForCompetitor(Competitor competitor) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
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
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
	}
}
