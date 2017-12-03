package haur_ranking.gui;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Match;
import haur_ranking.domain.Ranking;
import haur_ranking.domain.Stage;
import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.event.GUIDataEventListener;
import haur_ranking.service.DatabaseStatisticsService;
import haur_ranking.service.LoadResultDataFromWinMSSTask;
import haur_ranking.service.MatchService;
import haur_ranking.service.RankingService;
import haur_ranking.service.SaveSelectedResultsToHaurRankingDbTask;

public class GUIDataService {

	private static Ranking ranking;
	private static List<GUIDataEventListener> dataUpdateListeners = new ArrayList<GUIDataEventListener>();
	private static List<Match> importResultsPanelMatchList;

	public static void init() {
		MatchService.addImportProgressEventListener(new GUIDataImportEventListener());
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
			updateRankingData();
		}
	}

	public static void updateRankingData() {
		ranking = RankingService.findCurrentRanking();
		GUIDataEvent event = new GUIDataEvent(GUIDataEventType.GUI_DATA_UPDATE);
		event.setRanking(ranking);
		event.setDatabaseStatistics(DatabaseStatisticsService.getDatabaseStatistics());
		event.setImportedMatchesTableData(MatchService.getGUIImportedMatchesTableData());
		emitEvent(event);
	}

	public static Ranking getRanking() {
		return ranking;
	}

	public static void addRankingDataUpdatedEventListener(GUIDataEventListener listener) {
		dataUpdateListeners.add(listener);
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
				GUIDataService.getImportResultsPanelMatchList());
		Thread importTaskThread = new Thread(importResultsTask);
		importTaskThread.start();
	}

	private static void emitEvent(GUIDataEvent event) {
		for (GUIDataEventListener listener : dataUpdateListeners) {
			listener.processData(event);
		}
	}
}
