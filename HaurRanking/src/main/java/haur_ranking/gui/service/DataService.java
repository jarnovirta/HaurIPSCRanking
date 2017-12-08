package haur_ranking.gui.service;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Competitor;
import haur_ranking.domain.DatabaseStatistics;
import haur_ranking.domain.Match;
import haur_ranking.domain.Ranking;
import haur_ranking.domain.Stage;
import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.service.CompetitorService;
import haur_ranking.service.DatabaseStatisticsService;
import haur_ranking.service.LoadResultDataFromWinMSSTask;
import haur_ranking.service.MatchService;
import haur_ranking.service.RankingService;
import haur_ranking.service.SaveSelectedResultsToHaurRankingDbTask;
import haur_ranking.service.StageService;

public class DataService {

	private static Ranking ranking;
	private static List<GUIDataEventListener> dataEventListeners = new ArrayList<GUIDataEventListener>();
	private static List<Match> importResultsPanelMatchList = new ArrayList<Match>();
	private static List<Ranking> previousRankingsTableData = new ArrayList<Ranking>();
	private static List<Stage> databaseMatchInfoTableStages = new ArrayList<Stage>();;
	private static List<Stage> databaseMatchInfoTableStagesToDelete = new ArrayList<Stage>();
	private static List<Match> databaseMatchInfoTableData;
	private static List<Competitor> databaseCompetitorInfoTableData;
	private static DatabaseStatistics databaseStatistics;

	// Older ranking chosen for comparison of competitor ranking positions.
	// People having improved their position
	// are highlighted in ranking pdf.
	private static Ranking previousRankingsTableSelectedRanking;

	public static void init() {
		MatchService.addImportProgressEventListener(new DataImportEventHelper());
	}

	public static void processData(DataImportEvent event) {
		if (event.getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE
				&& event.getImportStatus() == ImportStatus.LOAD_FROM_WINMSS_DONE) {
			importResultsPanelMatchList = event.getWinMSSMatches();
		}

		GUIDataEvent guiEvent = new GUIDataEvent(GUIDataEventType.WINMSS_DATA_IMPORT_EVENT);
		guiEvent.setDataImportEvent(event);
		emitEvent(guiEvent);
		if (event.getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE
				&& event.getImportStatus() == ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE) {
			importResultsPanelMatchList = null;
			updateGUIData();
		}
	}

	public static void updateGUIData() {
		ranking = RankingService.findCurrentRanking();
		previousRankingsTableData = RankingService.findOldRankings();
		databaseStatistics = DatabaseStatisticsService.getDatabaseStatistics();
		databaseMatchInfoTableStages.clear();
		databaseMatchInfoTableData = MatchService.findAll();
		databaseCompetitorInfoTableData = CompetitorService.findCompetitorInfoTableData();
		for (Match match : databaseMatchInfoTableData) {
			if (match.getStages() != null)
				databaseMatchInfoTableStages.addAll(match.getStages());
		}
		GUIDataEvent event = new GUIDataEvent(GUIDataEventType.GUI_DATA_UPDATE);
		emitEvent(event);
	}

	public static void deleteStages() {
		StageService.delete(DataService.getDatabaseMatchInfoTableStagesToDelete());
		DataService.clearStagesToDelete();
		RankingService.generateRanking();
		DataService.updateGUIData();
	}

	public static Ranking getRanking() {
		return ranking;
	}

	public static void addDataEventListener(GUIDataEventListener listener) {
		dataEventListeners.add(listener);
	}

	public static List<Match> getImportResultsPanelMatchList() {
		return importResultsPanelMatchList;
	}

	public static void clearImportAsClassifierSelections() {
		for (Match match : importResultsPanelMatchList) {
			for (Stage stage : match.getStages()) {
				stage.setSaveAsClassifierStage(null);
			}
		}
		emitEvent(new GUIDataEvent(GUIDataEventType.GUI_DATA_UPDATE));
	}

	public static void loadDataFromWinMSS(String winMSSDatabasePath) {
		LoadResultDataFromWinMSSTask loadDataTask = new LoadResultDataFromWinMSSTask(winMSSDatabasePath);
		Thread importTaskThread = new Thread(loadDataTask);
		importTaskThread.start();
	}

	public static void saveResultsToHaurRankingDatabase() {
		SaveSelectedResultsToHaurRankingDbTask importResultsTask = new SaveSelectedResultsToHaurRankingDbTask(
				DataService.getImportResultsPanelMatchList());
		Thread importTaskThread = new Thread(importResultsTask);
		importTaskThread.start();
	}

	private static void emitEvent(GUIDataEvent event) {
		for (GUIDataEventListener listener : dataEventListeners) {
			listener.process(event);
		}
	}

	public static List<Ranking> getPreviousRankingsTableData() {
		return previousRankingsTableData;
	}

	public static void setPreviousRankingsTableData(List<Ranking> previousRankingsTableData) {
		DataService.previousRankingsTableData = previousRankingsTableData;
	}

	public static Ranking getPreviousRankingsTableSelectedRanking() {
		return previousRankingsTableSelectedRanking;
	}

	public static void setPreviousRankingsTableSelectedRanking(Ranking previousRankingsTableSelectedRanking) {
		DataService.previousRankingsTableSelectedRanking = previousRankingsTableSelectedRanking;
	}

	public static List<Stage> getDatabaseMatchInfoTableStages() {
		return databaseMatchInfoTableStages;
	}

	public static void setDatabaseMatchInfoTableStages(List<Stage> databaseMatchInfoTableStages) {
		DataService.databaseMatchInfoTableStages = databaseMatchInfoTableStages;
	}

	public static List<Stage> getDatabaseMatchInfoTableStagesToDelete() {
		return databaseMatchInfoTableStagesToDelete;
	}

	public static void setDatabaseMatchInfoTableStagesToDelete(List<Stage> databaseMatchInfoTableStagesToDelete) {
		DataService.databaseMatchInfoTableStagesToDelete = databaseMatchInfoTableStagesToDelete;
	}

	public static void clearStagesToDelete() {
		databaseMatchInfoTableStagesToDelete.clear();
	}

	public static DatabaseStatistics getDatabaseStatistics() {
		return databaseStatistics;
	}

	public static void setDatabaseStatistics(DatabaseStatistics databaseStatistics) {
		DataService.databaseStatistics = databaseStatistics;
	}

	public static List<Match> getDatabaseMatchInfoTableData() {
		return databaseMatchInfoTableData;
	}

	public static void setDatabaseMatchInfoTableData(List<Match> databaseMatchInfoTableData) {
		DataService.databaseMatchInfoTableData = databaseMatchInfoTableData;
	}

	public static List<Competitor> getDatabaseCompetitorInfoTableData() {
		return databaseCompetitorInfoTableData;
	}

	public static void setDatabaseCompetitorInfoTableData(List<Competitor> databaseCompetitorInfoTableData) {
		DataService.databaseCompetitorInfoTableData = databaseCompetitorInfoTableData;
	}

}
