package fi.haur_ranking.service;

import fi.haur_ranking.domain.DatabaseStatistics;

public class DatabaseStatisticsService {

	public static DatabaseStatistics getDatabaseStatistics() {
		DatabaseStatistics stats = new DatabaseStatistics();
		stats.setMatchCount(MatchService.getTotalMatchCount());
		stats.setCompetitorCount(CompetitorService.getTotalCompetitorCount());
		stats.setStageCount(StageScoreSheetService.getTotalStageScoreSheetCount());
		return stats;
	}
}
