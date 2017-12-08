package haur_ranking.service;

import java.util.List;

import javax.persistence.EntityManager;

import haur_ranking.domain.Competitor;
import haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class CompetitorService {

	public static Competitor find(String firstName, String lastName) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		entityManager.getTransaction().begin();
		Competitor competitor = find(firstName, lastName, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
		return competitor;

	}

	public static Competitor find(String firstName, String lastName, EntityManager entityManager) {
		return CompetitorRepository.find(firstName, lastName, entityManager);

	}

	public static List<Competitor> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Competitor> competitors = CompetitorRepository.findAll(entityManager);
		entityManager.close();
		return competitors;
	}

	public static int getTotalCompetitorCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		int count = CompetitorRepository.getTotalCompetitorCount(entityManager);
		entityManager.close();
		return count;
	}

	public static List<Competitor> findCompetitorInfoTableData() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		List<Competitor> competitors = CompetitorRepository.findAll(entityManager);
		for (Competitor competitor : competitors) {
			competitor.setResultCount(
					StageScoreSheetRepository.getCompetitorStageScoreSheetCount(competitor, entityManager));
		}
		entityManager.close();
		return competitors;
	}
}
