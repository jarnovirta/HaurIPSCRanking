package fi.haur_ranking.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.Stage;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.CompetitorRepository;
import fi.haur_ranking.repository.haur_ranking_repository.HaurRankingDatabaseUtils;
import fi.haur_ranking.repository.haur_ranking_repository.StageRepository;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class StageScoreSheetService {

	public static List<StageScoreSheet> findAll() {
		return StageScoreSheetRepository.findAll();
	}

	public static List<StageScoreSheet> find(String firstName, String lastName,
			IPSCDivision division) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		List<StageScoreSheet> sheets = StageScoreSheetRepository.find(firstName, lastName,
				division, entityManager);
		entityManager.close();
		return sheets;
	}

	public static int getTotalStageScoreSheetCount() {
		return StageScoreSheetRepository.getTotalStageScoreSheetCount();
	}

	public static void removeExtraStageScoreSheets(List<StageScoreSheet> newlyAddedScoreSheets) {
		removeSheetsForSameCompetitorAndClassifier(newlyAddedScoreSheets);

		Map<Competitor, List<IPSCDivision>> competitorsWithNewResults = new HashMap<Competitor, List<IPSCDivision>>();
		// Remove old score sheets for same competitor, division and classifier
		for (StageScoreSheet sheet : newlyAddedScoreSheets) {
			if (!competitorsWithNewResults.keySet().contains(sheet.getCompetitor())) {
				List<IPSCDivision> divisionList = new ArrayList<IPSCDivision>();
				divisionList.add(sheet.getIpscDivision());
				competitorsWithNewResults.put(sheet.getCompetitor(), divisionList);
			} else {
				if (!competitorsWithNewResults.get(sheet.getCompetitor()).contains(sheet.getIpscDivision())) {
					competitorsWithNewResults.get(sheet.getCompetitor()).add(sheet.getIpscDivision());
				}
			}
		}
		removeOldStageScoreSheets(competitorsWithNewResults);
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

	private static void removeOldStageScoreSheets(Map<Competitor, List<IPSCDivision>> competitorsWithNewResults) {
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();
		// Remove old score sheets for competitor with more than 8 results
		// in the division.
		List<Long> removeSheetsIdList = new ArrayList<Long>();
		for (Competitor competitor : competitorsWithNewResults.keySet()) {
			for (IPSCDivision division : competitorsWithNewResults.get(competitor)) {
				List<StageScoreSheet> sheets = StageScoreSheetRepository.find(
						competitor.getFirstName(), competitor.getLastName(), division, entityManager);
				if (sheets.size() > 8) {
					for (StageScoreSheet removeSheet : sheets.subList(8, sheets.size())) {
						removeSheetsIdList.add(removeSheet.getId());
					}
				}
			}
		}

		entityManager.close();
		removeInBatch(removeSheetsIdList);
	}

	private static void removeSheetsForSameCompetitorAndClassifier(List<StageScoreSheet> newlyAddedScoreSheets) {
		List<Long> sheetsToBeRemoved = new ArrayList<Long>();
		EntityManager entityManager = HaurRankingDatabaseUtils.createEntityManager();

		// Remove old score sheets for same competitor, division and classifier
		for (StageScoreSheet sheet : newlyAddedScoreSheets) {
			List<StageScoreSheet> databaseScoreSheets = StageScoreSheetRepository.find(
					sheet.getCompetitor().getFirstName(), sheet.getCompetitor().getLastName(), sheet.getIpscDivision(),
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
			Competitor existingCompetitor = CompetitorRepository.find(sheet.getCompetitor().getFirstName(),
					sheet.getCompetitor().getLastName(), entityManager);
			if (existingCompetitor != null)
				sheet.setCompetitor(existingCompetitor);
			else
				sheet.setCompetitor(CompetitorRepository.persist(sheet.getCompetitor(), entityManager));
		}
	}
}
