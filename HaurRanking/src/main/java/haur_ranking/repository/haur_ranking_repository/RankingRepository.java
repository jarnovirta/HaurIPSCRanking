package haur_ranking.repository.haur_ranking_repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import haur_ranking.domain.Ranking;

public class RankingRepository {
	public static void save(Ranking ranking, EntityManager entityManager) {
		try {
			entityManager.persist(ranking);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Ranking> findOldRankings(EntityManager entityManager) {
		List<Ranking> rankings = null;
		try {
			String queryString = "SELECT r FROM Ranking r ORDER BY r.date DESC";
			TypedQuery<Ranking> query = entityManager.createQuery(queryString, Ranking.class);
			rankings = query.getResultList();
			if (rankings != null) {
				if (rankings.size() == 0 || rankings.size() == 1)
					rankings = null;
				else
					rankings = rankings.subList(1, rankings.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rankings;
	}

	public static void deleteAll(EntityManager entityManager) {
		try {
			String queryString = "DELETE FROM Ranking r";
			entityManager.createQuery(queryString).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void delete(Ranking ranking, EntityManager entityManager) {

		try {
			if (!entityManager.contains(ranking))
				ranking = entityManager.merge(ranking);
			entityManager.remove(ranking);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Ranking findCurrentRanking(EntityManager entityManager) {
		try {
			String queryString = "SELECT r FROM Ranking r ORDER BY r.date DESC";
			TypedQuery<Ranking> query = entityManager.createQuery(queryString, Ranking.class);
			query.setMaxResults(1);
			List<Ranking> result = query.getResultList();
			if (result.size() == 0)
				return null;
			else {
				return result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
