package haur_ranking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import haur_ranking.domain.ClassifierStage;
import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.domain.IPSCDivision;

public class DatabaseStatisticsService {

	public static DatabaseStatistics getDatabaseStatistics() {
		DatabaseStatistics stats = new DatabaseStatistics();
		stats.setMatchCount(MatchService.getTotalMatchCount());
		stats.setStageCount(StageService.getTotalStageCount());
		stats.setCompetitorCount(CompetitorService.getTotalCompetitorCount());
		stats.setStageScoreSheetCount(StageScoreSheetService.getTotalStageScoreSheetCount());
		List<ClassifierStage> validClassifiers = new ArrayList<ClassifierStage>();
		for (IPSCDivision division : IPSCDivision.values()) {
			Map<ClassifierStage, Double> validClassifiersByDivision = StageService
					.getClassifierStagesWithTwoOrMoreResults(division);
			for (ClassifierStage classifier : validClassifiersByDivision.keySet()) {
				if (!validClassifiers.contains(classifier))
					validClassifiers.add(classifier);
			}
		}
		stats.setValidClassifiersCount(validClassifiers.size());
		return stats;
	}
}
