package haur_ranking.core;

import haur_ranking.gui.MainWindow;
import haur_ranking.repository.haur_ranking_repository.implementation.CompetitorRepositoryImpl;
import haur_ranking.repository.haur_ranking_repository.implementation.MatchRepositoryImpl;
import haur_ranking.repository.haur_ranking_repository.implementation.RankingRepositoryImpl;
import haur_ranking.repository.haur_ranking_repository.implementation.StageRepositoryImpl;
import haur_ranking.repository.haur_ranking_repository.implementation.StageScoreSheetRepositoryImpl;
import haur_ranking.repository.winmss_repository.implementation.WinMSSMatchRepositoryImpl;
import haur_ranking.repository.winmss_repository.implementation.WinMSSStageRepositoryImpl;
import haur_ranking.repository.winmss_repository.implementation.WinMSSStageScoreSheetRepositoryImpl;
import haur_ranking.service.CompetitorService;
import haur_ranking.service.MatchService;
import haur_ranking.service.RankingService;
import haur_ranking.service.StageScoreSheetService;
import haur_ranking.service.StageService;
import haur_ranking.service.WinMSSDataImportService;

public class App {

	public static void main(String[] args) {
		initializeServices();
		MainWindow gui = new MainWindow();
		gui.prepareGUI();
		gui.showHaurRankingGui();
	}

	private static void initializeServices() {
		MatchService.init(new MatchRepositoryImpl());
		RankingService.init(new RankingRepositoryImpl());
		CompetitorService.init(new CompetitorRepositoryImpl());
		StageScoreSheetService.init(new StageScoreSheetRepositoryImpl());
		StageService.init(new StageRepositoryImpl(), new WinMSSStageRepositoryImpl());
		WinMSSDataImportService.init(new WinMSSMatchRepositoryImpl(), new WinMSSStageRepositoryImpl(),
				new WinMSSStageScoreSheetRepositoryImpl());
	}
}
