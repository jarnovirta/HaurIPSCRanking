package fi.haur_ranking.repository.haur_ranking_repository;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.Ranking;

public class RankingRepository {
	public static void save(Ranking ranking, EntityManager entityManager) {
		try {
			entityManager.persist(ranking);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
