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

	private static int currentPreviousRankingsListPage;
	private static int totalPreviousRankingsListPages;

	private static final int previousRankingsListPageSize = 50;

	public static void init() {
		loadRankingTableData();
		loadPreviousRankingsTableData(1);
	}

	public static void loadRankingTableData() {
		ranking = RankingService.findCurrentRanking();
		DataEventService.emit(new GUIDataEvent(GUIDataEventType.RANKING_TABLE_UPDATE));
	}

	public static void loadPreviousRankingsTableData(int page) {
		previousRankingsTableData = RankingService.getPreviousRankingsTableData(page, previousRankingsListPageSize);

		currentPreviousRankingsListPage = page;
		int totalPreviousRankingsCount = RankingService.getRankingsCount() - 1;

		totalPreviousRankingsListPages = totalPreviousRankingsCount / previousRankingsListPageSize;
		if (totalPreviousRankingsCount > totalPreviousRankingsListPages * previousRankingsListPageSize) {
			totalPreviousRankingsListPages++;
		}

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
			loadPreviousRankingsTableData(1);
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
			init();
		}
	}

	public static int getCurrentPreviousRankingsListPage() {
		return currentPreviousRankingsListPage;
	}

	public static int getTotalPreviousRankingsListPages() {
		return totalPreviousRankingsListPages;
	}

	public static int getPreviousrankingslistpagesize() {
		return previousRankingsListPageSize;
	}

}
