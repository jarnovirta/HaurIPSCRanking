package fi.haur_ranking.service;

import java.util.Map;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.repository.haur_ranking_repository.StageRepository;

public class StageService {
	public static Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		Map<ClassifierStage, Double> classifierStages = StageRepository.getClassifierStagesWithTwoOrMoreResults(division, entityManager);
		entityManager.close();
		return classifierStages;
	}
	
	public static Stage find(Stage stage, EntityManager entityManager) {
		return StageRepository.find(stage, entityManager);
	}
}
