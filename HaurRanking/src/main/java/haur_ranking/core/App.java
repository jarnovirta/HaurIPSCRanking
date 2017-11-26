package haur_ranking.core;

import haur_ranking.gui.MainWindow;

public class App {

	public static void main(String[] args) {
		MainWindow gui = new MainWindow();
		gui.prepareGUI();
		gui.showHaurRankingGui();

	}
}
