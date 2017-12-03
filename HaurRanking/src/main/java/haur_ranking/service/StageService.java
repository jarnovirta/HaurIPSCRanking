package haur_ranking.service;

import java.util.Map;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Stage;
import haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import haur_ranking.repository.haur_ranking_repository.StageRepository;
import haur_ranking.repository.winmss_repository.WinMSSStageRepository;

public class StageService {
	private static Map<ClassifierStage, Stage> validClassifiers;

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

	public static boolean isValidClassifier(Stage stage) {
		if (validClassifiers == null) {
			validClassifiers = WinMSSStageRepository.getValidClassifiers();
		}
		Stage validStage = validClassifiers.get(stage.getSaveAsClassifierStage());
		if (stage.getPaperTargetCount() != validStage.getPaperTargetCount()
				|| stage.getPlateCount() != validStage.getPlateCount()
				|| stage.getPopperCount() != validStage.getPopperCount()
				|| stage.getMinShotsCount() != validStage.getMinShotsCount()) {
			return false;
		}
		return true;
	}
}
