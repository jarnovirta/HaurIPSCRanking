package fi.haur_ranking.service;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;

public class CompetitorService {

	public static int getTotalCompetitorCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int count = CompetitorRepository.getTotalCompetitorCount(entityManager);
		entityManager.close();
		return count;
	}
	public static Competitor findByName(String firstName, String lastName) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		Competitor competitor = CompetitorRepository.findByName(firstName, lastName, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
		return competitor;
				
	}
}
