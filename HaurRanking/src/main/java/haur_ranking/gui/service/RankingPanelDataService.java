package haur_ranking.gui.service;

import java.util.ArrayList;
import java.util.List;

import haur_ranking.domain.Ranking;
import haur_ranking.event.GUIDataEvent;
import haur_ranking.event.GUIDataEvent.GUIDataEventType;
import haur_ranking.service.RankingService;

public class RankingPanelDataService {
	private static Ranking previousRankingsTableSelectedRanking;

	private static Ranking ranking;

	private static List<Ranking> previousRankingsTableData = new ArrayList<Ranking>();
	private static List<Ranking> previousRankingsToDelete = new ArrayList<Ranking>();

	public static void init() {
		loadRankingTableData();
		loadPreviousRankingsTableData();
	}

	public static void loadRankingTableData() {
		ranking = RankingService.findCurrentRanking();
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.RANKING_TABLE_UPDATE));
	}

	public static void loadPreviousRankingsTableData() {
		previousRankingsTableData = RankingService.findOldRankings();
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.PREVIOUS_RANKINGS_TABLE_UPDATE));
	}

	public static void addPreviousRankingToDelete(Ranking ranking) {
		previousRankingsToDelete.add(ranking);
	}

	public static void clearPreviousRankingsToDelete() {
		previousRankingsToDelete.clear();
	}

	public static void deletePreviousRankings() {
		if (previousRankingsToDelete != null) {
			RankingService.delete(previousRankingsToDelete);
			previousRankingsTableData = RankingService.findOldRankings();
			DataEventService.emit(new GUIDataEvent(GUIDataEventType.PREVIOUS_RANKINGS_TABLE_UPDATE));
		}
	}

	public static List<Ranking> getPreviousRankingsTableData() {
		return previousRankingsTableData;
	}

	public static Ranking getPreviousRankingsTableSelectedRanking() {
		return previousRankingsTableSelectedRanking;
	}

	public static void setPreviousRankingsTableSelectedRanking(Ranking selectedRanking) {
		previousRankingsTableSelectedRanking = selectedRanking;
	}

	public static Ranking getRanking() {
		return ranking;
	}

	public static List<Ranking> getPreviousRankingsToDelete() {
		return previousRankingsToDelete;
	}

	public static void process(GUIDataEvent event) {
		if (event.getEventType() == GUIDataEventType.NEW_RANKING_GENERATED) {
			loadRankingTableData();
			loadPreviousRankingsTableData();
		}
	}

}
