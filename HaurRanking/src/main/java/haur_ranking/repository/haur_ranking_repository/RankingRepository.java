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

	public static void delete(EntityManager entityManager) {
		try {
			String queryString = "DELETE FROM Ranking r";
			entityManager.createQuery(queryString).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Ranking findCurrentRanking(EntityManager entityManager) {
		try {
			String queryString = "SELECT r FROM Ranking r";
			TypedQuery<Ranking> query = entityManager.createQuery(queryString, Ranking.class);
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
