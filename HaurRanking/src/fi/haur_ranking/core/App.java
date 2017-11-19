package fi.haur_ranking.core;

import fi.haur_ranking.pdf.PdfGenerator;
import fi.haur_ranking.service.RankingService;

public class App {

	public static void main(String[] args) {
		PdfGenerator.createPdfRankingFile(RankingService.getRanking(), "");
		System.out.println("Pdf Done");
		// MainWindow gui = new MainWindow();
		// gui.prepareGUI();
		// gui.showHaurRankingGui();

	}
}
