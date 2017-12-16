package haur_ranking.gui.service;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.service.CompetitorService;
import haur_ranking.service.DatabaseStatisticsService;
import haur_ranking.service.MatchService;
import haur_ranking.service.RankingService;
import haur_ranking.service.StageService;

public class DatabasePanelDataService {
	private static List<Stage> databaseMatchInfoTableStages = new ArrayList<Stage>();
	private static List<Stage> databaseMatchInfoTableStagesToDelete = new ArrayList<Stage>();
	private static List<Match> databaseMatchInfoTableData;
	private static List<Competitor> databaseCompetitorInfoTableData;
	private static List<Competitor> competitorsToDelete = new ArrayList<Competitor>();
	private static DatabaseStatistics databaseStatistics;

	public static void init() {
		loadMatchTableData();
		loadCompetitorTableData();
		loadStatisticsTableData();

	}

	private static void loadMatchTableData() {
		databaseMatchInfoTableStages.clear();
		databaseMatchInfoTableData = MatchService.findAll();
		for (Match match : databaseMatchInfoTableData) {
			if (match.getStages() != null)
				databaseMatchInfoTableStages.addAll(match.getStages());
		}
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.DATABASE_MATCH_TABLE_UPDATE));
	}

	private static void loadCompetitorTableData() {
		databaseCompetitorInfoTableData = CompetitorService.findCompetitorInfoTableData();
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.DATABASE_COMPETITOR_TABLE_UPDATE));
	}

	private static void loadStatisticsTableData() {
		databaseStatistics = DatabaseStatisticsService.getDatabaseStatistics();
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.DATABASE_STATISTICS_TABLE_UPDATE));
	}

	public static void clearStagesToDelete() {
		databaseMatchInfoTableStagesToDelete.clear();
	}

	public static List<Match> getDatabaseMatchInfoTableData() {
		return databaseMatchInfoTableData;
	}

	public static List<Competitor> getDatabaseCompetitorInfoTableData() {
		return databaseCompetitorInfoTableData;
	}

	public static void addCompetitorToDelete(Competitor competitor) {
		competitorsToDelete.add(competitor);
	}

	public static void clearCompetitorsToDelete() {
		competitorsToDelete.clear();
	}

	public static void deleteCompetitors() {
		if (competitorsToDelete.size() > 0)
			CompetitorService.deleteAll(competitorsToDelete);
		RankingService.generateRanking();
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.NEW_RANKING_GENERATED));
	}

	public static List<Stage> getDatabaseMatchInfoTableStages() {
		return databaseMatchInfoTableStages;
	}

	public static List<Stage> getDatabaseMatchInfoTableStagesToDelete() {
		return databaseMatchInfoTableStagesToDelete;
	}

	public static void deleteStages() {
		StageService.delete(databaseMatchInfoTableStagesToDelete);
		RankingService.generateRanking();
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.NEW_RANKING_GENERATED));
	}

	public static DatabaseStatistics getDatabaseStatistics() {
		return databaseStatistics;
	}

	public static void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.NEW_RANKING_GENERATED) {
			init();
		}
	}
}
