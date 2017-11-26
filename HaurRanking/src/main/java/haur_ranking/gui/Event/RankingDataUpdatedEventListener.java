package haur_ranking.gui.Event;

import haur_ranking.domain.Ranking;

public interface RankingDataUpdatedEventListener {
	void rankingDataUpdate(Ranking ranking);
}
