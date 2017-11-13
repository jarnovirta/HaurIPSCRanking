package fi.haur_ranking.service;

import java.util.List;

import fi.haur_ranking.domain.StageScoreSheet;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;
	
public class StageScoreSheetService {
	
	public static List<StageScoreSheet> findAllFromHaurRankingDB() {
		return StageScoreSheetRepository.findAll();
	}
	public static int getTotalStageScoreSheetCount() {
		return StageScoreSheetRepository.getTotalStageScoreSheetCount();
	}
	
}
