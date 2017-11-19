package fi.haur_ranking.service;

import java.util.List;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;

public class CompetitorService {

	public static List<Competitor> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<Competitor> competitors = CompetitorRepository.findAll(entityManager);
		entityManager.close();
		return competitors;
	}

	public static Competitor find(String firstName, String lastName) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		Competitor competitor = find(firstName, lastName, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
		return competitor;

	}

	public static Competitor find(String firstName, String lastName, EntityManager entityManager) {
		return CompetitorRepository.find(firstName, lastName, entityManager);

	}

	public static int getTotalCompetitorCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int count = CompetitorRepository.getTotalCompetitorCount(entityManager);
		entityManager.close();
		return count;
	}
}
