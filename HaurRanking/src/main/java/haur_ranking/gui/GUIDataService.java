package haur_ranking.gui;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.Event.GUIDataEvent;
import haur_ranking.Event.GUIDataEvent.GUIDataEventType;
import haur_ranking.Event.GUIDataEventListener;
import haur_ranking.domain.Match;
import haur_ranking.domain.Ranking;
import haur_ranking.service.DatabaseStatisticsService;
import haur_ranking.service.MatchService;
import haur_ranking.service.RankingService;

public class GUIDataService {

	private static Ranking ranking;
	private static List<GUIDataEventListener> dataUpdateListeners = new ArrayList<GUIDataEventListener>();
	private static List<Match> importResultsPanelMatchList;

	public static void updateRankingData() {
		ranking = RankingService.findCurrentRanking();
		GUIDataEvent event = new GUIDataEvent(GUIDataEventType.NEW_HAUR_RANKING_DB_DATA);
		event.setRanking(ranking);
		event.setDatabaseStatistics(DatabaseStatisticsService.getDatabaseStatistics());
		event.setImportedMatchesTableData(MatchService.getGUIImportedMatchesTableData());
		emitEvent(event);

	}

	public static void loadWinMSSData(String winMSSFilePath) {

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

	public static void setImportResultsPanelMatchList(List<Match> matches) {
		importResultsPanelMatchList = matches;
		GUIDataEvent event = new GUIDataEvent(GUIDataEventType.NEW_WINMSS_DB_DATA);
		event.setWinMSSNewMatches(matches);
		emitEvent(event);
	}

	private static void emitEvent(GUIDataEvent event) {
		for (GUIDataEventListener listener : dataUpdateListeners) {
			listener.processData(event);
		}
	}
}
