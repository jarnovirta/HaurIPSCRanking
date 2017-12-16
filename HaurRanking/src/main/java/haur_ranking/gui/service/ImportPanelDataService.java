package haur_ranking.gui.service;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Match;
import haur_ranking.domain.Stage;
import haur_ranking.event.DataImportEvent;
import haur_ranking.event.DataImportEvent.DataImportEventType;
import haur_ranking.event.DataImportEvent.ImportStatus;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.service.LoadResultDataFromWinMSSTask;
import haur_ranking.service.SaveSelectedResultsToHaurRankingDbTask;

public class ImportPanelDataService {
	private static List<Match> importResultsPanelMatchList;
	private static List<Stage> importResultsPanelStageList;

	public static void clearImportAsClassifierSelections() {
		for (Match match : importResultsPanelMatchList) {
			for (Stage stage : match.getStages()) {
				stage.setSaveAsClassifierStage(null);

			}
		}
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.IMPORT_RESULTS_TABLE_UPDATE));
	}

	public static void saveResultsToHaurRankingDatabase() {
		SaveSelectedResultsToHaurRankingDbTask importResultsTask = new SaveSelectedResultsToHaurRankingDbTask(
				importResultsPanelMatchList);
		Thread importTaskThread = new Thread(importResultsTask);
		importTaskThread.start();
	}

	public static List<Match> getImportResultsPanelMatchList() {
		return importResultsPanelMatchList;
	}

	public static void loadDataFromWinMSS(String winMSSDatabasePath) {
		LoadResultDataFromWinMSSTask loadDataTask = new LoadResultDataFromWinMSSTask(winMSSDatabasePath);
		Thread importTaskThread = new Thread(loadDataTask);
		importTaskThread.start();
	}

	public static void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.WINMSS_DATA_IMPORT_EVENT) {
			DataImportEvent importEvent = event.getDataImportEvent();
			if (importEvent.getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE
					&& importEvent.getImportStatus() == ImportStatus.LOAD_FROM_WINMSS_DONE) {
				importResultsPanelMatchList = importEvent.getWinMSSMatches();
				importResultsPanelStageList = new ArrayList<Stage>();
				for (Match match : importResultsPanelMatchList) {
					importResultsPanelStageList.addAll(match.getStages());
				}
				DataEventService.emit(new GUIDataEvent(GUIDataEventType.IMPORT_RESULTS_TABLE_UPDATE));
			}
			if (importEvent.getDataImportEventType() == DataImportEventType.IMPORT_STATUS_CHANGE
					&& importEvent.getImportStatus() == ImportStatus.SAVE_TO_HAUR_RANKING_DB_DONE) {
				DataEventService.emit(new GUIDataEvent(GUIDataEventType.NEW_RANKING_GENERATED));
			}
		}
	}

	public static List<Stage> getImportResultsPanelStageList() {
		return importResultsPanelStageList;
	}

}
