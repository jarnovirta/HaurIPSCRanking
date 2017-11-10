package fi.haur_ranking.service;

import fi.haur_ranking.domain.DatabaseStatistics;

public class DatabaseStatisticsService {
	
	public static DatabaseStatistics getDatabaseStatistics() {
		DatabaseStatistics stats = new DatabaseStatistics();
		stats.setMatchCount(MatchService.getTotalMatchCount());
		System.out.println("\n returning MATCH COUNT: " + stats.getMatchCount());
//		stats.setCompetitorCount(competitorRepository.getCount());
//		stats.setStageCount(stageScoreSheetRepository.getCount());
		return stats;
	}
}
