package fi.haur_ranking.service;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import fi.haur_ranking.domain.ClassifierStage;
import fi.haur_ranking.domain.Competitor;
import fi.haur_ranking.domain.IPSCDivision;
import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;
	
public class StageScoreSheetService {
	
	public static List<StageScoreSheet> findAllFromHaurRankingDB() {
		return StageScoreSheetRepository.findAll();
	}
	public static int getTotalStageScoreSheetCount() {
		return StageScoreSheetRepository.getTotalStageScoreSheetCount();
	}
	
	public static List<StageScoreSheet> findClassifierStageResultsForCompetitor(Competitor competitor, IPSCDivision division, Set<ClassifierStage> classifierStages, 
			EntityManager entityManager) {
		return StageScoreSheetRepository.findClassifierStageResultsForCompetitor(competitor, division, classifierStages, entityManager);
	}
	public static void filterStageScoreSheetsExistingInDatabase(List<StageScoreSheet> sheets, EntityManager entityManager) {
		StageScoreSheetRepository.filterStageScoreSheetsExistingInDatabase(sheets, entityManager);
	}
}
