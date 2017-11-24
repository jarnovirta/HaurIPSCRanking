package haur_ranking.core;

import haur_ranking.gui.MainWindow;
import haur_ranking.pdf.PdfGenerator;
import haur_ranking.service.RankingService;

public class App {

	public static void main(String[] args) {

		// List<Match> matches =
		// WinMSSMatchRepository.findAll("E:/Programs/WinMss/Data/WinMSS.mdb");
		// for (Match match : matches) {
		// for (Stage stage : WinMSSStageRepository.findStagesForMatch(match)) {
		// for (StageScoreSheet sheet :
		// WinMSSStageScoreSheetRepository.find(match, stage)) {
		// if (sheet.getCompetitor().getFirstName().equals("Mikael")
		// && sheet.getCompetitor().getLastName().equals("Hakkarainen")
		// && sheet.getIpscDivision().equals(IPSCDivision.PRODUCTION)) {
		// System.out.println("PRODU STAGE " +
		// sheet.getStage().getMatch().getWinMssDateString());
		// }
		// }
		// }
		// }

		MainWindow gui = new MainWindow();
		gui.prepareGUI();
		gui.showHaurRankingGui();

		System.out.println("Generating pdf");
		PdfGenerator.createPdfRankingFile(RankingService.getRanking(), "");
		System.out.println("Pdf Done");
	}
}
