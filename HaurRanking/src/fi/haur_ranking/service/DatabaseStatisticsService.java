package fi.haur_ranking.service;

import fi.haur_ranking.domain.DatabaseStatistics;
import fi.haur_ranking.repository.haur_ranking_repository.StageScoreSheetRepository;

public class DatabaseStatisticsService {
	
	public static DatabaseStatistics getDatabaseStatistics() {
		DatabaseStatistics stats = new DatabaseStatistics();
		stats.setMatchCount(MatchService.getTotalMatchCount());
		System.out.println("\n returning MATCH COUNT: " + stats.getMatchCount());
		stats.setCompetitorCount(CompetitorService.getTotalCompetitorCount());
		stats.setStageCount(StageScoreSheetService.getTotalStageScoreSheetCount());
		return stats;
	}
}
