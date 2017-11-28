package haur_ranking.service;

import java.util.Map;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Stage;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.StageRepository;

public class StageService {
	public static Stage find(Stage stage, EntityManager entityManager) {
		return StageRepository.find(stage, entityManager);
	}

	public static Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManager();
		Map<ClassifierStage, Double> classifierStages = StageRepository
				.getClassifierStagesWithTwoOrMoreResults(division, entityManager);
		entityManager.close();
		return classifierStages;
	}

}
