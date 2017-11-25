package haur_ranking.gui;

import haur_ranking.domain.Ranking;

public class GUIData {
	private static Ranking ranking;

	public static Ranking getRanking() {
		return ranking;
	}

	public static void setRanking(Ranking newRanking) {
		ranking = newRanking;
	}
}
