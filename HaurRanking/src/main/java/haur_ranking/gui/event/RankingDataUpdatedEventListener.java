package haur_ranking.gui.event;

import haur_ranking.domain.Ranking;

public interface RankingDataUpdatedEventListener {
	void rankingDataUpdate(Ranking ranking);
}
