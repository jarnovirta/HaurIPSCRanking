package haur_ranking.core;

import haur_ranking.pdf.PdfGenerator;
import haur_ranking.service.RankingService;

public class App {

	public static void main(String[] args) {
		PdfGenerator.createPdfRankingFile(RankingService.getRanking(), "");
		System.out.println("Pdf Done");
		// MainWindow gui = new MainWindow();
		// gui.prepareGUI();
		// gui.showHaurRankingGui();

	}
}
