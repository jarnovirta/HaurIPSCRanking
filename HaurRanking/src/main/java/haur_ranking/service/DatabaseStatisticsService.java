package haur_ranking.service;

import haur_ranking.domain.DatabaseStatistics;

public class DatabaseStatisticsService {

	public static DatabaseStatistics getDatabaseStatistics() {
		DatabaseStatistics stats = new DatabaseStatistics();
		stats.setMatchCount(MatchService.getTotalMatchCount());
		stats.setCompetitorCount(CompetitorService.getTotalCompetitorCount());
		stats.setStageScoreSheetCount(StageScoreSheetService.getTotalStageScoreSheetCount());
		return stats;
	}
}
