package haur_ranking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.Competitor;
import haur_ranking.domain.IPSCDivision;
import haur_ranking.domain.Stage;
import haur_ranking.domain.StageScoreSheet;
import haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;
import haur_ranking.repository.haur_ranking_repository.implementation.HaurRankingDatabaseUtils;

public class StageScoreSheetService {

	private static StageScoreSheetRepository sheetRepository;

	public static void init(StageScoreSheetRepository stageScoreSheetRepository) {
		sheetRepository = stageScoreSheetRepository;
	}

	public static List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<StageScoreSheet> sheets = sheetRepository.find(firstName, lastName, division, entityManager);
		entityManager.close();
		return sheets;
	}

	public static List<StageScoreSheet> findAll() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<StageScoreSheet> sheets = sheetRepository.findAll(entityManager);
		entityManager.close();
		return sheets;
	}

	// Returns a list of score sheets for classifiers which are valid for
	// ranking, ie. have minimum two results.

	public static List<StageScoreSheet> findCompetitorScoreSheetsForValidClassifiers(String firstName, String lastName,
			IPSCDivision division) {
		List<StageScoreSheet> sheets = null;
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		try {
			Set<ClassifierStage> classifiersWithTwoOrMoreResults = StageService
					.getClassifierStagesWithTwoOrMoreResults(division).keySet();
			sheets = sheetRepository.find(firstName, lastName, division, classifiersWithTwoOrMoreResults,
					entityManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return sheets;
	}

	public static int getTotalStageScoreSheetCount() {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		int sheetCount = sheetRepository.getCount(entityManager);
		entityManager.close();
		return sheetCount;
	}

	public static int removeExtraStageScoreSheets(List<StageScoreSheet> newlyAddedScoreSheets) {
		List<Long> sheetsToBeRemoved = new ArrayList<Long>();
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		// Remove old score sheets for same competitor, division and classifier
		for (StageScoreSheet sheet : newlyAddedScoreSheets) {
			List<StageScoreSheet> databaseScoreSheets = sheetRepository.find(sheet.getCompetitor().getFirstName(),
					sheet.getCompetitor().getLastName(), sheet.getIpscDivision(), sheet.getStage().getClassifierStage(),
					entityManager);
			// Remove scores for same classifier where match date is older
			// (StageScoreSheetRepository returns list in descending order by
			// match date)
			if (databaseScoreSheets.size() > 1) {
				for (StageScoreSheet dbSheet : databaseScoreSheets.subList(1, databaseScoreSheets.size())) {
					boolean idAlreadyListed = false;
					// Check if Id has already been added to list for removal
					for (Long listedId : sheetsToBeRemoved) {
						if (listedId.equals(dbSheet.getId()))
							idAlreadyListed = true;
					}
					if (!idAlreadyListed) {
						sheetsToBeRemoved.add(dbSheet.getId());
					}
				}
			}
		}
		entityManager.close();
		removeInBatch(sheetsToBeRemoved);
		return sheetsToBeRemoved.size();
	}

	public static void removeInBatch(List<Long> idLIst) {
		List<Long> idList = new ArrayList<Long>();
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		// Remove reference from stage to score sheet in database
		for (Long id : idLIst) {
			StageScoreSheet sheet = sheetRepository.find(id, entityManager);
			Stage stage = StageService.find(sheet.getStage().getId());
			StageScoreSheet sheetToRemoveFromStage = null;
			for (StageScoreSheet s : stage.getStageScoreSheets()) {
				if (s.getId().equals(sheet.getId()))
					sheetToRemoveFromStage = s;
			}
			if (sheetToRemoveFromStage != null)
				stage.getStageScoreSheets().remove(sheetToRemoveFromStage);
			idList.add(sheet.getId());
		}

		idList.add(new Long(1));

		// Remove score sheet from database
		if (idList.size() > 0)
			sheetRepository.removeInBatch(idList, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static void removeStageScoreSheetsForCompetitor(Competitor competitor) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<StageScoreSheet> sheets = sheetRepository.find(competitor, entityManager);
		entityManager.close();
		List<Long> sheetIds = new ArrayList<Long>();
		for (StageScoreSheet sheet : sheets) {
			sheetIds.add(sheet.getId());
		}
		removeInBatch(sheetIds);
	}

	public static int getCompetitorStageScoreSheetCount(Competitor competitor) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		int count = sheetRepository.getCompetitorStageScoreSheetCount(competitor, entityManager);
		entityManager.close();
		return count;
	}

	public static List<StageScoreSheet> find(String firstName, String lastName, IPSCDivision division,
			Set<ClassifierStage> classifierSet) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<StageScoreSheet> sheets = sheetRepository.find(firstName, lastName, division, classifierSet,
				entityManager);
		entityManager.close();
		return sheets;
	}

	public static double getCompetitorHitFactorAverage(Competitor competitor, IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.getEntityManagerFactory().createEntityManager();
		List<Double> latestHitFactors = sheetRepository.getCompetitorLatestScores(competitor, division, entityManager);
		entityManager.close();
		double hitFactorSum = 0.0;
		for (Double hf : latestHitFactors) {
			hitFactorSum += hf;
		}
		return hitFactorSum / latestHitFactors.size();
	}
}
