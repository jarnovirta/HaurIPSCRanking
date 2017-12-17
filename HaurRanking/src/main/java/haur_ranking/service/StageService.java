package haur_ranking.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.repository.haur_ranking_repository.StageRepository;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;
import haur_ranking.repository.winmss_repository.WinMSSStageRepository;

public class StageService {
	private static Map<ClassifierStage, Stage> validClassifiers;

	private static StageRepository stageRepository;
	private static WinMSSStageRepository winMSSStageRepository;

	public static void init(StageRepository stageRepo, WinMSSStageRepository winMSSStageRepo) {
		stageRepository = stageRepo;
		winMSSStageRepository = winMSSStageRepo;
	}

	public static Stage find(Stage stage) {
		return stageRepository.find(stage);
	}

	public static Stage find(Long id) {
		return stageRepository.find(id);
	}

	public static int getTotalStageCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		int count = stageRepository.getCount();
		entityManager.close();
		return count;

	}

	public static Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		Map<ClassifierStage, Double> classifierStages = stageRepository
				.getClassifierStagesWithTwoOrMoreResults(division);
		entityManager.close();
		return classifierStages;
	}

	public static boolean isValidClassifier(Stage stage) {
		if (validClassifiers == null) {
			validClassifiers = winMSSStageRepository.getValidClassifiers();
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

	public static void delete(List<Stage> stages) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		for (Stage stage : stages) {
			Match match = stage.getMatch();
			match.getStages().remove(stage);
			if (!entityManager.contains(stage))
				stage = entityManager.merge(stage);
			stageRepository.delete(stage);
			entityManager.flush();
			if (match.getStages().size() == 0)
				MatchService.delete(match);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
