package haur_ranking.gui;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.Event.NewGUIDataEvent;
import haur_ranking.Event.NewGUIDataEventListener;
import haur_ranking.domain.Ranking;
import haur_ranking.service.DatabaseStatisticsService;
import haur_ranking.service.MatchService;
import haur_ranking.service.RankingService;

public class GUIDataService {

	private static Ranking ranking;

	private static List<NewGUIDataEventListener> dataUpdateListeners = new ArrayList<NewGUIDataEventListener>();

	public static void updateRankingData() {
		ranking = RankingService.findCurrentRanking();
		NewGUIDataEvent event = new NewGUIDataEvent(ranking, DatabaseStatisticsService.getDatabaseStatistics(),
				MatchService.findAll());
		for (NewGUIDataEventListener listener : dataUpdateListeners) {
			listener.updateGUIData(event);
		}
	}

	public static Ranking getRanking() {
		return ranking;
	}

	public static void addRankingDataUpdatedEventListener(NewGUIDataEventListener listener) {
		dataUpdateListeners.add(listener);
	}
}
