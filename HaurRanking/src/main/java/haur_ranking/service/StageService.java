package haur_ranking.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.exception.DatabaseException;
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
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		Stage resultStage = stageRepository.find(stage, entityManager);
		entityManager.close();
		return resultStage;
	}

	public static Stage find(Long id) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		Stage stage = stageRepository.find(id, entityManager);
		entityManager.close();
		return stage;
	}

	public static int getTotalStageCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		int count = stageRepository.getCount(entityManager);
		entityManager.close();
		return count;
	}

	public static Map<ClassifierStage, Double> getClassifierStagesWithTwoOrMoreResults(IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		Map<ClassifierStage, Double> classifierStages = stageRepository
				.getClassifierStagesWithTwoOrMoreResults(division, entityManager);
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

	public static void delete(List<Stage> stages) throws DatabaseException {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();

		try {
			for (Stage stage : stages) {
				Match match = stage.getMatch();
				match.getStages().remove(stage);
				if (!entityManager.contains(stage))
					stage = entityManager.merge(stage);
				stageRepository.delete(stage, entityManager);
				entityManager.flush();
				if (match.getStages().size() == 0)
					MatchService.delete(match);
			}
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}

}
