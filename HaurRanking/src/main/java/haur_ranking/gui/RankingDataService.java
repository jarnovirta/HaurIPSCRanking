package haur_ranking.gui;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Ranking;
import haur_ranking.gui.EventListeners.RankingDataUpdatedEventListener;
import haur_ranking.service.RankingService;

public class RankingDataService {
	private static Ranking ranking;
	private static List<RankingDataUpdatedEventListener> rankingUpdateListeners = new ArrayList<RankingDataUpdatedEventListener>();

	public static void updateRankingData() {
		ranking = RankingService.getRanking();
		for (RankingDataUpdatedEventListener listener : rankingUpdateListeners) {
			listener.rankingDataUpdate(ranking);
		}
	}

	public static Ranking getRanking() {
		return ranking;
	}

	public static void setRanking(Ranking ranking) {
		RankingDataService.ranking = ranking;
	}

	public static void addRankingDataUpdatedEventListener(RankingDataUpdatedEventListener listener) {
		rankingUpdateListeners.add(listener);
	}
}
