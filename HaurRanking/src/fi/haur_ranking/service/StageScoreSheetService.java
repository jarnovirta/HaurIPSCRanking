package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.repository.haur_ranking_repository.StageRepository;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class StageScoreSheetService {

	public static void filterStageScoreSheetsExistingInDatabase(List<StageScoreSheet> sheets,
			EntityManager entityManager) {
		StageScoreSheetRepository.filterStageScoreSheetsExistingInDatabase(sheets, entityManager);
	}

	public static List<StageScoreSheet> findAll() {
		return StageScoreSheetRepository.findAll();
	}

	public static List<StageScoreSheet> findAllForClassifier(IPSCDivision division, ClassifierStage classifier) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = StageScoreSheetRepository.findAllForClassifier(division, classifier,
				entityManager);
		entityManager.close();
		return sheets;
	}

	public static List<StageScoreSheet> findClassifierStageResultsForCompetitor(Competitor competitor,
			IPSCDivision division, Set<ClassifierStage> classifierStages, EntityManager entityManager) {
		return StageScoreSheetRepository.findClassifierStageResultsForCompetitor(competitor, division, classifierStages,
				entityManager);
	}

	public static List<StageScoreSheet> findClassifierStageResultsForCompetitor(String firstName, String lastName,
			IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = StageScoreSheetRepository.findDivisionResultsForCompetitor(firstName, lastName,
				division, entityManager);
		entityManager.close();
		return sheets;
	}

	public static List<StageScoreSheet> findDivisionForCompetitor(String firstName, String lastName,
			IPSCDivision division, EntityManager entityManager) {
		return StageScoreSheetRepository.findDivisionResultsForCompetitor(firstName, lastName, division, entityManager);
	}

	public static int getTotalStageScoreSheetCount() {
		return StageScoreSheetRepository.getTotalStageScoreSheetCount();
	}

	public static void removeInBatch(List<Long> idLIst) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		entityManager.getTransaction().begin();
		List<Long> idList = new ArrayList<Long>();

		// Remove reference from stage to score sheet in database
		for (Long id : idLIst) {
			StageScoreSheet sheet = StageScoreSheetRepository.find(id, entityManager);
			Stage stage = StageRepository.find(sheet.getStage().getId(), entityManager);
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
			StageScoreSheetRepository.removeInBatch(idList, entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static void removeOldClassifierResults(List<StageScoreSheet> newlyAddedScoreSheets) {
		List<Long> sheetsToBeRemoved = new ArrayList<Long>();
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();

		for (StageScoreSheet sheet : newlyAddedScoreSheets) {
			List<StageScoreSheet> databaseScoreSheets = StageScoreSheetRepository
					.findClassifierStageResultsForCompetitor(sheet.getCompetitor().getFirstName(),
							sheet.getCompetitor().getLastName(), sheet.getIpscDivision(),
							sheet.getStage().getClassifierStage(), entityManager);
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
	}

	public static void setCompetitorsToStageScoreSheets(List<StageScoreSheet> sheets, EntityManager entityManager) {
		for (StageScoreSheet sheet : sheets) {
			// Check if competitor exists in database.
			Competitor existingCompetitor = CompetitorRepository.findByName(sheet.getCompetitor().getFirstName(),
					sheet.getCompetitor().getLastName(), entityManager);
			if (existingCompetitor != null)
				sheet.setCompetitor(existingCompetitor);
			else
				sheet.setCompetitor(CompetitorRepository.persist(sheet.getCompetitor(), entityManager));
		}
	}
}
